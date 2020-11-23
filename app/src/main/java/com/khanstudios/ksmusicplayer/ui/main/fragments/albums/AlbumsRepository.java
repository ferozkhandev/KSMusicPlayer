package com.khanstudios.ksmusicplayer.ui.main.fragments.albums;

import android.database.Cursor;

import androidx.lifecycle.MutableLiveData;

import com.khanstudios.ksmusicplayer.utils.MusicFiles;

import java.util.List;

import javax.inject.Inject;

public class AlbumsRepository {

    private Cursor albumsCursor;
    private MutableLiveData<List<MusicFiles>> albumsLiveData;

    @Inject
    public AlbumsRepository(MutableLiveData<List<MusicFiles>> albumsLiveData) {
        this.albumsLiveData = albumsLiveData;
    }
}
