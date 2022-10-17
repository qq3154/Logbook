package com.example.logbook.Model;

public class MyImage {
    private int id;
    private String url;
    private String path;
    private String bitmap;
    private String location;

    public MyImage(int id, String url, String path, String bitmap, String location) {
        this.id = id;
        this.url = url;
        this.path = path;
        this.bitmap = bitmap;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
