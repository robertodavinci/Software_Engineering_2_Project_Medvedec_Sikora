package com.example.clup.Services.Implementation;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.Timeslot;
import com.example.clup.Services.DatabaseManagerService;
import com.example.clup.Services.QueueService;
import com.example.clup.Services.TicketService;

import java.util.List;

public class RequestManager implements QueueService, TicketService {
    private StoreSelectionManager storeSelectionManager;
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    //TODO
    private int averageMinutesInStore = 15;

    @Override
    public Timeslot getFreeTimeslot(Store store) {
        List<Ticket> tickets = databaseManager.getTickets(store);
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

        return timeslot;
    }

    @Override
    public Ticket getTicket(Store store) {
        Timeslot timeslot = getFreeTimeslot(store);
        Ticket ticket = new Ticket(databaseManager.getMaxTicketId(store).get(0)+1, store, timeslot);
        // TODO
        // persist new ticket
        // change maxticketid
        return ticket;
    }
}
