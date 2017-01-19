package com.buychat.verifynumber;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buychat.fragments.TandCView;
import com.buychat.greatjob.GreateJob;
import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.register.RegisterView;
import com.buychat.utils.AlertDialogManager;
import com.buychat.utils.OtpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Snyxius Technologies on 8/8/2016.
 */
public class VerifcationCodeView extends Fragment implements IVerificationCodeView{

    private Unbinder unbinder;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.otp_view)
    OtpView otpView;
    @BindView(R.id.phone_text)
    TextView phone;
    @BindView(R.id.verify_fragment) RelativeLayout verifyLayout;
    private ProgressDialog dialog;
    VerficationCodePresenter presenter;
    @BindView(R.id.privacyText)
    TextView privactText;

    public static VerifcationCodeView newInstance(String sample) {
        Bundle args = new Bundle();
//        args.putSerializable(EXTRA_SAMPLE, sample);
        VerifcationCodeView fragment = new VerifcationCodeView();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verificaitoncode_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new VerficationCodePresenter(this);
        initialize();
    }


    private void initialize() {


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        phone.setText(Constants.Enter_Code(getArguments().getString(Keys.data)));



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

    @OnClick(R.id.next) void onClickNext(){
        presenter.attemptVerifyOtp(otpView.checkOtp());
    }

    @OnClick(R.id.sendAgain) void sendAgain(){
        BuyChat.saveToPreferences(getActivity(),Keys.email,Constants.DEFAULT_STRING);
        BuyChat.saveToPreferences(getActivity(),Keys.mobile,Constants.DEFAULT_STRING);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new RegisterView(), Constants.Register)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                .commit();
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

        addNextFragment("",verifyLayout,true);
    }

    @Override
    public void onSuccessSendAgain(String message) {
        BuyChat.showAToast(Constants.Successful);
    }

    @Override
    public void emptyOtp() {
        BuyChat.showAToast(Constants.Validate_Otp);
    }



    @Override
    public void emptyPhone() {
        BuyChat.showAToast(Constants.Validate_Mobile);
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
//        VerifcationCodeView sharedElementFragment2 = VerifcationCodeView.newInstance("");
        Fragment fragment = new GreateJob();

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


        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment, Constants.SelectCityView)
                //.addToBackStack(Constants.VerifcationCodeView)
                .addSharedElement(squareBlue, getString(R.string.square_blue_name))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                .commit();
    }
}
