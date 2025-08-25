package com.example.myapplication;

public interface DirectionsCallback {
    void onSuccess(int durationSeconds);
    void onFailure(Exception e);
}
