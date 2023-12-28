package com.example.redis_v5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;

public class Dailog_keys extends AppCompatDialogFragment {
    private EditText key_name;
    private EditText value;
    private AutoCompleteTextView autoCompleteTextView;
    private EditText ttl;


    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dailog_keys, null);
        builder.setView(view).setTitle("New keys")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String key = key_name.getText().toString();
                        String value_ = value.getText().toString();
                        String value_type = autoCompleteTextView.getText().toString();
                        String ttl_ = ttl.getText().toString();
                        listener.applyTexts2(key, value_, value_type, ttl_);
                    }
                });

        String[] items = {"String", "List", "Hash map"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.drop_menu_item, items);

        TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.value_type_list); // Replace with your actual ID
        autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
        autoCompleteTextView.setText(items[0], false);
        autoCompleteTextView.setAdapter(adapter);

        key_name = view.findViewById(R.id.key_name);
        value = view.findViewById(R.id.value);
        ttl = view.findViewById(R.id.ttl);
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
        void applyTexts2(String key_name, String value, String value_type, String ttl);
    }
}
