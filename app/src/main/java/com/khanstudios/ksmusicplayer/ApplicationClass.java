package com.khanstudios.ksmusicplayer;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
