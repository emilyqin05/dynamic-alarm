package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleDirections {
    private static final String API_KEY = "";
    private static final String MODE = "driving";
    private static final String ARRIVAL_TIME = "1766749200";
    private static final String ORIGIN = "Vancouver";
    private static final String DESTINATION = "Coquitlam";

    public static void fetchDirections() {
        String urlString = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + ORIGIN
                + "&destination=" + DESTINATION
                + "&arrival_time=" + ARRIVAL_TIME
                + "&mode=" + MODE
                + "&key=" + API_KEY;

        new FetchTask().execute(urlString);
    }

    private static class FetchTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                response = sb.toString();
            } catch (Exception e) {
                Log.e("GoogleDirections", "Error fetching directions", e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("GoogleDirections", result);
        }
    }
}
