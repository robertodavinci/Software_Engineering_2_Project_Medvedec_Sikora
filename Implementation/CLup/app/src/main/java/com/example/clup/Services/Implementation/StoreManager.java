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
import com.google.firebase.database.DatabaseError;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StoreManager implements EnterService, ExitService {
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    private Ticket ticket;
    // manages entrance of a customer and qrcode scan
    // checks the validity of the Ticket and sets ticket state based on the Store status
    @Override
    public void manageEntrance(String qrCodeText, OnTaskCompleteListener onTaskCompleteListener) {
        //TODO: parse qrCodeText/check
        String city = qrCodeText.split(";")[0].trim();
        String name = qrCodeText.split(";")[1].trim();
        String storeId  = qrCodeText.split(";")[2].trim();
        String ticketId = qrCodeText.split(";")[3].trim();
        Store store = new Store(Integer.parseInt(storeId), name, city);
        databaseManager.getTicket(store, ticketId, new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null) {
                    // errCode 1 - Invalid ticket
                    onTaskCompleteListener.onFailure(2);
                    return;
                }
                Timeslot timeslot = new Timeslot(Timestamp.valueOf(dataSnapshot.child("expires").getValue().toString()));
                Ticket t = new Ticket(Integer.parseInt(ticketId), store, timeslot);
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(dataSnapshot.child("ticketState").getValue().toString().equals("ACTIVE") && now.before(Timestamp.valueOf(dataSnapshot.child("expires").getValue().toString()))) {
                    databaseManager.persistEnter(store, t); // update occupancy and delete ticket after entrance
                    onTaskCompleteListener.onSuccess();
                } else {
                    onTaskCompleteListener.onFailure(2);
                }
                updateQueue(store, new OnTaskCompleteListener() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onFailure(int errCode) {

                    }
                });
                return;
            }
            @Override
            public void onFailure(DatabaseError databaseError){
            }
        });
    }

    // sends exit signal to the store, updates queue and sets another Ticket to Active if it's waiting in the queue
    @Override
    public void manageExit(Store store, OnTaskCompleteListener onTaskCompleteListener) {
        databaseManager.persistExit(store);
        updateQueue(store, new OnTaskCompleteListener() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onFailure(int errCode) {
            }
        });
        //System.out.println("ch");
        onTaskCompleteListener.onSuccess();
    }
    public void openStore(Store store){ databaseManager.openStore(store);}
    public void closeStore(Store store){ databaseManager.closeStore(store);}
    public void updateQueue(Store store, OnTaskCompleteListener onTaskCompleteListener){
        databaseManager.getStore(store, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    int occupancy, maxNoCustomers;
                    maxNoCustomers = Integer.parseInt(dataSnapshot.child("maxNoCustomers").getValue().toString());
                    occupancy = Integer.parseInt(dataSnapshot.child("occupancy").getValue().toString());
                    int active = maxNoCustomers - occupancy;
                    for (DataSnapshot i : dataSnapshot.child("Tickets").getChildren()) {
                        if (active < 1) return;
                        if (i.child("ticketState").getValue().toString().equals("ACTIVE")) {
                            if (now.after(Timestamp.valueOf(i.child("expires").getValue().toString())))
                                i.getRef().setValue(null);
                            else active--;
                        } else if (i.child("ticketState").getValue().toString().equals("WAITING") && active > 0) {
                            Timeslot timeslot = new Timeslot(new Timestamp(System.currentTimeMillis() + 1000 * 60 * 5));
                            i.getRef().child("expires").setValue(timeslot.getExpectedEnter().toString());
                            i.getRef().child("ticketState").setValue("ACTIVE");
                            active--;
                        }
                    }
                    onTaskCompleteListener.onSuccess();
                }
            }
            @Override
            public void onFailure(DatabaseError databaseError){
                onTaskCompleteListener.onFailure(0);
            }
        });
    }
}
