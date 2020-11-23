package com.khanstudios.ksmusicplayer.di;

import android.app.Application;
import android.database.Cursor;
import android.provider.MediaStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

/*
@Module
@InstallIn(ApplicationComponent.class)
public class CursorsModule {

    */
/*@Provides
    @Singleton
    public static Cursor getSongsCursor(Application application){
        String[] projection = new String[] {
                MediaStore.Audio.Media.DISPLAY_NAME
        };
        String selection = null;
        String[] selectionArgs = new String[] {};
        String sortOrder = null;

        return application.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );
    }*//*


    */
/*@Provides
    @Singleton
    public static Cursor getAlbumsCursor(Application application){
        String[] projection = new String[] {
                MediaStore.Audio.Media.DISPLAY_NAME
        };
        String selection = null;
        String[] selectionArgs = new String[] {};
        String sortOrder = null;

        return application.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );
    }*//*

}
*/
