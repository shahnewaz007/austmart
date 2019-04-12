package com.ayon.austmart.activities.Chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayon.austmart.Adapters.MessageAdapter;
import com.ayon.austmart.Models.Chat;
import com.ayon.austmart.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    DatabaseReference reference;

    TextView userName;
    ImageView userPhoto;

    Button sendbtn;
    EditText writemsg;

    MessageAdapter mMessageAdapter;
    List<Chat>mChat;

    RecyclerView mRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userName = findViewById(R.id.current_user_name);
        userPhoto =findViewById(R.id.message_user_photo);

        sendbtn = findViewById(R.id.send_butn);
        writemsg = findViewById(R.id.write_message);

        mAuth =FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();






        mRecyclerView = findViewById(R.id.recycler_view1);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);












        final String CUserName = getIntent().getExtras().getString("UserName");
        userName.setText(CUserName);


        final String sellerImg = getIntent().getExtras().getString("UserImg");
        Glide.with(this).load(sellerImg).into(userPhoto);

        final String sellerUserID = getIntent().getExtras().getString("UserID");



        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = writemsg.getText().toString();
                if(!msg.equals("")){


                    sendMessage(currentUser.getUid(),sellerUserID,msg);


                }
                else
                {
                    Toast.makeText(MessageActivity.this,"Empty Message!",Toast.LENGTH_SHORT).show();
                }

                writemsg.setText("");

            }
        });



        reference = FirebaseDatabase.getInstance().getReference("Product Posts").child(sellerUserID);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                readMessages(currentUser.getUid(),sellerUserID,sellerImg);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }


    private void sendMessage(String sender, String receiver, String message)
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);



    }



    private void readMessages(final String myid, final String userid, final String imageurl)
    {
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);


                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid))
                    {
                        mChat.add(chat);
                    }


                    mMessageAdapter = new MessageAdapter(getApplicationContext(), mChat, imageurl);

                    
                    mRecyclerView.setAdapter(mMessageAdapter);









                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
