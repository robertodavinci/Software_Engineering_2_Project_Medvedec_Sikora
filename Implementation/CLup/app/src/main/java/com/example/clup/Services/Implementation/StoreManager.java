package com.example.clup.Services.Implementation;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.TicketState;
import com.example.clup.Entities.Timeslot;
import com.example.clup.OnGetDataListener;
import com.example.clup.OnTaskCompleteListener;
import com.example.clup.Services.EnterService;
import com.example.clup.Services.ExitService;
import com.google.firebase.database.DataSnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StoreManager implements EnterService, ExitService {
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    private Ticket ticket;

    @Override
    public void manageEntrance(String qrCodeText, OnTaskCompleteListener onTaskCompleteListener) {
        //TODO: parse qrCodeText/check
        String city = qrCodeText.split(";")[0].trim();
        String name = qrCodeText.split(";")[1].trim();
        String address = qrCodeText.split(";")[2].trim();
        String storeId  = qrCodeText.split(";")[3].trim();
        String ticketId = qrCodeText.split(";")[4].trim();
        Store store = new Store(Integer.parseInt(storeId), name, address, city);
        databaseManager.getTicket(store, ticketId, new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Timeslot timeslot = new Timeslot(Timestamp.valueOf(dataSnapshot.child("expectedEnter").getValue().toString()));
                Ticket t = new Ticket(Integer.parseInt(ticketId), store, timeslot);
                switch (dataSnapshot.child("ticketState").getValue().toString()){
                    case "WAITING":
                        t.setTicketState(TicketState.WAITING);
                        break;
                    case "IN_STORE":
                        t.setTicketState(TicketState.IN_STORE);
                        break;
                    case "EXPIRED":
                        t.setTicketState(TicketState.EXPIRED);
                        break;
                    default:
                        System.err.println("Error with ticketstate");
                }
                t.setStore(store);
                databaseManager.getStoreOcupancy(store, new OnGetDataListener(){
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        int occupancy = ((Long)dataSnapshot.getValue()).intValue();
                        databaseManager.getStoreMaxNoCustomers(store, new OnGetDataListener(){
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                int maxNoCustomers = ((Long)dataSnapshot.getValue()).intValue();
                                databaseManager.getMaxTicketId(store, new OnGetDataListener(){
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        int maxIdTemp = 0;
                                        for(DataSnapshot i : dataSnapshot.getChildren()) {
                                            if(i.child("ticketState").getValue().toString().equals("IN_STORE") && Integer.parseInt(i.getKey())>maxIdTemp){
                                                maxIdTemp = Integer.parseInt(i.getKey());
                                            }
                                        }
                                        int maxId = maxIdTemp;
                                        System.out.println("Occupancy "+occupancy+" maxC "+maxNoCustomers+" maxId "+maxId+" ticket id "+t.getId());
                                        if(occupancy<maxNoCustomers && maxId+1==t.getId()) {
                                            databaseManager.persistEnter(store, t);
                                            onTaskCompleteListener.onSuccess();
                                        } else {
                                            onTaskCompleteListener.onFailure();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void manageExit(Store store) {
        databaseManager.persistExit(store);
    }

}
