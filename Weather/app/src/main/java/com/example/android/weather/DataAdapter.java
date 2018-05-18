package com.example.android.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.example.android.weather.R.id.temperature;

/**
 * Created by Sherlock on 8/5/2017.
 */

public class DataAdapter extends ArrayAdapter<Weather>
{
    public DataAdapter (Context context, ArrayList<Weather> objects)
    {
        super(context, 0, objects);
    }
    
    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItemView = convertView;
    
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        
        Weather currentInfo = getItem(position);
        
        TextView temperatureItem = (TextView) listItemView.findViewById(R.id.temperature_item);
        TextView weatherTypeItem = (TextView) listItemView.findViewById(R.id.weather_type_item);
        TextView currentDayItem = (TextView) listItemView.findViewById(R.id.current_day_item);
    
        SharedPreferences sp = getContext().getSharedPreferences("Settings" , MODE_PRIVATE);
        
        if (sp.getString("scale" , "").equals("Celsius"))
        {
            temperatureItem.setText("" + currentInfo.getTemperatureInCelsius() + " \u2103");
        }
    
        else if(sp.getString("scale" , "").equals("Fahrenheit"))
        {
            temperatureItem.setText("" + currentInfo.getTemperatureInFahrenheit() + " \u2109");
        }
        
        weatherTypeItem.setText(currentInfo.weatherType);
        
        currentDayItem.setText(currentInfo.weatherWeekDay);
    
        ImageView weatherTypeImageItem = (ImageView) listItemView.findViewById(R.id.image_item);
        weatherTypeImageItem.setImageResource(currentInfo.imageId64);
        
        return listItemView;
    }
}