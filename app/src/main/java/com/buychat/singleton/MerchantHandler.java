package com.buychat.singleton;

import com.buychat.app.BuyChat;
import com.buychat.extras.Keys;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonObject;

import java.net.URISyntaxException;

/**
 * Created by Snyxius Technologies on 8/30/2016.
 */
public class MerchantHandler {

    IMerchantEvents iSocketEvents;
    private Socket mSocket = SocketSingleton.get(BuyChat.getAppContext()).getSocket();
    public MerchantHandler(IMerchantEvents iSocketEvents){
        this.iSocketEvents = iSocketEvents;
    }

    /**
     * Connecting to socket, and start listening on the socket events
     */
    public void connectToSocket () {
        //Connect to socket and start listen on

        mSocket.on(Socket.EVENT_CONNECT, onConnectEvent);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnectEvent);
        mSocket.on(Keys.user, onMessageEvent);
        mSocket.connect();
    }

    /**
     * Disconnecting from the socket, and removing already listening events
     */
    public void disconnectFromSocket () {
        //Disconnecting everything
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnectEvent);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnectEvent);
        mSocket.off(Keys.user, onMessageEvent);
    }

    /**
     * Listen on connection error, and notify who's implementing the interface
     */


    /**
     * Listen on connected event, and notify who's implementing the interface
     * */
    private Emitter.Listener onConnectEvent = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            iSocketEvents.onSocketConnected();
        }
    };

    /**
     * Listen on message event type msg from the socket, and notify who's implementing the interface
     */
    private Emitter.Listener onMessageEvent = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            iSocketEvents.onGetMessageEvent(args);
        }
    };

    /**
     * Listen on disconnect event, and notify who's implementing the interface
     * */
    private Emitter.Listener onDisconnectEvent = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            iSocketEvents.onSocketDisconnected();
        }
    };

    /**
     * Registering to the socket via this method, each time we connect to socket
     * @param token Used in session
     */
    public void emitToSocket(JsonObject token) {
        System.out.println(Keys.socket_id +" "+token);
        mSocket.emit(Keys.chatMessageToMerchant, token, new Ack() {
            @Override
            public void call(Object... args) {
                //args is a JSON Object
                iSocketEvents.onSocketRegistered(args);
            }
        });
    }


}
