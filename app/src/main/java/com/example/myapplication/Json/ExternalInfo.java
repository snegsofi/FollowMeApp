package com.example.myapplication.Json;

import com.google.gson.annotations.SerializedName;

   
public class ExternalInfo {

   @SerializedName("url")
   String url;

   @SerializedName("serviceName")
   String serviceName;


    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getServiceName() {
        return serviceName;
    }
    
}