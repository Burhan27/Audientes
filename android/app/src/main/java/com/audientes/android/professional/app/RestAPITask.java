package com.audientes.android.professional.app;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by morten on 03/08/2017.
 */

public class RestAPITask extends AsyncTask<TestResult, Void, String>
{
    static final String TAG = "AD RestAPITask";
    static final String API_URL = "https://audientes.sens.dk";

    private Exception exception;

    protected void onPreExecute() {

    }

    protected String doInBackground(TestResult... values) {
        Log.i(TAG, "Submitting");

        // Do some validation here
        InputStream in = null;

        try {
            URL url = new URL(API_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            // Variables
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write("");
            writer.flush();
            writer.close();
            os.close();

            int status = urlConnection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)
            {
                in = new BufferedInputStream(urlConnection.getErrorStream());
            }
            else
            {
                in = new BufferedInputStream(urlConnection.getInputStream());
            }

            // Extract Response
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            StringBuilder response  = new StringBuilder();
            String strLine;
            while ((strLine = input.readLine()) != null)
            {
                response.append(strLine);
            }
            in.close();
            String stringResponse = response.toString();

            if (status != HttpURLConnection.HTTP_OK)
            {
                Log.w(TAG, API_URL + " - " + stringResponse);
                return null;
            }
            else
            {
                Log.i(TAG, API_URL + " - " + stringResponse);
                return "";
            }

        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
    }
}
