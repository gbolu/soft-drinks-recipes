package com.org.softdrinks.adapters;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.R;
import com.org.softdrinks.models.DrinkModel;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> {

    Context context;
    ArrayList<DrinkModel> arrayList;

    public SearchAdapter(Context context, ArrayList<DrinkModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public SearchAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_sample, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.viewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public viewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.std_name);
        }
    }
}
