package com.org.softdrinks.ui.single_category;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.softdrinks.R;
import com.org.softdrinks.adapters.DrinkAdapter;
import com.org.softdrinks.controllers.DrinkController;
import com.org.softdrinks.models.DrinkModel;

import java.util.ArrayList;
import java.util.Objects;

public class SingleCategoryFragment extends Fragment {

    private static final String CATEGORY_NAME = "category_name";
    private static final String CATEGORY_IMAGE_URI = "category_image_uri";
    private static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_DETAILS = "category_details";

    private String categoryName;
    private String categoryImageURI;
    private ArrayList<DrinkModel> categoryDrinks;
    private String categoryDetails;

    public SingleCategoryFragment() {
        // Required empty public constructor
    }

    public static SingleCategoryFragment newInstance(String categoryName, String categoryImageURI,
                                                     int categoryID, String categoryDetails) {
        SingleCategoryFragment fragment = new SingleCategoryFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY_NAME, categoryName);
        args.putString(CATEGORY_IMAGE_URI, categoryImageURI);
        args.putInt(CATEGORY_ID, categoryID);
        args.putString(CATEGORY_DETAILS, categoryDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString(CATEGORY_NAME);
            categoryImageURI = getArguments().getString(CATEGORY_IMAGE_URI);
            DrinkController dc = new DrinkController(getContext());
            categoryDrinks = dc.getDrinksByCategory(getArguments().getInt(CATEGORY_ID));
            categoryDetails = getArguments().getString(CATEGORY_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_single_category, container, false);

        TextView categoryTitle =  root.findViewById(R.id.category_title);
        categoryTitle.setText(categoryName);

        ImageView categoryImage = root.findViewById(R.id.category_image);
        categoryImage.setImageURI(Uri.parse(
                "android.resource://" + requireContext().getPackageName() +
                "/drawable/" + categoryImageURI));

        TextView categoryDetailsView = root.findViewById(R.id.category_details);
        categoryDetailsView.setText(String.format("Category Title: %s", categoryDetails));

        RecyclerView categoryDrinksView = root.findViewById(R.id.category_drinks);
        categoryDrinksView.setItemAnimator(new DefaultItemAnimator());
        categoryDrinksView.setLayoutManager(new LinearLayoutManager(requireContext()));
        categoryDrinksView.setAdapter(new DrinkAdapter(requireContext(), categoryDrinks));

        return root;
    }
}