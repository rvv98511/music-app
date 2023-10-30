package com.example.musicapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import static com.example.musicapp.MusicAdapter.myMusicFiles;
import static com.example.musicapp.AlbumDetailsAdapter.myAlbumSong;
import static com.example.musicapp.MainActivity.repeatBoolean;
import static com.example.musicapp.MainActivity.shuffleBoolean;


public class PlayerActivity extends AppCompatActivity {

    TextView txtSongTitle, txtSongSinger, txtDurationPlayed, txtDurationTotal;
    ImageView imgCoverSong, imgBackButton, imgPreviousButton, imgNextButton, imgShuffleButton, imgRepeatButton;
    FloatingActionButton floatActPlayPauseButton;
    SeekBar seekBar;
    int position;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        myInformation();
        getIntentMethod();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        imgShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffleBoolean) {
                    shuffleBoolean = false;
                    imgShuffleButton.setImageResource(R.drawable.shuffle_off);
                }
                else {
                    shuffleBoolean = true;
                    imgShuffleButton.setImageResource(R.drawable.shuffle_on);
                    repeatBoolean = false;
                    imgRepeatButton.setImageResource(R.drawable.repeat_off);
                }
            }
        });

        imgRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatBoolean) {
                    repeatBoolean = false;
                    imgRepeatButton.setImageResource(R.drawable.repeat_off);
                }
                else {
                    repeatBoolean = true;
                    imgRepeatButton.setImageResource(R.drawable.repeat_on);
                    shuffleBoolean = false;
                    imgShuffleButton.setImageResource(R.drawable.shuffle_off);
                }
            }
        });

        imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        playThreadButton();
        preThreadButton();
        nextThreadButton();
        super.onResume();
    }

    private void playThreadButton() {
        Thread playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                floatActPlayPauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            floatActPlayPauseButton.setImageResource(R.drawable.play);
        }
        else {
            mediaPlayer.start();
            floatActPlayPauseButton.setImageResource(R.drawable.pause);
        }
        setDurationSongTotal();
        setDurationSongPlayed();
    }

    private void preThreadButton() {
        Thread preThread = new Thread() {
            @Override
            public void run() {
                super.run();
                imgPreviousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preBtnClicked();
                    }
                });
            }
        };
        preThread.start();
    }

    private void preBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffleBoolean)
                position = getRandom(listSongs.size() - 1);
            else if (!repeatBoolean)
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffleBoolean)
                position = getRandom(listSongs.size() - 1);
            else if (!repeatBoolean)
                position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            floatActPlayPauseButton.setImageResource(R.drawable.play);
        }
        setDurationSongTotal();
        setDurationSongPlayed();
        getInfoSong();
        getCoverSong(uri);
    }

    private void nextThreadButton() {
        Thread nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                imgNextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffleBoolean)
                position = getRandom(listSongs.size() - 1);
            else if (!repeatBoolean)
                position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffleBoolean)
                position = getRandom(listSongs.size() - 1);
            else if (!repeatBoolean)
                position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            floatActPlayPauseButton.setImageResource(R.drawable.play);
        }
        setDurationSongTotal();
        setDurationSongPlayed();
        getInfoSong();
        getCoverSong(uri);
    }

    private void setDurationSongTotal() {
        SimpleDateFormat durationSong = new SimpleDateFormat("mm:ss");
        txtDurationTotal.setText(durationSong.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void setDurationSongPlayed() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat durationSong = new SimpleDateFormat("mm:ss");
                txtDurationPlayed.setText(durationSong.format(mediaPlayer.getCurrentPosition()));
                handler.postDelayed(this, 500);
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nextBtnClicked();
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            floatActPlayPauseButton.setImageResource(R.drawable.pause);
                        }
                    }
                });
            }
        }, 100);
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", position);
        String sender = getIntent().getStringExtra("sender");
        if (sender != null)
            listSongs = myAlbumSong;
        else
            listSongs = myMusicFiles;
        if (listSongs != null) {
            floatActPlayPauseButton.setImageResource(R.drawable.pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if (shuffleBoolean)
            imgShuffleButton.setImageResource(R.drawable.shuffle_on);
        else if (repeatBoolean)
            imgRepeatButton.setImageResource(R.drawable.repeat_on);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        setDurationSongTotal();
        setDurationSongPlayed();
        getInfoSong();
        getCoverSong(uri);
    }

    private void myInformation() {
        txtSongTitle = findViewById(R.id.song_title);
        txtSongSinger = findViewById(R.id.singer_name);
        txtDurationPlayed = findViewById(R.id.textViewDurationPlayed);
        txtDurationTotal = findViewById(R.id.textViewDurationTotal);
        imgCoverSong = findViewById(R.id.imageViewCoverSong);
        imgBackButton = findViewById(R.id.imageViewBackButton);
        imgPreviousButton = findViewById(R.id.imageViewPreviousButton);
        imgNextButton = findViewById(R.id.imageViewNextButton);
        imgShuffleButton = findViewById(R.id.imageViewShuffleButton);
        imgRepeatButton = findViewById(R.id.imageViewRepeatButton);
        floatActPlayPauseButton = findViewById(R.id.playPauseButton);
        seekBar = findViewById(R.id.seekBarSong);
    }

    private void getInfoSong() {
        txtSongTitle.setText(listSongs.get(position).getTitle());
        txtSongSinger.setText(listSongs.get(position).getSinger());
    }

    private void getCoverSong(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] hinhAnh = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if (hinhAnh != null) {
            bitmap = BitmapFactory.decodeByteArray(hinhAnh, 0, hinhAnh.length);
            imageCoverAnimation(this, imgCoverSong, bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null) {
                        ImageView gradient = findViewById(R.id.imageViewGradient);
                        RelativeLayout myPlayer = findViewById(R.id.musicPlayer);
                        gradient.setBackgroundResource(R.drawable.player_gradient);
                        myPlayer.setBackgroundResource(R.drawable.player_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {0x00000000, swatch.getRgb()});
                        gradient.setBackground(gradientDrawable);
                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {swatch.getRgb(), swatch.getRgb()});
                        myPlayer.setBackground(gradientDrawableBg);
                        txtSongTitle.setTextColor(swatch.getTitleTextColor());
                        txtSongSinger.setTextColor(swatch.getBodyTextColor());
                    }
                }
            });
        }
        else {
            Glide.with(this).asBitmap().load(R.drawable.music_playing).into(imgCoverSong);
            ImageView gradient = findViewById(R.id.imageViewGradient);
            RelativeLayout myPlayer = findViewById(R.id.musicPlayer);
            gradient.setBackgroundResource(R.drawable.player_gradient);
            myPlayer.setBackgroundResource(R.drawable.player_bg);
            txtSongTitle.setTextColor(Color.WHITE);
            txtSongSinger.setTextColor(Color.LTGRAY);
        }
    }

    private void imageCoverAnimation(final Context context, final ImageView imageView, final Bitmap bitmap) {
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        final Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
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
}
