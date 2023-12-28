package com.example.redis_v5.redis;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class Term extends AsyncTask<String, Void, String> {
    private Context context;
//    private ListView terminal;
//    private ArrayAdapter<String> adapter;
//    private String command;
//    private ArrayList<String> cmd;

    public Term(Context context){//, ListView terminal, String command, ArrayAdapter<String> adapter){
        this.context = context;
//        this.terminal = terminal;
//        this.command = command;
//        this.adapter = adapter;
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
                case "runCommand":
                    return runCommand(jedis, params[3]);
// Add more cases for other operations as needed
                default:
                    return null;
            }
        } catch (JedisConnectionException e) {
            Log.e("RedisConnectionError", "Failed to connect to Redis server", e);
            return null;
        }
    }

//    @Override
//    protected void onPostExecute(String result) {
//        if (result != null) {
//
//        } else {
//            // Handle connection error
//            Toast.makeText(context, "error of connection", Toast.LENGTH_SHORT).show();
//        }
//    }

    private String runCommand(Jedis jedis, String query) {
        String[] q=query.split("\\s+");
        String cmd='\''+q[0]+'\'';
        for(int i=1;i<q.length;i++)
            cmd+=",\'"+q[i]+'\'';
        String result = jedis.eval("return redis.call("+cmd+")").toString();
        return result;
    }

    // Add more methods for additional Redis operations as needed
}
