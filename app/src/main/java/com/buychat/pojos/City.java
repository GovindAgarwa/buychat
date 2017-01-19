package com.buychat.pojos;

/**
 * Created by Snyxius Technologies on 8/19/2016.
 */
public class City {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    String city_name;




    String city_id;
    String category_id;

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getMarket_name() {
        return market_name;
    }

    public void setMarket_name(String market_name) {
        this.market_name = market_name;
    }

    public String getMarket_image() {
        return market_image;
    }

    public void setMarket_image(String market_image) {
        this.market_image = market_image;
    }

    String market_name;
    String market_image;

}
