package com.buychat.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.buychat.BuildConfig;
import com.buychat.activities.ChatMainActivity;
import com.buychat.activities.HomeActivity;
import com.buychat.R;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.greatjob.GreateJob;
import com.buychat.register.RegisterView;
import com.buychat.singleton.DataSingleton;
import com.buychat.verifynumber.VerifcationCodeView;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snyxius Technologies on 8/8/2016.
 */
public class SplashView extends Fragment implements Callback<JsonObject> {

    private Unbinder unbinder;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    static  int position;

    public static SplashView newInstance(int flag) {
        Bundle args = new Bundle();
        position = flag;
        SplashView fragment = new SplashView();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            checkPermission();

        } else {
           callApi();

        }

    }


    public void callApi(){
        String versionName = BuildConfig.VERSION_NAME;
        TelephonyManager mngr = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        BuyChat.saveToPreferences(getActivity(), Keys.version, versionName);
        BuyChat.saveToPreferences(getActivity(), Keys.deviceId,imei);
        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.get_city(Parse.getAccessToken());
        call.enqueue(this);
    }
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;


    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.READ_PHONE_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.i("TAG",
                        "Displaying storage permission rationale to provide additional context.");

                Snackbar.make(getView(), "Read Phone info is needed to fetch the app info.",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                            }
                        })
                        .show();
            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);


                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if (response.code() == Constants.SUCCESS) {
            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                DataSingleton.getInstance().setCityData(response.body().toString());



                WebRequests service1 = BuyChat.initializeRetrofit().create(WebRequests.class);
                Call<JsonObject> call1 = service1.getBrands(Parse.getCityId());
                call1.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.code() == Constants.SUCCESS) {
                            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                                DataSingleton.getInstance().setArrayBrandsList(Parse.parseBrands(response.body().toString()));
                                WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
                                Call<JsonObject> call1 = service.get_verify_deviceId(Parse.getDeviceTokenImeiVersion());
                                call1.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        if (response.code() == Constants.SUCCESS) {
                                            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {

                                                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.access_token,Parse.checkMessage(response.body().toString(),Keys.access_token));
                                                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.name,Parse.checkMessage(response.body().toString(),Keys.user_name));
                                                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.city_name,Parse.checkMessage(response.body().toString(),Keys.city));
                                                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.mobile,Parse.checkMessage(response.body().toString(),Keys.mobile));
                                                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.buychat_id,Parse.checkMessage(response.body().toString(),Keys.mobile));
                                                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.email,Parse.checkMessage(response.body().toString(),Keys.email));
                                                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.user_image,Parse.checkMessage(response.body().toString(),Keys.user_image));
                                                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.notification,Parse.checkMessage(response.body().toString(),Keys.notification));
                                                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.approved_status,Parse.checkMessage(response.body().toString(),Keys.approved_status));
                                                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.user_status,Parse.checkMessage(response.body().toString(),Keys.user_status));



                                                settingView();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        BuyChat.showAToast(Constants.Internet);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        BuyChat.showAToast(Constants.Internet);
                    }
                });















            }
        }
    }

    public void settingView() {

        if(BuyChat.readFromPreferences(getActivity(), Keys.user_status, Constants.DEFAULT_STRING).equals(""+Constants.INT_ONE)) {
            if (position == 0) {

                if (Build.VERSION.SDK_INT >= 21) {
                    Transition transitionSlideRight =
                            TransitionInflater.from(getActivity()).inflateTransition(R.transition.slide_right);
                }
                progressBar.setVisibility(View.GONE);


                if (
                        !BuyChat.readFromPreferences(getActivity(), Keys.mobile, Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING) &&
                        BuyChat.readFromPreferences(getActivity(), Keys.approved_status, Constants.DEFAULT_STRING).equals("" + Constants.INT_ONE) &&
                        !BuyChat.readFromPreferences(getActivity(), Keys.buychat_id, Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING) &&
                        !BuyChat.readFromPreferences(getActivity(), Keys.city_name, Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING) &&
                        !BuyChat.readFromPreferences(getActivity(), Keys.intro_time, Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING)) {
                    String city_id = "";
                    for (int i = 0; i < Parse.parseCity(DataSingleton.getInstance().getCityData()).size(); i++) {
                        if (Parse.parseCity(DataSingleton.getInstance().getCityData()).get(i).getCity_name().equals(BuyChat.readFromPreferences(getActivity(), Keys.city_name, Constants.DEFAULT_STRING))) {
                            city_id = Parse.parseCity(DataSingleton.getInstance().getCityData()).get(i).getId();
                        }
                    }
                    BuyChat.saveToPreferences(BuyChat.getAppContext(), Keys.city_id, city_id);
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    getActivity().finish();


                } else if (BuyChat.readFromPreferences(getActivity(), Keys.approved_status, Constants.DEFAULT_STRING).equals("" + Constants.INT_ONE) &&
                        BuyChat.readFromPreferences(getActivity(), Keys.city_name, Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING) &&
                        !BuyChat.readFromPreferences(getActivity(), Keys.intro_time, Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING)) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new GreateJob(), Constants.SelectCityView)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                            .commitAllowingStateLoss();


                } else if (
                        !BuyChat.readFromPreferences(getActivity(), Keys.mobile, Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING) &&
                        BuyChat.readFromPreferences(getActivity(), Keys.approved_status, Constants.DEFAULT_STRING).equals("" + Constants.DEFAULT_INT) &&
                        !BuyChat.readFromPreferences(getActivity(), Keys.intro_time, Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING)) {

                    Fragment fragment = new VerifcationCodeView();
                    Bundle args = new Bundle();
                    args.putString(Keys.data, "(" + BuyChat.readFromPreferences(getActivity(), Keys.code, Constants.DEFAULT_STRING) + ") " + BuyChat.readFromPreferences(getActivity(), Keys.mobile, Constants.DEFAULT_STRING));
                    fragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, fragment, Constants.VerifcationCodeView)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                            .commitAllowingStateLoss();

                } else if (
                        BuyChat.readFromPreferences(getActivity(), Keys.mobile, Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING) &&
                        !BuyChat.readFromPreferences(getActivity(), Keys.intro_time, Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING)) {

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new RegisterView(), Constants.Register)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                            .commitAllowingStateLoss();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new IntroView(), Constants.IntroView)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                            .commitAllowingStateLoss();
                }
            } else if (position == 1) {
                String city_id = "";
                for (int i = 0; i < Parse.parseCity(DataSingleton.getInstance().getCityData()).size(); i++) {
                    if (Parse.parseCity(DataSingleton.getInstance().getCityData()).get(i).getCity_name().equals(BuyChat.readFromPreferences(getActivity(), Keys.city_name, Constants.DEFAULT_STRING))) {
                        city_id = Parse.parseCity(DataSingleton.getInstance().getCityData()).get(i).getId();
                    }
                }
                BuyChat.saveToPreferences(BuyChat.getAppContext(), Keys.city_id, city_id);
                startActivity(new Intent(getActivity(), ChatMainActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                getActivity().finish();
            }
        }else if(BuyChat.readFromPreferences(getActivity(), Keys.user_status, Constants.DEFAULT_STRING).equals(""+Constants.DEFAULT_INT     )){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RegisterView(), Constants.Register)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_left)
                    .commitAllowingStateLoss();
        }
}


    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        BuyChat.showAToast(Constants.Internet);
    }

}

