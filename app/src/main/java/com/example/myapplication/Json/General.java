package com.example.myapplication.Json;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

   
public class General implements Serializable {

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

    @SerializedName("status")
    String status;

    @SerializedName("address")
    Address address;

    @SerializedName("category")
    Category category;

    @SerializedName("contacts")
    Contacts contacts;

    @SerializedName("externalInfo")
    List<ExternalInfo> externalInfo;

    @SerializedName("gallery")
    List<Gallery> gallery;

    @SerializedName("image")
    Image image;

    @SerializedName("localeIds")
    List<Integer> localeIds;

    @SerializedName("locale")
    Locale locale;

    @SerializedName("organization")
    Organization organization;



    // exhibitions
    @SerializedName("ageRestriction")
    int ageRestriction;
    @SerializedName("isFree")
    boolean isFree;
    @SerializedName("price")
    int price;
    @SerializedName("maxPrice")
    int maxPrice;
    @SerializedName("start")
    Date start;
    @SerializedName("end")
    Date end;

    @SerializedName("places")
    List<Places> places;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public Contacts getContacts() {
        return contacts;
    }
    

    public List<ExternalInfo> getExternalInfo() {
        return externalInfo;
    }
    
    public List<Gallery> getGallery() {
        return gallery;
    }
    
    public Image getImage() {
        return image;
    }
    
    public List<Integer> getLocaleIds() {
        return localeIds;
    }
    
    public Locale getLocale() {
        return locale;
    }
    
    public Organization getOrganization() {
        return organization;
    }

    // exhibitions
    public int getAgeRestriction() {
        return ageRestriction;
    }

    public boolean isFree() {
        return isFree;
    }

    public int getPrice() {
        return price;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public List<Places> getPlaces() {
        return places;
    }
}
