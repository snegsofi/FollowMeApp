package com.example.myapplication.Json;

import com.google.gson.annotations.SerializedName;

   
public class Address {

   @SerializedName("street")
   String street;

   @SerializedName("comment")
   String comment;

   @SerializedName("fiasHouseId")
   String fiasHouseId;

   @SerializedName("fiasStreetId")
   String fiasStreetId;

   @SerializedName("fiasCityId")
   String fiasCityId;

   @SerializedName("fiasRegionId")
   String fiasRegionId;

   @SerializedName("fullAddress")
   String fullAddress;

   @SerializedName("mapPosition")
   MapPosition mapPosition;

    public MapPosition getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(MapPosition mapPosition) {
        this.mapPosition = mapPosition;
    }

    public void setStreet(String street) {
        this.street = street;
    }
    public String getStreet() {
        return street;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }
    
    public void setFiasHouseId(String fiasHouseId) {
        this.fiasHouseId = fiasHouseId;
    }
    public String getFiasHouseId() {
        return fiasHouseId;
    }
    
    public void setFiasStreetId(String fiasStreetId) {
        this.fiasStreetId = fiasStreetId;
    }
    public String getFiasStreetId() {
        return fiasStreetId;
    }
    
    public void setFiasCityId(String fiasCityId) {
        this.fiasCityId = fiasCityId;
    }
    public String getFiasCityId() {
        return fiasCityId;
    }
    
    public void setFiasRegionId(String fiasRegionId) {
        this.fiasRegionId = fiasRegionId;
    }
    public String getFiasRegionId() {
        return fiasRegionId;
    }
    
    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
    public String getFullAddress() {
        return fullAddress;
    }
    
}