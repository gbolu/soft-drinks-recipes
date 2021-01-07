package com.org.softdrinks.ui.single_drink;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.softdrinks.R;
import com.org.softdrinks.adapters.DrinkAdapter;
import com.org.softdrinks.adapters.RecipeAdapter;
import com.org.softdrinks.controllers.DrinkController;
import com.org.softdrinks.models.DrinkModel;

import java.util.ArrayList;
import java.util.Objects;

public class SingleDrinkFragment extends Fragment {

    public static final String DRINK_NAME = "drink_name";
    public static final String DRINK_ID = "drink_id";
    public static final String DRINK_IMAGE_URI = "drink_image_uri";
    public static final String DRINK_CATEGORY_ID = "drink_category_id";
    public static final String DRINK_DETAILS = "drink_details";
    public static final String DRINK_RECIPE = "drink_recipe";

    private String drinkName;
    private String drinkImageURI;
    private ArrayList<DrinkModel> similarDrinks;
    private String drinkDetails;
    private String[] drinkRecipe;
    private int drinkID;

    public SingleDrinkFragment() {
        // Required empty public constructor
    }

    public static SingleDrinkFragment newInstance(String drinkName, String drinkImageURI, int drinkCategoryID,
                                                     String drinkDetails, String drinkRecipe, int drinkID) {
        SingleDrinkFragment fragment = new SingleDrinkFragment();

        Bundle args = new Bundle();
        args.putString(DRINK_NAME, drinkName);
        args.putString(DRINK_IMAGE_URI, drinkImageURI);
        args.putInt(DRINK_CATEGORY_ID, drinkCategoryID);
        args.putString(DRINK_DETAILS, drinkDetails);
        args.putString(DRINK_RECIPE, drinkRecipe);
        args.putInt(DRINK_ID, drinkID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drinkName = getArguments().getString(DRINK_NAME);
            drinkImageURI = getArguments().getString(DRINK_IMAGE_URI);
            drinkDetails = getArguments().getString(DRINK_DETAILS);
            drinkID = getArguments().getInt(DRINK_ID);

            DrinkController dc = new DrinkController(getContext());
            similarDrinks = dc.getDrinksByCategory(getArguments().getInt(DRINK_CATEGORY_ID));

            drinkRecipe = Objects.requireNonNull(requireArguments().getString(DRINK_RECIPE)).split(";");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

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

        int index = 0;
        while(index < similarDrinks.size()){
            if(similarDrinks.get(index).getName().equalsIgnoreCase(drinkName)){
                similarDrinks.remove(index);
                break;
            }
            index++;
        }

        RecyclerView similarDrinksView = root.findViewById(R.id.similar_drinks);
        similarDrinksView.setItemAnimator(new DefaultItemAnimator());
        similarDrinksView.setLayoutManager(new LinearLayoutManager(requireContext()));
        similarDrinksView.setAdapter(new DrinkAdapter(requireContext(), similarDrinks));

        final Button addFav = root.findViewById(R.id.addFav);
        final DrinkController dc = new DrinkController(getContext());
        if(dc.isFav(drinkID)){
            addFav.setText("Remove From Favorites");
        } else {
            addFav.setText("Add To Favorites");
        }
        addFav.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(addFav.getText() == "Add To Favorites"){
                    if(dc.insertFav(drinkID)){
                        addFav.setText("Remove From Favorites");
                    }
                } else {
                    dc.removeFav(drinkID);
                    addFav.setText("Add To Favorites");
                }
                Log.i("Fav Button: ", String.valueOf(addFav.getText()));
            }
        });

        final Button shareRecipe = root.findViewById(R.id.share);
        shareRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < drinkRecipe.length; i++)
                {
                    s.append("Step " + String.valueOf(i + 1));
                    s.append("\n");
                    s.append(drinkRecipe[i]);
                    s.append("\n\n");

                }
                for (String v: drinkRecipe) {

                }
                String shareBody = s.toString();
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, drinkName + " Recipe");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        return root;
    }
}