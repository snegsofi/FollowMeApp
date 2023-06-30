package com.example.myapplication.Json;
import java.util.List;

import com.google.gson.annotations.SerializedName;

   
public class MapPosition {

   @SerializedName("type")
   String type;

   @SerializedName("coordinates")
   List<Double> coordinates;


    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    
    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
    public List<Double> getCoordinates() {
        return coordinates;
    }
    
}