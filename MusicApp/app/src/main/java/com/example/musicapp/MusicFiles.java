package com.example.musicapp;

public class MusicFiles {

    private String path, title, singer, album, duration, id;

    public MusicFiles(String path, String title, String singer, String album, String duration, String id) {    // nhấn chuột phải chọn generate / constructor để thêm
        this.path = path;
        this.title = title;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.id = id;
    }

    // nhấn chuột phải chọn generate / getter and setter để thêm

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
