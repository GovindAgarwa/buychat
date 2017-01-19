package com.buychat.register;

/**
 * Created by Snyxius Technologies on 8/9/2016.
 */
public interface IRegisterView {
    void onError(String message);
    void onError();
    void onSuccess(String message);
    void emptyEmail();
    void emptyPhone();
    void invalidEmail();
    void showProgress();
    void hideProgress();
    void internetIssue();
}
