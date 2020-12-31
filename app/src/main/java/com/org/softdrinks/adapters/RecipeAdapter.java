package com.org.softdrinks.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.R;
import com.org.softdrinks.models.CategoryModel;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.viewHolder> {
    Context context;
    ArrayList<String> drinkRecipe;

    public RecipeAdapter(Context context, String[] drinkRecipe) 
    {
        this.context = context;
        this.drinkRecipe = new ArrayList<>(Arrays.asList(drinkRecipe));
    }

    @NonNull
    @Override
    public RecipeAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.drink_recipe_sample, parent, false);
        return new RecipeAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.viewHolder holder, int position) {
        String item = drinkRecipe.get(position);
        holder.bind(item);
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public viewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.drink_recipe_item);
        }

        public void bind(final String item) {
            name.setText(item);
        }
    }

    @Override
    public int getItemCount() {
        return drinkRecipe.size();
    }
}
