package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScannerView;
import com.example.clup.Entities.ApplicationState;
import com.example.clup.Entities.Store;
import com.example.clup.Entities.Timeslot;
import com.example.clup.Services.Implementation.Director;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.sql.Timestamp;

public class StoreManagerController extends AppCompatActivity {

    private Button closeStore, openStore, customerControl;
    private TextView storeInfo3, storeOpenText;
    private Integer one = 1;
    private Boolean storeOpen;
    private String storeName;
    private String storeCity;
    private String storeAddress;
    private Integer storeID;
    Director director = new Director();
    private Store store;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manager_controller);
        closeStore = findViewById(R.id.closeStore);
        openStore = findViewById(R.id.openStore);
        customerControl = findViewById(R.id.customerControl);
        storeInfo3 = findViewById(R.id.storeInfo3);
        storeOpenText = findViewById(R.id.storeOpen);

        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
        /*System.out.println(storeName);
        System.out.println(storeID.toString());
        System.out.println(storeCity);
        */

        //storeInfo3.setText(((ApplicationState) getApplication()).getStoreName() + ", " + ((ApplicationState) getApplication()).getAddress() + ", " + ((ApplicationState) getApplication()).getStoreCity());

       // director.getDatabaseManager().getStoreOpen(new Store(((ApplicationState) getApplication()).getStoreID(), ((ApplicationState) getApplication()).getStoreName(), ((ApplicationState) getApplication()).getStoreCity()), new OnGetDataListener() {
        director.getDatabaseManager().getUserStore(FirebaseAuth.getInstance().getCurrentUser().getUid(), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ((ApplicationState) getApplication()).setStoreName(dataSnapshot.child("name").getValue().toString());
                ((ApplicationState) getApplication()).setStoreCity(dataSnapshot.child("city").getValue().toString());
                ((ApplicationState) getApplication()).setAddress(dataSnapshot.child("address").getValue().toString());
                ((ApplicationState) getApplication()).setStoreID(Integer.valueOf(String.valueOf(dataSnapshot.child("id").getValue())));
                ((ApplicationState) getApplication()).setStoreManager(true);
                storeName = ((ApplicationState) getApplication()).getStoreName();
                storeCity = ((ApplicationState) getApplication()).getStoreCity();
                storeID = ((ApplicationState) getApplication()).getStoreID();
                storeAddress = ((ApplicationState) getApplication()).getAddress();
                Store store = new Store(((ApplicationState) getApplication()).getStoreID(), ((ApplicationState) getApplication()).getStoreName(), ((ApplicationState) getApplication()).getStoreCity());
                storeInfo3.setText(storeName + ", " + storeAddress + ", " + storeCity);
                director.getDatabaseManager().getStoreOpen(store, new OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String op = (dataSnapshot.getValue().toString());
                        if (op.equals("1")) ((ApplicationState) getApplication()).setStoreOpened(true);
                        else ((ApplicationState) getApplication()).setStoreOpened(false);
                        storeOpen = ((ApplicationState) getApplication()).getStoreOpened();
                        //System.out.println(String.valueOf(dataSnapshot.getValue()));
                        //System.out.println(dataSnapshot.getValue().toString());
                        //System.out.println(storeOpen.toString());
                        if (storeOpen == false) {
                            ButCloseStore(openStore, closeStore, customerControl);
                        }
                        else {
                            ButOpenStore(openStore, closeStore, customerControl);
                        }

                        customerControl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view){
                                startActivity((new Intent(view.getContext(), CustomerController.class)));
                            };
                        });

                        closeStore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view){
                                director.getDatabaseManager().closeStore(store);
                                ((ApplicationState) getApplication()).setStoreOpened(false);
                                ButCloseStore(openStore, closeStore, customerControl);
                            };
                        });

                        openStore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view){
                                director.getDatabaseManager().openStore(store);
                                ((ApplicationState) getApplication()).setStoreOpened(true);
                                ButOpenStore(openStore, closeStore, customerControl);
                            };
                        });
                    }
                });
            }
        });


    }

    protected void ButCloseStore(Button open, Button close, Button cc){
        open.setBackgroundColor(ContextCompat.getColor(StoreManagerController.this, R.color.white));
        // open.setTextColor(ContextCompat.getColor(StoreManagerController.this, R.color.colorPrimaryDark));
        open.setClickable(true);
        close.setBackgroundColor(ContextCompat.getColor(StoreManagerController.this, R.color.greyed_out));
        close.setClickable(false);
        cc.setBackgroundColor(ContextCompat.getColor(StoreManagerController.this, R.color.greyed_out));
        cc.setTextColor(ContextCompat.getColor(StoreManagerController.this, R.color.colorPrimaryDark));
        cc.setClickable(false);

        storeOpenText.setText("The store is closed");
    }

    protected void ButOpenStore(Button open, Button close, Button cc){
        close.setBackgroundColor(ContextCompat.getColor(StoreManagerController.this, R.color.white));
        close.setClickable(true);
        open.setBackgroundColor(ContextCompat.getColor(StoreManagerController.this, R.color.greyed_out));
        open.setClickable(false);
        cc.setBackgroundColor(ContextCompat.getColor(StoreManagerController.this, R.color.colorPrimary));
        cc.setTextColor(ContextCompat.getColor(StoreManagerController.this, R.color.white));
        cc.setClickable(true);
        storeOpenText.setText("The store is open");
    }

}