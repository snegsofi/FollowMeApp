package com.example.myapplication.Json;

import com.google.gson.annotations.SerializedName;

   
public class Locale {

   @SerializedName("name")
   String name;

   @SerializedName("timezone")
   String timezone;

   @SerializedName("sysName")
   String sysName;

   @SerializedName("id")
   int id;


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    public String getTimezone() {
        return timezone;
    }
    
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }
    public String getSysName() {
        return sysName;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    
}