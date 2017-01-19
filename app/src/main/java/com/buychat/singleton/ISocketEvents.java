package com.buychat.singleton;

/**
 * Created by Snyxius Technologies on 8/30/2016.
 */
public interface ISocketEvents {
    /**
     * OnSocketConnectionError event
     */
    void onSocketConnectionError();

    /**
     * onSocketConnected event
     */
    void onSocketConnected();

    /**
     * onSocketRegistered event
     * @param object object that actually returns from socket event
     */


    /**
     * onMessageEvent
     * @param object array of objects that actually returns from socket event
     */
    void onMessageEvent(Object[] object);

    /**
     * Message sent to client tellin if there is any response from the WS! if it's sill alive
     * */
    void connectionIsAlive();

    /**
     * Sendin socket disconnection message
     * */
    void onSocketDisconnected();

    /**
     * Sending connection timeout error!
     * */
    void onSocketConnectionTimeOut();


}
