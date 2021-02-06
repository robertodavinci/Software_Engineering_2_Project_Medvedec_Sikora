package com.example.clup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clup.Entities.ApplicationState;
import com.example.clup.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private TextView nameField, surnameField, emailField;
    private Button logoutButton;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile3);

        nameField = (TextView) findViewById(R.id.nameField);
        surnameField = (TextView) findViewById(R.id.surnameField);
        emailField = (TextView) findViewById(R.id.emailField);
        // CHECK APP STATE
        ((ApplicationState) getApplication()).printAppState();
        logoutButton = (Button) findViewById(R.id.continueButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfile.this, LoginController.class));
            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    String name = user.name;
                    String surname = user.surname;
                    String email = user.email;

                    nameField.setText(name);
                    surnameField.setText(surname);
                    emailField.setText(email);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, "Something has gone wrong!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed () {
        ((ApplicationState) getApplication()).setStoreManager(false);
        startActivity(new Intent(UserProfile.this, HomeController.class));
    }
}