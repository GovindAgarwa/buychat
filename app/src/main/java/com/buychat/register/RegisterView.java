package com.buychat.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.activities.HomeActivity;
import com.buychat.api.Parse;
import com.buychat.fragments.TandCView;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.fonts.TextViewBold;
import com.buychat.verifynumber.VerifcationCodeView;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.utils.AlertDialogManager;
import com.hbb20.CountryCodePicker;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Snyxius Technologies on 8/8/2016.
 */
public class RegisterView extends Fragment implements IRegisterView {

    private Unbinder unbinder;
    @BindViews({R.id.phone_number,R.id.email})
    List<EditText> editTextArrayList;
    RegisterPresenter presenter;
    private ProgressDialog dialog;
    @BindView(R.id.register_fragment)
    RelativeLayout verifyFrag;

    @BindView(R.id.countrycode)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.privacyText)
    TextView privactText;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new RegisterPresenter(this);


//        TextView myTextView = new TextView(this);
//        SpannableString ss = new SpannableString(getResources().getString(R.string.usage));
        String myString = getResources().getString(R.string.usage);
        int i1 = myString.indexOf("T");
        int i2 = myString.indexOf("s & privacy");

        int i3 = myString.indexOf("privacy");
        int i4 = myString.indexOf("y. ");
        privactText.setMovementMethod(LinkMovementMethod.getInstance());
        privactText.setText(myString, TextView.BufferType.SPANNABLE);
        Spannable mySpannable = (Spannable)privactText.getText();
        ClickableSpan tandcClickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick(View widget) { /* do something */
                TandCView tandcView = TandCView.newInstance(1);
                tandcView.show(getActivity().getSupportFragmentManager(), Constants.privacy_fragment);
            }
        };
        mySpannable.setSpan(tandcClickableSpan, i1, i2 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Spannable privacySpannable = (Spannable)privactText.getText();
        ClickableSpan privacyClickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick(View widget) { /* do something */
                TandCView privacy_fragment = TandCView.newInstance(2);
                privacy_fragment.show(getActivity().getSupportFragmentManager(), Constants.privacy_fragment);
            }
        };
        privacySpannable.setSpan(privacyClickableSpan, i3, i4 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

   @OnClick(R.id.next) void onClick(){
       presenter.attemptNormalSignIn(countryCodePicker.getSelectedCountryCodeWithPlus()+editTextArrayList.get(Constants.DEFAULT_INT).getText().toString(),
                                     editTextArrayList.get(Constants.INT_ONE).getText().toString());
   }

    @OnClick(R.id.create) void onClickCreate(){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL+Constants.MERCHANT_SIGN_UP));
        startActivity(intent);
    }


    @Override
    public void onError(String message) {
        BuyChat.showAToast(message);
    }

    @Override
    public void onError() {
        BuyChat.showAToast(Constants.Some_Went_Wrong);
    }


    @Override
    public void onSuccess(String message) {
        BuyChat.showAToast(message);
        addNextFragment("",verifyFrag,true);
    }

    @Override
    public void emptyEmail() {
        BuyChat.showAToast(Constants.Validate_email);
    }

    @Override
    public void emptyPhone() {
        BuyChat.showAToast(Constants.Validate_Mobile);
    }

    @Override
    public void invalidEmail() {
        BuyChat.showAToast(Constants.Validate_invalid_email);
    }

    @Override
    public void showProgress() {
        dialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        AlertDialogManager.showDialog(dialog);
    }

    @Override
    public void hideProgress() {
        AlertDialogManager.dismissDialog(dialog);
    }

    @Override
    public void internetIssue() {
        BuyChat.showAToast(Constants.Internet);
    }


    private void addNextFragment(String sample, RelativeLayout squareBlue, boolean overlap) {
        BuyChat.saveToPreferences(getActivity(),Keys.code,countryCodePicker.getSelectedCountryCodeWithPlus());
//        if(!BuyChat.readFromPreferences(getActivity(),Keys.city_name,Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING)){
//            BuyChat.saveToPreferences(getActivity(),Keys.buychat_id,BuyChat.readFromPreferences(getActivity(),Keys.mobile,Constants.DEFAULT_STRING));
//            BuyChat.saveToPreferences(getActivity(),Keys.mobile,editTextArrayList.get(Constants.DEFAULT_INT).getText().toString());
//                String city_id = "";
//            for(int i=0;i< Parse.parseCity(DataSingleton.getInstance().getCityData()).size();i++){
//                if( Parse.parseCity(DataSingleton.getInstance().getCityData()).get(i).getCity_name().equals(BuyChat.readFromPreferences(getActivity(),Keys.city_name,Constants.DEFAULT_STRING))){
//                    city_id =  Parse.parseCity(DataSingleton.getInstance().getCityData()).get(i).getId();
//                }
//            }
//            BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.city_id,city_id);
//
//            startActivity(new Intent(getActivity(), HomeActivity.class));
//            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            getActivity().finish();
//
//        }else{
            BuyChat.saveToPreferences(getActivity(),Keys.mobile,editTextArrayList.get(Constants.DEFAULT_INT).getText().toString());
        BuyChat.saveToPreferences(getActivity(),Keys.code,countryCodePicker.getSelectedCountryCodeWithPlus());
            Fragment fragment = new VerifcationCodeView();
            Bundle args = new Bundle();
            args.putString(Keys.data, "("+countryCodePicker.getSelectedCountryCodeWithPlus()+") "+editTextArrayList.get(Constants.DEFAULT_INT).getText().toString());
            fragment.setArguments(args);

            if (Build.VERSION.SDK_INT >= 21 ) {
                Slide slideTransition = new Slide(Gravity.RIGHT);
                slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
                ChangeBounds changeBoundsTransition = new ChangeBounds();
                changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
                fragment.setEnterTransition(slideTransition);
                fragment.setAllowEnterTransitionOverlap(overlap);
                fragment.setAllowReturnTransitionOverlap(overlap);
                fragment.setSharedElementEnterTransition(slideTransition);
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, Constants.VerifcationCodeView)
//                .addToBackStack(Constants.Register)
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .addSharedElement(squareBlue, getString(R.string.square_blue_name))
                    .commit();


    }



}
