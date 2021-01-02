package com.org.softdrinks.models;

public class DrinkModel
{
    String name;
    int ID;
    String drinkDetails;
    int categoryID;
    String category;
    String drinkImageURI;
    String drinkRecipe;

    public String getDrinkRecipe() {
        return drinkRecipe;
    }

    public void setDrinkRecipe(String drinkRecipe) {
        this.drinkRecipe = drinkRecipe;
    }

    public DrinkModel(String category, String name,
                      int ID, String drinkDetails,
                      int categoryID, String drinkImageURI,
                      String drinkRecipe) {
        this.category = category;
        this.name = name;
        this.ID = ID;
        this.drinkDetails = drinkDetails;
        this.categoryID = categoryID;
        this.drinkImageURI = drinkImageURI;
        this.drinkRecipe = drinkRecipe;
    }

    public String getDrinkImageURI() {
        return drinkImageURI;
    }

    public void setDrinkImageURI(String drinkImageURI) {
        this.drinkImageURI = drinkImageURI;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDrinkDetails() {
        return drinkDetails;
    }

    public void setDrinkDetails(String drinkDetails) {
        this.drinkDetails = drinkDetails;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
