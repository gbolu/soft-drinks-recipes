package com.org.softdrinks.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.R;
import com.org.softdrinks.models.SearchModel;
import com.org.softdrinks.ui.start.MainActivity;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.viewHolder> {

    Context context;
    ArrayList<SearchModel> arrayList;

    public SearchAdapter(Context context, ArrayList<SearchModel> arrayList) {
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
        final SearchModel t_search = arrayList.get(position);

        holder.name.setText(t_search.getName());
        holder.type.setText(t_search.getType());
        holder.image.setImageURI(Uri.parse(
                "android.resource://" + context.getPackageName() +
                        "/drawable/" + t_search.getImageURI()));

        //  add listener for click on search item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  start intent to start main activity
                Log.i("Clicked:", "Reached");
                final Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                switch (t_search.getType()){
                    case "Category":
                        i.putExtra("frgToLoad", 2);
                        break;

                    default:
                        i.putExtra("frgToLoad", 1);
                        break;
                }

                i.putExtra("fragID", t_search.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        ImageView image;
        public viewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.search_name);
            type = itemView.findViewById(R.id.search_type);
            image = itemView.findViewById(R.id.search_image);
        }
    }


}
