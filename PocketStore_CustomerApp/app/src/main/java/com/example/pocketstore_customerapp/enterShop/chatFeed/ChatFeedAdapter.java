package com.example.pocketstore_customerapp.enterShop.chatFeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avatarfirst.avatargenlib.AvatarConstants;
import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.example.pocketstore_customerapp.R;

import java.util.ArrayList;

public class ChatFeedAdapter extends RecyclerView.Adapter<ChatFeedAdapter.MyViewHolder>{

    ArrayList<Chat> chatList;
    Context context;

    public ChatFeedAdapter(ArrayList<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.message_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.authorName.setText(chatList.get(position).getUserName());
        holder.messageBody.setText(chatList.get(position).getChatBody());
        holder.time.setText(chatList.get(position).getTime());
        holder.imageView.setImageDrawable(AvatarGenerator.Companion.avatarImage(context,155,AvatarConstants.Companion.getCIRCLE(),chatList.get(position).getUserName(),AvatarConstants.Companion.getCOLOR700()));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView messageBody, authorName, time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.messageview_avatar);
            messageBody = itemView.findViewById(R.id.messageView_body);
            authorName = itemView.findViewById(R.id.messageView_author);
            time = itemView.findViewById(R.id.messageView_time);
        }
    }
}
