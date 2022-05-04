package com.visionDev.traintimetableassistant.ui.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visionDev.traintimetableassistant.R;

import java.util.ArrayList;

public class MessageRVAdapter extends RecyclerView.Adapter<MessageRVAdapter.ChatVH> {


    final ArrayList<Message> messages = new ArrayList<>();

    @NonNull
    @Override
    public ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MessageType.INCOMING){
            return new ChatVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_incoming,parent,false));
        }else{

            return new ChatVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_outgoing,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatVH holder, int position) {
        holder.m.setText(messages.get(position).message);
    }


    @Override
    public int getItemViewType(int position) {
        return messages.get(position).fromApp ? MessageType.INCOMING : MessageType.OUTGOING;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.indexOf(message));
    }

    static class ChatVH extends RecyclerView.ViewHolder{
        final TextView m;
        public ChatVH(@NonNull View itemView) {
            super(itemView);
            m = (TextView) itemView.findViewById(R.id.message);
        }
    }

   public static class Message{
        final String message;
        final boolean fromApp;

        Message(String message, boolean fromApp) {
            this.message = message;
            this.fromApp = fromApp;
        }
    }

    static class MessageType{
       static final int INCOMING = 0;
       static final int OUTGOING = 1;
    }
}
