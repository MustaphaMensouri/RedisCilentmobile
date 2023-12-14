package com.example.redis_v5.redis;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.Set;

public class RedisGetAll extends AsyncTask<String, Void, ArrayList<String>> {

    private ListView listView;
    private Context context;
    private RelativeLayout sheet;
    public RedisGetAll(Context context, ListView listView, RelativeLayout relativeLayout){
        this.context = context;
        this.listView = listView;
        this.sheet = relativeLayout;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        if (params.length < 3) {
            Log.e("RedisTask", "Insufficient parameters. Expected IP, port, and operation.");
            return null;
        }

        String ip = params[0];
        int port = Integer.parseInt(params[1]);
        String operation = params[2];

        try {
            Jedis jedis = new Jedis(ip, port);

            // Perform Redis operations based on the requested operation
            switch (operation) {
                case "getAllKeys":
                    return getAllKeys(jedis);
                case "addKey":
                    return addKey(jedis, params[3], params[4], params[5]);
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
    protected void onPostExecute(ArrayList<String> result) {
        if (result != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, result);
            listView.setAdapter(adapter);
            if (sheet != null) {
                sheet.setVisibility(View.GONE);
            }
        } else {
            // Handle connection error
            Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<String> addKey(Jedis jedis, String key, String value, String ttl) {
        jedis.set(key, value);
        jedis.expire(key, Long.parseLong(ttl));
        ArrayList<String > r = new ArrayList<> ();
        r.add("Key added successfully.");
        return r;
    }

    private ArrayList<String> getAllKeys(Jedis jedis) {
        Set<String> keys = jedis.keys("*");
        return new ArrayList<>(keys);
    }

    private ArrayList<String> getKey(Jedis jedis, String key) {
        String value = jedis.get(key);
        ArrayList<String > rp = new ArrayList<> ();
        rp.add(key + ":" + value);
        return rp;
    }

    // Add more methods for additional Redis operations as needed
}
