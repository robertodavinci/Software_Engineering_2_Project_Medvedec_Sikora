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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener{

    private TextView banner, registerButton;
    private EditText nameText, surnameText, passwordText, emailText;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView)findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        nameText = (EditText) findViewById(R.id.editPersonName);
        surnameText = (EditText) findViewById((R.id.editPersonSurname));
        passwordText = (EditText)findViewById(R.id.editPassword);
        emailText = (EditText)findViewById(R.id.editEmailAddress);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerButton:
                registerUser();
        }
    }

    private void registerUser() {
        String name, surname, password, email;
        name = nameText.getText().toString().trim();
        surname = surnameText.getText().toString().trim();
        password = passwordText.getText().toString().trim();
        email = emailText.getText().toString().trim();

        if(name.isEmpty()){
            nameText.setError("Name is required!");
            nameText.requestFocus();
            return;
        }
        if(surname.isEmpty()){
            surnameText.setError("Surame is required!");
            surnameText.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordText.setError("Password is required!");
            passwordText.requestFocus();
            return;
        }
        if(password.length()<6){
            passwordText.setError("Password has to have at least 6 characters!");
            passwordText.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailText.setError("Email is required!");
            emailText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("Please provide valid email!");
            emailText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, surname, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterScreen.this, "User has been successfully registered!", Toast.LENGTH_LONG).show();
                                        //redirect to user profile
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        if(user.isEmailVerified()){
                                            startActivity(new Intent(RegisterScreen.this, UserProfile.class));
                                        } else{
                                            user.sendEmailVerification();
                                            Toast.makeText(RegisterScreen.this, "Check your mail to verify the account!", Toast.LENGTH_LONG).show();
                                        }
                                    } else{
                                        Toast.makeText(RegisterScreen.this, "User registration failed!", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else{
                            Toast.makeText(RegisterScreen.this, "User registration failed!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}