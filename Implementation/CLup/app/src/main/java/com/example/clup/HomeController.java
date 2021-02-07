package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.clup.Entities.ApplicationState;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class HomeController extends AppCompatActivity implements View.OnClickListener{

    private Button storeButton, loginButton;
    // private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_controller);
        // CHECK APP STATE
        // ((ApplicationState) getApplication()).printAppState();

        storeButton = (Button) findViewById(R.id.storeButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        // Update user
        //FirebaseAuth.getInstance().updateCurrentUser(FirebaseAuth.getInstance().getCurrentUser());
        if (FirebaseAuth.getInstance().getCurrentUser() == null) System.out.println("NOPE");
        //else System.out.println("EMAILL " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        //FirebaseAuth.getInstance().signOut();

        // checks for camera permission and asks for it if it's not permitted - scanner will crash the app if
        // the camera is not enabled
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Shared preferences can be used to store data permanently on the phone
                // like old tickets and such - not implemented in this version of the app
                // sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedpreferences.edit();
                // String cit = sharedpreferences.getString("City", "NoCity");
                //System.out.println(cit);
                startActivity((new Intent(v.getContext(), StoreController.class)));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null)
                    startActivity((new Intent(v2.getContext(), LoginController.class)));
                else
                    startActivity((new Intent(v2.getContext(), PreLoginController.class)));
            }
        });
    }

    @Override
    public void onClick(View v) {

        /* ERASING SHARED PREFERENCES
         SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedpreferences.edit();
         editor.clear();
         editor.commit();
         */
    }
    // Sets the action of a back button pressed from Android
    @Override
    public void onBackPressed () {
        ((ApplicationState) getApplication()).clearAppState();
        //startActivity(new Intent(HomeController.this, StoreController.class));
        // Clears stack of activities
        finishAffinity();

    }
}