package com.example.tiennguyen.layoutmusicapplication;

/**
 * Created by TIENNGUYEN on 5/11/2017.
 */

public class Album {
    private String img;
    private String title;
    private String artist;


    public Album(String img, String title, String artise) {
        this.img = img;
        this.title = title;
        this.artist = artise;
    }



    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }


}

