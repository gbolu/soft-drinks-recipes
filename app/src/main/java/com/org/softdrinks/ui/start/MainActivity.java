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
import com.org.softdrinks.ui.categories.CategoriesFragment;
import com.org.softdrinks.ui.favorites.FavoritesFragment;
import com.org.softdrinks.ui.home.HomeFragment;

import androidx.annotation.NonNull;
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
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.i("BStack: ", String.valueOf(count));
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
            case "Favorites":
                switchContent(R.id.nav_host_fragment, new FavoritesFragment());
                break;
            default:
                break;
        }
        return true;
    }
}