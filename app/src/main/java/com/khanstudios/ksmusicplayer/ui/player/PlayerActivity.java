package com.khanstudios.ksmusicplayer.ui.player;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.khanstudios.ksmusicplayer.R;
import com.khanstudios.ksmusicplayer.databinding.ActivityPlayerBinding;
import com.khanstudios.ksmusicplayer.ui.main.fragments.songs.SongsFragment;
import com.khanstudios.ksmusicplayer.utils.MusicFiles;

import java.util.Random;

import static com.khanstudios.ksmusicplayer.utils.AlbumArt.getAlbumArt;

public class PlayerActivity extends AppCompatActivity implements LifecycleOwner, MediaPlayer.OnCompletionListener {

    private ActivityPlayerBinding binding;
    private PlayerViewModel viewModel;
    private int position = -1;
    private Uri uri;
    private MediaPlayer mediaPlayer;
    private boolean shuffle = false;
    private boolean repeat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        viewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        position = getIntent().getIntExtra("position", -1);
        if (position>0){
            if (SongsFragment.musicFiles!=null && !SongsFragment.musicFiles.isEmpty()){
                binding.playStatus.setImageResource(R.drawable.ic_baseline_pause_24);
                MusicFiles musicFiles = SongsFragment.musicFiles.get(position);
                uri = Uri.parse(musicFiles.getPath());
                initMetaData(musicFiles);
                if (mediaPlayer!=null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(this, uri);
                mediaPlayer.start();
                binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
            }
        }
        mediaPlayer.setOnCompletionListener(this);
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                binding.runTime.setText(""+progress);
                if (mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekBar.setProgress(currentPosition);
                    binding.runTime.setText(formattedTime(currentPosition));
                }
                new Handler().postDelayed(this, 1000);
            }
        });
        binding.shuffle.setOnClickListener(v -> {
            if (shuffle){
                shuffle = false;
                binding.shuffle.setImageResource(R.drawable.ic_baseline_shuffle_off_24);
            } else {
                shuffle = true;
                binding.shuffle.setImageResource(R.drawable.ic_baseline_shuffle_on_24);
            }
        });
        binding.repeat.setOnClickListener(v -> {
            if (repeat){
                repeat = false;
                binding.repeat.setImageResource(R.drawable.ic_baseline_repeat_off_24);
            } else {
                repeat = true;
                binding.repeat.setImageResource(R.drawable.ic_baseline_repeat_on_24);
            }
        });
    }

    private String formattedTime(int currentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":0" + seconds;
        if (seconds.length() == 1){
            return totalNew;
        } else {
            return totalOut;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        playStatusThread();
        skipNextThread();
        skipPreviousThread();
    }

    private void initMetaData(MusicFiles musicFiles) {
        loadCoverArt(musicFiles);
        binding.songTitle.setText(musicFiles.getTitle());
        binding.songArtist.setText(musicFiles.getArtist());
        int duration = Integer.parseInt(musicFiles.getDuration()) / 1000;
        binding.totalTime.setText(formattedTime(duration));
    }

    private void loadCoverArt(MusicFiles musicFiles){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            byte[] image = getAlbumArt(musicFiles.getPath());
            Bitmap bitmap = null;
            if (image != null){
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageAnimation(this, binding.albumImage, bitmap);
                Palette.from(bitmap).generate(palette -> {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch!=null){
                        binding.gradientImage.setImageResource(R.drawable.gradient_bg);
                        binding.playerActivityLayout.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), 0x00000000});
                        binding.gradientImage.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableLayout = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), swatch.getRgb()});
                        binding.playerActivityLayout.setBackground(gradientDrawableLayout);
                        binding.songTitle.setTextColor(swatch.getTitleTextColor());
                        binding.songArtist.setTextColor(swatch.getBodyTextColor());
                    } else {
                        binding.gradientImage.setImageResource(R.drawable.gradient_bg);
                        binding.playerActivityLayout.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0x00000000});
                        binding.gradientImage.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableLayout = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000, 0xff000000});
                        binding.playerActivityLayout.setBackground(gradientDrawableLayout);
                        binding.songTitle.setTextColor(Color.WHITE);
                        binding.songArtist.setTextColor(Color.DKGRAY);
                    }
                });
            } else {
                Glide.with(this).asBitmap()
                        .load(R.drawable.music_icon)
                        .into(binding.albumImage);
            }
        } else {
            Glide.with(this).asBitmap()
                    .load(R.drawable.music_icon)
                    .into(binding.albumImage);
        }
    }

    private void playStatusThread(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                binding.playStatus.setOnClickListener(v -> {
                    if (mediaPlayer.isPlaying()){
                        binding.playStatus.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        mediaPlayer.pause();
                        binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
                        runOnUiThread(() -> {
                            if (mediaPlayer!=null){
                                int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                binding.seekBar.setProgress(currentPosition);
                                binding.runTime.setText(formattedTime(currentPosition));
                            }
                            new Handler().postDelayed(this, 1000);
                        });
                    } else {
                        binding.playStatus.setImageResource(R.drawable.ic_baseline_pause_24);
                        mediaPlayer.start();
                        binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    }
                });
            }
        };
        thread.start();
    }

    private void skipNextThread(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                binding.skipNext.setOnClickListener(v -> {
                    skipNextClicked();
                });
            }
        };
        thread.start();
    }

    private void skipNextClicked() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (shuffle && !repeat){
            position = new Random().nextInt(SongsFragment.musicFiles.size()-1)+1;
        } else if (!shuffle && !repeat){
            position = (position + 1) % SongsFragment.musicFiles.size();
        }
        MusicFiles musicFiles = SongsFragment.musicFiles.get(position);
        uri = Uri.parse(musicFiles.getPath());
        mediaPlayer = MediaPlayer.create(PlayerActivity.this, uri);
        initMetaData(musicFiles);
        binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekBar.setProgress(currentPosition);
                    binding.runTime.setText(formattedTime(currentPosition));
                }
                new Handler().postDelayed(this, 1000);
            }
        });
        mediaPlayer.setOnCompletionListener(PlayerActivity.this);
        if (mediaPlayer.isPlaying()){
            binding.playStatus.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();
        } else {
            binding.playStatus.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    private void skipPreviousThread(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                binding.skipPrevious.setOnClickListener(v -> {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if (shuffle && !repeat){
                        position = new Random().nextInt(SongsFragment.musicFiles.size()-1)+1;
                    } else if (!shuffle && !repeat){
                        position = (position - 1) < 0 ? SongsFragment.musicFiles.size() - 1 : (position - 1);
                    }
                    MusicFiles musicFiles = SongsFragment.musicFiles.get(position);
                    uri = Uri.parse(musicFiles.getPath());
                    mediaPlayer = MediaPlayer.create(PlayerActivity.this, uri);
                    initMetaData(musicFiles);
                    binding.seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    runOnUiThread(() -> {
                        if (mediaPlayer!=null){
                            int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                            binding.seekBar.setProgress(currentPosition);
                            binding.runTime.setText(formattedTime(currentPosition));
                        }
                        new Handler().postDelayed(this, 1000);
                    });
                    if (mediaPlayer.isPlaying()){
                        binding.playStatus.setImageResource(R.drawable.ic_baseline_pause_24);
                        mediaPlayer.start();
                    } else {
                        binding.playStatus.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }
                });
            }
        };
        thread.start();
    }

    public void imageAnimation(Context context, ImageView imageView, Bitmap bitmap){
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        skipNextClicked();
        if (mediaPlayer!=null){
            mediaPlayer = MediaPlayer.create(this, uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }
}