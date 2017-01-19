package com.buychat.register;

/**
 * Created by Snyxius Technologies on 8/9/2016.
 */
public interface IAsyncValidaterInteraction {
    void validateCredentialsAsync(OnRegisterFinishListner onRegisterFinishListner, String phone, String email);
}
