package com.ayon.austmart.activities;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ayon.austmart.R;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

   Button login ;
   Button register;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_activity);

        login = findViewById(R.id.button_login);
        register = findViewById(R.id.button_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });



       register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent registerIntent = new Intent(BaseActivity.this, RegisterActivity.class);
               startActivity(registerIntent);
               finish();
           }
       });



    }



}
