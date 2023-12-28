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

import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RedisGetAll extends AsyncTask<String, Void, ArrayList<String>> {

    private ListView listView;
    private Toolbar toolbar;
    private Context context;
    private RelativeLayout sheet;
    private String host;
    public RedisGetAll(Context context, ListView listView, RelativeLayout relativeLayout, Toolbar toolbar){
        this.context = context;
        this.listView = listView;
        this.toolbar = toolbar;
        this.sheet = relativeLayout;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        if (params.length < 3) {
            Log.e("RedisTask", "Insufficient parameters. Expected IP, port, and operation.");
            return null;
        }

        String ip = params[0];
        host = ip;
        int port = 6379;
        if(!params[1].isEmpty()){
        port = Integer.parseInt(params[1]);
        }
        String operation = params[2];

        try {

            Jedis jedis = new Jedis(ip, port);

            // Perform Redis operations based on the requested operation
            switch (operation) {
                case "getAllKeys":
                    return getAllKeys(jedis, params[3]);
                case "addKey":
                    return addKey(jedis, params[3], params[4], params[5], params[6], params[7]);
                // Add more cases for other operations as needed
                default:
                    return null;
            }
        } catch (Exception e) {
            Log.e("RedisConnectionError", "Failed to connect to Redis server", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        if (result != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, result);
            toolbar.setTitle(host);
            listView.setAdapter(adapter);
            if (sheet != null) {
                sheet.setVisibility(View.GONE);
            }
        } else {
            // Handle connection error
            Toast.makeText(context, "Failed to connect to Redis server", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<String> addKey(Jedis jedis, String key, String value, String ttl, String type, String index) {
        jedis.select(Integer.parseInt(index));
        if (type.equals("String")){
        jedis.set(key, value);
        if (!ttl.equals("-1")){
            jedis.expire(key, Long.parseLong(ttl));
        }}else if (type.equals("List")){
            String result=value.replaceAll("\\s*,\\s*", ",");
            String[] elements = result.replaceAll("\\[|\\]", "").split(",");

            // Convert the array to an ArrayList
            List<String> arrayList = new ArrayList<>(Arrays.asList(elements));

            // Print the ArrayList
            for (int i =0; i<arrayList.size(); i++) {
                jedis.rpush(key, arrayList.get(i));
            }
        }

        Set<String> keys = jedis.keys("*");
        return new ArrayList<>(keys);
    }

    private ArrayList<String> getAllKeys(Jedis jedis, String index) {
        jedis.select(Integer.parseInt(index));
        Set<String> keys = jedis.keys("*");
        return new ArrayList<>(keys);
    }

    // Add more methods for additional Redis operations as needed
}
