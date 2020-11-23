package com.khanstudios.ksmusicplayer.ui.main.fragments.songs;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.khanstudios.ksmusicplayer.utils.MusicFiles;

import java.util.List;

public class SongsViewModel extends ViewModel {

    private SongsRepository repository;

    @ViewModelInject
    public SongsViewModel(SongsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<MusicFiles>> getAudioFiles(){
        return repository.getAudioFiles();
    }
}
