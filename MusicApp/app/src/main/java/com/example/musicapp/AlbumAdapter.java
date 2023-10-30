package com.example.musicapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

    private Context musicContext;
    private ArrayList<MusicFiles> albumFiles;

    public AlbumAdapter(Context musicContext, ArrayList<MusicFiles> albumFiles) {
        this.musicContext = musicContext;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(musicContext).inflate(R.layout.album_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txtAlbumTitle.setText(albumFiles.get(position).getAlbum());
        byte[] albumPic = getAlbumPic(albumFiles.get(position).getPath());
        if (albumPic != null)
            Glide.with(musicContext).asBitmap().load(albumPic).into(holder.imgAlbum);
        else
            Glide.with(musicContext).asBitmap().load(R.drawable.music_playing).into(holder.imgAlbum);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(musicContext, AlbumDetails.class);
                intent.putExtra("albumName", albumFiles.get(position).getAlbum());
                musicContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAlbum;
        TextView txtAlbumTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.imageViewAlbumMusic);
            txtAlbumTitle = itemView.findViewById(R.id.textViewAlbumTitle);
        }
    }

    private byte[] getAlbumPic(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] hinhAnh = retriever.getEmbeddedPicture();
        retriever.release();
        return hinhAnh;
    }

    public void updateAlbumList(ArrayList<MusicFiles> musicFilesArrayList) {
        albumFiles = new ArrayList<>();
        albumFiles.addAll(musicFilesArrayList);
        notifyDataSetChanged();
    }
}
