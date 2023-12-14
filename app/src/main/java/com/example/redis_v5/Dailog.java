package com.example.redis_v5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dailog extends AppCompatDialogFragment {
    private EditText host;
    private EditText port;
    private EditText username;
    private EditText password;


    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dailog_layout, null);
        builder.setView(view).setTitle("New connection")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String host_value = host.getText().toString();
                        String port_value = port.getText().toString();
                        String username_value = username.getText().toString();
                        String password_value = password.getText().toString();
                        listener.applyTexts(host_value, port_value, username_value, password_value);
                    }
                });
        host = view.findViewById(R.id.host);
        port = view.findViewById(R.id.port);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DialogListener");
        }
    }

    public interface DialogListener {
        void applyTexts(String host, String port, String username, String password);
    }
}
