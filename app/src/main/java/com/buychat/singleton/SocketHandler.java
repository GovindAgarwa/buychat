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
public class SocketHandler {


    ISocketEvents iSocketEvents;
    private Socket mSocket = SocketSingleton.get(BuyChat.getAppContext()).getSocket();


    /**
     * Constructor with Interface init
     * */
    public SocketHandler(ISocketEvents iSocketEvents){
        this.iSocketEvents = iSocketEvents;
    }

    /**
     * Connecting to socket, and start listening on the socket events
     */
    public void connectToSocket () {

        //Connect to socket and start listen on
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeOut);
        mSocket.on(Socket.EVENT_CONNECT, onConnectEvent);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnectEvent);
        mSocket.on(Keys.socketId, onMessageEvent);
        mSocket.connect();
    }

    /**
     * Disconnecting from the socket, and removing already listening events
     */
    public void disconnectFromSocket () {
        //Disconnecting everything
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeOut);
        mSocket.off(Socket.EVENT_CONNECT, onConnectEvent);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnectEvent);
        mSocket.off(Keys.socketId, onMessageEvent);
    }

    /**
     * Listen on connection error, and notify who's implementing the interface
     */
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            iSocketEvents.onSocketConnectionError();
        }
    };

    /**
     * Listen on connection error, and notify who's implementing the interface
     */
    private Emitter.Listener onConnectionTimeOut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            iSocketEvents.onSocketConnectionTimeOut();
        }
    };

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
            iSocketEvents.onMessageEvent(args);
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



}
