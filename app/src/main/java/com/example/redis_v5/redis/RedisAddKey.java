package com.example.redis_v5.redis;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;

public class RedisAddKey extends AsyncTask<String, Void, String> {
private Context context;
public RedisAddKey(Context context){
        this.context = context;
        }

@Override
protected String doInBackground(String... params) {
        if (params.length < 3) {
        Log.e("RedisTask", "Insufficient parameters. Expected IP, port, and operation.");
        Toast.makeText(context, "Insufficient parameters. Expected IP, port, and operation.", Toast.LENGTH_SHORT).show();
        return null;
        }

        String ip = params[0];
        int port = 6379;
        if(!params[1].isEmpty())
                port = Integer.parseInt(params[1]);
        String operation = params[2];

        try {
        Jedis jedis = new Jedis(ip, port);

        // Perform Redis operations based on the requested operation
        switch (operation) {
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
protected void onPostExecute(String result) {
        if (result != null) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        } else {
        // Handle connection error
        Toast.makeText(context, "error of connection", Toast.LENGTH_SHORT).show();
        }
        }

private String addKey(Jedis jedis, String key, String value, String ttl) {
        jedis.set(key, value);
        jedis.expire(key, Long.parseLong(ttl));
        return "Key added successfully.";
        }

        // Add more methods for additional Redis operations as needed
        }