package com.example.clup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.clup.Entities.ApplicationState;
import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Services.Implementation.Director;

public class TicketController extends AppCompatActivity {

    Button getTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_controller);
        Director director = new Director();
        getTicket = findViewById(R.id.getTicket);
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
}