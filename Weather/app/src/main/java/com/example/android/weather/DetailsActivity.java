package com.example.android.weather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity
{
    public Weather weather = null;
    
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_details_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        weather = (Weather) this.getIntent().getSerializableExtra("WeatherObject");
        
        SharedPreferences sp = getSharedPreferences("Settings" , MODE_PRIVATE);
    
        TextView temperature = (TextView) findViewById(R.id.detail_activity_temp_value);
    
        if (sp.getString("scale" , "").equals("Celsius"))
        {
            temperature.setText("" + weather.getTemperatureInCelsius() + " \u2103");
        }
    
        else if(sp.getString("scale" , "").equals("Fahrenheit"))
        {
            temperature.setText("" + weather.getTemperatureInFahrenheit() + " \u2109");
        }
    
        TextView weatherType = (TextView) findViewById(R.id.detail_activity_weather_value);
        weatherType.setText(weather.weatherType);
    
        TextView currentDay = (TextView) findViewById(R.id.detail_activity_day_value);
        currentDay.setText(weather.weatherWeekDay);
        
        TextView time = (TextView) findViewById(R.id.detail_activity_time_value);
        time.setText(weather.time);
    
        ImageView weatherTypeImage = (ImageView) findViewById(R.id.detail_activity_image);
        weatherTypeImage.setImageResource(weather.imageId128);
    
        View backgroundView = findViewById(R.id.detail_activity_view);
        backgroundView.setBackgroundColor(Color.parseColor(weather.weatherColor));
        
        TextView cityName = (TextView) findViewById(R.id.detail_activity_city_value);
        cityName.setText(weather.cityName);
    }
}
