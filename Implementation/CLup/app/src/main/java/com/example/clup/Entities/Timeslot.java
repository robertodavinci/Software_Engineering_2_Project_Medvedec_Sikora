package com.example.clup.Entities;

import java.sql.Timestamp;

public class Timeslot {
    private Timestamp expectedEnter, expectedExit;
    // exptectedEnter is used as an expire timeslot for the ticket, 5 minutes after it has been activated
    // expectedExit is not used in this version

    // Timeslot constructors
    public Timeslot(Timestamp expectedEnter){
        this.expectedEnter = expectedEnter;
        this.expectedExit = null;
    }

    public Timeslot(Timestamp expectedEnter, Timestamp expectedExit){
        this.expectedEnter = expectedEnter;
        this.expectedExit = expectedExit;
    }

    public Timeslot(String expectedEnter, String expectedExit){
        try{
            this.expectedEnter = Timestamp.valueOf(expectedEnter);
            this.expectedExit = Timestamp.valueOf(expectedExit);
        } catch (IllegalArgumentException exception){
            System.err.println("Wrong date time format");
        }
    }

    public Timeslot(String expectedEnter){
        try{
            this.expectedEnter = Timestamp.valueOf(expectedEnter);
        } catch (IllegalArgumentException exception){
            System.err.println("Wrong date time format");
        }
    }

    // Timeslot getters and setters
    public Timestamp getExpectedEnter() {
        return expectedEnter;
    }
    public void setExpectedEnter(Timestamp expectedEnter) {
        this.expectedEnter = expectedEnter;
    }
    public void setExpectedExit(Timestamp expectedExit) {
        this.expectedExit = expectedExit;
    }
}
