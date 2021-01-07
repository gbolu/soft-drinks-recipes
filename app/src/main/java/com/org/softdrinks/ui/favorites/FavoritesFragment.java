package com.org.softdrinks.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.R;
import com.org.softdrinks.adapters.DrinkAdapter;
import com.org.softdrinks.adapters.SearchAdapter;
import com.org.softdrinks.controllers.DrinkController;
import com.org.softdrinks.models.DrinkModel;
import com.org.softdrinks.models.SearchModel;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    ArrayList<DrinkModel> favorites;
    RecyclerView favoritesView;
    DrinkAdapter favAdapter;
    TextView noFavs;
    LinearLayout noFavsLinearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        DrinkController dc = new DrinkController(getContext());

        favorites = dc.getFavorites();
        favoritesView = root.findViewById(R.id.favorites_results);
        favAdapter = new DrinkAdapter(getContext(), favorites);
        favoritesView.setAdapter(favAdapter);
        favoritesView.setItemAnimator(new DefaultItemAnimator());
        favoritesView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoritesView.setFocusable(false);
        noFavs = root.findViewById(R.id.no_favorites_results);
        noFavsLinearLayout = root.findViewById(R.id.no_favorites_layout);

        if(favorites.size() != 0)
        {
            noFavsLinearLayout.setVisibility(View.GONE);
        }
        else {
            favoritesView.setVisibility(View.GONE);
            noFavs.setText("You have no favorites");
        }

        return root;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        ArrayList<DrinkModel> newFavs = new DrinkController(getContext()).getFavorites();
        this.favorites.clear();
        this.favorites.addAll(newFavs);
        if(this.favorites.size() == 0){
            this.favoritesView.setVisibility(View.GONE);
            this.noFavs.setText("You have no favorites");
            this.noFavsLinearLayout.setVisibility(View.VISIBLE);
            super.onViewStateRestored(savedInstanceState);
            return;
        }
        this.favAdapter.notifyDataSetChanged();
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onResume() {
        ArrayList<DrinkModel> newFavs = new DrinkController(getContext()).getFavorites();
        this.favorites.clear();
        this.favorites.addAll(newFavs);
        if(this.favorites.size() == 0){
            this.favoritesView.setVisibility(View.GONE);
            this.noFavs.setText("You have no favorites");
            this.noFavsLinearLayout.setVisibility(View.VISIBLE);
            super.onResume();
            return;
        }
        this.favAdapter.notifyDataSetChanged();
        super.onResume();
    }
}