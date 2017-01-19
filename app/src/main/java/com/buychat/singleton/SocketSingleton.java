package com.buychat.singleton;

import android.content.Context;
import android.util.Log;

import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Snyxius Technologies on 8/30/2016.
 */
public class SocketSingleton {

    private static SocketSingleton instance;

    private Socket mSocket;
    private Context context;

    public SocketSingleton(Context context) {
        this.context = context;
        this.mSocket = getServerSocket();
    }

    public static SocketSingleton get(Context context){
        if(instance == null){
            instance = getSync(context);
        }
        instance.context = context;
        return instance;
    }

    private static synchronized SocketSingleton getSync(Context context) {
        if(instance == null){
            instance = new SocketSingleton(context);
        }
        return instance;
    }

    public Socket getSocket(){
        return this.mSocket;
    }

    public Socket getServerSocket() {
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = true;
            mSocket = IO.socket(Constants.SERVER_ADDRESS, opts);
            return mSocket;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


}