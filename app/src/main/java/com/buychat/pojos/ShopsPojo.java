package com.buychat.pojos;

import java.util.ArrayList;

/**
 * Created by Hos_Logicbox on 20/08/16.
 */
public class ShopsPojo {

    String merchant_id;

    public int getProduct_count() {
        return product_count;
    }

    public void setProduct_count(int product_count) {
        this.product_count = product_count;
    }

    int product_count;

    ArrayList<String> order_type;

    public ArrayList<String> getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(ArrayList<String> payment_type) {
        this.payment_type = payment_type;
    }

    public ArrayList<String> getOrder_type() {
        return order_type;
    }

    public void setOrder_type(ArrayList<String> order_type) {
        this.order_type = order_type;
    }

    ArrayList<String> payment_type;

    public String getPackage_value() {
        return package_value;
    }

    public void setPackage_value(String package_value) {
        this.package_value = package_value;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    String package_value;
    String delivery_charge;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    int count;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;
    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    private long messageTime;
    public String getMerchant_image() {
        return merchant_image;
    }

    public void setMerchant_image(String merchant_image) {
        this.merchant_image = merchant_image;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    String merchant_image;
    String id;
    String approved_status;
    String business_address;
  //  String business_description;
    String business_hours;
    String business_city;
    String business_image;

    public String getBusiness_description() {
        return business_description;
    }

    public void setBusiness_description(String business_description) {
        this.business_description = business_description;
    }

    String business_description;
    String business_logo;
    String business_name;
    String category_master_id;
    String merchant_password;
    String merchant_brand;
    String merchant_email;
    String delivery_time;
    String min_order_value;
    String merchant_name;
    String mobile;
    String rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApproved_status() {
        return approved_status;
    }

    public void setApproved_status(String approved_status) {
        this.approved_status = approved_status;
    }

    public String getBusiness_address() {
        return business_address;
    }

    public void setBusiness_address(String business_address) {
        this.business_address = business_address;
    }

//    public String getBusiness_description() {
//        return business_description;
//    }
//
//    public void setBusiness_description(String business_description) {
//        this.business_description = business_description;
//    }

    public String getBusiness_hours() {
        return business_hours;
    }

    public void setBusiness_hours(String business_hours) {
        this.business_hours = business_hours;
    }

    public String getBusiness_city() {
        return business_city;
    }

    public void setBusiness_city(String business_city) {
        this.business_city = business_city;
    }

    public String getBusiness_image() {
        return business_image;
    }

    public void setBusiness_image(String business_image) {
        this.business_image = business_image;
    }

    public String getBusiness_logo() {
        return business_logo;
    }

    public void setBusiness_logo(String business_logo) {
        this.business_logo = business_logo;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getCategory_master_id() {
        return category_master_id;
    }

    public void setCategory_master_id(String category_master_id) {
        this.category_master_id = category_master_id;
    }

    public String getMerchant_password() {
        return merchant_password;
    }

    public void setMerchant_password(String merchant_password) {
        this.merchant_password = merchant_password;
    }

    public String getMerchant_brand() {
        return merchant_brand;
    }

    public void setMerchant_brand(String merchant_brand) {
        this.merchant_brand = merchant_brand;
    }

    public String getMerchant_email() {
        return merchant_email;
    }

    public void setMerchant_email(String merchant_email) {
        this.merchant_email = merchant_email;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getMin_order_value() {
        return min_order_value;
    }

    public void setMin_order_value(String min_order_value) {
        this.min_order_value = min_order_value;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
