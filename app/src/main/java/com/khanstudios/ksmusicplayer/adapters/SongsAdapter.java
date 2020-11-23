package com.khanstudios.ksmusicplayer.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.khanstudios.ksmusicplayer.R;
import com.khanstudios.ksmusicplayer.ui.player.PlayerActivity;
import com.khanstudios.ksmusicplayer.utils.MusicFiles;

import java.util.ArrayList;
import java.util.List;

import static com.khanstudios.ksmusicplayer.utils.AlbumArt.getAlbumArt;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    List<MusicFiles> musicFilesList = new ArrayList<>();

    public void setMusicFilesList(List<MusicFiles> musicFilesList) {
        this.musicFilesList = musicFilesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songs_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicFiles musicFiles = musicFilesList.get(position);
        holder.songTitle.setText(musicFiles.getTitle());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlayerActivity.class);
            intent.putExtra("position", position);
            v.getContext().startActivity(intent);
        });
        Bitmap bitmap = BitmapFactory.decodeFile(musicFiles.getPath());
        holder.songIcon.setImageBitmap(bitmap);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            byte[] image = getAlbumArt(musicFiles.getPath());
            if (image != null){
                Glide.with(holder.itemView.getContext()).asBitmap()
                        .load(image)
                        .into(holder.songIcon);
            } else {
                Glide.with(holder.itemView.getContext()).asBitmap()
                        .load(R.drawable.music_icon)
                        .into(holder.songIcon);
            }
        } else {
            Glide.with(holder.itemView.getContext()).asBitmap()
                    .load(R.drawable.music_icon)
                    .into(holder.songIcon);
        }
    }

    @Override
    public int getItemCount() {
        return musicFilesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView songIcon;
        MaterialTextView songTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            songIcon = itemView.findViewById(R.id.song_icon);
            songTitle = itemView.findViewById(R.id.song_title);
        }
    }
}
