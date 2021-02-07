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

    private Button getTicket, changeStore;
    private TextView storeInfo;
    private TextView storeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_controller);
        Director director = new Director();
        getTicket = findViewById(R.id.getTicket);
        changeStore = findViewById(R.id.changeStore);
        storeInfo = (TextView) findViewById(R.id.storeInfo2);
        storeStatus = (TextView) findViewById(R.id.storeStatus);
        storeInfo.setText(((ApplicationState) getApplication()).getStoreName() + ", " + ((ApplicationState) getApplication()).getAddress() + ", " + ((ApplicationState) getApplication()).getStoreCity());

        // Checks current store queue status and prints it out on the UI
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

        // Starts ticket generation and starts QrController if ticket info is valid
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

        // if change store is pressed, clear ApplicationState data and go back to store selection
        changeStore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((ApplicationState) getApplication()).clearAppState();
                startActivity((new Intent(view.getContext(), StoreController.class)));
                finish();
            }
        });
    }
    // Sets the action of a back button pressed from Android
    @Override
    public void onBackPressed () {
        ((ApplicationState) getApplication()).clearAppState();
        startActivity(new Intent(TicketController.this, StoreController.class));
        finish();
    }
}