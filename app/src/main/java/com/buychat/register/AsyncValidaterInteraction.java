package com.buychat.register;

import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.buychat.R;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.extras.Validater;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snyxius Technologies on 8/9/2016.
 */
public class AsyncValidaterInteraction implements IAsyncValidaterInteraction , Callback<JsonObject> {
    OnRegisterFinishListner onRegisterFinishListner;

    @Override
    public void validateCredentialsAsync(final OnRegisterFinishListner onRegisterFinishListner, String phone, String email) {
       this.onRegisterFinishListner = onRegisterFinishListner;
        if(phone.length() < 9){
            onRegisterFinishListner.emptyPhone();
        }
//        else if(email.isEmpty()){
//            onRegisterFinishListner.emptyEmail();
//        }else if(!Validater.isValidEmail(email)){
//            onRegisterFinishListner.invalidEmail();
//        }

        else {
            WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
            Call<JsonObject> call = service.signup(Parse.makeSignUpData(phone,email));
            call.enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if(response.code() == Constants.SUCCESS){
            if(Parse.checkStatus(response.body().toString()).equals(Keys.success)){
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.access_token,Parse.checkMessage(response.body().toString(),Keys.access_token));
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.name,Parse.checkMessage(response.body().toString(),Keys.user_name));
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.city_name,Parse.checkMessage(response.body().toString(),Keys.city));
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.mobile,Parse.checkMessage(response.body().toString(),Keys.mobile));
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.email,Parse.checkMessage(response.body().toString(),Keys.email));
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.user_image,Parse.checkMessage(response.body().toString(),Keys.user_image));
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.notification,Parse.checkMessage(response.body().toString(),Keys.notification));
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.approved_status,Parse.checkMessage(response.body().toString(),Keys.approved_status));

                onRegisterFinishListner.onSuccess(Parse.checkMessage(response.body().toString(),Keys.message));
            }else{
                onRegisterFinishListner.onError(Parse.checkMessage(response.body().toString(),Keys.message));
            }
        }else{
            onRegisterFinishListner.onError();
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        onRegisterFinishListner.internetIssue();
    }
}
