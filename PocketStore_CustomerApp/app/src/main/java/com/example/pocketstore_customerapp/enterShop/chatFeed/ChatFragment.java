package com.example.pocketstore_customerapp.enterShop.chatFeed;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.pocketstore_customerapp.R;
import com.example.pocketstore_customerapp.misc.DateTimeHandler;
import com.example.pocketstore_customerapp.misc.sharedPreferences.EnteredShopSharedPreferences;
import com.example.pocketstore_customerapp.misc.sharedPreferences.UserInfoSharedPreferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ChatFragment extends Fragment {

    DatabaseReference reference,referenceFeed;
    EditText chatText;
    Button sendButton;
    ArrayList<Chat> chatList;
    ChatFeedAdapter adapter;
    RecyclerView recyclerView;
    Parcelable recyclerViewState;
    String shopId;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ChatFragment() { }

    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shopId = EnteredShopSharedPreferences.getShopID(getContext());
        chatText = container.findViewById(R.id.chatTextBox);
        sendButton = container.findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chatText.getText().toString().trim().isEmpty())
                {
                    sendButton.setEnabled(false);
                    reference = FirebaseDatabase.getInstance().getReference("shops").child(shopId).child("chatFeed");
                    String userId = UserInfoSharedPreferences.getUserID(getContext());
                    String userName = UserInfoSharedPreferences.getUsername(getContext());
                    String chatBody = chatText.getText().toString().trim();
                    String time = DateTimeHandler.TimeNow();
                    String chatId = reference.push().getKey();
                    Chat chat = new Chat(chatId,userName,userId,chatBody,time);
                    reference.child(chatId).setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(getContext(),"Sent!").show();
                            chatText.setText("");
                            sendButton.setEnabled(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(getContext(),"Failed to sent message!").show();
                            chatText.setText("");
                            sendButton.setEnabled(true);
                        }
                    });
                }
            }
        });


        referenceFeed = FirebaseDatabase.getInstance().getReference("shops").child(shopId);
        referenceFeed.keepSynced(true);
        View view = inflater.inflate(R.layout.chat_feed, container, false);
        recyclerView = view.findViewById(R.id.chat_feed_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(manager);


        referenceFeed.child("chatFeed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList = new ArrayList<>();
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        chatList.add(chat);
                    }
                }
                adapter = new ChatFeedAdapter(chatList,getContext());
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                recyclerView.setAdapter(adapter);
                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.error(Objects.requireNonNull(getContext()),"Failed load messages!").show();
            }
        });

        return view;
    }

}