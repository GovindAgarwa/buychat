package com.buychat.pojos;

import java.io.Serializable;

/**
 * Created by nihas-mac on 12/08/2016.
 */
public class CategoriesPojos implements Serializable {

    String name,sub,location,min_order,img_url;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    String count;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMin_order() {
        return min_order;
    }

    public void setMin_order(String min_order) {
        this.min_order = min_order;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }



    String id;
    String merchant_id;
    String category_master_id;
    String subcategory_name;
    String subcategory_des;
    String is_parent;
    String subcategory_level;
    String subcategory_image;

    public int getHas_subcategory() {
        return has_subcategory;
    }

    public void setHas_subcategory(int has_subcategory) {
        this.has_subcategory = has_subcategory;
    }

    int has_subcategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getCategory_master_id() {
        return category_master_id;
    }

    public void setCategory_master_id(String category_master_id) {
        this.category_master_id = category_master_id;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public String getSubcategory_des() {
        return subcategory_des;
    }

    public void setSubcategory_des(String subcategory_des) {
        this.subcategory_des = subcategory_des;
    }

    public String getIs_parent() {
        return is_parent;
    }

    public void setIs_parent(String is_parent) {
        this.is_parent = is_parent;
    }

    public String getSubcategory_level() {
        return subcategory_level;
    }

    public void setSubcategory_level(String subcategory_level) {
        this.subcategory_level = subcategory_level;
    }

    public String getSubcategory_image() {
        return subcategory_image;
    }

    public void setSubcategory_image(String subcategory_image) {
        this.subcategory_image = subcategory_image;
    }
}
