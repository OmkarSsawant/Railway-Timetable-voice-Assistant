package com.visionDev.traintimetableassistant.ui.user;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        if (res==null) return  "No Train Available currently";
        StringBuilder stoppingStaions = new StringBuilder();
        for (Arrival a:
             res.first.arrivals) {
            stoppingStaions.append(Util.getStationName(stations, a.station_id)).append(" , ");
        }

        return  "The Train "+ res.first.name +" will be arriving on " + res.second.arrivalTime.toString() + " on platform number " + res.second.platformNumber + " and Stopping stations are " + stoppingStaions.toString() +(res.first.isFastTrain ? " This is a fast train" : "This is a slow train");
    }


    public TrainTimeTableDB db;

    private SpeechRecognizer speechRecognizer;


    void speakAndAddMessage(MessageRVAdapter.Message m){
        messageRVAdapter.addMessage(m);
        textToSpeech.speak(m.message,TextToSpeech.QUEUE_FLUSH,null);
    }

    final Handler h = new Handler(Looper.getMainLooper());


    String lastDest=null;

    @Override
    public void onInit(int i) {
        isTtsInitialized = true;
        speakAndAddMessage(new MessageRVAdapter.Message("Say your source Location",true));
       h.postDelayed(() -> getInput(src->{
           messageRVAdapter.addMessage(new MessageRVAdapter.Message(src,false));
           speakAndAddMessage(new MessageRVAdapter.Message("Say your destination Location",true));
           h.postDelayed(()->{
               getInput(dest->{
                   lastDest = dest;
                   messageRVAdapter.addMessage(new MessageRVAdapter.Message(dest,false));

                   Util.getAvailableTrains(dao,src,dest,trains->{
                       speakAndAddMessage(new MessageRVAdapter.Message(createMessage(getFirstTrain(trains,stations,src)),true));




                   });
               });
           },4000);

       }), 5000);


    }

    interface  OnSpeechResult{
        void onResult(String s);
    }
    void getInput(OnSpeechResult listener ){
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        db = Room.databaseBuilder(this,TrainTimeTableDB.class,"train_system")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        if(PermissionChecker.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PermissionChecker.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},100);
            return;
        }

         dao = db.getTrainDAO();
        Util.addLines(dao);
        Util.addStations(dao);
        Util.addTrain(dao,"titwala-thane","Titwala","Mumbai CST",false);
        Util.addTrain(dao,"thane-titwala","Mumbai CST","Titwala",false);
        Util.addTrain(dao,"dombivli-thane","Dombivli","Thane",false);
        Util.addTrain(dao,"thane-dombivli","Thane","Dombivli",false);

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
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isTtsInitialized){
            textToSpeech.stop();
            textToSpeech.shutdown();
            isTtsInitialized  =false;
        }
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