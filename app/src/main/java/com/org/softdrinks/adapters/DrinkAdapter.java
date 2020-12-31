package com.org.softdrinks.adapters;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.R;

import com.org.softdrinks.models.DrinkModel;

import java.util.ArrayList;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.viewHolder> {

    Context context;
    ArrayList<DrinkModel> arrayList;

    public DrinkAdapter(Context context, ArrayList<DrinkModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DrinkAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.drink_popular_sample, viewGroup, false);
        return new viewHolder(view);
    }
    @Override
    public void onBindViewHolder(DrinkAdapter.viewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.category.setText(arrayList.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView category;

        public viewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.popular_drink_name);
            category = itemView.findViewById(R.id.popular_drink_category);
        }
    }
}
