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

    Button getTicket;
    TextView storeInfo;
    TextView storeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_controller);
        // CHECK APP STATE
        ((ApplicationState) getApplication()).printAppState();
        Director director = new Director();
        getTicket = findViewById(R.id.getTicket);
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
        });
        getTicket.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                director.getRequestManager().getTicket(new Store(((ApplicationState) getApplication()).getStoreID(), (((ApplicationState) getApplication()).getStoreName()), ((ApplicationState) getApplication()).getStoreCity()), new OnGetTicketListener() {
                    @Override
                    public void onSuccess(Ticket ticket) {
                        ((ApplicationState) getApplication()).setTicket(ticket);
                        startActivity((new Intent(view.getContext(), QrController.class)));
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
    });
    }

    @Override
    public void onBackPressed () {
        ((ApplicationState) getApplication()).clearAppState();
        startActivity(new Intent(TicketController.this, StoreController.class));
    }
}