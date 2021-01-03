package com.org.softdrinks.models;

public class SearchModel
{
    String name;
    String type;
    int id;
    String imageURI;

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public SearchModel(String name, String type, int id, String imageURI) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.imageURI = imageURI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
