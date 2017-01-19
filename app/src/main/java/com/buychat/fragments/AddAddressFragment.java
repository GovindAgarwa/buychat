package com.buychat.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.buychat.R;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.AddressReload;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.Address;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.AlertDialogManager;
import com.buychat.utils.fonts.EditTextRegular;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nihas-mac on 16/08/2016.
 */
public class AddAddressFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, Callback<JsonObject> {
    private ProgressDialog dialog;
    private Unbinder unbinder;
    private AddressReload mListener;
    private String REQUIRED="required";
    String locationName="HOME";
    boolean isOther=false;
    @BindView(R.id.spinner_location)
    Spinner citySpinner;
    @BindView(R.id.home_radio)RadioButton homeRadio;
    @BindView(R.id.work_radio)RadioButton workRadio;
    @BindView(R.id.other_radio)RadioButton otherRadio;
    @BindView(R.id.radioTopGroup)RadioGroup radioGroup;

    @BindView(R.id.address_other)EditTextRegular addressOther;
    @BindView(R.id.address_no_building_name)EditTextRegular addressBuildingName;
    @BindView(R.id.address_landmark)EditTextRegular addressLandmark;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (AddressReload) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_address_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initializeRecyclerView();
        addressOther.setVisibility(View.GONE);
        radioGroup.setOnCheckedChangeListener(this);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,  Parse.parseCityString(DataSingleton.getInstance().getCityData()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.home_radio) {
            isOther=false;
            locationName="HOME";
            homeRadio.setTextColor(getResources().getColor(R.color.colorPrimary));
            workRadio.setTextColor(getResources().getColor(R.color.black));
            otherRadio.setTextColor(getResources().getColor(R.color.black));
            addressOther.setVisibility(View.GONE);
        }else if (checkedId == R.id.work_radio) {
            isOther=false;
            locationName="WORK";
            workRadio.setTextColor(getResources().getColor(R.color.colorPrimary));
            homeRadio.setTextColor(getResources().getColor(R.color.black));
            otherRadio.setTextColor(getResources().getColor(R.color.black));
            addressOther.setVisibility(View.GONE);
        }else if (checkedId == R.id.other_radio) {
            otherRadio.setTextColor(getResources().getColor(R.color.colorPrimary));
            homeRadio.setTextColor(getResources().getColor(R.color.black));
            workRadio.setTextColor(getResources().getColor(R.color.black));
            isOther=true;
            addressOther.setVisibility(View.VISIBLE);
        }
    }



    @OnClick(R.id.add_address)
    public void addAddress(){

        if(citySpinner.getSelectedItem().toString().equals("Select City")){
            BuyChat.showAToast(Constants.SelectCity);
            return;
        }

        if(TextUtils.isEmpty(Parse.getTextEdittext(addressBuildingName))){
            addressBuildingName.setError(REQUIRED);
            return;
        }

        if(TextUtils.isEmpty(Parse.getTextEdittext(addressLandmark))){
            addressLandmark.setError(REQUIRED);
            return;
        }

        if(isOther) {
            if (TextUtils.isEmpty(Parse.getTextEdittext(addressOther))) {
                addressOther.setError(REQUIRED);
                return;
            }
        }


        dialog = new ProgressDialog(getActivity(),R.style.MyTheme);
        AlertDialogManager.showDialog(dialog);
        if(isOther) {
            Address address = new Address();
            address.setAccess_token(BuyChat.readFromPreferences(getActivity(),Keys.access_token,Constants.DEFAULT_STRING));
            address.setLocality( citySpinner.getSelectedItem().toString());
            address.setFlat_no_floor_name(addressBuildingName.getText().toString());
            address.setLandmark(addressLandmark.getText().toString());
            address.setTag_address(addressOther.getText().toString());
            BuyChat.dbHelper.InsertAddress(address);
        }else{
            Address address = new Address();
            address.setAccess_token(BuyChat.readFromPreferences(getActivity(),Keys.access_token,Constants.DEFAULT_STRING));
            address.setLocality( citySpinner.getSelectedItem().toString());
            address.setFlat_no_floor_name(addressBuildingName.getText().toString());
            address.setLandmark(addressLandmark.getText().toString());
            address.setTag_address(locationName);
            BuyChat.dbHelper.InsertAddress(address);
        }
        AlertDialogManager.dismissDialog(dialog);
        getFragmentManager().popBackStack();
        mListener.onAddAddress();
//        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
//        Call<JsonObject> call;
//        if(isOther) {
//           call = service.addAddress(Parse.setAddressData(citySpinner.getSelectedItem().toString(),addressBuildingName.getText().toString(),addressLandmark.getText().toString(),addressOther.getText().toString()));
//        }else {
//            call = service.addAddress(Parse.setAddressData(citySpinner.getSelectedItem().toString(),addressBuildingName.getText().toString(),addressLandmark.getText().toString(),locationName));
//        }
//        call.enqueue(this);

    }



    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        AlertDialogManager.dismissDialog(dialog);
        if(response.code() == Constants.SUCCESS){
            if(Parse.checkStatus(response.body().toString()).equals(Keys.success)){
                getFragmentManager().popBackStack();
                mListener.onAddAddress();
            }
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        AlertDialogManager.dismissDialog(dialog);
    }

}
