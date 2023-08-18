package com.example.messeger.ChatActivityPackage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messeger.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_MY_MESSAGE = 0;
    private static final int VIEW_TYPE_OTHER_MESSAGE = 1;
    private static final String TAG = "Message_tag";

    private final String currentUserId;
    public MessageAdapter(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    private List<Message> messages = new ArrayList<>();
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int messageBgId;
        if(viewType == VIEW_TYPE_MY_MESSAGE) {
            messageBgId = R.layout.my_message_item;
        } else {
            messageBgId = R.layout.other_message_item;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(
                messageBgId,
                parent,
                false
        );
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        if(message.getText().equals("")) {
            holder.textMessage.setText("edited text");
        } else {
            holder.textMessage.setText(message.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        /*
        Log.d("TAG_AMG", "currentId: " + currentUserId);
        Log.d("TAG_AMG", "sendId: " + message.getSendId());
        Log.d("TAG_AMG", "text: " + message.getText());
        */
        if(message.getSenderId() == null) {
            Log.d("TAG", "message is null????");
        }

        if(message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_MY_MESSAGE;
        } else {
            return VIEW_TYPE_OTHER_MESSAGE;
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView textMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message);
        }
    }
}
