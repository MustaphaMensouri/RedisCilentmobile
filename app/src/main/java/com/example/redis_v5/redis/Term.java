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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        int port = 6379;
        if(!params[1].isEmpty())
            port = Integer.parseInt(params[1]);
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
        } catch (Exception e) {
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
        String cmd;

        String pattern = "\"([^\"]*)\"";
        Pattern r = Pattern.compile(pattern);

        // Create a matcher with the input query
        Matcher m = r.matcher(query);

        // Find the first occurrence of the pattern
        if (m.find()) {
            // Extract the matched value (group 1)
            String extractedValue = m.group(1);

            String modifiedQuery = query.replace("\"" + extractedValue + "\"", "");
            String[] q=modifiedQuery.split("\\s+");

            cmd='\''+q[0]+'\'';
            for(int i=1;i<q.length;i++)
                cmd+=",\'"+q[i]+'\'';
            cmd += ",\'"+extractedValue+'\'';
        }else {
            String[] q=query.split("\\s+");

            cmd='\''+q[0]+'\'';
            for(int i=1;i<q.length;i++)
                cmd+=",\'"+q[i]+'\'';

        }
        String result;
        try {
            result = jedis.eval("return redis.call("+cmd+")").toString();
        }catch (Exception e){
            result = "(error) ERR unknown command";
        }

        return result;
    }

    // Add more methods for additional Redis operations as needed
}
