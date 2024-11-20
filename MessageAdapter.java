package com.telemedicine.app;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.database.Message;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage;
        private TextView textViewTimestamp;  // Add a TextView for timestamp

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);  // Initialize timestamp TextView
        }

        public void bind(Message message) {
            textViewMessage.setText(message.getContent());

            // Customize background and alignment based on sender
            if (message.getSender().equals("patient")) {
                textViewMessage.setBackgroundResource(R.drawable.bg_message_patient);
                textViewMessage.setGravity(Gravity.END);
            } else {
                textViewMessage.setBackgroundResource(R.drawable.bg_message_ai);
                textViewMessage.setGravity(Gravity.START);
            }

            // Format timestamp and display it
            if (message.getTimestamp() != null) {
                // Convert timestamp (which is stored as an object) to long
                long timestamp = (long) message.getTimestamp();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");  // Format timestamp (e.g., 10:30 AM)
                String formattedTime = sdf.format(new Date(timestamp));  // Convert to Date object
                textViewTimestamp.setText(formattedTime);  // Set the timestamp text
            }
        }
    }
}
