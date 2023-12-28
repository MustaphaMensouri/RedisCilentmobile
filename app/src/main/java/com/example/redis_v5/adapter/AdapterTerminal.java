package com.example.redis_v5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redis_v5.R;
import com.example.redis_v5.data.TerminalData;

import java.util.ArrayList;

public class AdapterTerminal extends RecyclerView.Adapter<AdapterTerminal.ViewHolder> {
    Context context;
    ArrayList<TerminalData> data;


    public AdapterTerminal(Context context, ArrayList<TerminalData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public AdapterTerminal.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_terminal_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTerminal.ViewHolder holder, int position) {
        holder.command.setText(data.get(position).getCommand());
        holder.result.setText(data.get(position).getResult());
        String host_port = data.get(position).getIp()+ ":" + data.get(position).getPort() + ">";
        holder.host_port.setText(host_port);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView command;
        private TextView result;
        private TextView host_port;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            command = itemView.findViewById(R.id.command_to_run);
            result = itemView.findViewById(R.id.result);
            host_port = itemView.findViewById(R.id.host);
        }
    }
}
