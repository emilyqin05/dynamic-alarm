package com.example.myapplication;
import java.time.LocalDateTime;


// this class will be used to store the alarm items
// return what the alarm will do once set off
public class AlarmItem {
    // unique identifier for the alarm
    private final int id;
    // this is the time the alarm will go off
    // TODO: make setters to change this
    private LocalDateTime time;
    // this is what the alarm will do once set off.
    // TODO: change this into an alarm
    private final String message;
    // constructor
    // When you do new AlarmItem(...), this runs
    public AlarmItem(int id, LocalDateTime time, String message) {
        this.id = id;
        this.time = time;
        this.message = message;
    }

    public int getId() {
        return id;
    }
    public LocalDateTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

}
