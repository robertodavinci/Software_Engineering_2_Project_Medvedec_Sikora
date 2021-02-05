package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.example.clup.Entities.ApplicationState;
import com.example.clup.Entities.Store;
import com.example.clup.Services.Implementation.Director;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

// implements AdapterView.OnItemSelectedListener
public class StoreController extends AppCompatActivity{

        SharedPreferences sharedpreferences;
        public static final String MyPREFERENCES = "MyPrefs" ;
        Spinner stores, cities;
        TextView textView;
        ListView listView;
        String sCit = new String();
        String sStore = new String();
        ArrayList<String> filteredList = new ArrayList<>();
        Director director = new Director();
        Button selButton;
        private Boolean citySet = false;
        private Boolean storeSet = false;
        private Integer cityPos;
        private Integer storePos;
        private Integer storeID;
        private String qrkey;
        private String storeAddress;
        List<String> adr = new ArrayList<String>();


        String[] country = { "","Esselunga", "Carrefour", "Eurospin", "Lidl", "Milan Store"};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ((ApplicationState) getApplication()).setStoreCity(null);
            ((ApplicationState) getApplication()).setStoreID(null);
            ((ApplicationState) getApplication()).setStoreName(null);
            setContentView(R.layout.activity_store);
            cities = findViewById(R.id.spinner_cities);
            stores = findViewById(R.id.spinner_stores);
            listView = findViewById(R.id.select_store);
            selButton = (Button) findViewById(R.id.storeSelectionButton);
            //textView = findViewById(R.id.text_v);
            // CHECK APP STATE
            ((ApplicationState) getApplication()).printAppState();


            ArrayList<String> cit = new ArrayList<>();
            cit.add("Split, Croatia");
            cit.add("Milano, Italy");

            ArrayList<String> cit2 = new ArrayList<>();
            cit2.add("Budva");
            cit2.add("Hvar");



            cities.setAdapter(new ArrayAdapter<>(StoreController.this,
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
                        List<String> res = new ArrayList<String>();
                        director.getStoreSelectionManager().getStores(sCit, new OnGetDataListener(){
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for(DataSnapshot i : dataSnapshot.getChildren()) {
                                    res.add(i.getKey());
                                }
                                System.out.println(sCit) ;
                                stores.setAdapter(new ArrayAdapter<>(StoreController.this,
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
                        adr.clear();
                        storeSet = true;
                        sStore = parent.getItemAtPosition(position).toString();

                        director.getStoreSelectionManager().getStoreAddresses(sCit, sStore, new OnGetDataListener(){
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for(DataSnapshot i : dataSnapshot.getChildren()) {
                                    adr.add(i.child("address").getValue().toString());
                                }
                                listView.setAdapter(new ArrayAdapter<>(StoreController.this, android.R.layout.simple_list_item_1, adr));
                            }

                        });

                //    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 public void onItemClick(AdapterView<?> parent, View view,
                                                     int position, long id) {
                                                     String selected = ((TextView) view).getText().toString();
                                                     storeID = position;
                                                     storeAddress = adr.get(storeID);
                                                     ((ApplicationState) getApplication()).setStoreID(storeID);
                                                     ((ApplicationState) getApplication()).setAddress(storeAddress);
                                                     //System.out.println(team);
                                                 }
                                             });


            selButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    ((ApplicationState) getApplication()).setStoreCity(sCit);
                    ((ApplicationState) getApplication()).setStoreID(storeID);
                    ((ApplicationState) getApplication()).setStoreName(sStore);
                    director.getDatabaseManager().getStoreKey(new Store(storeID, sStore, storeAddress, sCit), new OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            qrkey = dataSnapshot.getValue().toString();
                            ((ApplicationState) getApplication()).setStoreKey(qrkey);
                            System.out.println(((ApplicationState) getApplication()).getStoreCity());
                            System.out.println(((ApplicationState) getApplication()).getStoreID());
                            System.out.println(((ApplicationState) getApplication()).getStoreName());
                            System.out.println(((ApplicationState) getApplication()).getAddress());
                            System.out.println(((ApplicationState) getApplication()).getStoreKey());
                            //System.out.println(((ApplicationState) getApplication()).getTicket().getId());
                            startActivity((new Intent(view.getContext(), TicketController.class)));
                        }
                    });

                    /*sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("City", sCit);
                    editor.putString("StoreName", sStore);
                    editor.putString("StoreID", storeID.toString());
                    editor.commit();
                    */
                }

            });

        }

        @Override
        public void onBackPressed () {
            ((ApplicationState) getApplication()).clearAppState();
            startActivity(new Intent(StoreController.this, HomeController.class));
        }

       /* @Override
        public void onClick(View v) {
         startActivity((new Intent(this, StoreController.class)));
        }*/

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