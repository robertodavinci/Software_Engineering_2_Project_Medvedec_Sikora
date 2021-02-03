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

    private Button storeButton;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_controller);


        storeButton = (Button) findViewById(R.id.storeButton);
        storeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        String cit = sharedpreferences.getString("City", "NoCity");
        System.out.println(cit);
        startActivity((new Intent(this, StoreController.class)));


        /* ERASING SHARED PREFERENCES
         SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedpreferences.edit();
         editor.clear();
         editor.commit();
         */
    }
}