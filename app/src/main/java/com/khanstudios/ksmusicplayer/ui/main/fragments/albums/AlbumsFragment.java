package com.khanstudios.ksmusicplayer.ui.main.fragments.albums;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khanstudios.ksmusicplayer.R;
import com.khanstudios.ksmusicplayer.databinding.FragmentAlbumsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AlbumsFragment extends Fragment implements LifecycleOwner {

    private FragmentAlbumsBinding binding;
    private AlbumsViewModel viewModel;

    public AlbumsFragment() {
        // Required empty public constructor
    }

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_albums, container, false);
        viewModel = new ViewModelProvider(this).get(AlbumsViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }
}