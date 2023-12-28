package com.example.redis_v5.redis;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisDelete extends AsyncTask<String, Void, String> {
    private Context context;

    public RedisDelete(Context context){
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
        int port = Integer.parseInt(params[1]);
        String operation = params[2];

        try {
            Jedis jedis = new Jedis(ip, port);

            // Perform Redis operations based on the requested operation
            switch (operation) {
                case "DeleteKey":
                    return deleteKey(jedis, params[3]);
                case "flush":
                    return flash(jedis);
// Add more cases for other operations as needed
                default:
                    return null;
            }
        } catch (JedisConnectionException e) {
            Log.e("RedisConnectionError", "Failed to connect to Redis server", e);
            return null;
        }
    }

    private String flash(Jedis jedis) {
        jedis.flushAll();
        return "true";
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {

            Toast.makeText(context, "the key deleted successfully.", Toast.LENGTH_SHORT).show();

        } else {
            // Handle connection error
            Toast.makeText(context, "error of connection", Toast.LENGTH_SHORT).show();
        }
    }

    private String deleteKey(Jedis jedis, String key) {
        if (jedis.exists(key)){
            jedis.del(key);
        }
        return "true";
    }

    // Add more methods for additional Redis operations as needed
}