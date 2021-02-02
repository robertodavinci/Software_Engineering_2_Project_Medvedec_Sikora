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

import com.example.clup.Entities.Store;
import com.example.clup.Services.Implementation.DatabaseManager;
import com.example.clup.Services.Implementation.StoreSelectionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail, editPassword;

    private TextView register, forgottenPassword;
    private Button loginButton;
    private ProgressBar progressBar;
    private List<String> temp;
    private List<Integer> occupancy;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editEmail = (EditText) findViewById(R.id.editEmailAddress);
        editPassword = (EditText) findViewById(R.id.editPassword);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        
        loginButton = (Button) findViewById(R.id.button);
        loginButton.setOnClickListener(this);

        register = (TextView) findViewById(R.id.registerLink);
        register.setOnClickListener(this);
        forgottenPassword = (TextView) findViewById(R.id.forgottenPassword);
        forgottenPassword.setOnClickListener(this);

        DatabaseManager db = new DatabaseManager();
        List<String> cities = db.getStoreCities();
        temp = db.getStoreCities();
        System.out.println("Temp size "+temp.size());
        db.getStores("Milano, Italy");
        db.getStoreAddresses("Milano, Italy", "Esselunga");

        StoreSelectionManager ssm = new StoreSelectionManager();
        temp = ssm.getStores("Milano, Italy");
        //occupancy = db.getStoreOcupancy(new Store(0, "Carrefour", "Viale Ungheria 3", "Milano, Italy"));
        occupancy = db.getMaxTicketId(new Store(0, "Carrefour", "Viale Ungheria 3", "Milano, Italy"));
        System.out.println("Temp size "+temp.size());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerLink:
                startActivity(new Intent(this, RegisterScreen.class));
                break;
            case R.id.button:
                if(occupancy.size()>0) System.out.println("maxId is "+occupancy.get(0));
                userLogin();
                break;
            case R.id.forgottenPassword:
                startActivity((new Intent(this, ForgotPassword.class)));
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

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, UserProfile.class));
                    } else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your mail to verify the account!", Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(MainActivity.this, "Failed to log in!", Toast.LENGTH_LONG).show();
                }
            }
        });

        progressBar.setVisibility(View.GONE);
    }
}