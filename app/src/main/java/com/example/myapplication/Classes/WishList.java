package com.example.myapplication.Classes;

import java.util.List;

public class WishList {
    // класс, хранящий поля списка избранных туров

    private String userId;
    private List<String> favouriteList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getFavouriteList() {
        return favouriteList;
    }

    public void setFavouriteList(List<String> favouriteList) {
        this.favouriteList = favouriteList;
    }

    public WishList(String userId, List<String> favouriteList) {
        this.userId = userId;
        this.favouriteList = favouriteList;
    }
    public WishList() {
    }
}
