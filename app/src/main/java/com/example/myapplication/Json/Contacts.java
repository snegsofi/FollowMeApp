package com.example.myapplication.Json;
import java.util.List;

import com.google.gson.annotations.SerializedName;

   
public class Contacts {

   @SerializedName("website")
   String website;

   @SerializedName("phones")
   List<Phones> phones;


    public void setWebsite(String website) {
        this.website = website;
    }
    public String getWebsite() {
        return website;
    }
    
    public void setPhones(List<Phones> phones) {
        this.phones = phones;
    }
    public List<Phones> getPhones() {
        return phones;
    }
    
}