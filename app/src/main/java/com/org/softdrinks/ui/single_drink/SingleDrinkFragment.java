package com.org.softdrinks.ui.single_drink;

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
import com.org.softdrinks.adapters.RecipeAdapter;
import com.org.softdrinks.controllers.DrinkController;
import com.org.softdrinks.models.DrinkModel;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleDrinkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleDrinkFragment extends Fragment {

    private static final String DRINK_NAME = "drink_name";
    private static final String DRINK_IMAGE_URI = "drink_image_uri";
    private static final String DRINK_CATEGORY_ID = "drink_category_id";
    private static final String DRINK_DETAILS = "drink_details";
    private static final String DRINK_RECIPE = "drink_recipe";

    private String drinkName;
    private String drinkImageURI;
    private ArrayList<DrinkModel> similarDrinks;
    private String drinkDetails;
    private String[] drinkRecipe;

    public SingleDrinkFragment() {
        // Required empty public constructor
    }

    public static SingleDrinkFragment newInstance(String drinkName, String drinkImageURI, int drinkCategoryID,
                                                     String drinkDetails, String drinkRecipe) {
        SingleDrinkFragment fragment = new SingleDrinkFragment();
        Bundle args = new Bundle();
        args.putString(DRINK_NAME, drinkName);
        args.putString(DRINK_IMAGE_URI, drinkImageURI);
        args.putInt(DRINK_CATEGORY_ID, drinkCategoryID);
        args.putString(DRINK_DETAILS, drinkDetails);
        args.putString(DRINK_RECIPE, drinkRecipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drinkName = getArguments().getString(DRINK_NAME);
            drinkImageURI = getArguments().getString(DRINK_IMAGE_URI);
            DrinkController dc = new DrinkController(getContext());
            similarDrinks = dc.getDrinksByCategory(getArguments().getInt(DRINK_CATEGORY_ID));
            drinkDetails = getArguments().getString(DRINK_DETAILS);
            drinkRecipe = Objects.requireNonNull(requireArguments().getString(DRINK_RECIPE)).split(",");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_single_drink, container, false);

        TextView drinkTitle =  root.findViewById(R.id.drink_title);
        drinkTitle.setText(drinkName);

        ImageView drinkImage = root.findViewById(R.id.drink_image);
        drinkImage.setImageURI(Uri.parse(
            "android.resource://" + requireContext().getPackageName() +
            "/drawable/" + drinkImageURI));

        TextView drinkDetailsView = root.findViewById(R.id.drink_details);
        drinkDetailsView.setText(drinkDetails);

        RecyclerView drinkRecipeView = root.findViewById(R.id.drink_recipe);
        drinkRecipeView.setItemAnimator(new DefaultItemAnimator());
        drinkRecipeView.setLayoutManager(new LinearLayoutManager(requireContext()));
        drinkRecipeView.setAdapter(new RecipeAdapter(requireContext(), drinkRecipe));

        RecyclerView drinkDrinksView = root.findViewById(R.id.similar_drinks);
        drinkDrinksView.setItemAnimator(new DefaultItemAnimator());
        drinkDrinksView.setLayoutManager(new LinearLayoutManager(requireContext()));
        drinkDrinksView.setAdapter(new DrinkAdapter(requireContext(), similarDrinks));

        return root;
    }
}