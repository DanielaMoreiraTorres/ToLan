
package com.example.tolan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUploadImage {

    @SerializedName("publicid")
    @Expose
    private String publicid;
    @SerializedName("url")
    @Expose
    private String url;

    public String getPublicid() {
        return publicid;
    }

    public void setPublicid(String publicid) {
        this.publicid = publicid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
