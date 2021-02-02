package com.example.clup.Services;

import com.example.clup.Entities.Store;
import com.example.clup.Entities.Timeslot;

public interface QueueService {
    public Timeslot getFreeTimeslot(Store store);
}
