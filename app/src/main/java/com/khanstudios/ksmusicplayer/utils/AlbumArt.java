package com.khanstudios.ksmusicplayer.utils;

import android.media.MediaMetadataRetriever;

public class AlbumArt {

    public static byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
