package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.example.clup.Services.Implementation.DatabaseManager;
import com.example.clup.Services.Implementation.Director;
import com.google.firebase.database.DataSnapshot;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

// implements AdapterView.OnItemSelectedListener
public class HomeController extends AppCompatActivity{

        Spinner stores, cities;
        TextView textView;
        ListView listView;
        String sCit = new String();
        String sStore = new String();
        ArrayList<String> filteredList = new ArrayList<>();
        private Boolean citySet = false;
        private Boolean storeSet = false;

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
            cit.add("Split, Croatia");
            cit.add("Milano, Italy");

            ArrayList<String> cit2 = new ArrayList<>();
            cit2.add("Budva");
            cit2.add("Hvar");



            cities.setAdapter(new ArrayAdapter<>(HomeController.this,
            android.R.layout.simple_spinner_dropdown_item, cit));


            cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   /* if (position == 0) {
                        citySet = false;
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Select a city", Toast.LENGTH_LONG);
                        //toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }*/
                   // else {
                        citySet = true;
                        System.out.println("AA");
                        sCit = parent.getItemAtPosition(position).toString();
                        //System.out.println(sCit) ;
                        Director director = new Director();
                        List<String> res = new ArrayList<String>();
                        director.getStoreSelectionManager().getStores(sCit, new OnGetDataListener(){
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for(DataSnapshot i : dataSnapshot.getChildren()) {
                                    res.add(i.getKey());
                                }
                                stores.setAdapter(new ArrayAdapter<>(HomeController.this,
                                        android.R.layout.simple_spinner_dropdown_item, res));
                            }

                        });


                        //listView.setAdapter(new ArrayAdapter<>(HomeController.this, android.R.layout.simple_list_item_1, res));
                        //filteredList = (ArrayList<String>) director.getStoreSelectionManager().getStores(sCit);
                        //System.out.println(filteredList.size());
                        //listView.setAdapter(new ArrayAdapter<>(HomeController.this, android.R.layout.simple_list_item_1, filteredList));
                       // textView.setText(sCit);
                   // }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            stores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   /* if (position == 0) {
                        storeSet = false;
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Select a store", Toast.LENGTH_LONG);
                        //toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }*/
                 //   else {
                        storeSet = true;
                        sStore = parent.getItemAtPosition(position).toString();
                        Director director = new Director();
                        List<String> adr = new ArrayList<String>();
                        director.getStoreSelectionManager().getStoreAddresses(sCit, sStore, new OnGetDataListener(){
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for(DataSnapshot i : dataSnapshot.getChildren()) {
                                    adr.add(i.child("address").getValue().toString());
                                }
                                listView.setAdapter(new ArrayAdapter<>(HomeController.this, android.R.layout.simple_list_item_1, adr));
                            }

                        });

                //    }
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