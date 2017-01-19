package com.buychat.register;

/**
 * Created by Snyxius Technologies on 8/9/2016.
 */
public class RegisterPresenter implements IRegisterPresenter,OnRegisterFinishListner {


    IRegisterView view;
    AsyncValidaterInteraction interaction;


    RegisterPresenter(IRegisterView view){
        this.view = view;
        interaction = new AsyncValidaterInteraction();
    }


    @Override
    public void attemptNormalSignIn(String phone, String email) {

        if(view != null) {
            view.showProgress();
            interaction.validateCredentialsAsync(this,phone,email);
        }


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
    public void onSuccess(String message) {
        if(view != null) {
            view.hideProgress();
            view.onSuccess(message);
        }
    }

    @Override
    public void emptyEmail() {
        if(view != null) {
            view.hideProgress();
            view.emptyEmail();
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
    public void invalidEmail() {
        if(view != null) {
            view.hideProgress();
            view.invalidEmail();
        }

    }



    @Override
    public void internetIssue() {
        if(view != null) {
            view.hideProgress();
            view.internetIssue();
        }

    }
}
