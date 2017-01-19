package com.buychat.verifynumber;

/**
 * Created by Snyxius Technologies on 8/9/2016.
 */
public interface IVerificationPresenter {
    void attemptVerifyOtp(String otp);
    void attemptSendAgainOtp(String phone);
}
