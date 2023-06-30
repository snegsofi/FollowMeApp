package com.example.myapplication.Json;

import com.google.gson.annotations.SerializedName;

public class Phones {

   @SerializedName("value")
   String value;

    public void setValue(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    
}