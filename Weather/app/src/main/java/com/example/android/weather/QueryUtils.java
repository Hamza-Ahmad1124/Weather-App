package com.example.android.weather;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class QueryUtils
{
    public static int check = 0;
    
    private QueryUtils ()
    {}
    
    public static ArrayList<Weather> fetchWeatherData(String url)
    {
        URL requestURL = createURL(url);
        String jsonResponse = "" ;
        
        try
        {
            jsonResponse = makeHttpRequest(requestURL);
        }
        
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return extractWeathers(jsonResponse);
    }
    
    private static URL createURL(String requestURL)
    {
        URL url = null;
        
        try
        {
            url = new URL(requestURL);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        
        return url;
    }
    
    private static String makeHttpRequest (URL url) throws IOException
    {
        String jsonResponse = "";
        
        if (url == null)
        {
            return jsonResponse ;
        }
        
        InputStream stream = null;
        HttpURLConnection urlConnection = null;
        
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            
            if (urlConnection.getResponseCode() == 200)
            {
                stream = urlConnection.getInputStream();
                jsonResponse = readFromStream(stream);
            }
            else
            {
                Log.i("QueryUtils.java" , "Response Code not 200");
            }
        }
        
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        finally
        {
            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }
            
            if(stream != null)
            {
                // function must handle java.io.IOException here
                stream.close();
            }
        }
        
        return jsonResponse;
    }
    
    private static String readFromStream (InputStream stream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        
        if (stream != null)
        {
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            
            String line = bufferedReader.readLine();
            
            while (line != null)
            {
                output.append(line);
                line = bufferedReader.readLine();
            }
            
            return output.toString();
        }
        
        return "";
    }
    
    public static ArrayList<Weather> extractWeathers (String jsonResponse)
    {
        if (TextUtils.isEmpty(jsonResponse))
        {
            return null;
        }
        
        ArrayList<Weather> weather = new ArrayList<>();
        
        try
        {
            Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
            Date currentLocalTime1 = cal1.getTime();
            DateFormat date1 = new SimpleDateFormat("HH");
            date1.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
            String currentHour = date1.format(currentLocalTime1);
            
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray listArray = root.getJSONArray("list");
            
            for (int i = 0 ; i < listArray.length() ; i++)
            {
                JSONObject listObject = listArray.getJSONObject(i);
                
                String weatherDateTime = listObject.getString("dt_txt");
                String [] randomArray = new String [3];
                
                randomArray = weatherDateTime.split(" ");
                
                randomArray = randomArray[1].split(":");
                String givenHour = randomArray[0];
                
                if ((Integer.parseInt(givenHour) == (Integer.parseInt(currentHour)))||(Integer.parseInt(givenHour) == (Integer.parseInt(currentHour) + 1))||(Integer.parseInt(givenHour) == (Integer.parseInt(currentHour) + 2)))
                {
                    String [] randomArray2 = weatherDateTime.split(" ");
                    
                    String time = null;
                    
                    if(weather.size() == 0 && check == 0)
                    {
                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
                        Date currentLocalTime = cal.getTime();
                        DateFormat date = new SimpleDateFormat("HH:mm:ss");
                        date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
                        time = date.format(currentLocalTime);
                    }
                    
                    else
                    {
                        time = randomArray2[1];
                    }
                    
                    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    
                    try
                    {
                        date = inFormat.parse(randomArray2[0]);
                    }
                    
                    catch (ParseException e) {e.printStackTrace();}
                    
                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                    String dayOfWeek = outFormat.format(date);
                    
                    JSONObject mainObject = listObject.getJSONObject("main");
                    JSONArray weatherArray = listObject.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    JSONObject cloudsOBject = listObject.getJSONObject("clouds");
                    JSONObject windObject = listObject.optJSONObject("wind");
                    JSONObject cityObject = root.getJSONObject("city");
                    
                    if (weather.size() == 0 && check == 0)
                    {
                        MainActivity.mainWeather = new Weather
                                (
                                        weatherObject.getString("main"),
                                        weatherObject.getString("description"),
                                        weatherObject.getString("icon"),
                                        mainObject.getDouble("temp"),
                                        mainObject.getDouble("pressure"),
                                        mainObject.getDouble("humidity"),
                                        windObject.getDouble("speed"),
                                        windObject.getDouble("deg"),
                                        cloudsOBject.getDouble("all"),
                                        cityObject.getString("country"),
                                        cityObject.getString("name"),
                                        dayOfWeek,
                                        time
                                );
                        
                        check = 1;
                    }
                    
                    else
                    {
                        weather.add(new Weather
                                (
                                        weatherObject.getString("main"),
                                        weatherObject.getString("description"),
                                        weatherObject.getString("icon"),
                                        mainObject.getDouble("temp"),
                                        mainObject.getDouble("pressure"),
                                        mainObject.getDouble("humidity"),
                                        windObject.getDouble("speed"),
                                        windObject.getDouble("deg"),
                                        cloudsOBject.getDouble("all"),
                                        cityObject.getString("country"),
                                        cityObject.getString("name"),
                                        dayOfWeek,
                                        time
                                ));
                    }
                }
            }
        }
        
        catch (JSONException e)
        {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the weather JSON results", e);
        }
        
        return weather;
    }
}
