package com.example.myapplication;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;


public class GoogleDirections {

    public static void fetchDirections(Context context, String mode, String arrival_time, String origin, String destination, DirectionsCallback callback) {
        String urlString = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin
                + "&destination=" + destination
                + "&arrival_time=" + arrival_time
                + "&mode=" + mode
                + "&key=" + BuildConfig.MAPS_API_KEY;;

        Data inputData = new Data.Builder()
                .putString("url", urlString)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(FetchWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueue(workRequest);

        LiveData<WorkInfo> liveData = workManager.getWorkInfoByIdLiveData(workRequest.getId());
        liveData.observeForever(new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    liveData.removeObserver(this); // stop observing once finished

                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        int durationSeconds = workInfo.getOutputData().getInt("durationSeconds", -1);
                        if (durationSeconds != -1) {
                            callback.onSuccess(durationSeconds);
                        } else {
                            callback.onFailure(new Exception("No duration found in response"));
                        }
                    } else {
                        callback.onFailure(new Exception("Directions request failed"));
                    }
                }
            }
        });
    }

    public static class FetchWorker extends Worker {
        public FetchWorker(@NonNull Context context, @NonNull WorkerParameters params) {
            super(context, params);
        }

        @NonNull
        @Override
        public Result doWork() {
            String urlString = getInputData().getString("url");
            String response;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                response = sb.toString();

                JSONObject json = new JSONObject(response);
                JSONArray routes = json.getJSONArray("routes");
                JSONObject firstRoute = routes.getJSONObject(0);
                JSONArray legs = firstRoute.getJSONArray("legs");
                JSONObject firstLeg = legs.getJSONObject(0);
                JSONObject duration = firstLeg.getJSONObject("duration");

                // this is the number of seconds
                int durationSeconds = duration.getInt("value");

                Data output = new Data.Builder()
                        .putInt("durationSeconds", durationSeconds)
                        .build();

                Log.d("GoogleDirections", "Duration: " + durationSeconds);

                Log.d("GoogleDirections", response);

                return Result.success(output);

            } catch (Exception e) {
                Log.e("GoogleDirections", "Error fetching directions", e);
                return Result.failure();
            }

        }
    }
}

