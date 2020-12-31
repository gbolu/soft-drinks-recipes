package com.org.softdrinks.adapters;

import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.MainActivity;
import com.org.softdrinks.R;

import com.org.softdrinks.models.DrinkModel;
import com.org.softdrinks.ui.single_drink.SingleDrinkFragment;

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
        final DrinkModel item = arrayList.get(position);
        holder.name.setText(arrayList.get(position).getName());
        holder.category.setText(arrayList.get(position).getCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleDrinkFragment t_frag = SingleDrinkFragment.newInstance(
                        item.getName(), item.getDrinkImageURI(),
                        item.getCategoryID(), item.getDrinkDetails(), item.getDrinkRecipe());
                switchContent(R.id.f_layout, t_frag);
            }
        });
    }

    public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchContent(id, fragment);
        }

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
