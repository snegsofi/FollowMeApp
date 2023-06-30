package com.example.myapplication.Json;

import com.google.gson.annotations.SerializedName;

   
public class Category {

   @SerializedName("name")
   String name;

   @SerializedName("sysName")
   String sysName;


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }
    public String getSysName() {
        return sysName;
    }
    
}