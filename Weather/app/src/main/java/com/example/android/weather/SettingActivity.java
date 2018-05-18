package com.example.android.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SettingActivity extends AppCompatActivity
{
    SharedPreferences sp;
    RadioGroup rg;
    Location l;
    AutoCompleteTextView autoCompleteTextView = null;
    
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    
        Intent i = getIntent();
        int state = i.getIntExtra("state", 10);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_settings_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        
        if (state != 1)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        Button save = (Button) findViewById(R.id.save);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
       
        AutoCompleteTextAdapter names = new AutoCompleteTextAdapter(this, R.layout.city_suggestions);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.enterLocation);
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.setAdapter(names);
    
        sp = getSharedPreferences("Settings", MODE_PRIVATE);
        
        if (!(sp.getString("city" , "") .equals("")))
        {
            autoCompleteTextView.setHint(sp.getString("city", ""));
        }
        
        ImageButton gps = (ImageButton) findViewById(R.id.gps_button);
       
        gps.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                GetLocationFromGps();
            }
        });
        
        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                String [] locations = autoCompleteTextView.getText().toString().split(",");
                
                if(locations[0].equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Enter a Valid Location", Toast.LENGTH_SHORT).show();
                }
                
                else
                {
                    sp = getSharedPreferences("Settings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    
                    int id = rg.getCheckedRadioButtonId();
                    
                    if(id == R.id.radioButton2)
                    {
                        editor.putString("scale", "Celsius");
                    }
                    
                    else
                    {
                        editor.putString("scale", "Fahrenheit");
                    }
                    
                    editor.putString("city", locations[0]);
    
                    editor.apply();
    
                    Intent i = getIntent();
                    int state = i.getIntExtra("state", 10);
                    
                    if (state == 1)
                    {
                        Intent intent = new Intent (getApplicationContext() , MainActivity.class);
                        startActivity(intent);
                    }
                    
                    finish();
                }
            }
        });
    }
    
    public Location GetLocationFromGps ()
    {
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, new LocationListener()
            {
                @Override
                public void onLocationChanged (Location location)
                {
                    
                    NameFromGooglePlace gp = new NameFromGooglePlace();
                    gp.execute(location);
                    
                    if(location != null)
                    {
                        lm.removeUpdates(this);
                    }
                }
                
                @Override
                public void onStatusChanged (String s, int i, Bundle bundle)
                {
                    
                }
                
                @Override
                public void onProviderEnabled (String s)
                {
                    
                }
                
                @Override
                public void onProviderDisabled (String s)
                {
                    Toast.makeText(getApplicationContext(), "Enable GPS", Toast.LENGTH_LONG).show();
                }
            });
            
        }
        return l;
    }
    
    public class NameFromGooglePlace extends AsyncTask<Location, Void, String>
    {
        @Override
        protected String doInBackground (Location... locations)
        {
            StringBuilder loc = new StringBuilder();
            String urlString = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
            String key = "AIzaSyCnq3A-9TlRa5UR-0q9UcRRrAxqOwuGmiM";
            urlString = urlString + locations[0].getLatitude() + "," + locations[0].getLongitude() + "&key=" + key;
            
            try
            {
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream ins = con.getInputStream();
                InputStreamReader bin = new InputStreamReader(ins);
                BufferedReader br = new BufferedReader(bin);
                String line = br.readLine();
              
                while (line != null)
                {
                    loc.append(line);
                    line = br.readLine();
                }
            }
            
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            return loc.toString();
        }
        
        @Override
        protected void onPostExecute (String s)
        {
            super.onPostExecute(s);
            
            try
            {
                JSONObject root = new JSONObject(s);
                JSONArray address_components = root.getJSONArray("results");
                JSONObject resultcomp = address_components.getJSONObject(0);
                JSONArray secondArray = resultcomp.getJSONArray("address_components");
                JSONObject finalpbj = secondArray.optJSONObject(3);
                String name = finalpbj.getString("long_name");
                String n = "";
                
                for (int i = 0; i < name.length(); i++)
                {
                    if(name.charAt(i) == ' ' || name.charAt(i) == ',')
                    {
                        break;
                    }
                    else
                    {
                        n = n + name.charAt(i);
                    }
                }
                
                name = n;
                
                autoCompleteTextView.setText(name);
                autoCompleteTextView.dismissDropDown();
                
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
