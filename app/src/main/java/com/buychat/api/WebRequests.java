package com.buychat.api;

import com.buychat.extras.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Snyxius Technologies on 6/3/2016.
 */
public interface WebRequests {



    @POST(Constants.API+Constants.SIGN_IN)
    Call<JsonObject> signin(@Body JsonObject task);
//    {
//        "email":"aman.jham@snyxius.com",
//        "mobile":"asdasd"
//
//    }


    @POST(Constants.API+Constants.SIGN_UP)
    Call<JsonObject> signup(@Body JsonObject task);
//    {
//        "email":"aman.jham@snyxius.com",
//        "mobile":"asdasd"
//
//    }

    @POST(Constants.API+Constants.VERIFYOTP)
    Call<JsonObject> verifyOtp(@Body JsonObject task);

//   {
//    "user_mobile_ver_code":"1234",
//            "access_token": "57b6a4e15f9fc"
//  }

    @POST(Constants.API+Constants.GET_CITY)
    Call<JsonObject> get_city(@Body JsonObject task);
//
//    {
//        "access_token":"57b569f9bb79f",
//
//    }

    @POST(Constants.API+Constants.GET_VERIFYDEVICEID)
    Call<JsonObject> get_verify_deviceId(@Body JsonObject task);
//
//    {
//        "access_token":"57b569f9bb79f",
//
//    }

    @POST(Constants.API+Constants.SAVE_CITY)
    Call<JsonObject> save_city(@Body JsonObject task);
//
//    {
//        "access_token":"57b569f9bb79f",
//        "user_city":"Bangalore"
//    }

    @POST(Constants.API+Constants.GET_BRANDS)
    Call<JsonObject> getBrands(@Body JsonObject task);
//    {
//        "city_id":"57b6a73f830f09cd1d591590"
//    }



    @POST(Constants.API+Constants.GET_CITYMARKET)
    Call<JsonObject> getCityMarket(@Body JsonObject task);

//    1.Request( for all citymarketlist):
//    {
//        "city_id":"57a1c61edf3e78293ea41087"
//    }

//    2.Request(citymarket list of perticular category):
//    {
//        "city_id":"57b6a735830f09cd1d59158f",
//            "category_id":"57b5ab6166724dec7409fff0"
//    }


    @POST(Constants.API+Constants.GET_SHOPLIST)
    Call<JsonObject> getShopList(@Body JsonObject task);

//    {
//        "category_id":"57b5aad666724dec7409ffee",
//            "city_id":"57b6a73f830f09cd1d591590"
//    }


    @POST(Constants.API+Constants.GET_SHOPLIST_SEARCH)
    Call<JsonObject> getShopListBySearch(@Body JsonObject task);

//    {
//        "category_id":"57b5aad666724dec7409ffee",
//            "city_id":"57b6a73f830f09cd1d591590"
//    }

    @POST(Constants.API+Constants.GET_SHOPDETAILS)
    Call<JsonObject> getShopDetails(@Body JsonObject task);

//    {
//        "merchant_id":"57b6f746fbb597ba108b4568"
//    }
//
//
    @POST(Constants.API+Constants.GET_SUBCATEGORYLIST)
    Call<JsonObject> getSubCategories(@Body JsonObject task);
//
//    {
//        "merchant_id":"57b6acdcfbb597ea6f8b4567"
//    }

    @POST(Constants.API+Constants.GET_PRODUCTLIST)
    Call<JsonObject> getProductList(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }

    @POST(Constants.API+Constants.UPDATE_SOCKETID)
    Call<JsonObject> update_socket(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }

    @POST(Constants.API+Constants.CHAT_LIST)
    Call<JsonObject> chat_list(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }

    @POST(Constants.API+Constants.CHAT_VIEW)
    Call<JsonObject> chat_view(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }


    @POST(Constants.API+Constants.DELETE_ADDRESS)
    Call<JsonObject> deleteAddress(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }

    @POST(Constants.API+Constants.ADD_ADDRESS)
    Call<JsonObject> addAddress(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }



    @POST(Constants.API+Constants.UPDATE_ADDRESS)
    Call<JsonObject> updateAddress(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }

    @POST(Constants.API+Constants.GET_ADDRESS)
    Call<JsonObject> getAddress(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }


    @POST(Constants.API+Constants.GET_OFFERS)
    Call<JsonObject> getOffers(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }


    @POST(Constants.API+Constants.GET_ORDER_HISTROY)
    Call<JsonObject> getOrdersList(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }


    @POST(Constants.API+Constants.CREATE_ORDERS)
    Call<JsonObject> createOrders(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }

    @POST(Constants.API+Constants.UPDATE_PROFILE)
    Call<JsonObject> updateProfile(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }


    @POST(Constants.API+Constants.UPDATE_TRANSACTIONID)
    Call<JsonObject> updateTransactionId(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }

    @POST(Constants.API+Constants.CANCEL_ORDER)
    Call<JsonObject> cancelOrder(@Body JsonObject task);
//    {
//    "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }


    @POST(Constants.API+Constants.GET_ORDER_HISTROY_BY_MERCHANT)
    Call<JsonObject> getOrdersMerchantList(@Body JsonObject task);
//    {
//            "merchant_id":"57b6acdcfbb597ea6f8b4567",
//            "subcategory_id":"57b6be44fbb597de088b4568"
//    }


}
