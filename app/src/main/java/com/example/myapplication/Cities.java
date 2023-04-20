package com.example.myapplication;

public class Cities {


    private int photo;
    private String name;

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }

    private String url_photo;

    public Cities(String name, String url_photo) {
        this.name = name;
        this.url_photo = url_photo;
    }

    public Cities(int photo, String name) {
        this.photo = photo;
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
