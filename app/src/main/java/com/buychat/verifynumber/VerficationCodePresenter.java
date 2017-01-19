package com.buychat.verifynumber;

import com.buychat.register.AsyncValidaterInteraction;
import com.buychat.register.IRegisterPresenter;
import com.buychat.register.IRegisterView;
import com.buychat.register.OnRegisterFinishListner;

/**
 * Created by Snyxius Technologies on 8/9/2016.
 */
public class VerficationCodePresenter implements IVerificationPresenter,OnVerificationCodeFinishListner {


    IVerificationCodeView view;
    AsyncVerificationValidaterInteraction interaction;


    VerficationCodePresenter(IVerificationCodeView view){
        this.view = view;
        interaction = new AsyncVerificationValidaterInteraction();
    }



    @Override
    public void onError(String message) {
        if(view != null) {
            view.hideProgress();
            view.onError(message);
        }
    }

    @Override
    public void onError() {
        if(view != null) {
            view.hideProgress();
            view.onError();
        }
    }

    @Override
    public void emptyOtp() {
        if(view != null) {
            view.hideProgress();
            view.emptyOtp();
        }
    }

    @Override
    public void onSuccess(String message) {
        if(view != null) {
            view.hideProgress();
            view.onSuccess(message);
        }
    }



    @Override
    public void emptyPhone() {
        if(view != null) {
            view.hideProgress();
            view.emptyPhone();
        }

    }

    @Override
    public void onSuccessSendAgain(String message) {
        if(view != null) {
            view.hideProgress();
            view.onSuccessSendAgain(message);
        }
    }



    @Override
    public void internetIssue() {
        if(view != null) {
            view.hideProgress();
            view.internetIssue();
        }

    }

    @Override
    public void attemptVerifyOtp(String otp) {

        if(view != null) {
            view.showProgress();
            interaction.validateCredentialsAsync(this,otp);
        }


    }

    @Override
    public void attemptSendAgainOtp(String phone) {
        if(view != null) {
            view.showProgress();
            interaction.validateSendAgainAsync(this,phone);
        }

    }
}
