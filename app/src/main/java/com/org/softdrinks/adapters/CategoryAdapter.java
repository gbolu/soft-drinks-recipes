package com.org.softdrinks.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.MainActivity;
import com.org.softdrinks.R;
import com.org.softdrinks.models.CategoryModel;
import com.org.softdrinks.ui.single_category.SingleCategoryFragment;

import java.net.URI;
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
        final CategoryModel item = arrayList.get(position);
        holder.bind(item, context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleCategoryFragment t_frag = SingleCategoryFragment.newInstance(
                        item.getName(), item.getImageURI(), item.getDbID(), item.getCategoryDetails()
                );
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

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public viewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.popular_category_name);
        }

        public void bind(final CategoryModel item, Context mContext) {
            name.setText(item.getName());
            Uri temp = Uri.parse("android.resource://" + mContext.getPackageName() + "/drawable/" + item.);
            Log.println(Log.INFO, "URI info", temp.toString());
            image.setImageURI(temp);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
