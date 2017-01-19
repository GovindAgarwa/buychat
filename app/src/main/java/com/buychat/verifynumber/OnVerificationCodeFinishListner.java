package com.buychat.verifynumber;

/**
 * Created by Snyxius Technologies on 8/9/2016.
 */
public interface OnVerificationCodeFinishListner {
    void onError(String message);
    void onError();
    void onSuccess(String message);
    void emptyOtp();
    void emptyPhone();
    void onSuccessSendAgain(String message);
    void internetIssue();
}
