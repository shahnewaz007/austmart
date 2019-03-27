package com.ayon.austmart.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ayon.austmart.Fragments.HomeFragment;
import com.ayon.austmart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        mAuth = FirebaseAuth.getInstance();


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


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null)
        {
            //user is already connected so redirect to home page
            Intent redirectIntent = new Intent(this, Home.class);
            startActivity(redirectIntent);
            finish();

        }
    }


    @Override
    public void onBackPressed()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(true);

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                finish();

            }
        });

        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }



}
