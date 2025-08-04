package com.v2v.blogapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LogEntryAdapter extends RecyclerView.Adapter<LogEntryAdapter.LogViewHolder> {

    private Context context;
    private List<LogEntry> logList;

    public LogEntryAdapter(Context context, List<LogEntry> logList) {
        this.context = context;
        this.logList = logList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_entry, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        LogEntry entry = logList.get(position);
        holder.title.setText(entry.getTitle());
        holder.description.setText(entry.getDescription());
        holder.url.setText(entry.getUrl());
        holder.author.setText("By: " + entry.getAuthor());

        // Delete functionality
        holder.deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference()
                                .child("entries") // or "blocks", depending on what you're using
                                .child(entry.getId()) // make sure LogEntry has getId()
                                .removeValue()
                                .addOnSuccessListener(unused -> {
                                    logList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, logList.size());
                                    Toast.makeText(context, "Entry deleted", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Edit functionality (you can open a custom dialog or a new activity)
        holder.editBtn.setOnClickListener(v -> {
            if (context instanceof EditEntryListener) {
                ((EditEntryListener) context).onEditEntry(entry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, url, author;
        ImageButton deleteBtn, editBtn;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.entryTitle);
            description = itemView.findViewById(R.id.entryDescription);
            url = itemView.findViewById(R.id.entryUrl);
            author = itemView.findViewById(R.id.entryAuthor);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
        }
    }

    public interface EditEntryListener {
        void onEditEntry(LogEntry entry);
    }
}