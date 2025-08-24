package com.example.myapplication;

// interface defines a contract for classes that implement it
// basically this is a blueprint for the class

// TODO: flush out the relationship between AlarmScheduler and AndroidAlarmScheduler
public interface AlarmScheduler {
    void schedule(AlarmItem item);
    void cancel(AlarmItem item);
}