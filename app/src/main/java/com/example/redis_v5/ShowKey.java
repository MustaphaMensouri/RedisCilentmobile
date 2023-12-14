package com.example.redis_v5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ShowKey extends AppCompatActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_key);
        Intent intent = getIntent();
        if (intent != null) {
            String receivedData = intent.getStringExtra("key_name");

        }
        }
}