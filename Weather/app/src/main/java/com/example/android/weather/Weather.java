package com.example.android.weather;

import android.os.Parcelable;

import java.io.Serializable;
import java.text.DecimalFormat;

import static android.R.attr.value;

/**
 * Created by Sherlock on 11/19/2017.
 */

public class Weather implements Serializable
{

    public String weatherType ;
    public String weatherDescription ;
    public String icon;
    public double terperatureInKelvin;
    public double atmosphericPressure;
    public double humidity;
    public double windSpeed;
    public double windDirectionInDegrees;
    public double percentageOfCloudiness;
    public String countryCode;
    public String cityName;
    public String [] weekDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    public String weatherWeekDay;
    public int imageId64;
    public int imageId128;
    public String time;
    public String weatherColor;

    
    public Weather
    (
            String mWeatherType,
            String mWeatherDescription,
            String mIcon,
            double mTerperatureInKelvin,
            double mAtmosphericPressure,
            double mHumidity,
            double mWindSpeed,
            double mWindDirectionInDegrees,
            double mPercentageOfCloudiness,
            String mCountryCode,
            String mCityName,
            String mWeekDay,
            String mTime
    )
    {

        weatherType = mWeatherType;
        weatherDescription = mWeatherDescription;
        icon = mIcon;
        terperatureInKelvin = mTerperatureInKelvin;
        atmosphericPressure = mAtmosphericPressure;
        humidity = mHumidity;
        windSpeed = mWindSpeed;
        windDirectionInDegrees = mWindDirectionInDegrees;
        percentageOfCloudiness = mPercentageOfCloudiness;
        countryCode = mCountryCode;
        cityName = mCityName;
        weatherWeekDay = mWeekDay;
        time = mTime;
        setImage();
    }
    
    public double getTemperatureInCelsius()
    {
        double value = (terperatureInKelvin - 273);
        return (Math.round(value * 100.0) / 100.0);
    }
    
    public double getTemperatureInFahrenheit()
    {
        double value = ((1.8 * (terperatureInKelvin - 273)) + 32);
        return (Math.round(value * 100.0) / 100.0);
    }
    
    public void setImage()
    {
        if (icon.charAt(2) == 'd')
        {
            if(icon.charAt(0) == '0' && icon.charAt(1) == '1')
            {
                imageId64 = R.drawable.clear_sun_64dp;
                imageId128 = R.drawable.clear_sun_128dp;
                weatherColor = "#FFAA00" ;
            }
    
            if(icon.charAt(0) == '0' && icon.charAt(1) == '2')
            {
                imageId64 = R.drawable.cloudy_sunny_64dp;
                imageId128 = R.drawable.cloudy_sunny_128dp;
                weatherColor = "#F7FF00";
            }
    
            if(icon.charAt(0) == '0' && icon.charAt(1) == '3')
            {
                imageId64 = R.drawable.only_clouds_64dp;
                imageId128 = R.drawable.only_clouds_128dp;
                weatherColor = "#00FFDC";
            }
    
            if(icon.charAt(0) == '0' && icon.charAt(1) == '4')
            {
                imageId64 = R.drawable.only_clouds_64dp;
                imageId128 = R.drawable.only_clouds_128dp;
                weatherColor = "#00FFDC";
            }
    
            if(icon.charAt(0) == '0' && icon.charAt(1) == '9')
            {
                imageId64 = R.drawable.rain_64dp;
                imageId128 = R.drawable.rain_128dp;
                weatherColor = "#0076FF";
            }
    
            if(icon.charAt(0) == '1' && icon.charAt(1) == '0')
            {
                imageId64 = R.drawable.rain_64dp;
                imageId128 = R.drawable.rain_128dp;
                weatherColor = "#0076FF";
            }
    
            if(icon.charAt(0) == '1' && icon.charAt(1) == '1')
            {
                imageId64 = R.drawable.thunder_storm_64dp;
                imageId128 = R.drawable.thunder_storm_128dp;
                weatherColor = "#0008FF";
            }
    
            if(icon.charAt(0) == '1' && icon.charAt(1) == '3')
            {
                imageId64 = R.drawable.snow_64dp;
                imageId128 = R.drawable.snow_128dp;
                weatherColor = "#52FF00" ;
            }
    
            if(icon.charAt(0) == '5' && icon.charAt(1) == '0')
            {
                imageId64 = R.drawable.morning_fog_64dp;
                imageId128 = R.drawable.morning_fog_128dp;
                weatherColor = "#C8FF00";
            }
        }
        
        else if (icon.charAt(2) == 'n')
        {
            if(icon.charAt(0) == '0' && icon.charAt(1) == '1')
            {
                imageId64 = R.drawable.clear_moon_64dp;
                imageId128 = R.drawable.clear_moon_128dp;
                weatherColor = "#A500FF";
            }
    
            if(icon.charAt(0) == '0' && icon.charAt(1) == '2')
            {
                imageId64 = R.drawable.cloudy_night_64dp;
                imageId128 = R.drawable.cloudy_night_128dp;
                weatherColor = "#E700FF" ;
            }
    
            if(icon.charAt(0) == '0' && icon.charAt(1) == '3')
            {
                imageId64 = R.drawable.only_clouds_64dp;
                imageId128 = R.drawable.only_clouds_128dp;
                weatherColor = "#00FFDC";
            }
    
            if(icon.charAt(0) == '0' && icon.charAt(1) == '4')
            {
                imageId64 = R.drawable.only_clouds_64dp;
                imageId128 = R.drawable.only_clouds_128dp;
                weatherColor = "#00FFDC" ;
            }
    
            if(icon.charAt(0) == '0' && icon.charAt(1) == '9')
            {
                imageId64 = R.drawable.rain_64dp;
                imageId128 = R.drawable.rain_128dp;
                weatherColor = "#0076FF";
            }
    
            if(icon.charAt(0) == '1' && icon.charAt(1) == '0')
            {
                imageId64 = R.drawable.rain_64dp;
                imageId128 = R.drawable.rain_128dp;
                weatherColor = "#0076FF";
            }
    
            if(icon.charAt(0) == '1' && icon.charAt(1) == '1')
            {
                imageId64 = R.drawable.thunder_storm_64dp;
                imageId128 = R.drawable.thunder_storm_128dp;
                weatherColor = "#000CFF";
            }
    
            if(icon.charAt(0) == '1' && icon.charAt(1) == '3')
            {
                imageId64 = R.drawable.snow_64dp;
                imageId128 = R.drawable.snow_128dp;
                weatherColor = "#00B1FF";
            }
    
            if(icon.charAt(0) == '5' && icon.charAt(1) == '0')
            {
                imageId64 = R.drawable.night_fog_64dp;
                imageId128 = R.drawable.night_fog_128dp;
                weatherColor = "#FF00AD";
            }
        }
    }
}