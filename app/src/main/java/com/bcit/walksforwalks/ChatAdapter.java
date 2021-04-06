package com.bcit.walksforwalks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context mContext;
    private final List<ChatMessage> messages;

    public ChatAdapter(Context context, List<ChatMessage> messages) {
        this.mContext = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_received_message, parent,
                false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chat = messages.get(position);
        holder.date.setText(chat.getMessageStringTime());
        holder.name.setText(chat.getMessageUser());
        holder.message.setText(chat.getMessageText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView message;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textView_recyler_received_message);
            name = itemView.findViewById(R.id.textView2_recycler_received_message);
            message = itemView.findViewById(R.id.textView3_recycler_received_message);
        }
    }
}
