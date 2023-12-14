package com.example.redis_v5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class Terminal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);
        EditText command = findViewById(R.id.command_write);
        TextView textView = findViewById(R.id.show_command);

        Button run = findViewById(R.id.run);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = command.getText().toString();
                textView.setText(result); // hna khas itbdl b query
                command.setText("");
            }
        });




    }
}