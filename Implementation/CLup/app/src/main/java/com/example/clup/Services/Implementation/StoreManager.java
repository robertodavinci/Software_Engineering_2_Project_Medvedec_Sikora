package com.example.clup.Services.Implementation;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Ticket;
import com.example.clup.Entities.TicketState;
import com.example.clup.Entities.Timeslot;
import com.example.clup.OnGetDataListener;
import com.example.clup.Services.EnterService;
import com.example.clup.Services.ExitService;
import com.google.firebase.database.DataSnapshot;

import java.sql.Timestamp;

public class StoreManager implements EnterService, ExitService {
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    private Ticket ticket;
    private int temp;

    @Override
    public boolean manageEntrance(String qrCodeText) {
        //TODO: parse qrCodeText/check
        String city = qrCodeText.split(";")[0];
        String name = qrCodeText.split(";")[1];
        String address = qrCodeText.split(";")[2];
        String storeId  = qrCodeText.split(";")[3];
        String ticketId = qrCodeText.split(";")[4];
        Store store = new Store(Integer.getInteger(storeId), name, address, city);
        databaseManager.getTicket(new Store(0, "Carrefour", "Viale Ungheria 3", "Milano, Italy"), "0", new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Ticket tempTicket = new Ticket(-1, null, null);
                for(DataSnapshot i : dataSnapshot.getChildren()) {
                    if(i.getKey().equals("expectedEnter")){
                        tempTicket.setTimeslot(new Timeslot(Timestamp.valueOf(i.getValue().toString())));
                    } else if(i.getKey().equals("ticketState")){
                        switch (i.getValue().toString()){
                            case "WAITING":
                                tempTicket.setTicketState(TicketState.WAITING);
                                break;
                            case "IN_STORE":
                                tempTicket.setTicketState(TicketState.IN_STORE);
                                break;
                            case "EXPIRED":
                                tempTicket.setTicketState(TicketState.EXPIRED);
                                break;
                            default:
                                break;
                        }
                    }
                    ticket = tempTicket;
                }
            }
        });
        temp = -1;
        databaseManager.getStoreOcupancy(ticket.getStore(), new OnGetDataListener(){
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                temp = ((Long)dataSnapshot.getValue()).intValue();
            }
        });
        if(temp<ticket.getStore().maxNoCustomers) {
            //TODO: persist change in occupancy
            return true;
        }
        return false;
    }

    @Override
    public void manageExit() {
        //TODO: persist change in occupancy
    }

}
