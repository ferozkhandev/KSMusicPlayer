package com.khanstudios.ksmusicplayer.ui.main.fragments.songs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khanstudios.ksmusicplayer.R;
import com.khanstudios.ksmusicplayer.adapters.SongsAdapter;
import com.khanstudios.ksmusicplayer.databinding.FragmentSongsBinding;
import com.khanstudios.ksmusicplayer.utils.MusicFiles;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SongsFragment extends Fragment implements LifecycleOwner {

    private FragmentSongsBinding binding;
    private SongsViewModel viewModel;
    private SongsAdapter adapter;
    public static final int REQUEST_CODE = 1001;
    public static List<MusicFiles> musicFiles;

    public SongsFragment() {
        // Required empty public constructor
    }

    public static SongsFragment newInstance() {
        return new SongsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_songs, container, false);
        viewModel = new ViewModelProvider(this).get(SongsViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        initRecyclerView();
        return binding.getRoot();
    }

    private void getAudioFiles(){
        viewModel.getAudioFiles().observe(getViewLifecycleOwner(), musicFiles -> {
            if (musicFiles!=null && !musicFiles.isEmpty()){
                adapter.setMusicFilesList(musicFiles);
                SongsFragment.musicFiles = musicFiles;
            }
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext());
        binding.recyclerview.setLayoutManager(layoutManager);
        binding.recyclerview.setItemViewCacheSize(20);
        binding.recyclerview.setDrawingCacheEnabled(true);
        binding.recyclerview.setHasFixedSize(true);
        adapter = new SongsAdapter();
        binding.recyclerview.setAdapter(adapter);
        checkPermissions();
    }

    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P ){
            if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            } else {
                getAudioFiles();
            }
        } else {
            getAudioFiles();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                getActivity().finish();
            } else {
                getAudioFiles();
            }
        }
    }
}