package com.example.android.weather;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AutoCompleteTextAdapter extends ArrayAdapter implements Filterable
{
    ArrayList<String> result;
    Context cont;
    
    @Override
    public int getCount ()
    {
        return result.size();
    }
    
    public AutoCompleteTextAdapter (@NonNull Context context, @LayoutRes int resource)
    {
        super(context, resource);
        result = new ArrayList<String>();
        cont = context;
    }
    
    @Nullable
    @Override
    public Object getItem (int position)
    {
        return result.get(position);
    }
    
    @Override
    public int getPosition (@Nullable Object item)
    {
        return result.indexOf(item);
    }
    
    @NonNull
    @Override
    public Filter getFilter ()
    {
        Filter filter = new Filter()
        {
            @Override
            protected FilterResults performFiltering (CharSequence charSequence)
            {
                FilterResults filterResults = new FilterResults();
                ArrayList<String> temp = new ArrayList<String>();
     
                if(charSequence != null)
                {
                    Matching_places mp = new Matching_places();
                    temp = mp.doInBackground(charSequence.toString());
                    // Log.i(charSequence.toString(),charSequence.toString());
                    // Toast.makeText(getContext(),charSequence.toString(),Toast.LENGTH_LONG).show();
                    result = temp;
                    filterResults.values = temp;
                    filterResults.count = temp.size();
                    
                }
                
                return filterResults;
            }
            
            @Override
            protected void publishResults (CharSequence charSequence, FilterResults filterResults)
            {
                if(filterResults != null && filterResults.count > 0)
                {
                    
                    notifyDataSetChanged();
                }
     
                else
                {
                    notifyDataSetInvalidated();
                }
            }
        };
        
        return filter;
    }
    
    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View v = convertView;
        
        if(v == null)
        {
            LayoutInflater li = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.city_suggestions, parent, false);
            
        }
        
        TextView tv = (TextView) v.findViewById(R.id.city);
        String data = result.get(position);
        // Log.i(data,data+"Viewwww");
        tv.setText(data);
        return v;
    }
    
    public class Matching_places
    {
        String path = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
        String key = "AIzaSyDgaKJFJU05vltmjJrlqQdagF-XN1ow4b8";
        
        public ArrayList<String> doInBackground (String... prams)
        {
            ArrayList<String> t = new ArrayList<String>();
            String url_string = path + prams[0] + "&types=(cities)&key=" + key;
            StringBuilder stringBuilder = new StringBuilder();
            
            try
            {
                URL url = new URL(url_string);
                InputStream insrt;
                
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(500);
                
                if(con.getResponseCode() == 200)
                {
                    insrt = con.getInputStream();
                    InputStreamReader bin = new InputStreamReader(insrt);
                    BufferedReader bufferedReader = new BufferedReader(bin);
                    
                    String line = bufferedReader.readLine();
                    
                    while (line != null)
                    {
                        stringBuilder.append(line);
                        line = bufferedReader.readLine();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                
            }
            try
            {
                JSONObject root = new JSONObject(stringBuilder.toString());
                JSONArray predictions = root.getJSONArray("predictions");
                Log.i(" predictions", "" + predictions.length());
 
                for (int i = 0; i < predictions.length(); i++)
                {
                    t.add(predictions.getJSONObject(i).getString("description"));
                    Log.i(" " + i, "" + predictions.getJSONObject(i).getString("description"));
                }
                
            }
            catch (JSONException m)
            {
                m.printStackTrace();
            }
            return t;
        }
    }
}