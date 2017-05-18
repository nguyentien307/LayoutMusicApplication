package com.example.tiennguyen.layoutmusicapplication;

/**
 * Created by TIENNGUYEN on 5/10/2017.
 */

public class Song {
    private String title;
    private String artist;
    private String url;
    private String liric;
    private String bgcover;
    private String mv;
    private String artisturl;

    public Song(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public Song(String title, String artist, String url, String liric, String bgcover, String mv, String artisturl) {
        this.title = title;
        this.artist = artist;
        this.url = url;
        this.liric = liric;
        this.bgcover = bgcover;
        this.mv = mv;
        this.artisturl = artisturl;
    }

    public Song(String title, String artist, String url, String bgcover, String artisturl) {
        this.title = title;
        this.artist = artist;
        this.url = url;
        this.bgcover = bgcover;
        this.artisturl = artisturl;
    }

    public Song(String title, String artist, String url, String bgcover, String mv, String artisturl) {
        this.title = title;
        this.artist = artist;
        this.url = url;
        this.bgcover = bgcover;
        this.mv = mv;
        this.artisturl = artisturl;
    }

    public String getLiric() {
        return liric;
    }

    public void setLiric(String liric) {
        this.liric = liric;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBgcover() {
        return bgcover;
    }

    public void setBgcover(String bgcover) {
        this.bgcover = bgcover;
    }

    public String getMv() {
        return mv;
    }

    public void setMv(String mv) {
        this.mv = mv;
    }

    public String getArtisturl() {
        return artisturl;
    }

    public void setArtisturl(String artisturl) {
        this.artisturl = artisturl;
    }
}
