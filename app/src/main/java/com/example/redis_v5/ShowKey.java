package com.example.redis_v5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.redis_v5.redis.RedisSowkey;
import com.google.android.material.textfield.TextInputEditText;

public class ShowKey extends AppCompatActivity {
    private AutoCompleteTextView type;
    private TextInputEditText name;
    private TextInputEditText ttl;

    private TextInputEditText value;
    private Button ok;
    private Button del;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_key);
        Intent intent = getIntent();
        if (intent != null) {
            String key_Name = intent.getStringExtra("key_name");
            String ip = intent.getStringExtra("ip");
            String port = intent.getStringExtra("port");

            type = findViewById(R.id.type_value);
            name = findViewById(R.id.name_value);
            ttl = findViewById(R.id.ttl_value);
            value = findViewById(R.id.value);
            new RedisSowkey(ShowKey.this, type, name, ttl, value).execute(ip, port, "getKey", key_Name);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String v = value.getText().toString();
                    String t = ttl.getText().toString();
                    new RedisSowkey(ShowKey.this, type, name, ttl, value).execute(ip, port, "updateKey", key_Name, v, t);
                }
            });
    }



        }
}