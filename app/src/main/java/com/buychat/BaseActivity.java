package com.buychat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.singleton.DataSingleton;
import com.buychat.singleton.SocketSingleton;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hos_Logicbox on 09/08/16.
 */
public class BaseActivity extends AppCompatActivity implements Callback<JsonObject>{


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private Emitter.Listener onMessageEvent = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject data = (JSONObject) args[0];
                Log.d(Keys.socket_id, data.getString(Keys.id));
                BuyChat.saveToPreferences(getApplicationContext(), Keys.socket_id, data.getString(Keys.id));
                WebRequests service1 = BuyChat.initializeRetrofit().create(WebRequests.class);
                Call<JsonObject> call1 = service1.update_socket(Parse.updateSocket());
                call1.enqueue(BaseActivity.this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocketSingleton.get(getApplicationContext()).getSocket().on(Keys.socketId,onMessageEvent);
        SocketSingleton.get(getApplicationContext()).getSocket().connect();
    }

    @SuppressWarnings("unchecked") void transitionTo(Intent i) {
        if (Build.VERSION.SDK_INT >= 16 ) {
            final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
            startActivity(i, transitionActivityOptions.toBundle());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SocketSingleton.get(getApplicationContext()).getSocket().off(Keys.socketId,onMessageEvent);
//        SocketSingleton.get(getApplicationContext()).getSocket().disconnect();
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {

    }
}
