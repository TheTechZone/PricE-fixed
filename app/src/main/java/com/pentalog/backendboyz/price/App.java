package com.pentalog.backendboyz.price;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class App extends AppCompatActivity {
    private LocationManager mLocationManager;
    private double mLatitude,mLongitude;
    //private TextView mLocationLabel;
    //private TextView mCityLabel;
    private  Context mContext;
    private  String mCity;

     private DrawerLayout mDrawerLayout;
     private ActionBarDrawerToggle mActionBarDrawerToggle;

    //FragmentTransaction mFragmentTransaction;
    //Nav

    private ImageButton mImageButton;

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRecyclerView;
    private AdapterList mAdapterList;

    SearchView searchView = null;

    String jsonData;


    private static final String TAG = "LOCATION STUFF PLZ WORK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_opened,R.string.drawer_closed){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.drawer_opened);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.drawer_closed);
                }
            }
        };

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        //mFragmentTransaction = getSupportFragmentManager().beginTransaction();

        //mFragmentTransaction.add(R.id.main_container,new HomeFragment());
        //mFragmentTransaction.commit();




        Intent mIntent = getIntent();
        final String email = mIntent.getStringExtra("user");
        final String pass = mIntent.getStringExtra("pass");
//        Log.d("email:",email);
//        Log.d("pass:",pass);
        //mLocationLabel = (TextView) findViewById(R.id.locationLabel);
        //mCityLabel = (TextView) findViewById(R.id.cityLabel);

        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        mImageButton = (ImageButton) findViewById(R.id.imageButton);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(App.this, "Potatoes", Toast.LENGTH_SHORT).show();
                Intent addItem = new Intent(App.this,AddItemForm.class);
                startActivity(addItem);
            }
        });

        LocationListener locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(final Location location) {
                Log.d(TAG, location.toString());

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
              //  mLocationLabel.setText(mLatitude+" , "+mLongitude);
                String mApiUrl="http://nominatim.openstreetmap.org/reverse?format=json&lat=" +mLatitude+ "&lon=" +mLongitude;


//
                if(isNetworkAvailable()) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(mApiUrl)
                            .build();

                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                String jsonData = response.body().string();
                                if (response.isSuccessful()){
                                    Log.d(TAG,jsonData);
                                    mCity = getCurrentCity(jsonData);
                                    Log.d(TAG,mCity);


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                           //mCityLabel.setText(mCity);
                                        }
                                    });
                                    mCity =        Normalizer
                                            .normalize(mCity, Normalizer.Form.NFD)
                                            .replaceAll("[^\\p{ASCII}]", "");
                                    String type= "location";
                                    new LocationWorker(mContext).execute(type,email,mCity);

                                }
                                else{
                                    alertUserAboutError();
                                }

                            } catch (IOException e) {
                                Log.e(TAG, "Exception Caught:", e);
                            }
                              catch (JSONException e){
                                Log.e(TAG, "Exception Caught:", e);
                              }

                        }
                    });

                }

                else {
                    Toast.makeText(App.this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
                }
//
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private String getCurrentCity(String jsonData) throws JSONException{
        JSONObject response = new JSONObject(jsonData);
        JSONObject address = response.getJSONObject("address");
        String mCity;

        if (address.has("city")){
            mCity = address.getString("city");
        }
        else {
            mCity = address.getString("town");
        }
        return mCity;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return  isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
     //   return true;}

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchManager searchManager = (SearchManager) App.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(App.this.getComponentName()));
            searchView.setIconified(false);
        }
        
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuItem1 :
                Toast.makeText(App.this, "Made with <3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuItem2 :
                finish();
                System.exit(0);
                break;
            default: break;
        }

//
//        if (mActionBarDrawerToggle.onOptionsItemSelected(item)){
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent) {
        // Get search query and create object of class AsyncFetch
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }
            new AsyncFetch(query).execute();

        }
    }

    // Create class AsyncFetch
    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(App.this);
        HttpURLConnection conn;
        URL url = null;
        String search;

        public AsyncFetch(String search){
            this.search=search;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://sirpotato.esy.es/search.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("search", search);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }


        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            pdLoading.dismiss();
            List<DataItem> data=new ArrayList<>();

            pdLoading.dismiss();
            if(result.equals("no rows")) {
                Toast.makeText(App.this, "No Results found for entered query", Toast.LENGTH_LONG).show();
            }else{

                try {

                    Log.e("WTFFFF",result);

                    JSONArray jArray = new JSONArray(result);

                    Log.e("WTFFF",jArray.toString());
                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        //Log.d("cartofi",json_data.getString("location"));
                          //  if(json_data.getString("location").equals(mCity)){
                            DataItem productData = new DataItem();
                            productData.productName = json_data.getString("name");
                            productData.productDescription = json_data.getString("description");
                            productData.productStore = json_data.getString("location");
                            productData.productPrice = json_data.getInt("price");
                            data.add(productData);
                        //}
                    }

                    // Setup and Handover data to recyclerview
                    mRecyclerView = (RecyclerView) findViewById(R.id.itemList);
                    mAdapterList = new AdapterList(App.this, data);
                    mRecyclerView.setAdapter(mAdapterList);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(App.this));

                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    Toast.makeText(App.this, e.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(App.this, result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }

}
