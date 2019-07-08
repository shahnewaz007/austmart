package com.ayon.austmart.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayon.austmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {


    EditText reset_email;
    Button reset_button;
    ProgressBar loading_reset;
    TextView reset_text;


    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        reset_email = findViewById(R.id.reset_mail);
        reset_button = findViewById(R.id.button_reset_pass);
        loading_reset = findViewById(R.id.reset_progressBar);
        reset_text = findViewById(R.id.reset_text);

        mFirebaseAuth = FirebaseAuth.getInstance();

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_button.setVisibility(View.INVISIBLE);
                loading_reset.setVisibility(View.VISIBLE);
                reset_text.setVisibility(View.VISIBLE);

                String email = reset_email.getText().toString();

                if(email.isEmpty())
                {
                    showMessage("All fields are required!");
                    reset_button.setVisibility(View.VISIBLE);
                    loading_reset.setVisibility(View.INVISIBLE);
                    reset_text.setVisibility(View.INVISIBLE);

                }else
                {
                    mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                showMessage("Please Check your email.");
                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                            }
                            else
                            {
                                String error = task.getException().getMessage().toString();
                                showMessage(error);
                                reset_button.setVisibility(View.VISIBLE);
                                loading_reset.setVisibility(View.INVISIBLE);
                                reset_text.setVisibility(View.INVISIBLE);

                            }

                        }
                    });
                }
            }
        });



    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
}


