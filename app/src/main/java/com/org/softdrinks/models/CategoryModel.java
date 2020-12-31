package com.org.softdrinks.models;

public class CategoryModel
{
    String name;
    String imageURI;
    int dbID;

    public String getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategoryDetails(String categoryDetails) {
        this.categoryDetails = categoryDetails;
    }

    String categoryDetails;

    public CategoryModel(int dbID, String name, String imageURI, String categoryDetails) {
        this.name = name;
        this.imageURI = imageURI;
        this.dbID = dbID;
        this.categoryDetails = categoryDetails;
    }

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
