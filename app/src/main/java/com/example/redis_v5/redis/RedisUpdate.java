package com.example.redis_v5.redis;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisUpdate extends AsyncTask<String, Void, String> {
    private Context context;
    private AutoCompleteTextView type;
    private TextInputEditText name;
    private TextInputEditText ttl;

    private TextInputEditText value;

    public RedisUpdate(Context context, AutoCompleteTextView type, TextInputEditText name, TextInputEditText ttl, TextInputEditText value){
        this.context = context;
        this.type = type;
        this.name = name;
        this.ttl = ttl;
        this.value = value;
    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length < 3) {
            Log.e("RedisTask", "Insufficient parameters. Expected IP, port, and operation.");
            Toast.makeText(context, "Insufficient parameters. Expected IP, port, and operation.", Toast.LENGTH_SHORT).show();
            return null;
        }

        String ip = params[0];
        int port = Integer.parseInt(params[1]);
        String operation = params[2];

        try {
            Jedis jedis = new Jedis(ip, port);

            // Perform Redis operations based on the requested operation
            switch (operation) {
                case "getKey":
                    return getKey(jedis, params[3]);
// Add more cases for other operations as needed
                default:
                    return null;
            }
        } catch (JedisConnectionException e) {
            Log.e("RedisConnectionError", "Failed to connect to Redis server", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            String[] splitArray = result.split(":");
            name.setText(splitArray[2]);
            value.setText(splitArray[0]);
            ttl.setText(splitArray[1]);


        } else {
            // Handle connection error
            Toast.makeText(context, "error of connection", Toast.LENGTH_SHORT).show();
        }
    }

    private String getKey(Jedis jedis, String key) {
        String val = jedis.get(key);
        String t = String.valueOf(jedis.ttl(key));
        return val+":"+t+":"+key;
    }

    // Add more methods for additional Redis operations as needed
}