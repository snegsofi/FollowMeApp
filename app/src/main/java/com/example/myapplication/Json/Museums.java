package com.example.myapplication.Json;
import java.util.List;

import com.google.gson.annotations.SerializedName;

   
public class Museums {

   @SerializedName("status")
   int status;

   @SerializedName("nextPage")
   String nextPage;

   @SerializedName("cursor")
   String cursor;

   @SerializedName("total")
   int total;


   @SerializedName("o")
   boolean o;

   @SerializedName("s")
   int s;

   @SerializedName("l")
   int l;

   @SerializedName("data")
   List<Data> data;

   @SerializedName("count")
   int count;


    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
    
    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
    public String getNextPage() {
        return nextPage;
    }
    
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
    public String getCursor() {
        return cursor;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }

    
    public void setO(boolean o) {
        this.o = o;
    }
    public boolean getO() {
        return o;
    }
    
    public void setS(int s) {
        this.s = s;
    }
    public int getS() {
        return s;
    }
    
    public void setL(int l) {
        this.l = l;
    }
    public int getL() {
        return l;
    }
    
    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }
    
}