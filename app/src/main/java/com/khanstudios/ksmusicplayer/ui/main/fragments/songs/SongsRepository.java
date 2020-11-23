package com.khanstudios.ksmusicplayer.ui.main.fragments.songs;

import android.database.Cursor;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.khanstudios.ksmusicplayer.utils.MusicFiles;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.khanstudios.ksmusicplayer.utils.ContextClass.application;

public class SongsRepository {

//    private Cursor songsCursor;
    private MutableLiveData<List<MusicFiles>> audioFilesLiveData;

    @Inject
    public SongsRepository(/*Cursor songsCursor,*/ MutableLiveData<List<MusicFiles>> audioFilesLiveData) {
//        this.songsCursor = songsCursor;
        this.audioFilesLiveData = audioFilesLiveData;
    }

    public LiveData<List<MusicFiles>> getAudioFiles(){
        String[] projection = new String[] {
//                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
        };
        String selection = null;
        String[] selectionArgs = new String[] {};
        String sortOrder = null;

        Cursor songsCursor =  application.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );
        List<MusicFiles> musicFilesList = new ArrayList<>();
        while (songsCursor.moveToNext()) {
            // Use an ID column from the projection to get
            // a URI representing the media item itself.
//            Log.d("tests", "hello: "+songsCursor.getCount());
            String album = songsCursor.getString(0);
            String title = songsCursor.getString(1);
            String duration = songsCursor.getString(2);
            String path = songsCursor.getString(3);
            String artist = songsCursor.getString(4);
            musicFilesList.add(new MusicFiles(path, title, artist, album, duration));
        }
        songsCursor.close();
        audioFilesLiveData.setValue(musicFilesList);
        return audioFilesLiveData;
    }
}
