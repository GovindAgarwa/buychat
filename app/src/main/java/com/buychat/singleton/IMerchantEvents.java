package com.buychat.singleton;

/**
 * Created by Snyxius Technologies on 8/30/2016.
 */
public interface IMerchantEvents {

    void onSocketConnected();

    /**
     * onSocketRegistered event
     * @param object object that actually returns from socket event
     */
    void onSocketRegistered(Object object);


    void onSocketDisconnected();


    void onGetMessageEvent(Object[] object);
}
