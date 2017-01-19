package com.buychat.api;

import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.extras.Validater;
import com.buychat.pojos.Address;
import com.buychat.pojos.BrandsPojo;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.pojos.Chat;
import com.buychat.pojos.City;
import com.buychat.pojos.Offers;
import com.buychat.pojos.ProductPojos;
import com.buychat.pojos.ShopsPojo;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.chats.Status;
import com.buychat.utils.chats.UserType;
import com.buychat.utils.fonts.EditTextRegular;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Snyxius Technologies on 6/7/2016.
 */
public class Parse {


    public static JsonObject makeSignUpData(String mobile, String email) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.deviceId, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.deviceId, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.email, email);
        jsonObject.addProperty(Keys.mobile, mobile);
        jsonObject.addProperty(Keys.device_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.device_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.socket_id, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.socket_id, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.notification,true);
        return jsonObject;
    }

    public static JsonObject updateSocket() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.socket_id, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.socket_id, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.device_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.device_token, Constants.DEFAULT_STRING));
        return jsonObject;
    }
    public static JsonObject getAccessTokenOrderIdTransactionId(String transaction_id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.order_id, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.order_id, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.transaction_id, transaction_id);

        return jsonObject;
    }

    public static JsonObject getAccessTokenOrderId() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.order_id, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.order_id, Constants.DEFAULT_STRING));
       return jsonObject;
    }

    public static JsonObject updateProfileData(String name, String notification,String image) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.name, name);
        jsonObject.addProperty(Keys.notification, notification);
        jsonObject.addProperty(Keys.user_image, image);
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        return jsonObject;
    }

    public static JsonObject getAccessTokenAndCode(String code) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.user_mobile_ver_code, Integer.valueOf(code));
        return jsonObject;
    }
    public static JsonObject setAddressData(String locality,String flat_no_floor_name,String landmark,String tag_address) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.locality, locality);
        jsonObject.addProperty(Keys.flat_no_floor_name, flat_no_floor_name);
        jsonObject.addProperty(Keys.landmark, landmark);
        jsonObject.addProperty(Keys.tag_address, tag_address);
        return jsonObject;
    }
    public static JsonObject updateAddressData(String locality,String flat_no_floor_name,String landmark,String tag_address) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.address_id, DataSingleton.getInstance().getAddressData().getId());
        jsonObject.addProperty(Keys.locality, locality);
        jsonObject.addProperty(Keys.flat_no_floor_name, flat_no_floor_name);
        jsonObject.addProperty(Keys.landmark, landmark);
        jsonObject.addProperty(Keys.tag_address, tag_address);
        return jsonObject;
    }
    public static JsonObject getAccessToken() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        return jsonObject;
    }
    public static JsonObject getDeviceTokenImeiVersion() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.version, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.version, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.device_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.device_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.deviceId, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.deviceId, Constants.DEFAULT_STRING));
        return jsonObject;
    }
    public static JsonObject getAccessTokenWithOrderDetails(String address_Id,String order_amount,int payment_type,int order_type) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.merchant_id, DataSingleton.getInstance().getData().getId());
        jsonObject.addProperty(Keys.user_shipping_id, address_Id);
        jsonObject.addProperty(Keys.locality,  DataSingleton.getInstance().getAddressData().getLocality());
        jsonObject.addProperty(Keys.flat_no_floor_name, DataSingleton.getInstance().getAddressData().getFlat_no_floor_name());
        jsonObject.addProperty(Keys.landmark, DataSingleton.getInstance().getAddressData().getLandmark());
        jsonObject.addProperty(Keys.tag_address, DataSingleton.getInstance().getAddressData().getTag_address());
        jsonObject.addProperty(Keys.order_quantity,DataSingleton.getInstance().getTotalItems(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId())) );
        jsonObject.addProperty(Keys.order_amount, order_amount);
        jsonObject.addProperty(Keys.order_type,String.valueOf(order_type));
        jsonObject.addProperty(Keys.payment_type,String.valueOf(payment_type));
        JsonArray jsonArray = new JsonArray();
        ArrayList<ProductPojos> arrayList = BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId());
        for(int i=0;i<arrayList.size();i++) {
            ProductPojos pojos = arrayList.get(i);
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty(Keys.product_id,pojos.getId());
            jsonObject1.addProperty(Keys.product_quantity, String.valueOf(pojos.getQuantity()));
            if(pojos.getOfferprice().equals(""+Constants.DEFAULT_INT)){
                jsonObject1.addProperty(Keys.product_price,pojos.getProduct_price());
                jsonObject1.addProperty(Keys.offerprice,pojos.getOfferprice());
            }else {
                jsonObject1.addProperty(Keys.product_price, pojos.getOfferprice());
                jsonObject1.addProperty(Keys.offerprice, pojos.getProduct_price());
            }
            jsonArray.add(jsonObject1);

        }
        jsonObject.add(Keys.product_details,jsonArray);

        return jsonObject;
    }


    public static JsonObject getAddressID(String address) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.address_id,address);
        return jsonObject;
    }

    public static JsonObject getCityId() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.city_id, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.city_id, Constants.DEFAULT_STRING));
        return jsonObject;
    }
    public static JsonObject getCityIdWithLimitOffset(int offset,int limit) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.city_id, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.city_id, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.offset, offset);
        jsonObject.addProperty(Keys.limit, limit);
        return jsonObject;
    }
    public static JsonObject getCityIdWithQueryWithLimitOffset(String query,int offset,int limit) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.city_id, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.city_id, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.keyword,query);
        jsonObject.addProperty(Keys.offset, offset);
        jsonObject.addProperty(Keys.limit, limit);
        return jsonObject;
    }
    public static JsonObject getAccessTokenWithLimitOffset(int offset,int limit) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.offset, offset);
        jsonObject.addProperty(Keys.limit, limit);
        return jsonObject;
    }

    public static JsonObject getAccessTokenWithMerchantIDLimitOffset(String merchant_id,int limit,int offset) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.merchant_id,merchant_id);
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.offset, offset);
        jsonObject.addProperty(Keys.limit, limit);
        return jsonObject;
    }

    public static JsonObject getMerchantId(String id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.merchant_id,id);
        return jsonObject;
    }

    public static JsonObject getMerchantIdAccessTokenAndMessage(String id,String message,String image) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.socket_id,BuyChat.readFromPreferences(BuyChat.getAppContext(),Keys.socket_id,Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.name,BuyChat.readFromPreferences(BuyChat.getAppContext(),Keys.name,Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.merchant_id,id);
        jsonObject.addProperty(Keys.message,message);
        jsonObject.addProperty(Keys.image,image);
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.user_image, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.user_image, Constants.DEFAULT_STRING));

        return jsonObject;
    }

    public static JsonObject getMerchantIdAndSubCategoryIdWithLimitOffset(String id,String sub_id,int limit,int offset) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.merchant_id,id);
        jsonObject.addProperty(Keys.subcategory_id,sub_id);
        jsonObject.addProperty(Keys.limit,limit);
        jsonObject.addProperty(Keys.offset,offset);
        return jsonObject;
    }

    public static JsonObject getCityIdAndCategoryId(String category_id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.city_id, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.city_id, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.category_id, category_id);

        return jsonObject;
    }

    public static JsonObject getCityIdAndCategoryIdWithLimitOffset(String category_id,int limit,int offset) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.city_id, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.city_id, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.category_id, category_id);
        jsonObject.addProperty(Keys.limit,limit);
        jsonObject.addProperty(Keys.offset,offset);

        return jsonObject;
    }
    public static JsonObject getCityIdAndCategoryIdWithLimitOffset(String city_market,String category_id,int limit,int offset) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.city_id, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.city_id, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.category_id, category_id);
        jsonObject.addProperty(Keys.city_market_id, city_market);
        jsonObject.addProperty(Keys.limit,limit);
        jsonObject.addProperty(Keys.offset,offset);

        return jsonObject;
    }
    public static JsonObject getAccessTokenAndCity(String city) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.user_city, city);
        return jsonObject;
    }

    public static JsonObject getAccessTokenAndCityAndImageAndName(String city,String user_image,String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Keys.access_token, BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.access_token, Constants.DEFAULT_STRING));
        jsonObject.addProperty(Keys.user_city, city);
        jsonObject.addProperty(Keys.user_image, user_image);
        jsonObject.addProperty(Keys.name, name);
        return jsonObject;
    }
    public static String checkStatus(String string) {

        try {

            JSONObject jsonObject = new JSONObject(string);
            if (jsonObject.has(Keys.status))
                return jsonObject.getString(Keys.status);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Keys.failed;

    }

    public static String getTextEdittext(EditTextRegular editt){
        return editt.getText().toString().trim();
    }


    public static String checkMessage(String string, String key) {

        try {

            JSONObject jsonObject = new JSONObject(string);
            if (jsonObject.has(key))
                return jsonObject.getString(key);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Constants.DEFAULT_STRING;

    }

    public static JSONArray checkMessageJsonArray(String string, String key) {

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject(string);
            if (jsonObject.has(key)) {
               jsonArray = jsonObject.getJSONArray(key);
                return jsonArray;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;

    }

    public static String parseProfileDetail(String string, String key) {

        try {

            JSONObject jsonObject = new JSONObject(string);
            if (jsonObject.has(key)) {
                JSONArray jsonArray =jsonObject.getJSONArray(key);
                JSONObject jsonObject1 = jsonArray.getJSONObject(Constants.DEFAULT_INT);
                return String.valueOf(jsonObject1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Constants.DEFAULT_STRING;

    }


    public static ArrayList<City> parseCity(String string) {
        ArrayList<City> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.city);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setId(jsonObject1.getString(Keys.id));
                    city.setCity_name(jsonObject1.getString(Keys.city_name));
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }

    public static ArrayList<City> parseCityMarket(String string) {
        ArrayList<City> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.citymarket);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setId(jsonObject1.getString(Keys.id));
                    city.setCity_id(jsonObject1.getString(Keys.city_id));
                    city.setCategory_id(jsonObject1.getString(Keys.category_id));
                    city.setMarket_name(jsonObject1.getString(Keys.market_name));
                    city.setMarket_image(jsonObject1.getString(Keys.market_image));
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }


    public static ArrayList<CategoriesPojos> parseProfileCategories(String string) {
        ArrayList<CategoriesPojos> arrayList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(string);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    CategoriesPojos city = new CategoriesPojos();
                    city.setId(jsonObject1.getString(Keys.id));
                    city.setMerchant_id(jsonObject1.getString(Keys.merchant_id));
                    city.setCategory_master_id(jsonObject1.getString(Keys.category_master_id));
                    city.setSubcategory_name(jsonObject1.getString(Keys.subcategory_name));
                    city.setSubcategory_des(jsonObject1.getString(Keys.subcategory_des));
                    city.setHas_subcategory(jsonObject1.getInt(Keys.has_subcategory));
                    city.setIs_parent(jsonObject1.getString(Keys.is_parent));
                    city.setSubcategory_level(jsonObject1.getString(Keys.subcategory_level));
                    city.setSubcategory_image(jsonObject1.getString(Keys.subcategory_image));
                    city.setCount(jsonObject1.getString(Keys.count  ));
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }


    public static ArrayList<CategoriesPojos> parseCategories(String string) {
        ArrayList<CategoriesPojos> arrayList = new ArrayList<>();

        try {
           JSONObject jsonObject = new JSONObject(string);
//            JSONArray jsonArray = new JSONArray(string);
            JSONArray jsonArray =  jsonObject.getJSONArray(Keys.subcategory);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    CategoriesPojos city = new CategoriesPojos();
                    city.setId(jsonObject1.getString(Keys.id));
                    city.setMerchant_id(jsonObject1.getString(Keys.merchant_id));
                    city.setCategory_master_id(jsonObject1.getString(Keys.category_master_id));
                    city.setSubcategory_name(jsonObject1.getString(Keys.subcategory_name));
                    city.setSubcategory_des(jsonObject1.getString(Keys.subcategory_des));
                    city.setHas_subcategory(jsonObject1.getInt(Keys.has_subcategory));
                    city.setIs_parent(jsonObject1.getString(Keys.is_parent));
                    city.setSubcategory_level(jsonObject1.getString(Keys.subcategory_level));
                    city.setSubcategory_image(jsonObject1.getString(Keys.subcategory_image));
                    city.setCount(jsonObject1.getString(Keys.count  ));
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }


    public static ArrayList<CategoriesPojos> parseSubCategories(String string) {
        ArrayList<CategoriesPojos> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.subcategory);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    CategoriesPojos city = new CategoriesPojos();
                    city.setId(jsonObject1.getString(Keys.id));
                    city.setMerchant_id(jsonObject1.getString(Keys.merchant_id));
                    city.setCategory_master_id(jsonObject1.getString(Keys.category_master_id));
                    city.setSubcategory_name(jsonObject1.getString(Keys.subcategory_name));
                    city.setSubcategory_des(jsonObject1.getString(Keys.subcategory_des));
                    city.setIs_parent(jsonObject1.getString(Keys.is_parent));
                    city.setSubcategory_level(jsonObject1.getString(Keys.subcategory_level));
                    city.setSubcategory_image(jsonObject1.getString(Keys.subcategory_image));
                    city.setCount(jsonObject1.getString(Keys.count));
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }


    public static ArrayList<Offers> parseOffers(String string) {
        ArrayList<Offers> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.offerlist);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Offers city = new Offers();
                    city.setOffer_name(jsonObject1.getString(Keys.offer_name));
                    city.setOffer_description(jsonObject1.getString(Keys.offer_description));
                    city.setOffer_end_date(jsonObject1.getString(Keys.offer_end_date));
                    city.setOffer_status(jsonObject1.getString(Keys.offer_status));
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }
    public static ArrayList<Address> parseAddress(String string) {
        ArrayList<Address> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.address_list);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Address city = new Address();
                    city.setId(jsonObject1.getString(Keys.id));
                    city.setLocality(jsonObject1.getString(Keys.locality));
                    city.setFlat_no_floor_name(jsonObject1.getString(Keys.flat_no_floor_name));
                    city.setLandmark(jsonObject1.getString(Keys.landmark));
                    city.setTag_address(jsonObject1.getString(Keys.tag_address));
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }

    public static ArrayList<Chat> parseChatView(String string) {
        ArrayList<Chat> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.messgae);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Chat city = new Chat();
                   // city.setId(jsonObject1.getString(Keys.id));
                    city.setMessageStatus(Status.SENT);
                    city.setMessageText(jsonObject1.getString(Keys.message));
                    if(jsonObject1.has(Keys.image)) {
                        city.setImage(jsonObject1.getString(Keys.image));
                    }
                    city.setMessageTime(jsonObject1.getLong(Keys.add_date));
                    if(jsonObject1.getInt(Keys.from_user) == Constants.INT_ONE){
                        city.setUserType(UserType.OTHER);
                    }
                    if(jsonObject1.getInt(Keys.from_merchant) == Constants.INT_ONE){
                        city.setUserType(UserType.SELF);
                    }
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }

    public static ArrayList<ShopsPojo> parseChat(String string) {
        ArrayList<ShopsPojo> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.chatlist);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    ShopsPojo city = new ShopsPojo();
                    city.setCategory_master_id(jsonObject1.getString(Keys.category_master_id));
                    city.setId(jsonObject1.getString(Keys.merchant_id));
                 //   city.setMessage(jsonObject1.getString(Keys.message));
                    city.setBusiness_name(jsonObject1.getString(Keys.buisness_name));
                    city.setMerchant_name(jsonObject1.getString(Keys.merchant_name));
                    city.setBusiness_address(jsonObject1.getString(Keys.business_address));
                    city.setMerchant_image(jsonObject1.getString(Keys.merchant_image));
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }


    public static ArrayList<ProductPojos> parseProduct(String string) {
        ArrayList<ProductPojos> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.subcategorylist);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    ProductPojos city = new ProductPojos();
                    city.setId(jsonObject1.getString(Keys.id));
                    city.setMerchant_id(jsonObject1.getString(Keys.merchant_id));
                    city.setProduct_name(jsonObject1.getString(Keys.product_name));
                    city.setQuantity(Constants.INT_ONE);
                    city.setFlag(false);
                    city.setProduct_description(jsonObject1.getString(Keys.product_description));
                    city.setProduct_short_description(jsonObject1.getString(Keys.product_short_description));
                    city.setProduct_sku(jsonObject1.getString(Keys.product_sku));
                    city.setDiscount(jsonObject1.getString(Keys.discount));
                    city.setIs_discount(jsonObject1.getString(Keys.is_discount));
                    if(jsonObject1.getString(Keys.is_discount).equals("0")){
                        city.setProduct_price(jsonObject1.getString(Keys.product_price));
                        city.setOfferprice("0");
                    }else{
                        city.setProduct_price(jsonObject1.getString(Keys.offerprice));
                        city.setOfferprice(jsonObject1.getString(Keys.product_price));
                    }
                    city.setProduct_prep_time(jsonObject1.getString(Keys.product_prep_time));
                    city.setProduct_status(jsonObject1.getString(Keys.product_status));
                    city.setProduct_image(jsonObject1.getString(Keys.product_image));
                    city.setProduct_categories(jsonObject1.getString(Keys.product_categories));
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }

    public static ArrayList<ProductPojos> parseHistory(String string) {
        ArrayList<ProductPojos> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.orderlist);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    ProductPojos city = new ProductPojos();
                    city.setOrder_id(jsonObject1.getString(Keys.order_id));
                    city.setMerchant_id(jsonObject1.getString(Keys.merchant_id));
                    city.setOrder_status(jsonObject1.getString(Keys.order_status));

                    if(jsonObject1.has(Keys.package_value)) {
                        if (jsonObject1.getString(Keys.package_value).equals(Constants.DEFAULT_STRING) ||
                                jsonObject1.getString(Keys.package_value).equals("" + Constants.DEFAULT_INT)) {
                            city.setPackage_value("" + Constants.DEFAULT_INT);
                        } else {
                            city.setPackage_value(jsonObject1.getString(Keys.package_value));
                        }
                    }else{
                        city.setPackage_value("" + Constants.DEFAULT_INT);

                    }
                    if(jsonObject1.has(Keys.delivery_charge)) {
                        if(jsonObject1.getString(Keys.delivery_charge).equals(Constants.DEFAULT_STRING) ||
                                jsonObject1.getString(Keys.delivery_charge).equals(""+Constants.DEFAULT_INT)){
                            city.setDelivery_charge("" + Constants.DEFAULT_INT);
                        }else{
                            city.setDelivery_charge(jsonObject1.getString(Keys.delivery_charge));
                        }
                    }else{
                        city.setDelivery_charge("" + Constants.DEFAULT_INT);
                    }



                    city.setBusiness_name(jsonObject1.getString(Keys.business_name));
                    city.setMerchant_name(jsonObject1.getString(Keys.merchant_name));
                    city.setMerchant_image(jsonObject1.getString(Keys.merchant_image));
                    city.setOrder_type(jsonObject1.getString(Keys.order_type));
                    city.setPayment_type(jsonObject1.getString(Keys.payment_type));
                    city.setQuantity(jsonObject1.getInt(Keys.order_quantity));
                    city.setOrder_amount(jsonObject1.getString(Keys.order_amount));
                    JSONArray jsonArray1 = jsonObject1.getJSONArray(Keys.product_name);
                    String name = "";
                    ArrayList<ProductPojos> arrayList1 = new ArrayList<>();
                    for(int j=0;j<jsonArray1.length();j++){
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                        if((jsonArray1.length()-1) == j) {
                            name += jsonObject2.getString(Keys.product_name) + "";
                        }else{
                            name += jsonObject2.getString(Keys.product_name) + ",";
                        }
                        ProductPojos pojos = new ProductPojos();
                        pojos.setId(jsonObject2.getString(Keys.product_id));
                        pojos.setProduct_name(jsonObject2.getString(Keys.product_name));
                        if(jsonObject2.getString(Keys.offerprice).equals(""+Constants.DEFAULT_INT)) {
                            pojos.setProduct_price(jsonObject2.getString(Keys.product_price));
                        }else{
                            pojos.setProduct_price(jsonObject2.getString(Keys.offerprice));
                        }


                        pojos.setQuantity(jsonObject2.getInt(Keys.producy_quantity));
                        arrayList1.add(pojos);
                    }
                    city.setProduct_name(name);
                    city.setProduct_details(arrayList1);
                    city.setOrder_date(jsonObject1.getString(Keys.order_date));
                    arrayList.add(city);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }

    public static ArrayList<BrandsPojo> parseBrands(String string) {
        ArrayList<BrandsPojo> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.brandlist);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    BrandsPojo brandsPojo = new BrandsPojo();
                    brandsPojo.setId(jsonObject1.getString(Keys.id));
                    brandsPojo.setApproved_status(jsonObject1.getString(Keys.approved_status));
                    brandsPojo.setBusiness_address(jsonObject1.getString(Keys.business_address));
                    brandsPojo.setBusiness_description(jsonObject1.getString(Keys.business_description));
                    brandsPojo.setBusiness_hours(jsonObject1.getString(Keys.business_hours));
                    brandsPojo.setBusiness_image(jsonObject1.getString(Keys.business_image));
                    brandsPojo.setBusiness_logo(jsonObject1.getString(Keys.business_logo));
                    brandsPojo.setBusiness_name(jsonObject1.getString(Keys.business_name));
                    brandsPojo.setCategory_master_id(jsonObject1.getString(Keys.category_master_id));
                    brandsPojo.setFacebook_url(jsonObject1.getString(Keys.facebook_url));
                    brandsPojo.setLinkedin_url(jsonObject1.getString(Keys.linkedin_url));
                    brandsPojo.setMerchant_brand(jsonObject1.getString(Keys.merchant_brand));
                    brandsPojo.setMerchant_email(jsonObject1.getString(Keys.merchant_email));
                    brandsPojo.setMerchant_name(jsonObject1.getString(Keys.merchant_name));
                    brandsPojo.setMobile(jsonObject1.getString(Keys.mobile));
                    brandsPojo.setProduct_count(jsonObject1.getInt(Keys.product_count));
                    brandsPojo.setTwitter_url(jsonObject1.getString(Keys.twitter_url));
                    brandsPojo.setVideo_url(jsonObject1.getString(Keys.video_url));
                    brandsPojo.setWebsite_url(jsonObject1.getString(Keys.website_url));
                    arrayList.add(brandsPojo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return arrayList;

    }





    public static ArrayList<ShopsPojo> parseShopList(String string) {
        ArrayList<ShopsPojo> arrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.shoplist);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    ShopsPojo brandsPojo = new ShopsPojo();
                    brandsPojo.setId(jsonObject1.getString(Keys.id));
                    brandsPojo.setApproved_status(jsonObject1.getString(Keys.approved_status));
                    brandsPojo.setBusiness_address(jsonObject1.getString(Keys.business_address));
                    brandsPojo.setBusiness_city(jsonObject1.getString(Keys.business_city));
                    brandsPojo.setBusiness_hours(jsonObject1.getString(Keys.business_hours));
                    brandsPojo.setBusiness_image(jsonObject1.getString(Keys.business_image));
                    brandsPojo.setBusiness_logo(jsonObject1.getString(Keys.business_logo));
                    brandsPojo.setBusiness_name(jsonObject1.getString(Keys.business_name));

                    brandsPojo.setProduct_count(jsonObject1.getInt(Keys.product_count));


                    JSONArray jsonArray1 = jsonObject1.getJSONArray(Keys.order_type);
                    ArrayList<String> order_type = new ArrayList<>();
                    if(jsonArray1.length()!=0){
                        for(int j=0;j<jsonArray1.length();j++){
                             order_type.add(jsonArray1.getString(j));
                        }
                        brandsPojo.setOrder_type(order_type);
                    }else {
                        brandsPojo.setOrder_type(order_type);
                    }

                    JSONArray jsonArray2 = jsonObject1.getJSONArray(Keys.payment_type);
                    ArrayList<String> payment_type = new ArrayList<>();
                    if(jsonArray2.length()!=0){
                        for(int j=0;j<jsonArray2.length();j++){
                            payment_type.add(jsonArray2.getString(j));
                        }
                        brandsPojo.setPayment_type(payment_type);
                    }else {
                        brandsPojo.setPayment_type(payment_type);
                    }

//                    brandsPojo.setBusiness_description(jsonObject1.getString(Keys.business_description));
                    brandsPojo.setCategory_master_id(jsonObject1.getString(Keys.category_master_id));
                   // brandsPojo.setMerchant_brand(jsonObject1.getString(Keys.merchant_brand));
                    brandsPojo.setMerchant_email(jsonObject1.getString(Keys.merchant_email));
                    brandsPojo.setMerchant_name(jsonObject1.getString(Keys.merchant_name));
                    brandsPojo.setMobile(jsonObject1.getString(Keys.mobile));
              //      brandsPojo.setRating(jsonObject1.getString(Keys.rating));
                    if(jsonObject1.has(Keys.rating)) {
                        if(jsonObject1.getString(Keys.rating).equals(Constants.DEFAULT_STRING)) {
                            brandsPojo.setRating("" + Constants.DEFAULT_INT);
                        }else{
                            brandsPojo.setRating(jsonObject1.getString(Keys.rating));
                        }

                    }else {

                        brandsPojo.setRating("" + Constants.DEFAULT_INT);
                    }
                    brandsPojo.setMerchant_password(jsonObject1.getString(Keys.merchant_password));
                    if(jsonObject1.has(Keys.delivery_time))
                    brandsPojo.setDelivery_time(jsonObject1.getString(Keys.delivery_time));
                    else
                        brandsPojo.setDelivery_time(Constants.DEFAULT_STRING);

                    if(jsonObject1.has(Keys.min_order_value))
                        brandsPojo.setMin_order_value(jsonObject1.getString(Keys.min_order_value));
                    else
                        brandsPojo.setMin_order_value(Constants.DEFAULT_STRING);

                    if(jsonObject1.has(Keys.package_value)) {
                        if (jsonObject1.getString(Keys.package_value).equals(Constants.DEFAULT_STRING) ||
                                jsonObject1.getString(Keys.package_value).equals("" + Constants.DEFAULT_INT)) {
                            brandsPojo.setPackage_value("" + Constants.DEFAULT_INT);
                        } else {
                            brandsPojo.setPackage_value(jsonObject1.getString(Keys.package_value));
                        }
                    }else{
                        brandsPojo.setPackage_value("" + Constants.DEFAULT_INT);

                    }
                    if(jsonObject1.has(Keys.delivery_charge)) {
                    if(jsonObject1.getString(Keys.delivery_charge).equals(Constants.DEFAULT_STRING) ||
                            jsonObject1.getString(Keys.delivery_charge).equals(""+Constants.DEFAULT_INT)){
                        brandsPojo.setDelivery_charge("" + Constants.DEFAULT_INT);
                    }else{
                        brandsPojo.setDelivery_charge(jsonObject1.getString(Keys.delivery_charge));


                    }
                }else{
                        brandsPojo.setDelivery_charge("" + Constants.DEFAULT_INT);
                }


                    arrayList.add(brandsPojo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }


    public static  ArrayList<String> parseCityString(String string){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select City");
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray(Keys.city);
            if(jsonArray.length() != 0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    arrayList.add(jsonObject1.getString(Keys.city_name));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }





}
