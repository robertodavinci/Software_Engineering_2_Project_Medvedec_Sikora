package com.example.clup.Entities;

public class Ticket {
    private int id;
    private TicketState ticketState;
    private Store store;
    private Timeslot timeslot;
    private String qrCode = "www.stipe.stipe";

    public Ticket(int id, Store store){
        this.id = id;
        this.ticketState = TicketState.WAITING;
        this.store = store;
    }
    public Ticket(int id, Store store, Timeslot timeslot){
        this.id = id;
        this.ticketState = TicketState.WAITING;
        this.store = store;
        this.timeslot = timeslot;
    }

    public void setTicketState(TicketState ticketState) {
        this.ticketState = ticketState;
    }

    public Store getStore() {
        return store;
    }

    public int getId() {
        return id;
    }

    public TicketState getTicketState() {
        return ticketState;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", ticketState=" + ticketState +
                ", store=" + store +
                ", timeslot=" + timeslot +
                '}';
    }
}
