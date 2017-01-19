package com.buychat.pojos;

/**
 * Created by nihas-mac on 11/08/2016.
 */
public class Image {

    String name;
    String url;

    public Image(String name,String url){
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
