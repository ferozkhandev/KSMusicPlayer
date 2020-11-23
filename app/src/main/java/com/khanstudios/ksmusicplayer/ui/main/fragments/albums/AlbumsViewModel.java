package com.khanstudios.ksmusicplayer.ui.main.fragments.albums;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.ViewModel;

public class AlbumsViewModel extends ViewModel {

    private AlbumsRepository repository;

    @ViewModelInject
    public AlbumsViewModel(AlbumsRepository repository) {
        this.repository = repository;
    }
}
