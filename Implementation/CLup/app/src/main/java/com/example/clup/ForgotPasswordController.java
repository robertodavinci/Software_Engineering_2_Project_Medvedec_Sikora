package com.example.clup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clup.Entities.ApplicationState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordController extends AppCompatActivity implements View.OnClickListener {

    private TextView banner;
    private EditText emailField;
    private Button resetButton;
    // private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        banner = (TextView) findViewById(R.id.banner);
        emailField = (EditText) findViewById(R.id.emailField);
        resetButton = (Button) findViewById(R.id.resetButton);
        // progressBar = (ProgressBar) findViewById(R.id.progressBar);
        banner.setOnClickListener(this);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        auth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(ForgotPasswordController.this, LoginController.class));
                break;
            case R.id.resetButton:

        }
    }

    private void resetPassword() {
        String email = emailField.getText().toString().trim();

        if(email.isEmpty()){
            emailField.setError("Email must be provided!");
            emailField.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailField.setError("Email address not valid!");
            emailField.requestFocus();
            return;
        }
        // progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordController.this, "Check your email!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPasswordController.this, "Something has gone wrong!", Toast.LENGTH_LONG).show();
                }
                // progressBar.setVisibility(View.GONE);
            }
        });
    }
}