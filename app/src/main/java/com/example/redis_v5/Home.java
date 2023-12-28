package com.example.redis_v5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.redis_v5.redis.RedisDelete;
import com.example.redis_v5.redis.RedisGetAll;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class Home extends AppCompatActivity implements Dailog_keys.DialogListener, Dailog.DialogListener {
    String host = "";
    String port = "";
    String username = "";
    String password = "";
    ListView l;
    Toolbar toolbar;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(host); // hna fin khas t7t l ip address dyal redis
        setSupportActionBar(toolbar);

//        this is the code that create the drop menu for the data base index DB0 DB1 ....
        String[] items = getResources().getStringArray(R.array.bd_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Home.this, R.layout.bd_drop_menu_item, items);

        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.menu); // Replace with your actual ID
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
        autoCompleteTextView.setText(items[0], false);
        autoCompleteTextView.setAdapter(adapter);
//        this is the end of the code.
// flash the database
        Button flash = findViewById(R.id.flash_db);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // query to clear all the database
                confirmDialog();
            }
        });

        // this is the array that all keys should be in to display them
        // Adding elements to the ArrayList
        l = findViewById(R.id.list_view);
        relativeLayout = findViewById(R.id.sheet);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(Home.this, ShowKey.class);
                intent.putExtra("key_name", selectedItem);
                intent.putExtra("ip", host);
                intent.putExtra("port", port);
                startActivity(intent);
            }
        });


//        this if we click the new key dialog should pop up
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog_keys();
            }
        });

        FloatingActionButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean is = !host.isEmpty() && !port.isEmpty();
                if(!host.isEmpty() && !port.isEmpty()){
                new RedisGetAll(Home.this, l, relativeLayout, toolbar).execute(host, port, "getAllKeys");
            }else Toast.makeText(Home.this, "no connection", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void openDialog_keys() {
        Dailog_keys dailog = new Dailog_keys();
        dailog.show(getSupportFragmentManager(), "dialog_keys");

    }

    @Override
    public void applyTexts2(String keyname, String value,String valueType, String ttl) {
//        this is where the query should be created ~~~~~~~~~~~~~~~~~~~~~~~~~################
        if (ttl.isEmpty()){
            ttl = "-1";
        }
        if(!host.isEmpty() && !port.isEmpty())
        new RedisGetAll(Home.this, l, relativeLayout, toolbar).execute(host, port, "addKey", keyname, value, ttl, valueType);
        else Toast.makeText(Home.this, "no connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.connection) {
            openDialog();
            return true;
        } else if (item.getItemId() == R.id.terminal) {

            if(!host.isEmpty() && !port.isEmpty()) {
                Intent intent = new Intent(Home.this, Terminal.class);

                intent.putExtra("ip", host);
                intent.putExtra("port", port);
                startActivity(intent);
                return true;
            }else {
                return false;
            }
        } else if (item.getItemId() == R.id.log_out) {
            Toast.makeText(this, "log out", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.exit) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle("Flash Redis DATABASE ?");
        builder.setMessage("Are you sure you want a flash your database ?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Home.this, "ok your database is flashed", Toast.LENGTH_SHORT).show();
                if(!host.isEmpty() && !port.isEmpty())
                new RedisDelete(Home.this).execute(host, port, "flush");
                else Toast.makeText(Home.this, "no connection", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
    @Override
    public void applyTexts(String host, String port,String username, String password) {
//        this is where the query should be created ~~~~~~~~~~~~~~~~~~~~~~~~~################

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        new RedisGetAll(Home.this, l, relativeLayout, toolbar).execute(host, port, "getAllKeys");


    }

    public void openDialog(){
        Dailog dailog = new Dailog();
        dailog.show(getSupportFragmentManager(), "dialog");
    }
}