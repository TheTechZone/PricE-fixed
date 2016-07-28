package com.pentalog.backendboyz.price;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LocationWorker extends AsyncTask<String, Void, String> {
    Context mContext;

    LocationWorker(Context ctx){
        mContext = ctx;
    }

    String email;

    String location;

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String locationUrl = "http://sirpotato.esy.es/location.php";
        if(type.equals("location")){
            try{
                email = params[1];
                location = params[2];
                URL url = new URL(locationUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String postData = URLEncoder.encode("email","UTF-8") + "=" +URLEncoder.encode(email,"UTF-8")
                        +"&"+URLEncoder.encode("location","UTF-8") + "=" +URLEncoder.encode(location,"UTF-8");
                Log.d("test",postData);
                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //return null;
        return null;
    }

    @Override
    protected void onPreExecute() {
        //mAlertDialog = new AlertDialog.Builder(mContext).create();
       // mAlertDialog.setTitle("Login Status");
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
