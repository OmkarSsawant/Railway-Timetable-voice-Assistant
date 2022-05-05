package com.visionDev.traintimetableassistant.ui.user;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;
import com.visionDev.traintimetableassistant.data.room.TrainDAO;
import com.visionDev.traintimetableassistant.data.room.TrainTimeTableDB;
import com.visionDev.traintimetableassistant.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

public class UserActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    MessageRVAdapter messageRVAdapter;
    final ArrayList<String> stationNames=new ArrayList<>();
     TextToSpeech textToSpeech ;
     boolean isTtsInitialized=false;
     TrainDAO dao;
      List<Station> stations;

    Pair<Train, Arrival> getFirstTrain(List<Train> trainList, List<Station> stations, String start){
        if(trainList.isEmpty()) return  null;
        Train ft = trainList.get(0);
        return  withArrival(ft,stations,start);
    }


    int trainUsedIndex = 1;
    Pair<Train, Arrival> getNextTrain(List<Train> trainList, List<Station> stations, String start){
        if(trainUsedIndex >= trainList.size() || trainList.isEmpty()) return  null;
        Train ft = trainList.get(trainUsedIndex);
        trainUsedIndex++;
        return  withArrival(ft,stations,start);
    }

    Pair<Train, Arrival> getFastTrain(List<Train> trainList, List<Station> stations, String start){
        if(trainList.isEmpty()) return  null;
        Train ft = Util.getFastTrain(trainList);
        if(ft==null) return  null;
        return  withArrival(ft,stations,start);
    }


    Pair<Train, Arrival> withArrival(Train t,List<Station> stations, String start){
        Arrival ta = null;
        for (Arrival a:
                t.arrivals) {
            if(Util.getStationName(stations,a.station_id).equals(start)){
                ta = a;
                break;
            }
        }
        return  new Pair<>(t,ta);
    }

    String createMessage(Pair<Train,Arrival> res){
        if (res==null) return  getResStringLanguage(R.string.no_train_av,isHindi ? "hi": "en");
        StringBuilder stoppingStaions = new StringBuilder();
        for (Arrival a:
             res.first.arrivals) {
            stoppingStaions.append(Util.getStationName(stations, a.station_id)).append(" , ");
        }

        return  getResStringLanguage(R.string.train,isHindi ? "hi": "en") + " " + res.first.name + " " +getResStringLanguage(R.string.arr,isHindi ? "hi": "en") + " "+res.second.arrivalTime.toString() + " " + getResStringLanguage(R.string.on_p_f_n,isHindi ? "hi": "en") +" " + res.second.platformNumber + " " + getResStringLanguage(R.string.stop_on,isHindi ? "hi": "en") + " " + stoppingStaions.toString() + " " +(res.first.isFastTrain ? getResStringLanguage(R.string.fast_train,isHindi ? "hi": "en"): getResStringLanguage(R.string.slow_train,isHindi ? "hi": "en"));
    }


    public TrainTimeTableDB db;

    private SpeechRecognizer speechRecognizer;


    void speakAndAddMessage(MessageRVAdapter.Message m){
        Log.i("TAG", "speakAndAddMessage: " + isHindi);
        textToSpeech.setLanguage(isHindi ?  hindi : Locale.getDefault());
        messageRVAdapter.addMessage(m);
        textToSpeech.speak(m.message,TextToSpeech.QUEUE_FLUSH,null);
    }

    final Handler h = new Handler(Looper.getMainLooper());


    String lastDest=null;

    void next2(String src,String dest){
        speakAndAddMessage(new MessageRVAdapter.Message(getResStringLanguage(R.string.ch_up_train,isHindi ? "hi": "en"),true));
        h.postDelayed(()->{
            getInput(ans3->{
                messageRVAdapter.addMessage(new MessageRVAdapter.Message(ans3,false));

                if(ans3.toLowerCase().contains("y") || ans3.toLowerCase().contains("ha")){

                    Util.getAvailableTrains(dao, src, dest, trains -> {
                        speakAndAddMessage(new MessageRVAdapter.Message(createMessage(getNextTrain(trains,stations,src)),true));
                        h.postDelayed(this::next3,15*1000);
                    });
                }
                else{
                    next3();
                }
            });
        },6000);
    }

    private void next3() {
        speakAndAddMessage(new MessageRVAdapter.Message(getResStringLanguage(R.string.do_continue,isHindi ? "hi": "en"),true));
        h.postDelayed(()->{
            getInput(ans4->{
                messageRVAdapter.addMessage(new MessageRVAdapter.Message(ans4,false));
                if(ans4.toLowerCase().contains("y") || ans4.toLowerCase().contains("ha")){
                    startConversation();
                }else{
                    finish();
                }
            });
        },6000);
    }

    void next1(String src,String dest){
        speakAndAddMessage(new MessageRVAdapter.Message(getResStringLanguage(R.string.ch_fst_train,isHindi ? "hi": "en"),true));
        h.postDelayed(()->{

            getInput(ans2->{
                messageRVAdapter.addMessage(new MessageRVAdapter.Message(ans2,false));

                if(ans2.toLowerCase().contains("y") || ans2.toLowerCase().contains("ha")) {
                    Util.getAvailableTrains(dao, src, dest, trains -> {
                        speakAndAddMessage(new MessageRVAdapter.Message(createMessage(getFastTrain(trains,stations,src)),true));
                        h.postDelayed(()->{
                            next2(src, dest);
                        },15*1000);
                    });

                }else{
                next2(src, dest);
                }
            });

        },6000);
    }

    void startConversation(){

        speakAndAddMessage(new MessageRVAdapter.Message(getResStringLanguage(R.string.ask_src_loc,isHindi ? "hi": "en"),true));
        h.postDelayed(() -> getInput(src->{
            Log.i("TAG", "startConversation: "+src);
            if(!stationNames.contains(src)){
                Toast.makeText(this,getResStringLanguage(R.string.no_station,isHindi ? "hi": "en"),Toast.LENGTH_SHORT).show();
                startConversation();
                return;
            }

            messageRVAdapter.addMessage(new MessageRVAdapter.Message(src,false));
            speakAndAddMessage(new MessageRVAdapter.Message(getResStringLanguage(R.string.ask_dst_loc,isHindi ? "hi": "en"),true));
            h.postDelayed(()->{
                getInput(dest->{
                    if(!stationNames.contains(dest)){
                        Toast.makeText(this,getResStringLanguage(R.string.no_station,isHindi ? "hi": "en"),Toast.LENGTH_SHORT).show();
                        startConversation();
                        return;
                    }
                    lastDest = dest;
                    messageRVAdapter.addMessage(new MessageRVAdapter.Message(dest,false));

                    Util.getAvailableTrains(dao,src,dest,trains->{
                        speakAndAddMessage(new MessageRVAdapter.Message(createMessage(getFirstTrain(trains,stations,src)),true));

                        h.postDelayed(()->{
                            speakAndAddMessage(new MessageRVAdapter.Message(getResStringLanguage(R.string.ask_nxt_station,isHindi ? "hi": "en"),true));

                            h.postDelayed(()->{
                                getInput(ans->{
                                    messageRVAdapter.addMessage(new MessageRVAdapter.Message(ans,false));
                                    if(ans.toLowerCase().contains("y") || ans.toLowerCase().contains("ha")){
                                        speakAndAddMessage(new MessageRVAdapter.Message(getResStringLanguage(R.string.nxt_dst,isHindi ? "hi": "en"),true));
                                        h.postDelayed(()->{
                                            getInput(nd->{
                                                if(!stationNames.contains(nd)){
                                                    Toast.makeText(this,getResStringLanguage(R.string.no_station,isHindi ? "hi": "en"),Toast.LENGTH_SHORT).show();
                                                    startConversation();
                                                    return;
                                                }

                                                    messageRVAdapter.addMessage(new MessageRVAdapter.Message(nd,false));
                                                    Util.getAvailableTrains(dao,lastDest,nd,nAvailableTrains->{
                                                        speakAndAddMessage(new MessageRVAdapter.Message(createMessage(getFirstTrain(nAvailableTrains,stations,src)),true));
                                                        h.postDelayed(()->{
                                                            next1(src,dest);
                                                        },15*1000);
                                                    });


                                            });
                                        },6000);
                                    }
                                    else{
                                      next1(src,dest);
                                    }
                                });
                            },6000);

                        },15*1000);


                    });
                });
            },6000);

        }), 5000);


    }


    @Override
    public void onInit(int i) {
        isTtsInitialized = true;

        speakAndAddMessage(new MessageRVAdapter.Message("Say 1 for English or 2 for Hindi",true));
        h.postDelayed(()->{
            getInput(a->{
                if(a.toLowerCase().contains("t")){
                    isHindi = true;
                    textToSpeech.setLanguage(hindi);
                }else{
                    isHindi = false;
                    textToSpeech.setLanguage(en);
                }
                startConversation();
            });
        },6000);

    }

    Locale hindi,en;
    boolean isHindi=false;
    interface  OnSpeechResult{
        void onResult(String s);
    }
    void getInput(OnSpeechResult listener ){
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 50000000);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,en);
        speechRecognizer.startListening(speechRecognizerIntent);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                listener.onResult(data.get(0));
                Log.i("TAG",data.get(0));
                speechRecognizer.stopListening();
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


    }
    
    public String getResStringLanguage(int id, String lang){
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        Locale savedLocale = conf.locale;
        Configuration confAr = getResources().getConfiguration();
        confAr.locale = new Locale(lang);
        DisplayMetrics metrics = new DisplayMetrics();
        Resources resources = new Resources(getAssets(), metrics, confAr);
        String string = resources.getString(id);
        conf.locale = savedLocale;
        res.updateConfiguration(conf, null);
        return string;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        db = Room.databaseBuilder(this,TrainTimeTableDB.class,"train_system")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //get hindi locale
        for (Locale l: Locale.getAvailableLocales()){
            if(l.getDisplayName().equals("hi")){
                hindi = l;
            }
            if(l.getDisplayName().equals("en")){
                en = l;
            }
        }


        if(PermissionChecker.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PermissionChecker.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},100);
            Toast.makeText(this,"Please Grant Permission ",Toast.LENGTH_SHORT).show();
            return;
        }

         dao = db.getTrainDAO();
        Util.addLines(dao);
        Util.addStations(dao);
        Util.addTrain(dao,"titwala-thane","Titwala","Mumbai CST",false);
        Util.addTrain(dao,"thane-titwala","Mumbai CST","Titwala",false);
        Util.addTrain(dao,"dombivli-thane","Dombivli","Thane",false);
        Util.addTrain(dao,"thane-dombivli","Thane","Dombivli",false);
        Util.addTrain(dao,"andheri-churchgate","Andheri","Church Gate",false);
        Util.addTrain(dao,"churchgate-andheri","Church Gate","Andheri",false);
        Util.addTrain(dao,"thane-dombivli","Thane","Dombivli",true);
        Util.addTrain(dao,"andheri-churchgate","Andheri","Church Gate",true);

        stationNames.clear();
         stationNames.addAll(dao.getStationNames());
        stations = dao.getStations().blockingGet();

        RecyclerView chatList = findViewById(R.id.chat_list);
        chatList.setLayoutManager(new LinearLayoutManager(this));
         messageRVAdapter = new MessageRVAdapter();
        chatList.setAdapter(messageRVAdapter);



    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!isTtsInitialized) {
            textToSpeech = new TextToSpeech(this, this);
            textToSpeech.setLanguage(isHindi ? hindi : en);
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isTtsInitialized){
            textToSpeech.stop();
            textToSpeech.shutdown();
            isTtsInitialized  =false;
        }
        speechRecognizer.stopListening();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults[0] == PermissionChecker.PERMISSION_DENIED){
            Toast.makeText(this,"Permission is Required to run app",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        db.close();
    }
}

























/*
*         final List<Station> stations = dao.getStations().blockingGet();
        start = findViewById(R.id.start_src);
         end = findViewById(R.id.end_dest);
         ArrayAdapter<String> a1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,stationNames);
        ArrayAdapter<String> a2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,stationNames);
        latest = findViewById(R.id.latest_train);
        fast = findViewById(R.id.fast_train);
        next = findViewById(R.id.next_train);
        start.setAdapter(a1);
        end.setAdapter(a2);
        Button search = findViewById(R.id.get_trains);
        search.setOnClickListener(view -> {
            String s = (String) start.getSelectedItem();
            String e = (String) end.getSelectedItem();
             Util.getAvailableTrains(dao, s, e, availableTrains -> {
                 latest.setText(createMessage(getFirstTrain(availableTrains,stations,s)));
                 fast.setText(createMessage(getFastTrain(availableTrains,stations,s)));
                 next.setText(createMessage(getNextTrain(availableTrains,stations,s)));
             });

        });
* */