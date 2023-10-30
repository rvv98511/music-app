package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.musicapp.MainActivity.musicFiles;

public class AlbumDetails extends AppCompatActivity {

    ImageView imgBackButton, imgAlbumPhoto;
    TextView txtAlbumTitle;
    String albumName;
    RecyclerView recyclerViewListSong;
    ArrayList<MusicFiles> albumSongs = new ArrayList<>();
    AlbumDetailsAdapter albumDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        myAlbumInfo();
        loadAlbumPhoto();

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

    private void myAlbumInfo() {
        imgBackButton = findViewById(R.id.imageViewBackButton);
        imgAlbumPhoto = findViewById(R.id.imageViewAlbumPhoto);
        txtAlbumTitle = findViewById(R.id.textViewAlbumTitle);
        albumName = getIntent().getStringExtra("albumName");
        recyclerViewListSong = findViewById(R.id.recyclerViewListSongAlbum);
    }

    private void loadAlbumPhoto() {
        int j = 0;
        for (int i = 0; i < musicFiles.size(); i++)
            if (albumName.equals(musicFiles.get(i).getAlbum())) {
                albumSongs.add(j, musicFiles.get(i));
                j++;
            }
        txtAlbumTitle.setText(albumSongs.get(0).getAlbum());
        txtAlbumTitle.setSelected(true);
        byte[] albumPic = getAlbumPic(albumSongs.get(0).getPath());
        if (albumPic != null)
            Glide.with(this).asBitmap().load(albumPic).into(imgAlbumPhoto);
        else
            Glide.with(this).asBitmap().load(R.drawable.music_playing).into(imgAlbumPhoto);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs.size() < 1)) {
            albumDetailsAdapter = new AlbumDetailsAdapter(this, albumSongs);
            recyclerViewListSong.setAdapter(albumDetailsAdapter);
            recyclerViewListSong.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
    }

    private byte[] getAlbumPic(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] hinhAnh = retriever.getEmbeddedPicture();
        retriever.release();
        return hinhAnh;
    }
}
