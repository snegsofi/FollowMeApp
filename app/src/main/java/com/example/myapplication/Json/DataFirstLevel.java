package com.example.myapplication.Json;

import com.google.gson.annotations.SerializedName;

public class DataFirstLevel {


    @SerializedName("general")
    General general;

    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }
}
