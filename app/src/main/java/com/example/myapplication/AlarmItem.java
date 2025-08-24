package com.example.myapplication;
import java.time.LocalDateTime;


// this class will be used to store the alarm items
// return what the alarm will do once set off
public class AlarmItem {
    // this is the time the alarm will go off
    // TODO: make setters to change this
    private LocalDateTime time;
    // this is what the alarm will do once set off.
    // TODO: change this into an alarm
    private final String message;
    // constructor
    // When you do new AlarmItem(...), this runs
    public AlarmItem(LocalDateTime time, String message) {
        this.time = time;
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

}
