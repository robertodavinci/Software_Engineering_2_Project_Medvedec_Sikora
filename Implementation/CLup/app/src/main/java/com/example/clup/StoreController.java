package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.example.clup.Entities.ApplicationState;
import com.example.clup.Entities.Store;
import com.example.clup.Services.Implementation.Director;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

// implements AdapterView.OnItemSelectedListener
public class StoreController extends AppCompatActivity{

        // SharedPreferences sharedpreferences;
        // private static final String MyPREFERENCES = "MyPrefs" ;
        private Spinner stores, cities;
        private TextView textView;
        private ListView listView;
        private String sCit = new String();
        private String sStore = new String();
        private ArrayList<String> filteredList = new ArrayList<>();
        private Director director = new Director();
        private Button selButton;
        private FrameLayout loadscreen;
        private Integer storeID;
        private String qrkey;
        private String storeAddress;
        private List<String> adr = new ArrayList<String>();


        String[] country = { "","Esselunga", "Carrefour", "Eurospin", "Lidl", "Milan Store"};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ((ApplicationState) getApplication()).setStoreCity(null);
            ((ApplicationState) getApplication()).setStoreID(null);
            ((ApplicationState) getApplication()).setStoreName(null);
            setContentView(R.layout.activity_store_controller);
            cities = findViewById(R.id.spinner_cities);
            stores = findViewById(R.id.spinner_stores);
            listView = findViewById(R.id.select_store);
            selButton = (Button) findViewById(R.id.storeSelectionButton);

            // Animation setup
            loadscreen = (FrameLayout) findViewById(R.id.loadscreen);
            ImageView loading = (ImageView) findViewById(R.id.loading2);
            loadscreen.setVisibility(View.VISIBLE);
            loading.setBackgroundResource(R.drawable.loading);
            AnimationDrawable animation = (AnimationDrawable)loading.getBackground();
            animation.start();
            //loadscreen.setVisibility(View.INVISIBLE);
            // CHECK APP STATE
            // ((ApplicationState) getApplication()).printAppState();


            ArrayList<String> cit = new ArrayList<>();
            /*cit.add("Split, Croatia");
            cit.add("Milano, Italy");*/

            // Fetching list of all cities from the database and adding it to spinner (drop down list)
            director.getDatabaseManager().getStoreCities(new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    for(DataSnapshot i : dataSnapshot.getChildren()) {
                        cit.add(i.getKey());
                    }
                    //System.out.println(sCit) ;
                    cities.setAdapter(new ArrayAdapter<>(StoreController.this,
                            android.R.layout.simple_spinner_dropdown_item, cit));
                    animation.stop();
                    loadscreen.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onFailure(DatabaseError databaseError) {

                }
            });
            // Fetching list of all stores from the certain city in the database and adding it to spinner (drop down list)
            cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    loadscreen.setVisibility(View.VISIBLE);
                    loading.setBackgroundResource(R.drawable.loading);
                    AnimationDrawable animation = (AnimationDrawable)loading.getBackground();
                    animation.start();
                        sCit = parent.getItemAtPosition(position).toString();
                        //System.out.println(sCit) ;
                        List<String> res = new ArrayList<String>();
                        director.getStoreSelectionManager().getStores(sCit, new OnGetDataListener(){
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for(DataSnapshot i : dataSnapshot.getChildren()) {
                                    res.add(i.getKey());
                                }
                                //System.out.println(sCit);
                                stores.setAdapter(new ArrayAdapter<>(StoreController.this,
                                        android.R.layout.simple_spinner_dropdown_item, res));
                                animation.stop();
                                loadscreen.setVisibility(View.GONE);
                            }
                            @Override
                            public void onFailure(DatabaseError databaseError){
                            }
                        });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            // Fetching list of all addresses of stores from the certain city of a certain chain in the database and adding it to spinner (drop down list)
            stores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        adr.clear();
                        sStore = parent.getItemAtPosition(position).toString();

                        director.getStoreSelectionManager().getStoreAddresses(sCit, sStore, new OnGetDataListener(){
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for(DataSnapshot i : dataSnapshot.getChildren()) {
                                    adr.add(i.child("address").getValue().toString());
                                }
                                listView.setAdapter(new ArrayAdapter<>(StoreController.this, android.R.layout.simple_list_item_1, adr));
                            }
                            @Override
                            public void onFailure(DatabaseError databaseError){

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
                            startActivity((new Intent(view.getContext(), TicketController.class)));
                        }
                        @Override
                        public void onFailure(DatabaseError databaseError){}
                    });

                    // Not used shared preferences
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
        // Sets the action of a back button pressed from Android
        @Override
        public void onBackPressed () {
            ((ApplicationState) getApplication()).clearAppState();
            startActivity(new Intent(StoreController.this, HomeController.class));
            finish();
        }

    }