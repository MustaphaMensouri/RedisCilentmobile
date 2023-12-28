package com.example.redis_v5.redis;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RedisSowkey extends AsyncTask<String, Void, String> {
    private Context context;
    private AutoCompleteTextView type;
    private TextInputEditText ttl;

    private TextInputEditText value;

    public RedisSowkey(Context context, AutoCompleteTextView type, TextInputEditText ttl, TextInputEditText value){
        this.context = context;
        this.type = type;
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
                case "updateKey":
                    return updateKey(jedis, params[3], params[4], params[5], params[6]);
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
            value.setText(splitArray[0]);
            ttl.setText(splitArray[1]);
            Toast.makeText(context, splitArray[3], Toast.LENGTH_SHORT).show();
            String[] items = {"string", "list", "hash map"};

            if (splitArray[3].equals("string")){
                type.setText(items[0], false);
            } else if (splitArray[3].equals("list")) {
                type.setText(items[1], false);
            }


        } else {
            // Handle connection error
            Toast.makeText(context, "error of connection", Toast.LENGTH_SHORT).show();
        }
    }

    private String getKey(Jedis jedis, String key) {
        if (jedis.type(key).equals("string")){
            String val = jedis.get(key);
            String t = String.valueOf(jedis.ttl(key));
            return val + ":" + t + ":" + key+ ":string";
        }else if (jedis.type(key).equals("list")){
            List<String> val = jedis.lrange(key, 0, -1);
            String t = String.valueOf(jedis.ttl(key));
            return val.toString() + ":" + t + ":" + key+ ":list";
        }
        return "";
    }
    private String updateKey(Jedis jedis, String key, String value, String ttl, String type) {
        if (type.equals("string")){
            jedis.set(key, value);

            if (!ttl.equals("-1")) {
                jedis.expire(key, Long.parseLong(ttl));
            }

            String val = jedis.get(key);
            String t = String.valueOf(jedis.ttl(key));
            return val + ":" + t + ":" + key+":string";
        } else if (type.equals("list")) {
            jedis.del(key);
            String result=value.replaceAll("\\s*,\\s*", ",");
            String[] elements = result.replaceAll("\\[|\\]", "").split(",");

            // Convert the array to an ArrayList
            List<String> arrayList = new ArrayList<>(Arrays.asList(elements));

            // Print the ArrayList
            for (int i =0; i<arrayList.size(); i++) {
                jedis.rpush(key, arrayList.get(i));
            }
            List<String> val = jedis.lrange(key, 0, -1);
            String t = String.valueOf(jedis.ttl(key));
            return val.toString() + ":" + t + ":" + key + ":list";

        }
        return "none:-1:none:none";
    }

    // Add more methods for additional Redis operations as needed
}
