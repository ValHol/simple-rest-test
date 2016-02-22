package com.valhol.SolstCodeExam.events;

/**
 * Created by Valentín on 22-Feb-16.
 */
public class ContactSelectedEvent {
    public final long employeeId;

    public ContactSelectedEvent(long employeeId) {
        this.employeeId = employeeId;
    }
}
