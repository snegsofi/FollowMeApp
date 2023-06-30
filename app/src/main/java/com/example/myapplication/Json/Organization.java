package com.example.myapplication.Json;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

   
public class Organization {

   @SerializedName("id")
   int id;

   @SerializedName("name")
   String name;

   @SerializedName("inn")
   String inn;

   @SerializedName("type")
   String type;

   @SerializedName("address")
   Address address;

   @SerializedName("subordinationIds")
   List<Integer> subordinationIds;

   @SerializedName("localeIds")
   List<Integer> localeIds;

   @SerializedName("locale")
   Locale locale;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    
    public void setInn(String inn) {
        this.inn = inn;
    }
    public String getInn() {
        return inn;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    public Address getAddress() {
        return address;
    }
    
    public void setSubordinationIds(List<Integer> subordinationIds) {
        this.subordinationIds = subordinationIds;
    }
    public List<Integer> getSubordinationIds() {
        return subordinationIds;
    }

    public void setLocaleIds(List<Integer> localeIds) {
        this.localeIds = localeIds;
    }
    public List<Integer> getLocaleIds() {
        return localeIds;
    }
    
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public Locale getLocale() {
        return locale;
    }

    
}