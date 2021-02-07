package com.example.clup.Entities;

public class Ticket {
    private int id; // Ticket ID - not unique for every ticket (tickets are defined by id and a store)
    private TicketState ticketState; // can be either WAITING or ACTIVE - TicketState enumeration
    private Store store; // contains a store ticket is connected to
    private Timeslot timeslot; // contains ticket start time and ticket expiration time
   // private String qrCode; // not used in this version, qrCode is retrieved and sent using different methods

    // Ticket constructors
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


    // Ticket getters and setters
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
    public void setTicketState(TicketState ticketState) {
        this.ticketState = ticketState;
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
    // public void setQrCode(String qrCode) {this.qrCode = qrCode;}
    // public String getQrCode() { return qrCode; }

    // method that prints Ticket variables to String
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
