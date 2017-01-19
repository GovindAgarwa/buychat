package com.buychat.singleton;

import com.buychat.api.Parse;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.Address;
import com.buychat.pojos.BrandsPojo;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.pojos.City;
import com.buychat.pojos.ProductPojos;
import com.buychat.pojos.ShopPojo;
import com.buychat.pojos.ShopsPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hos_Logicbox on 09/08/16.
 */
public class DataSingleton {
    //create an object of SingleObject
    private static DataSingleton instance = new DataSingleton();

    //make the constructor private so that this class cannot be
    //instantiated
    private DataSingleton(){}

    //Get the only object available
    public static DataSingleton getInstance(){
        return instance;
    }


    public  ArrayList<String> arrayCityList;

    public ArrayList<BrandsPojo> getArrayBrandsList() {
        return arrayBrandsList;
    }

    public void setArrayBrandsList(ArrayList<BrandsPojo> arrayBrandsList) {
        this.arrayBrandsList = arrayBrandsList;
    }

    public  ArrayList<BrandsPojo> arrayBrandsList;




    public ArrayList<String> getCityArray() {
        return arrayCityList;
    }
    public void setCityArray(ArrayList<String> count) {
        this.arrayCityList = count;
    }




    public void setCartArray(ArrayList<ProductPojos> count,int position) {
        this.productData = count.get(position);
    }

    public ProductPojos getCartArray() {
        return productData;
    }

    public ProductPojos productData;

    public  String getSubTotalData(ArrayList<ProductPojos> arrayList){
            float subtotal = 0.0f;
        for (int i=0;i <arrayList.size();i++){
              subtotal +=  arrayList.get(i).getQuantity() * Float.valueOf(arrayList.get(i).getProduct_price());
        }

        return String.valueOf(subtotal);
    }


    public  String getTotalData(ArrayList<ProductPojos> arrayList){
        float subtotal = 0.0f;
        for (int i=0;i <arrayList.size();i++){
            subtotal +=  arrayList.get(i).getQuantity() * Float.valueOf(arrayList.get(i).getProduct_price());
            System.out.println(subtotal);
        }
        System.out.println(subtotal);
        return String.valueOf(subtotal);
    }


    public  String getTotalWithPackageChargeData(ArrayList<ProductPojos> arrayList){
        float subtotal = 0.0f;
        for (int i=0;i <arrayList.size();i++){
            subtotal +=  arrayList.get(i).getQuantity() * Float.valueOf(arrayList.get(i).getProduct_price());
        }
        float total = subtotal + Float.valueOf(BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.package_value, Constants.DEFAULT_STRING));
        return String.valueOf(total);
    }

    public  String getTotalWithDeliveryChargeData(ArrayList<ProductPojos> arrayList){
        float subtotal = 0.0f;
        for (int i=0;i <arrayList.size();i++){
            subtotal +=  arrayList.get(i).getQuantity() * Float.valueOf(arrayList.get(i).getProduct_price());
        }
        float total = subtotal + Float.valueOf(BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.delivery_charge, Constants.DEFAULT_STRING));
        return String.valueOf(total);
    }

    public  String getTotalWithPackageChargeWithDeliveryData(ArrayList<ProductPojos> arrayList){
        float subtotal = 0.0f;
        for (int i=0;i <arrayList.size();i++){
            subtotal +=  arrayList.get(i).getQuantity() * Float.valueOf(arrayList.get(i).getProduct_price());
        }
        float total = subtotal + Float.valueOf(BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.package_value,Constants.DEFAULT_STRING))
                + Float.valueOf(BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.delivery_charge,Constants.DEFAULT_STRING));
        return String.valueOf(total);
    }

    public  String getTotalItems(ArrayList<ProductPojos> arrayList){
        int subtotal = 0;
        for (int i=0;i <arrayList.size();i++){
            subtotal +=  arrayList.get(i).getQuantity();
        }

        return String.valueOf(subtotal);
    }


    public Address Addresspojo;

    public void setAddressData(List<Address> count,int position) {
        this.Addresspojo = count.get(position);
    }

    public Address getAddressData() {
        return Addresspojo;
    }

    public  ShopsPojo pojo;

    public void setData(List<ShopsPojo> count,int position) {
        this.pojo = count.get(position);
    }

    public ShopsPojo getData() {
        return pojo;
    }

    public CategoriesPojos pojos;

    public void setCategoriesData(List<CategoriesPojos> count,int position) {
        this.pojos = count.get(position);
    }

    public CategoriesPojos getCategoriesData() {
        return pojos;
    }

    public String Citypojos;

    public void setCityData(String city) {
        this.Citypojos = city;
    }
    public String getCityData() {
        return Citypojos;
    }





}
