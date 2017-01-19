package com.buychat.pojos;

/**
 * Created by nihas-mac on 16/08/2016.
 */
public class Address {

    String id;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    String access_token;
    String locality;
    String flat_no_floor_name;
    String landmark;
    String tag_address;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag;

    public String getFlat_no_floor_name() {
        return flat_no_floor_name;
    }

    public void setFlat_no_floor_name(String flat_no_floor_name) {
        this.flat_no_floor_name = flat_no_floor_name;
    }


    public String getTag_address() {
        return tag_address;
    }

    public void setTag_address(String tag_address) {
        this.tag_address = tag_address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }


    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }


}
