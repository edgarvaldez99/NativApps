package com.example.nativapps.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nativapps.R;
import com.example.nativapps.database.Report;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<Report> dataset;

    public ItemAdapter(List<Report> dataset) {
        super();
        this.dataset = dataset;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        ItemViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.item_description);
            imageView = view.findViewById(R.id.item_image);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.example.nativapps.R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Report item = dataset.get(position);
        Bitmap bitmap = BitmapFactory.decodeFile(item.image);
        holder.textView.setText(item.description);
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
