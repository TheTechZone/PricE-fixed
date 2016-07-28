package com.pentalog.backendboyz.price;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

public class BackgroundWorker extends AsyncTask<String, Void, String> {
    Context mContext;
    AlertDialog mAlertDialog;
    BackgroundWorker(Context ctx){
        mContext = ctx;
    }
    String email;

    String password;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String loginUrl = "http://sirpotato.esy.es/login.php";
        String registerUrl = "http://sirpotato.esy.es/register.php";
        if (type.equals("login")){
            try {
                email = params[1];
                password = params[2];
                URL url = new URL(loginUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String postData = URLEncoder.encode("email","UTF-8") + "=" +URLEncoder.encode(email,"UTF-8")
                        +"&"+URLEncoder.encode("pass","UTF-8") + "=" +URLEncoder.encode(password,"UTF-8");
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
        if (type.equals("register")){
            try {
                String email = params[1];
                String password = params[2];
                URL url = new URL(registerUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String postData = URLEncoder.encode("email","UTF-8") + "=" +URLEncoder.encode(email,"UTF-8")
                        +"&"+URLEncoder.encode("pass","UTF-8") + "=" +URLEncoder.encode(password,"UTF-8");
                Log.d("linkuletz",postData);
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

        return null;
    }


    @Override
    protected void onPreExecute() {
       mAlertDialog = new AlertDialog.Builder(mContext).create();
       mAlertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
      mAlertDialog.setMessage(result);
       mAlertDialog.show();
        //Toast.makeText(mContext, "Login successful.", Toast.LENGTH_SHORT).show();

        if (!result.equals("Cancer!")){

            Intent intent = new Intent(mContext,App.class);
            intent.putExtra("user",email);
            intent.putExtra("pass",password);
            mContext.startActivity(intent);

        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
