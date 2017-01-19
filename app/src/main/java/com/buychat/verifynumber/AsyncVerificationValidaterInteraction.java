package com.buychat.verifynumber;

import android.os.Handler;

import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.extras.Validater;
import com.buychat.register.IAsyncValidaterInteraction;
import com.buychat.register.OnRegisterFinishListner;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snyxius Technologies on 8/9/2016.
 */
public class AsyncVerificationValidaterInteraction implements IAsyncVerificationValidaterInteraction, Callback<JsonObject> {
    OnVerificationCodeFinishListner onVerificationCodeFinishListner;
    @Override
    public void validateCredentialsAsync(final OnVerificationCodeFinishListner onVerificationCodeFinishListner, String otp) {
        this.onVerificationCodeFinishListner = onVerificationCodeFinishListner;
        if(otp.length() < Constants.INT_FOUR){
            onVerificationCodeFinishListner.emptyOtp();
        }else{
            WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
            Call<JsonObject> call = service.verifyOtp(Parse.getAccessTokenAndCode(otp));
            call.enqueue(this);
        }
    }

    @Override
    public void validateSendAgainAsync(final OnVerificationCodeFinishListner onVerificationCodeFinishListner, String phone) {
        if(phone.isEmpty()){
            onVerificationCodeFinishListner.emptyPhone();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onVerificationCodeFinishListner.onSuccessSendAgain(Constants.Success);
                }
            }, Constants.SPLASH_TIME_OUT);
        }
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if(response.code() == Constants.SUCCESS){
            if(Parse.checkStatus(response.body().toString()).equals(Keys.success)){
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.buychat_id,Parse.checkMessage(response.body().toString(),Keys.buychatid));
                onVerificationCodeFinishListner.onSuccess(Parse.checkMessage(response.body().toString(),Keys.message));
            }else{
                onVerificationCodeFinishListner.onError(Parse.checkMessage(response.body().toString(),Keys.message));
            }
        }else{
            onVerificationCodeFinishListner.onError();
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {

    }
}
