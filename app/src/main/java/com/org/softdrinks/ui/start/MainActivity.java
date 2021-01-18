package com.org.softdrinks.ui.start;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.org.softdrinks.R;
import com.org.softdrinks.controllers.DrinkController;
import com.org.softdrinks.models.CategoryModel;
import com.org.softdrinks.models.DrinkModel;
import com.org.softdrinks.ui.categories.CategoriesFragment;
import com.org.softdrinks.ui.favorites.FavoritesFragment;
import com.org.softdrinks.ui.home.HomeFragment;
import com.org.softdrinks.ui.single_category.SingleCategoryFragment;
import com.org.softdrinks.ui.single_drink.SingleDrinkFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // seed db with values
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchRequested();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        setupDrawerLayout();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_categories, R.id.nav_favorites)
                .setDrawerLayout(drawer)
                .build();

        NavHostFragment temp = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = temp.getNavController();
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        setNavigationViewListener();

        if(getIntent().getExtras() != null){
            Fragment t_frag = new HomeFragment();
            int fragID = getIntent().getExtras().getInt("fragID");
            DrinkController dc = new DrinkController(getApplicationContext());
            switch (getIntent().getExtras().getInt("frgToLoad"))
            {
                case 1:
                    DrinkModel tempDrink = dc.getDrink(fragID);
                    t_frag = SingleDrinkFragment.newInstance(tempDrink.getName(), tempDrink.getDrinkImageURI(), tempDrink.getCategoryID(), tempDrink.getDrinkDetails(), tempDrink.getDrinkRecipe(), temp.getId());
                    break;

                case 2:
                    CategoryModel tempCat = dc.getCategory(fragID);
                    t_frag = SingleCategoryFragment.newInstance(tempCat.getName(), tempCat.getImageURI(), tempCat.getDbID(), tempCat.getCategoryDetails());
                    break;
                default:
                    break;
            }
            switchContent(R.id.nav_host_fragment,t_frag);
        }
    }

    private void setupDrawerLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if(count == 0){
            finish();
        }
        else if(count > 1){
            getSupportFragmentManager().popBackStack();
        }
        else if(count == 1){
            clearBackStack();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            HomeFragment temp = new HomeFragment();
            ft.replace(R.id.nav_host_fragment, temp);
            ft.commit();
            getSupportFragmentManager().popBackStack();
        }
    }

    public final void clearBackStack()
    {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public final void switchContent(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack(fragment.toString());
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        switch (item.toString()){
            case "Categories":
                switchContent(R.id.nav_host_fragment, new CategoriesFragment());
                break;
            case "Home":
                switchContent(R.id.nav_host_fragment, new HomeFragment());
                clearBackStack();
                break;
            case "My Favorites":
                switchContent(R.id.nav_host_fragment, new FavoritesFragment());
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
}