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
import java.io.PipedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class AddWorker extends AsyncTask<String, Void, String> {
    Context mContext;

    AddWorker(Context ctx){
        mContext = ctx;
    }

    String name,description,price,store;

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String locationUrl = "http://sirpotato.esy.es/add.php";
        String name,description, store, price;

        if(type.equals("add")){
            try{
                name = params[1];
                description = params[2];
                store = params[3];
                price = params[4];
                Log.d("wdwd",name+" "+description+" "+price+" "+store+" ");
               // description = params[4];
                URL url = new URL(locationUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String postData = URLEncoder.encode("name","UTF-8") + "=" +URLEncoder.encode(name,"UTF-8")
                        +"&"+URLEncoder.encode("store","UTF-8") + "=" +URLEncoder.encode(store,"UTF-8")
                        +"&"+URLEncoder.encode("price","UTF-8") + "=" +URLEncoder.encode(price,"UTF-8")
                        +"&"+URLEncoder.encode("description","UTF-8") + "=" +URLEncoder.encode(description,"UTF-8");
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
}