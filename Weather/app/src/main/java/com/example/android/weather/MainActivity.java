package com.example.android.weather;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.weather.R.id.city;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener
{
    private static final String REQUEST_URL = "http://api.openweathermap.org/data/2.5/forecast?";
    
    private String oldLocation = null;
    
    private String API_ID = "cbffeef14355846097e3a1d3a01e8ffb";
    
    private String completeRequestURL = null;
    
    public DataAdapter adapter = null;
    public ListView weatherListView = null;
    public static Weather mainWeather = null;
    public SharedPreferences sp = null;
    
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        sp = getSharedPreferences("Settings", MODE_PRIVATE);
        
        if(sp.getString("city", "").equals(""))
        {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.putExtra("state", 1);
            startActivity(intent);
            finish();
        }
        
        setContentView(R.layout.progress_null_activity);
    
        adapter = new DataAdapter(this, new ArrayList<Weather>());
        
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        
        if(isConnected == true)
        {
            initTask();
        }
        
        else
        {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);
            
            TextView empty_textview = (TextView) findViewById(R.id.empty);
            empty_textview.setText("No Internet Connection");
        }
    }
    
    @Override
    protected void onPause ()
    {
        super.onPause();
        
        oldLocation = sp.getString("city" , "");
    }
    
    @Override
    protected void onResume ()
    {
        super.onResume();
        
        String newLocation = sp.getString("city", "");
        
        if(!(newLocation.equalsIgnoreCase(oldLocation)))
        {
            if(oldLocation != null)
            {
                setContentView(R.layout.progress_null_activity);
                initTask();
            }
        }
    }
    
    public void initTask ()
    {
        QueryUtils.check = 0;
        adapter.clear();
        sp = getSharedPreferences("Settings", MODE_PRIVATE);
        String currentCity = sp.getString("city", "");
        
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        
        uriBuilder.appendQueryParameter("q", currentCity);
        uriBuilder.appendQueryParameter("appid", API_ID);
        
        completeRequestURL = uriBuilder.toString();
        
        new WeatherAsyncTask().execute(completeRequestURL);
    }
    
    public void updateUI (Weather weather)
    {
        sp = getSharedPreferences("Settings", MODE_PRIVATE);
        
        TextView temperature = (TextView) findViewById(R.id.temperature);
        
        if(sp.getString("scale", "").equals("Celsius"))
        {
            temperature.setText("" + weather.getTemperatureInCelsius() + " \u2103");
        }
        
        else if(sp.getString("scale", "").equals("Fahrenheit"))
        {
            temperature.setText("" + weather.getTemperatureInFahrenheit() + " \u2109");
        }
        
        TextView weatherType = (TextView) findViewById(R.id.weather_type);
        weatherType.setText(weather.weatherType);
        
        TextView cityName = (TextView) findViewById(city);
        cityName.setText(weather.cityName);
        
        TextView currentDay = (TextView) findViewById(R.id.current_day);
        currentDay.setText(weather.weatherWeekDay);
        
        ImageView weatherTypeImage = (ImageView) findViewById(R.id.weather_type_image);
        weatherTypeImage.setImageResource(weather.imageId128);
        
        View backgroundView = findViewById(R.id.main_activity_view);
        backgroundView.setBackgroundColor(Color.parseColor(weather.weatherColor));
    }
    
    @Override
    public void onItemClick (AdapterView<?> adapterView, View view, int i, long l)
    {
        Weather currentWeather = adapter.getItem(i);
        
        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
        intent.putExtra("WeatherObject", currentWeather);
        startActivity(intent);
    }
    
    @Override
    public void onClick (View v)
    {
        int id = v.getId();
        
        if(id == R.id.setting_image)
        {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.putExtra("state", 0);
            startActivity(intent);
        }
        
        else if(id == R.id.details_image)
        {
            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
            intent.putExtra("WeatherObject", mainWeather);
            startActivity(intent);
        }
        
        else if(id == R.id.refresh_image)
        {
            setContentView(R.layout.progress_null_activity);
            initTask();
        }
    }
    
    private class WeatherAsyncTask extends AsyncTask<String, Void, ArrayList<Weather>>
    {
        @Override
        protected ArrayList<Weather> doInBackground (String... params)
        {
            ArrayList<Weather> weathers = QueryUtils.fetchWeatherData(params[0]);
            return weathers;
        }
        
        @Override
        protected void onPostExecute (ArrayList<Weather> weathers)
        {
            super.onPostExecute(weathers);
            
            if(weathers != null && !(weathers.isEmpty()))
            {
                adapter.addAll(weathers);
                
                setContentView(R.layout.activity_main);
                
                updateUI(mainWeather);
                
                weatherListView = (ListView) findViewById(R.id.listview);
                
                weatherListView.setAdapter(adapter);
    
                ImageView settingsButton = (ImageView) findViewById(R.id.setting_image);
                settingsButton.setOnClickListener(MainActivity.this);
    
                ImageView detailsButton = (ImageView) findViewById(R.id.details_image);
                detailsButton.setOnClickListener(MainActivity.this);
    
                ImageView refreshButton = (ImageView) findViewById(R.id.refresh_image);
                refreshButton.setOnClickListener(MainActivity.this);
    
                weatherListView.setOnItemClickListener(MainActivity.this);
            }
            
            else
            {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
                progressBar.setVisibility(View.GONE);
                
                TextView empty_textview = (TextView) findViewById(R.id.empty);
                empty_textview.setText("No Weathers Found");
                
                adapter.clear();
            }
        }
    }
}