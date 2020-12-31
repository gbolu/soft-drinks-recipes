package com.org.softdrinks.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.R;
import com.org.softdrinks.adapters.CategoryAdapter;
import com.org.softdrinks.controllers.DrinkController;
import com.org.softdrinks.models.CategoryModel;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    RecyclerView allCategoriesView;
    DrinkController dc;
    ArrayList<CategoryModel> allCategories;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        allCategoriesView = root.findViewById(R.id.all_categories);
        allCategoriesView.setLayoutManager(new LinearLayoutManager(getContext()));
        allCategoriesView.setItemAnimator(new DefaultItemAnimator());

        dc = new DrinkController(getContext());
        allCategories = dc.getAllCategories();

        CategoryAdapter adapter = new CategoryAdapter(getContext(), allCategories);
        allCategoriesView.setAdapter(adapter);

        return root;
    }
}