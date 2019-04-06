package com.ayon.austmart.activities.Chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayon.austmart.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    TextView userName;
    ImageView userPhoto;

    Button sendbtn;
    EditText writemsg;



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
                    //sendMessage(sellerUserID,sellerUserID,msg);

                }
                else
                {
                    Toast.makeText(MessageActivity.this,"Empty Message!",Toast.LENGTH_SHORT).show();
                }

                writemsg.setText("");

            }
        });


    }


    private void sendMessage(String sender, String receiver, String message)
    {
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);


        //DatabaseReference myRef = database.getReference("Chats").push().setValue(hashMap);




    }


}
