package com.ayon.austmart.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ayon.austmart.R;
import com.ayon.austmart.activities.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {


    private EditText userMail, userPassword;
    private Button btnLogin;
    private ProgressBar loginProgressbar;
    private FirebaseAuth mAuth;
    private Intent homeActivity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userMail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.button_login);

        loginProgressbar = findViewById(R.id.login_progressBar);
        loginProgressbar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();

       homeActivity = new Intent(this,Home.class );


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginProgressbar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if(mail.isEmpty() || password.isEmpty())
                {
                    showMessage("Please Varify All Fields!");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgressbar.setVisibility(View.INVISIBLE);

                }

                else
                {
                    signIn(mail,password);
                    btnLogin.setVisibility(View.INVISIBLE);
                    loginProgressbar.setVisibility(View.VISIBLE);
                }



            }
        });
    }

    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful())
                {
                    loginProgressbar.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    updateUI();
                }


                else
                    showMessage(task.getException().getMessage());
                 loginProgressbar.setVisibility(View.INVISIBLE);
                 btnLogin.setVisibility(View.VISIBLE);

            }
        });




    }

    private void updateUI() {


        startActivity(homeActivity);
        finish();
    }


    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
