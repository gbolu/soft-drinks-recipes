package com.org.softdrinks.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.org.softdrinks.models.CategoryModel;
import com.org.softdrinks.models.DrinkModel;
import com.org.softdrinks.models.SearchModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public final class DrinkController extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 1;
    private static final String DBNAME = "AptechDB";

    private static final String DRINKS_TABLE_NAME = "Drinks";
    private static final String DRINKS_COL_ID = "drinkID";
    private static final String DRINKS_COL_NAME = "drinkName";
    private static final String DRINKS_COL_CATEGORY = "drinkCategoryID";
    private static final String DRINKS_COL_FAV_COUNT = "drinkFavCount";
    public static final String DRINKS_COL_DETAILS = "drinkDetails";
    public static final String DRINKS_COL_IMAGE_URI = "drinkImageURI";
    public static final String DRINKS_COL_RECIPE = "drinkRecipe";

    private static final String CATEGORIES_TABLE_NAME = "Drink_Categories";
    private static final String CATEGORIES_COL_ID = "categoryID";
    private static final String CATEGORIES_COL_NAME = "categoryName";
    private static final String CATEGORIES_COL_FAV_COUNT = "categoryFavCount";
    private static final String CATEGORIES_COL_IMAGE_URI = "categoryURI";
    private static final String CATEGORIES_COL_DETAILS = "categoryDetails";

    public void seedDB(Context ctx)
    {
        insertSeedCategories(ctx);
        insertSeedDrinks(ctx);
    }


    public DrinkController(@Nullable Context context)
    {
        super(context, DBNAME, null, DB_VERSION);
    }

    // create tables for the drinks and the category of drinks
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String drinksQuery = "CREATE TABLE IF NOT EXISTS " + DRINKS_TABLE_NAME +
                "(" +
                    DRINKS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DRINKS_COL_NAME + " VARCHAR(200) UNIQUE," +
                    DRINKS_COL_CATEGORY + " INTEGER," +
                    DRINKS_COL_FAV_COUNT + " INTEGER," +
                    DRINKS_COL_DETAILS + " TEXT," +
                    DRINKS_COL_IMAGE_URI + " VARCHAR(200)," +
                    DRINKS_COL_RECIPE + " TEXT," +
                    "FOREIGN KEY(" + DRINKS_COL_CATEGORY + ") REFERENCES " + CATEGORIES_TABLE_NAME + "(" + CATEGORIES_COL_ID + ")" +
                ");";

        String categoryQuery = "CREATE TABLE IF NOT EXISTS " + CATEGORIES_TABLE_NAME +
                "(" +
                    CATEGORIES_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CATEGORIES_COL_NAME + " VARCHAR(200) UNIQUE," +
                    CATEGORIES_COL_FAV_COUNT + " INTEGER DEFAULT 0," +
                    CATEGORIES_COL_IMAGE_URI + " VARCHAR(200)," +
                    CATEGORIES_COL_DETAILS + " TEXT" +
                ");";

        sqLiteDatabase.execSQL(categoryQuery);
        sqLiteDatabase.execSQL(drinksQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + " " + DBNAME);
        onCreate(sqLiteDatabase);
    }

    //
    public DrinkModel getDrink(int drinkID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(
                   "SELECT * FROM " + DRINKS_TABLE_NAME + " a" +
                        " INNER JOIN " + CATEGORIES_TABLE_NAME + " b" +
                        " ON a." + DRINKS_COL_CATEGORY + " = " + "b." + CATEGORIES_COL_ID +
                        " WHERE a." + DRINKS_COL_ID + " = ?;",
                    new String[]{String.valueOf(drinkID)});

        cur.moveToFirst();
        DrinkModel temp = new DrinkModel(cur.getString(8), cur.getString(1),
                                cur.getInt(0), cur.getString(4),
                                cur.getInt(2), cur.getString(5), cur.getString(6));

        //  close cursors and database connection
        cur.close();
        db.close();

        return temp;
    }

    public CategoryModel getCategory(int categoryID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(
                "SELECT * FROM " + CATEGORIES_TABLE_NAME +
                        " WHERE " + CATEGORIES_COL_ID + " = ?;",
                new String[]{String.valueOf(categoryID)});

        cur.moveToFirst();
        CategoryModel temp = new CategoryModel(cur.getInt(0), cur.getString(1), cur.getString(3), cur.getString(4));

        //  close cursors and database connection
        cur.close();
        db.close();

        return temp;
    }

    private void insertDrink(String drinkName, int drinkCategory,
                             String drinkDetails, String drinkImageURI, String drinkRecipe)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues ct = new ContentValues();
        ct.put(DRINKS_COL_NAME, drinkName);
        ct.put(DRINKS_COL_CATEGORY, drinkCategory);
        ct.put(DRINKS_COL_DETAILS, drinkDetails);
        ct.put(DRINKS_COL_IMAGE_URI, drinkImageURI);
        ct.put(DRINKS_COL_RECIPE, drinkRecipe);

        try{
            db.insertOrThrow(DRINKS_TABLE_NAME, null, ct);
        }
        catch (SQLiteException e)
        {
            Log.i("Drink insert failed", Objects.requireNonNull(e.getMessage()));
        }
        db.close();
    }

    private void insertCategory(String categoryName, String categoryImageURI, String categoryDetails)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues ct = new ContentValues();
        ct.put(CATEGORIES_COL_NAME, categoryName);
        ct.put(CATEGORIES_COL_IMAGE_URI, categoryImageURI);
        ct.put(CATEGORIES_COL_DETAILS, categoryDetails);

        try
        {
            db.insertOrThrow(CATEGORIES_TABLE_NAME, null, ct);
        }
        catch (SQLiteException e)
        {
            Log.i("Category insert failed", Objects.requireNonNull(e.getMessage()));
        }
        db.close();
    }

    //  convert products JSON file to a JSON string
    private String toJSONString(Context context, String filename)
    {
        StringBuilder str = new StringBuilder();
        try
        {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open(filename);
            InputStreamReader isr = new InputStreamReader(in);
            char [] inputBuffer = new char[100];

            int charRead;
            while((charRead = isr.read(inputBuffer))>0)
            {
                String readString = String.copyValueOf(inputBuffer,0,charRead);
                str.append(readString);
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        return str.toString();
    }

    private void insertSeedCategories(final Context ctx)
    {
        String categoriesJSONStr = toJSONString(ctx, "categories.json");
        try
        {
            //  convert drinks from json to array obj
            JSONObject categoriesJSONObj = new JSONObject(categoriesJSONStr);
            JSONArray categories = categoriesJSONObj.getJSONArray("categories");

            //  loop through all products and add to db
            for (int index = 0; index < categories.length(); index++)
            {
                JSONObject category = categories.getJSONObject(index);
                insertCategory(category.getString("categoryName"),
                                category.getString("imageURI"),
                                category.getString("details"));
            }
        }

        catch (final JSONException e)
        {
            Log.e(this.getClass().getSimpleName(), "Json parsing error: " + e.getMessage());
            new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx,
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            };
        }
    }

    private String convertJSONArrToStr(JSONArray j_arr) throws JSONException {
        StringBuilder completeStr = new StringBuilder();
        for (int i = 0; i < j_arr.length(); i++)
        {
            if (i != 0) {
                completeStr.append(",");
            }
            completeStr.append(j_arr.getString(i));
        }

        return completeStr.toString();
    }

    //  add all drinks to db
    private void insertSeedDrinks(final Context ctx)
    {
        String productsJSONStr = toJSONString(ctx, "drinks.json");
        try
        {
            //  convert drinks from json to array obj
            JSONObject drinksJSONObj = new JSONObject(productsJSONStr);
            JSONArray drinks = drinksJSONObj.getJSONArray("drinks");

            //  loop through all products and add to db
            for (int index = 0; index < drinks.length(); index++)
            {
                JSONObject drink = drinks.getJSONObject(index);
                insertDrink(drink.getString("name"), drink.getInt("category"),
                            drink.getString("drinkDetails"), drink.getString("imageURI"),
                            convertJSONArrToStr(drink.getJSONArray("recipe")));
            }
        }

        catch (final JSONException e)
        {
            Log.e(this.getClass().getSimpleName(), "Json parsing error: " + e.getMessage());
            new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx,
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            };
        }
    }

    public ArrayList<DrinkModel> getPopularDrinks(int limit)
    {
        ArrayList<DrinkModel> temp = new ArrayList<>();

        //  grab most popular drinks from database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(
                "SELECT * FROM " + DRINKS_TABLE_NAME + " a" +
                        " INNER JOIN " + CATEGORIES_TABLE_NAME + " b" +
                        " ON a." + DRINKS_COL_CATEGORY + " = " + "b." + CATEGORIES_COL_ID +
                      " ORDER BY " + DRINKS_COL_FAV_COUNT + " DESC LIMIT " + limit + ";", null);
        cur.moveToFirst();

        //  add most popular drinks to list
        do{
            DrinkModel tempDrink = new DrinkModel(cur.getString(8), cur.getString(1),
                    cur.getInt(0), cur.getString(4),
                    cur.getInt(2), cur.getString(5), cur.getString(6));
            temp.add(tempDrink);
        }while (cur.moveToNext());

        cur.close();
        db.close();

        return temp;
    }

    public ArrayList<CategoryModel> getPopularCategories(int limit)
    {
        ArrayList<CategoryModel> temp = new ArrayList<>();

        //  grab most popular drinks from database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(
                "SELECT * FROM " + CATEGORIES_TABLE_NAME +
                        " ORDER BY " + CATEGORIES_COL_FAV_COUNT + " DESC LIMIT " + limit + ";", null);
        cur.moveToFirst();

        //  add most popular categories to list
        do{
            CategoryModel tempCategory = new CategoryModel(cur.getInt(0), cur.getString(1), cur.getString(3), cur.getString(4));
            temp.add(tempCategory);
        }while (cur.moveToNext());

        cur.close();
        db.close();

        return temp;
    }

    public ArrayList<CategoryModel> getAllCategories()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<CategoryModel> temp = new ArrayList<>();

        Cursor cur = db.rawQuery(
                "SELECT * FROM " + CATEGORIES_TABLE_NAME + ";"
                , null);

        cur.moveToFirst();
        do{
            temp.add(new CategoryModel(cur.getInt(0), cur.getString(1), cur.getString(3), cur.getString(4)));
        }while (cur.moveToNext());

        //  free up cursor and db resources
        cur.close();
        db.close();

        return temp;
    }

    private ArrayList<DrinkModel> getAllDrinks()
    {
        ArrayList<DrinkModel> temp = new ArrayList<>();

        //  grab most popular drinks from database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(
                "SELECT * FROM " + DRINKS_TABLE_NAME + " a" +
                        " INNER JOIN " + CATEGORIES_TABLE_NAME + " b" +
                        " ON a." + DRINKS_COL_CATEGORY + " = " + "b."
                        + CATEGORIES_COL_ID + ";", null);
        cur.moveToFirst();

        //  add most popular drinks to list
        do{
            DrinkModel tempDrink = new DrinkModel(cur.getString(8), cur.getString(1),
                    cur.getInt(0), cur.getString(4),
                    cur.getInt(2), cur.getString(5), cur.getString(6));
            temp.add(tempDrink);
        }while (cur.moveToNext());

        cur.close();
        db.close();

        return temp;
    }


    public ArrayList<SearchModel> searchProducts(String searchString)
    {
        String[] strings_search = searchString.split("\\s+");

        for (int i = 0; i < strings_search.length; i++) {
            strings_search[i] = strings_search[i]
                    .replaceFirst(String.valueOf(strings_search[i].charAt(0)),
                            String.valueOf(Character.toUpperCase(strings_search[i].charAt(0))));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            searchString = String.join(" ", strings_search).trim();
        } else {
            searchString = TextUtils.join(" ", strings_search);
        }

        ArrayList<SearchModel> searchResults = new ArrayList<>();
        ArrayList<DrinkModel> allDrinks = getAllDrinks();
        ArrayList<CategoryModel> allCategories = getAllCategories();

        for (int i = 0; i < allDrinks.size(); i++) {
            DrinkModel tempDrink = allDrinks.get(i);

            if(tempDrink.getName().contains(searchString))
            {
                searchResults.add(new SearchModel(tempDrink.getName(), "Drink", tempDrink.getID(), tempDrink.getDrinkImageURI()));
            }

        }

        for (int i = 0; i < allCategories.size(); i++) {
            CategoryModel tempCategory = allCategories.get(i);

            if(tempCategory.getName().contains(searchString))
            {
                searchResults.add(new SearchModel(tempCategory.getName(), "Category", tempCategory.getDbID(), tempCategory.getImageURI()));
            }
        }

        return searchResults;
    }

    public ArrayList<DrinkModel> getDrinksByCategory(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery(
                "SELECT * FROM " + DRINKS_TABLE_NAME + " a" +
                        " INNER JOIN " + CATEGORIES_TABLE_NAME + " b" +
                        " ON a." + DRINKS_COL_CATEGORY + " = " + "b." + CATEGORIES_COL_ID +
                      " WHERE " + "a." + DRINKS_COL_CATEGORY + " = " + id + ";", null);
        cur.moveToFirst();

        ArrayList<DrinkModel> temp = new ArrayList<>();
        if(cur.getCount() != 0)
        {
            do {
                temp.add(new DrinkModel(cur.getString(9), cur.getString(1),
                        cur.getInt(0), cur.getString(4),
                        cur.getInt(2), cur.getString(5), cur.getString(6)));
            }while(cur.moveToNext());
        }

        //  free up cursor and db resources
        cur.close();
        db.close();

        return temp;
    }

    public Array
}
