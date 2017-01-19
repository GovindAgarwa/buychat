package com.buychat.greatjob;

import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hos_Logicbox on 16/07/16.
 */
public class AsyncListInteraction implements IAsyncListInteraction, Callback<JsonObject> {

    OnListFinishedListner onListFinishedListner;


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {

    }

    @Override
    public void fetchCityData(OnListFinishedListner onListFinishedListner, int offset, int limit) {
        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.get_city(Parse.getAccessToken());
        call.enqueue(this);
    }
}
