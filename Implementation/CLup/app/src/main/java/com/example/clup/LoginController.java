package com.example.clup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Services.Implementation.DatabaseManager;
import com.example.clup.Services.Implementation.Director;
import com.example.clup.Services.Implementation.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LoginController extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail, editPassword;
    private TextView forgottenPassword;
    //private TextView register:
    private Button loginButton;
    private ProgressBar progressBar;
    private List<String> temp;
    private List<Integer> occupancy;
    private Director director = new Director();
    private FirebaseAuth mAuth;
    private FrameLayout loadscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_controller);
        // CHECK APP STATE
        //((ApplicationState) getApplication()).printAppState();

        mAuth = FirebaseAuth.getInstance();

        editEmail = (EditText) findViewById(R.id.editEmailAddress);
        editPassword = (EditText) findViewById(R.id.editPassword);

        loginButton = (Button) findViewById(R.id.button);
        loginButton.setOnClickListener(this);

        //register = (TextView) findViewById(R.id.registerLink);
        //register.setOnClickListener(this);
        forgottenPassword = (TextView) findViewById(R.id.forgottenPassword);
        forgottenPassword.setOnClickListener(this);
        loadscreen = (FrameLayout) findViewById(R.id.loadscreen);
        loadscreen.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.registerLink:
                startActivity(new Intent(this, RegisterScreen.class));
                break;*/
            case R.id.button:
                userLogin();
                break;
            case R.id.forgottenPassword:
                startActivity((new Intent(this, ForgotPasswordController.class)));
                break;
        }
    }

    private void userLogin() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if(email.isEmpty()){
            editEmail.setError("Email must be provided!");
            editEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Email address not valid!");
            editEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editPassword.setError("Password has to have at least 6 characters!");
            editPassword.requestFocus();
            return;
        }

        ImageView loading = (ImageView) findViewById(R.id.loading2); // Loading animation
        loadscreen.setVisibility(View.VISIBLE);
        loading.setBackgroundResource(R.drawable.loading);
        AnimationDrawable animation = (AnimationDrawable)loading.getBackground();
        animation.start();

        // Firebase default authentification check - passwords are not stored anywhere in the database
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    animation.stop();
                    loadscreen.setVisibility(View.GONE);
                    if(user.isEmailVerified()){
                        startActivity(new Intent(LoginController.this, StoreManagerController.class));
                        finish();
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginController.this, "Check your mail to verify the account!", Toast.LENGTH_LONG).show();
                    }
                } else{
                    animation.stop();
                    loadscreen.setVisibility(View.GONE);
                    Toast.makeText(LoginController.this, "Failed to log in!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    // Sets the action of a back button pressed from Android
    @Override
    public void onBackPressed () {
        startActivity(new Intent(LoginController.this, HomeController.class));
        finish();
    }
}