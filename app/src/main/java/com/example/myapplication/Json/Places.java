package com.example.myapplication.Json;

import com.google.gson.annotations.SerializedName;

public class Places {
    @SerializedName("address")
    Address address;

    public Address getAddress() {
        return address;
    }
}
