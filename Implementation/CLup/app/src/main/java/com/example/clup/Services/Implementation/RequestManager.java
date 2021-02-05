package com.example.clup.Services.Implementation;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.TicketState;
import com.example.clup.Entities.Timeslot;
import com.example.clup.OnCheckTicketListener;
import com.example.clup.OnGetDataListener;
import com.example.clup.OnGetTicketListener;
import com.example.clup.OnGetTimeslotListener;
import com.example.clup.OnTaskCompleteListener;
import com.example.clup.Services.DatabaseManagerService;
import com.example.clup.Services.QueueService;
import com.example.clup.Services.TicketService;
import com.google.firebase.database.DataSnapshot;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RequestManager implements QueueService, TicketService {
    private StoreSelectionManager storeSelectionManager;
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    List<Ticket> tickets;
    //TODO
    private int averageMinutesInStore = 15, maxId = -1;

    /*@Override
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
    }*/

    @Override
    public void getTicket(Store store, OnGetTicketListener onGetTicketListener) {
        System.out.println("Get ticket rm");
        maxId = -1;
        databaseManager.getStore(store, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (Integer.parseInt(dataSnapshot.child("open").getValue().toString()) == 0) {
                    onGetTicketListener.onFailure();
                    return;
                }
                maxId = Integer.parseInt(dataSnapshot.child("maxId").getValue().toString());
                Ticket ticket = new Ticket(maxId + 1, store);
                int occupancy = Integer.parseInt(dataSnapshot.child("occupancy").getValue().toString());
                int maxNoCustomers = Integer.parseInt(dataSnapshot.child("maxNoCustomers").getValue().toString());
                int activeTickets = 0;
                for (DataSnapshot i : dataSnapshot.child("Tickets").getChildren()) {
                    if (i.child("ticketState").getValue().toString().equals("ACTIVE"))
                        activeTickets++;
                }
                if (occupancy + activeTickets < maxNoCustomers) {
                    ticket.setTicketState(TicketState.ACTIVE);
                    ticket.setTimeslot(new Timeslot(new Timestamp(System.currentTimeMillis() + 1000 * 60 * 5))); // wait for customer 5 mins
                } else {
                    ticket.setTicketState(TicketState.WAITING);
                    ticket.setTimeslot(new Timeslot(new Timestamp(0)));
                }
                databaseManager.persistTicket(ticket);
                onGetTicketListener.onSuccess(ticket);
            }
        });
    }


    @Override
    public void checkTicket(Ticket ticket, OnCheckTicketListener onCheckTicketListener) {
        maxId = -1;
        databaseManager.getStore(ticket.getStore(), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Tickets").child(String.valueOf(ticket.getId())).child("ticketState").getValue().toString().equals("ACTIVE")) {
                    //how much does he have left
                    onCheckTicketListener.onActive(Timestamp.valueOf(dataSnapshot.child("Tickets").child(String.valueOf(ticket.getId())).child("expires").getValue().toString()));
                } else {
                    //calculate people in front
                    int peopleAhead = 1;
                    for (DataSnapshot i : dataSnapshot.child("Tickets").getChildren()) {
                        if (i.child("ticketState").getValue().toString().equals("WAITING") && Integer.parseInt(i.getKey()) < ticket.getId())
                            peopleAhead++;
                    }
                    onCheckTicketListener.onWaiting(peopleAhead);
                }
                return;
            }
        });
    }

    @Override
    public void checkQueue(Store store, OnCheckTicketListener onCheckTicketListener) {
        maxId = -1;
        databaseManager.getStore(store, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //calculate people in front
                int peopleAhead = 0;
                for (DataSnapshot i : dataSnapshot.child("Tickets").getChildren()) {
                    if (i.child("ticketState").getValue().toString().equals("WAITING"))
                        peopleAhead++;
                }
                onCheckTicketListener.onWaiting(peopleAhead);
                System.out.println("AAAAA" + peopleAhead);
                return;
            }
        });
    }

    @Override
    public void cancelTicket(Store store, Ticket ticket, OnTaskCompleteListener onTaskCompleteListener) {
        databaseManager.getTicket(store, String.valueOf(ticket.getId()), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    onTaskCompleteListener.onFailure();
                    return;
                }
                dataSnapshot.getRef().setValue(null);
                onTaskCompleteListener.onSuccess();
                return;
            }
        });
    }
}
