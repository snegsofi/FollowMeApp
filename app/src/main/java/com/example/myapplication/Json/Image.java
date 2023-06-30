package com.example.myapplication.Json;

import com.google.gson.annotations.SerializedName;

   
public class Image {

   @SerializedName("url")
   String url;

   @SerializedName("title")
   String title;


    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    
}