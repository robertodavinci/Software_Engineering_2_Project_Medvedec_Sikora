package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
 // implements AdapterView.OnItemSelectedListener
public class HomeController extends AppCompatActivity{

        Spinner stores, cities;
        TextView textView;
        ListView listView;

        String[] country = { "","Esselunga", "Carrefour", "Eurospin", "Lidl", "Milan Store"};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_controller);
            cities = findViewById(R.id.spinner_cities);
            stores = findViewById(R.id.spinner_stores);
            listView = findViewById(R.id.select_store);
            //textView = findViewById(R.id.text_v);

            ArrayList<String> cit = new ArrayList<>();
            cit.add("");
            cit.add("Tokyo");
            cit.add("Milano");
            cit.add("London");
            cit.add("Komiza");
            cit.add("Split");
            cit.add("Barcelona");
            cit.add("London");
            cit.add("Beč");
            cit.add("Firenca");
            cit.add("Rijeka");
            cit.add("Budva");
            cit.add("Hvar");

            ArrayList<String> cit2 = new ArrayList<>();
            cit2.add("Budva");
            cit2.add("Hvar");


            cities.setAdapter(new ArrayAdapter<>(HomeController.this,
            android.R.layout.simple_spinner_dropdown_item, cit));
            stores.setAdapter(new ArrayAdapter<>(HomeController.this,
                    android.R.layout.simple_spinner_dropdown_item, country));
            listView.setAdapter(new ArrayAdapter<>(HomeController.this, android.R.layout.simple_list_item_1, cit));

            cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                       // Toast.makeText(getApplicationContext(),
                         //       "Select City", Toast.LENGTH_SHORT).show();

                      //  textView.setText("");
                    }
                    else {
                        String sCit = parent.getItemAtPosition(position).toString();
                       // textView.setText(sCit);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            stores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                      //  Toast.makeText(getApplicationContext(),
                        //        "Select Store", Toast.LENGTH_SHORT).show();
                        //  textView.setText("");
                    }
                    else {
                        String sCit = parent.getItemAtPosition(position).toString();
                        listView.setAdapter(new ArrayAdapter<>(HomeController.this, android.R.layout.simple_list_item_1, cit2));
                        // textView.setText(sCit);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

            /*
            //Getting the instance of Spinner and applying OnItemSelectedListener on it
            Spinner spin = (Spinner) findViewById(R.id.spinner_cities);
            spin.setOnItemSelectedListener(this);

            //Creating the ArrayAdapter instance having the country list
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);

        }

        //Performing action onItemSelected and onNothing selected
        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
            Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        } */
    }