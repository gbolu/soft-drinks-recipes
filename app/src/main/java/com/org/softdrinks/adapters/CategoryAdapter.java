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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {
    Context context;
    ArrayList<CategoryModel> arrayList;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_category_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewHolder holder, int position) {
        CategoryModel item = arrayList.get(position);
        holder.bind(item, context);
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public viewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.popular_category_name);
        }

        public void bind(final CategoryModel item, Context mContext) {
            name.setText(item.getName());
            image.setImageURI(Uri.parse("android.resource://" + mContext.getPackageName() + "/drawable/" + item.getImageURI()));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
