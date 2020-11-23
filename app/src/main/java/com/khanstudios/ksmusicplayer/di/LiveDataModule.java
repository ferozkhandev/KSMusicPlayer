package com.khanstudios.ksmusicplayer.di;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.khanstudios.ksmusicplayer.utils.MusicFiles;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class LiveDataModule {

    @Provides
    @Singleton
    public static MutableLiveData<List<MusicFiles>> getAudioFiles(){
        return new MutableLiveData<>();
    }
}
