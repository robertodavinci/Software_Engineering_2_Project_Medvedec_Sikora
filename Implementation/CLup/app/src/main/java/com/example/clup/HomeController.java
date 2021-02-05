package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.clup.Entities.ApplicationState;

public class HomeController extends AppCompatActivity implements View.OnClickListener{

    private Button storeButton, loginButton;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_controller);
        // CHECK APP STATE
        ((ApplicationState) getApplication()).printAppState();

        storeButton = (Button) findViewById(R.id.storeButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedpreferences.edit();
                String cit = sharedpreferences.getString("City", "NoCity");
                System.out.println(cit);
                startActivity((new Intent(v.getContext(), StoreController.class)));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                startActivity((new Intent(v2.getContext(), LoginController.class)));
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
}