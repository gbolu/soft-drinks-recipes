package com.org.softdrinks.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.org.softdrinks.R;
import com.org.softdrinks.adapters.SearchAdapter;
import com.org.softdrinks.controllers.DrinkController;
import com.org.softdrinks.models.SearchModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<SearchModel> searchResults;
    RecyclerView searchResultsView;
    SearchAdapter searchAdapter;
    TextView noSearchResults;
    LinearLayout noSearchLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchResults = new ArrayList<>();
        searchAdapter = new SearchAdapter(getBaseContext(), searchResults);

        searchResultsView = findViewById(R.id.search_results);
        noSearchResults = findViewById(R.id.no_search_results);
        noSearchLinearLayout = findViewById(R.id.no_search_layout);
        searchResultsView.setAdapter(searchAdapter);
        searchResultsView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchResultsView.setItemAnimator(new DefaultItemAnimator());

        // Get the intent, verify the action and get the query
        handleIntent(getIntent());

    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            new Thread(){
                @Override
                public void run() {
                    doMySearch(query);
                }
            }.start();
        }
    }

    private void doMySearch(final String query) {
        Log.i("Search Query: ", query);
        ArrayList<SearchModel> newSearchResults;

        final DrinkController dc = new DrinkController(getApplicationContext());
        newSearchResults = dc.searchProducts(query);
        Log.i("Search Results: ", (String.valueOf(newSearchResults.size())));

        if(newSearchResults.size() != 0)
        {
            noSearchLinearLayout.setVisibility(View.GONE);
            searchResults.clear();
            searchResults.addAll(newSearchResults);
            searchAdapter.notifyDataSetChanged();
        }
        else {
            searchResultsView.setVisibility(View.GONE);
            noSearchResults.setText("No search results");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }
}