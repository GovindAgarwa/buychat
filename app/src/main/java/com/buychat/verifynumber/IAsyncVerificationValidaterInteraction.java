package com.buychat.verifynumber;

import com.buychat.register.OnRegisterFinishListner;

/**
 * Created by Snyxius Technologies on 8/9/2016.
 */
public interface IAsyncVerificationValidaterInteraction {
    void validateCredentialsAsync(OnVerificationCodeFinishListner onVerificationCodeFinishListner, String otp);
    void validateSendAgainAsync(OnVerificationCodeFinishListner onVerificationCodeFinishListner, String phone);

}
