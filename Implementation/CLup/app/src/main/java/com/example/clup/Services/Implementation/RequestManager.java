package com.example.clup.Services.Implementation;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.TicketState;
import com.example.clup.Entities.Timeslot;
import com.example.clup.OnGetDataListener;
import com.example.clup.OnGetTicketListener;
import com.example.clup.OnGetTimeslotListener;
import com.example.clup.Services.DatabaseManagerService;
import com.example.clup.Services.QueueService;
import com.example.clup.Services.TicketService;
import com.google.firebase.database.DataSnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RequestManager implements QueueService, TicketService {
    private StoreSelectionManager storeSelectionManager;
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    List<Ticket> tickets;
    //TODO
    private int averageMinutesInStore = 15, maxId = -1;

    @Override
    public void getFreeTimeslot(Store store, OnGetTimeslotListener onGetTimeslotListener) {
        List<Ticket> tickets = new ArrayList<Ticket>();
        databaseManager.getTickets(store, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<Ticket> tickets = new ArrayList<>();
                for(DataSnapshot ticket : dataSnapshot.getChildren()){
                    int ticketId = Integer.parseInt(ticket.getKey());
                    Timeslot timeslot = new Timeslot(Timestamp.valueOf(ticket.child("expectedEnter").getValue().toString()));
                    Ticket t = new Ticket(ticketId, store, timeslot);
                    switch (ticket.child("ticketState").getValue().toString()){
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
                    tickets.add(t);
                }
                Timeslot latest, timeslot = null;
                try {
                    latest = tickets.get(0).getTimeslot();
                    for(Ticket t : tickets){
                        if(t.getTimeslot().getExpectedEnter().compareTo(latest.getExpectedEnter())>0){
                            latest = t.getTimeslot();
                        }
                    }
                    timeslot = new Timeslot(latest.getExpectedEnter());
                    timeslot.getExpectedEnter().setTime(timeslot.getExpectedEnter().getTime()+averageMinutesInStore*60*1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Expected enter "+timeslot.getExpectedEnter().toString());
                onGetTimeslotListener.onSuccess(timeslot);
            }
        });
    }

    @Override
    public void getTicket(Store store, OnGetTicketListener onGetTicketListener) {
        System.out.println("Get ticket rm");
        getFreeTimeslot(store, new OnGetTimeslotListener() {
            @Override
            public void onSuccess(Timeslot timeslot) {
                maxId = -1;
                databaseManager.getMaxTicketId(store, new OnGetDataListener(){
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        int tempMaxId = 0;
                        for(DataSnapshot i : dataSnapshot.getChildren()) {
                            if(Integer.parseInt(i.getKey())>tempMaxId){
                                tempMaxId = Integer.parseInt(i.getKey());
                            }
                        }
                        maxId = tempMaxId;
                        Ticket ticket = new Ticket(maxId+1, store, timeslot);
                        ticket.setTicketState(TicketState.WAITING);
                        databaseManager.persistTicket(ticket);
                        onGetTicketListener.onSuccess(ticket);
                    }
                });
            }
        });
    }
}
