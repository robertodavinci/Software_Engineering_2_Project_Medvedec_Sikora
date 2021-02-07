package com.example.clup.Entities;

// TicketState enumeration - WAITING are the tickets that are in queue, and ACTIVE are the tickets
// that are able to enter the store but have not entered yet
// IN_STORE and EXPIRED are not used in this version since tickets get deleted as soon as they
// are validated
public enum TicketState {
    WAITING,
    ACTIVE;
    //IN_STORE,
    //EXPIRED;
}
