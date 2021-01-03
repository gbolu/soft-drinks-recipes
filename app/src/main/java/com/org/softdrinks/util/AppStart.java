package com.org.softdrinks.util;

import android.app.Application;

import com.org.softdrinks.controllers.DrinkController;

public class AppStart extends Application
{
    //  seed db on app start
    @Override
    public void onCreate() {
        new Thread(){
            @Override
            public void run() {
                new DrinkController(getApplicationContext()).seedDB(getApplicationContext());
            }
        }.start();
        super.onCreate();
    }
}
