package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.clup.Entities.ApplicationState;
import com.example.clup.Services.Implementation.Director;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

public class PreLoginController extends AppCompatActivity {

    private TextView email;
    private Button changeUser, keepUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login_controller);
        Director director = new Director();
        email = findViewById(R.id.printedEmail);
        changeUser = findViewById(R.id.changeUserButton);
        keepUser = findViewById(R.id.continueButton);

        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        changeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                ((ApplicationState) getApplication()).clearAppState();
                startActivity((new Intent(view.getContext(), LoginController.class)));
            };
        });

        keepUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                ((ApplicationState) getApplication()).setStoreManager(true);
                startActivity((new Intent(view.getContext(), StoreManagerController.class)));
            };
        });
    }
}