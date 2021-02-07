package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.clup.Entities.ApplicationState;
import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.Timeslot;
import com.example.clup.Services.Implementation.Director;

import java.sql.Timestamp;

public class TicketController extends AppCompatActivity {

    Button getTicket, changeStore;
    TextView storeInfo;
    TextView storeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_controller);
        // CHECK APP STATE
        System.out.println("CREATED");
        ((ApplicationState) getApplication()).printAppState();
        Director director = new Director();
        getTicket = findViewById(R.id.getTicket);
        changeStore = findViewById(R.id.changeStore);
        storeInfo = (TextView) findViewById(R.id.storeInfo2);
        storeStatus = (TextView) findViewById(R.id.storeStatus);
        storeInfo.setText(((ApplicationState) getApplication()).getStoreName() + ", " + ((ApplicationState) getApplication()).getAddress() + ", " + ((ApplicationState) getApplication()).getStoreCity());
        director.getRequestManager().checkQueue(new Store(((ApplicationState) getApplication()).getStoreID(), ((ApplicationState) getApplication()).getStoreName(),((ApplicationState) getApplication()).getStoreCity()), new OnCheckTicketListener() {
            @Override
            public void onWaiting(int peopleAhead) {
                if (peopleAhead < 1) storeStatus.setText("The store queue is empty");
                else storeStatus.setText("There " + (peopleAhead==1?"is ":"are ") + peopleAhead + " customer"+ (peopleAhead==1?"":"s") + " in the store queue");
            }

            @Override
            public void onActive(Timestamp expireTime) {
            }
            @Override
            public void onBadStore(String string){
                storeStatus.setText(string);
            }
        });

        getTicket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                director.getRequestManager().getTicket(new Store(((ApplicationState) getApplication()).getStoreID(), (((ApplicationState) getApplication()).getStoreName()), ((ApplicationState) getApplication()).getStoreCity()), new OnGetTicketListener() {
                    @Override
                    public void onSuccess(Ticket ticket) {
                        ((ApplicationState) getApplication()).setTicket(ticket);
                        startActivity((new Intent(view.getContext(), QrController.class)));
                        finish();
                    }

                    @Override
                    public void onFailure() {
                        storeStatus.setText("The store is not open");
                    }
                });
            }
        });

        changeStore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((ApplicationState) getApplication()).clearAppState();
                startActivity((new Intent(view.getContext(), StoreController.class)));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed () {
        ((ApplicationState) getApplication()).clearAppState();
        startActivity(new Intent(TicketController.this, StoreController.class));
        finish();
    }
}