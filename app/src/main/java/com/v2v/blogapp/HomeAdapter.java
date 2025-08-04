package com.v2v.blogapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private List<HomeEntry> entryList;

    public HomeAdapter(Context context, List<HomeEntry> entryList) {
        this.context = context;
        this.entryList = entryList;
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        HomeEntry entry = entryList.get(position);

        holder.titleTextView.setText(entry.getTitle());
        holder.authorTextView.setText(entry.getAuthor());
        holder.descTextView.setText(entry.getDescription());

        // Load image with Glide
        Glide.with(context)
                .load(entry.getImageUrl())  // Ensure it's a valid URL
                .placeholder(R.drawable.default_image) // fallback image in drawable
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView, descTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.tvItemTitle);
            authorTextView = itemView.findViewById(R.id.tvItemAuthor);
            descTextView = itemView.findViewById(R.id.tvItemDescription);
            imageView = itemView.findViewById(R.id.imgItemImage);
        }
    }
}