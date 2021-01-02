package com.org.softdrinks.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.R;
import com.org.softdrinks.adapters.CategoryAdapter;
import com.org.softdrinks.adapters.DrinkAdapter;
import com.org.softdrinks.controllers.DrinkController;
import com.org.softdrinks.models.CategoryModel;
import com.org.softdrinks.models.DrinkModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView popularDrinksView;
    RecyclerView popularCategoriesView;
    DrinkController dc;
    ArrayList<DrinkModel> popularDrinks;
    ArrayList<CategoryModel> popularCategories;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        popularDrinksView = root.findViewById(R.id.popular_drinks);
        popularDrinksView.setLayoutManager(new LinearLayoutManager(getContext()));
        popularDrinksView.setItemAnimator(new DefaultItemAnimator());

        popularCategoriesView = root.findViewById(R.id.popular_categories);
        popularCategoriesView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        dc = new DrinkController(getContext());
        popularDrinks = dc.getPopularDrinks(5);
        popularCategories = dc.getPopularCategories(6);

        DrinkAdapter adapter = new DrinkAdapter(getContext(), popularDrinks);
        popularDrinksView.setAdapter(adapter);

        CategoryAdapter c_adapter = new CategoryAdapter(getContext(), popularCategories);
        popularCategoriesView.setAdapter(c_adapter);

        return root;
    }
}