package com.example.musicapp;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context musicContext;
    static ArrayList<MusicFiles> myMusicFiles;

    MusicAdapter(Context musicContext, ArrayList<MusicFiles> myMusicFiles) {
        this.musicContext = musicContext;
        this.myMusicFiles = myMusicFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(musicContext).inflate(R.layout.music_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txtSongTitle.setText(myMusicFiles.get(position).getTitle());
        holder.txtSongSinger.setText(myMusicFiles.get(position).getSinger());
        byte[] albumPic = getAlbumPic(myMusicFiles.get(position).getPath());
        if (albumPic != null)
            Glide.with(musicContext).asBitmap().load(albumPic).into(holder.imgSong);
        else
            Glide.with(musicContext).asBitmap().load(R.drawable.music_icon).into(holder.imgSong);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(musicContext, PlayerActivity.class);
                intent.putExtra("position", position);
                musicContext.startActivity(intent);
            }
        });

        holder.imgMenuDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenuDelete = new PopupMenu(musicContext, v);
                popupMenuDelete.getMenuInflater().inflate(R.menu.menu_delete, popupMenuDelete.getMenu());
                popupMenuDelete.show();
                popupMenuDelete.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                Toast.makeText(musicContext, "Delete clicked!", Toast.LENGTH_SHORT).show();
                                cofirmDeleteDialog(position, v);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void cofirmDeleteDialog(final int position, final View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(musicContext, R.style.AlertDialogCustom);
        alertDialog.setIcon(R.drawable.dialog_question);
        alertDialog.setTitle("Permanently delete this song?");
        alertDialog.setMessage("This action will delete the song from the system and cannot be canceled once done.");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFile(position, v);
                updateAlbumFragment();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = alertDialog.show();

        TextView messageDialog = dialog.findViewById(android.R.id.message);
        messageDialog.setTextColor(Color.WHITE);
    }

    private void deleteFile(int position, View v) {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(myMusicFiles.get(position).getId()));
        String songTitle = myMusicFiles.get(position).getTitle();
        File file = new File(myMusicFiles.get(position).getPath());
        boolean deleted = file.delete();
        if (deleted) {
            musicContext.getContentResolver().delete(contentUri, null, null);
            myMusicFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, myMusicFiles.size());
            Snackbar.make(v, "Song deleted: " + songTitle, Snackbar.LENGTH_LONG).show();
        }
        else
            Snackbar.make(v, "Can't be deleted!", Snackbar.LENGTH_LONG).show();
    }

    private void updateAlbumFragment() {
        ArrayList<String> myAlbumTitle = new ArrayList<>();
        ArrayList<MusicFiles> myAlbumList = new ArrayList<>();
        for (MusicFiles myAlbum : myMusicFiles) {
            if (!myAlbumTitle.contains(myAlbum.getAlbum())) {
                myAlbumTitle.add(myAlbum.getAlbum());
                myAlbumList.add(myAlbum);
            }
        }
        AlbumFragment.albumAdapter.updateAlbumList(myAlbumList);
    }

    @Override
    public int getItemCount() {
        return myMusicFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtSongTitle, txtSongSinger;
        ImageView imgSong, imgMenuDots;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSongTitle = itemView.findViewById(R.id.textViewSongTitle);
            txtSongSinger = itemView.findViewById(R.id.textViewSongSinger);
            imgSong = itemView.findViewById(R.id.imageViewSongIcon);
            imgMenuDots = itemView.findViewById(R.id.imageViewMenuDots);
        }
    }

    private byte[] getAlbumPic(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] hinhAnh = retriever.getEmbeddedPicture();
        retriever.release();
        return hinhAnh;
    }

    public void updateSongList(ArrayList<MusicFiles> musicFilesArrayList) {
        myMusicFiles = new ArrayList<>();
        myMusicFiles.addAll(musicFilesArrayList);
        notifyDataSetChanged();
    }
}
