package com.example.myapplication.Json;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

   
public class Data {

   @SerializedName("_id")
   String Id;

   @SerializedName("nativeId")
   String nativeId;

   @SerializedName("hash")
   Date hash;

   @SerializedName("data")
   DataFirstLevel data;

   @SerializedName("status")
   int status;

   @SerializedName("nativeName")
   String nativeName;

   @SerializedName("activated")
   Date activated;

   @SerializedName("created")
   Date created;

   @SerializedName("modified")
   Date modified;

   @SerializedName("odSetVersions")
   List<String> odSetVersions;

   @SerializedName("odSetVersion")
   String odSetVersion;

   @SerializedName("updateSession")
   String updateSession;

   @SerializedName("odSchema")
   String odSchema;

   @SerializedName("dataset")
   String dataset;


    public void setId(String Id) {
        this.Id = Id;
    }
    public String getId() {
        return Id;
    }
    
    public void setNativeId(String nativeId) {
        this.nativeId = nativeId;
    }
    public String getNativeId() {
        return nativeId;
    }
    
    public void setHash(Date hash) {
        this.hash = hash;
    }
    public Date getHash() {
        return hash;
    }
    
    public void setData(DataFirstLevel data) {
        this.data = data;
    }
    public DataFirstLevel getData() {
        return data;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
    
    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }
    public String getNativeName() {
        return nativeName;
    }
    
    public void setActivated(Date activated) {
        this.activated = activated;
    }
    public Date getActivated() {
        return activated;
    }
    
    public void setCreated(Date created) {
        this.created = created;
    }
    public Date getCreated() {
        return created;
    }
    
    public void setModified(Date modified) {
        this.modified = modified;
    }
    public Date getModified() {
        return modified;
    }
    
    public void setOdSetVersions(List<String> odSetVersions) {
        this.odSetVersions = odSetVersions;
    }
    public List<String> getOdSetVersions() {
        return odSetVersions;
    }
    
    public void setOdSetVersion(String odSetVersion) {
        this.odSetVersion = odSetVersion;
    }
    public String getOdSetVersion() {
        return odSetVersion;
    }
    
    public void setUpdateSession(String updateSession) {
        this.updateSession = updateSession;
    }
    public String getUpdateSession() {
        return updateSession;
    }
    
    public void setOdSchema(String odSchema) {
        this.odSchema = odSchema;
    }
    public String getOdSchema() {
        return odSchema;
    }
    
    public void setDataset(String dataset) {
        this.dataset = dataset;
    }
    public String getDataset() {
        return dataset;
    }

}