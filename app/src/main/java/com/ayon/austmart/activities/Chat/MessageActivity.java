package com.ayon.austmart.activities.Chat;

import android.content.SharedPreferences;
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
import com.ayon.austmart.Fragments.APIService;
import com.ayon.austmart.Models.Chat;
import com.ayon.austmart.Models.User;
import com.ayon.austmart.Notifications.Client;
import com.ayon.austmart.Notifications.Data;
import com.ayon.austmart.Notifications.MyResponse;
import com.ayon.austmart.Notifications.Sender;
import com.ayon.austmart.Notifications.Token;
import com.ayon.austmart.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    DatabaseReference reference;

    TextView userName;
    ImageView userPhoto;

    Button sendbtn;
    EditText writemsg;
   String sellerUserID;

    MessageAdapter mMessageAdapter;
    List<Chat>mChat;

    RecyclerView mRecyclerView;

    ValueEventListener seenListner;

    APIService apiService;

    boolean notify = false;










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

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);






        mRecyclerView = findViewById(R.id.recycler_view1);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);







        final String CUserName = getIntent().getExtras().getString("UserName");
        userName.setText(CUserName);


        final String sellerImg = getIntent().getExtras().getString("UserImg");
        Glide.with(this).load(sellerImg).into(userPhoto);

         sellerUserID = getIntent().getExtras().getString("UserID");



        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notify = true;
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



        seenMessage(sellerUserID);

    }


    private void seenMessage(final String userid)
    {
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        seenListner = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(currentUser.getUid()) && chat.getSender().equals(userid))
                    {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isseen",true);
                            snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendMessage(String sender, final String receiver, String message)
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);

        reference.child("Chats").push().setValue(hashMap);


        final String msg = message;

        reference  = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(notify) {
                    sendNotification(receiver, user.getUserName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendNotification(final String receiver, final String username, final String message)
    {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");

        Query query = tokens.orderByKey().equalTo(receiver);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(currentUser.getUid(), R.mipmap.ic_launcher, username+": "+message, "New Message",sellerUserID);

                    Sender sender = new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    if(response.code() == 200)
                                    {
                                        if(response.body().success !=1)
                                        {
                                            Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }

                                    }


                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




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


    private void currentUser(String userid)
    {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser",userid);
        editor.apply();
    }


    private void status(String status)
    {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        HashMap<String, Object> hashmap = new HashMap<>();

        hashmap.put("status", status);

        reference.updateChildren(hashmap);
    }


    @Override
    protected void onResume() {
        super.onResume();
        status("Online");
        currentUser(sellerUserID);

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListner);
        status("Offline");
        currentUser("none");
    }

}
