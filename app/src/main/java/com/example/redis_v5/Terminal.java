package com.example.redis_v5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redis_v5.adapter.AdapterTerminal;
import com.example.redis_v5.data.TerminalData;
import com.example.redis_v5.redis.Term;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Terminal extends AppCompatActivity {
    private ArrayList<TerminalData> cmd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);
        Intent intent = getIntent();
        if (intent != null) {
            String ip = intent.getStringExtra("ip");
            String port = intent.getStringExtra("port");

            TextView logo = findViewById(R.id.image);
            TextView logo2 = findViewById(R.id.art2);
            String logo_ascci = " ____          _ _     \n" +
                    "|  _ \\ ___  __| (_)___ \n" +
                    "| |_) / _ \\/ _` | / __|\n" +
                    "|  _ <  __/ (_| | \\__ \\\n" +
                    "|_| \\_\\___|\\__,_|_|___/";
            String logo_ascci2 =" _____                   _             _ \n" +
                    "|_   _|__ _ __ _ __ ___ (_)_ __   __ _| |\n" +
                    "  | |/ _ \\ '__| '_ ` _ \\| | '_ \\ / _` | |\n" +
                    "  | |  __/ |  | | | | | | | | | | (_| | |\n" +
                    "  |_|\\___|_|  |_| |_| |_|_|_| |_|\\__,_|_|"
            ;
            logo.setText(logo_ascci);
            logo2.setText(logo_ascci2);

            RecyclerView l = (RecyclerView) findViewById(R.id.histroy);

            cmd = new ArrayList<>();


            AdapterTerminal adapter = new AdapterTerminal(Terminal.this, cmd);
            l.setAdapter(adapter);
            l.setLayoutManager(new LinearLayoutManager(Terminal.this));



//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cmd);
//            l.setAdapter(adapter);

            TextInputLayout textInputLayout = findViewById(R.id.outlinedTextField);

    // Set the end (trailing) icon programmatically
    // Set an OnClickListener for the end (trailing) icon
            textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the trailing icon click event
                    String command = textInputLayout.getEditText().getText().toString();

                    String result ="";
                    try {
                        result = new Term(Terminal.this).execute(ip, port,"runCommand", command).get();
                    } catch (ExecutionException | InterruptedException e) {
                        result = "Error";
                    }
                    TerminalData data = new TerminalData(ip,port,command, result);
                    cmd.add(data);
                    l.setAdapter(adapter);
                    l.setLayoutManager(new LinearLayoutManager(Terminal.this));

                    textInputLayout.getEditText().setText("");
                }
            });




        }
    }
}