package com.example.clup.Services;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.Timeslot;

import java.util.List;

public interface DatabaseManagerService {
    public List<String> getStoreCities();
    public List<String> getStores(String city);
    public List<Integer> getStoreOcupancy(Store store);
    public List<String> getStoreAddresses(String city, String name);
    public List<Ticket> getTickets(Store store);
    public List<Integer> getMaxTicketId(Store store);
    public Boolean checkCredentials(String email, String password);
    public Ticket getTicket(Store store, String ticketId);
}
