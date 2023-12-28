package com.example.redis_v5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.redis_v5.redis.RedisDelete;
import com.example.redis_v5.redis.RedisSowkey;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;

public class ShowKey extends AppCompatActivity {
    private AutoCompleteTextView type;
    private TextInputEditText ttl;

    private TextInputEditText value;
    private Button ok;
    private Button del;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_key);
        Intent intent = getIntent();
        if (intent != null) {


            String key_Name = intent.getStringExtra("key_name");
            String ip = intent.getStringExtra("ip");
            String port = intent.getStringExtra("port");

            toolbar = findViewById(R.id.toolbar2);
            toolbar.setTitle(key_Name); // hna fin khas t7t l ip address dyal redis
            setSupportActionBar(toolbar);

            String[] items = {"string", "list", "hash map"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.drop_menu_item, items);

            type = findViewById(R.id.type_value); // Replace with your actual ID
            type.setText(items[0], false);
            type.setAdapter(adapter);



            type = findViewById(R.id.type_value);
            ttl = findViewById(R.id.ttl_value);
            value = findViewById(R.id.value);
            new RedisSowkey(ShowKey.this, type, ttl, value).execute(ip, port, "getKey", key_Name);
            ok = findViewById(R.id.ok);
            del = findViewById(R.id.delete);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String v = value.getText().toString();
                    String t = ttl.getText().toString();
                    String typ = type.getText().toString();
                    new RedisSowkey(ShowKey.this, type, ttl, value).execute(ip, port, "updateKey", key_Name, v, t, typ);
                }
            });

            del.setOnClickListener(view -> {
                try {
                    String result = new RedisDelete(ShowKey.this).execute(ip, port, "DeleteKey", key_Name).get();
                    if (result.equals("true")){
                        finish();
                    }
                } catch (ExecutionException e) {
                    Log.e("AsyncTaskError", "An error occurred during AsyncTask execution", e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });

    }



        }
}