package com.org.softdrinks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.R;

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
        holder.bind(item, position);
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView recipeItem;
        TextView recipeNo;

        public viewHolder(View itemView) {
            super(itemView);
            recipeItem = itemView.findViewById(R.id.drink_recipe_item);
            recipeNo = itemView.findViewById(R.id.drink_recipe_no);
        }

        public void bind(final String item, int position) {
            recipeNo.setText(new StringBuilder().append("Step ").append(position + 1));
            recipeItem.setText(new StringBuilder().append(item).toString());
        }
    }

    @Override
    public int getItemCount() {
        return drinkRecipe.size();
    }
}
