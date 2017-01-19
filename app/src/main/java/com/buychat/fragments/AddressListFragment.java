package com.buychat.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.buychat.activities.PaymentActivity;
import com.buychat.R;
import com.buychat.adapter.AddressAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.AddressReload;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.Address;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.AlertDialogManager;
import com.buychat.utils.fonts.ButtonMedium;
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
public class AddressListFragment  extends Fragment implements AddressAdapter.clickEdit, Callback<JsonObject> {
    private ProgressDialog dialog;
    private Unbinder unbinder;
    @BindView(R.id.recycler_views)
    RecyclerView recyclerView;
    private AddressReload mListener;
    @BindView(R.id.add_address_relative)
    RelativeLayout addAddress;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Address> myList;
    AddressAdapter adapter;
    @BindView(R.id.continue_button)ButtonMedium continueButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecyclerView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (AddressReload) activity;
    }


    private void initializeRecyclerView(){
        myList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AddressAdapter(myList,getActivity(),this);
        recyclerView.setAdapter(adapter);

        dialog = new ProgressDialog(getActivity(),R.style.MyTheme);
        AlertDialogManager.showDialog(dialog);
        myList.addAll(BuyChat.dbHelper.getALLAddress());
        adapter.notifyDataSetChanged();
        AlertDialogManager.dismissDialog(dialog);
        if(myList.size()>0){
            continueButton.setVisibility(View.VISIBLE);
        }else
            continueButton.setVisibility(View.GONE);
//        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
//        Call<JsonObject> call = service.getAddress(Parse.getAccessToken());
//        call.enqueue(this);
    }

    @OnClick(R.id.add_an_address)
    public void addAnAddress(){
        addNextFragment( addAddress, false);
    }

    private void addNextFragment(RelativeLayout squareBlue, boolean overlap) {
        AddAddressFragment sharedElementFragment2 = new AddAddressFragment();

        if (Build.VERSION.SDK_INT >= 21 ) {
            Slide slideTransition = new Slide(Gravity.TOP);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
            ChangeBounds changeBoundsTransition = new ChangeBounds();
            changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
            sharedElementFragment2.setEnterTransition(slideTransition);
            sharedElementFragment2.setAllowEnterTransitionOverlap(overlap);
            sharedElementFragment2.setAllowReturnTransitionOverlap(overlap);
            sharedElementFragment2.setSharedElementEnterTransition(changeBoundsTransition);
        }

        getFragmentManager().beginTransaction()
                .add(R.id.sample2_content, sharedElementFragment2)
                .addToBackStack(Constants.AddressListFragment)
                .addSharedElement(squareBlue, getString(R.string.square_blue_name))
                .commit();
    }

    private void editNextFragment(RelativeLayout squareBlue, boolean overlap) {
        EditAddressFragment sharedElementFragment2 = new EditAddressFragment();

        if (Build.VERSION.SDK_INT >= 21 ) {
            Slide slideTransition = new Slide(Gravity.TOP);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
            ChangeBounds changeBoundsTransition = new ChangeBounds();
            changeBoundsTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
            sharedElementFragment2.setEnterTransition(slideTransition);
            sharedElementFragment2.setAllowEnterTransitionOverlap(overlap);
            sharedElementFragment2.setAllowReturnTransitionOverlap(overlap);
            sharedElementFragment2.setSharedElementEnterTransition(changeBoundsTransition);
        }

        getFragmentManager().beginTransaction()
                .add(R.id.sample2_content, sharedElementFragment2)
                .addToBackStack(Constants.AddressListFragment)
                .addSharedElement(squareBlue, getString(R.string.square_blue_name))
                .commit();
    }

    @OnClick(R.id.continue_button)
    public void continueClick(){
        String addressId = Constants.DEFAULT_STRING;
        String city_name = Constants.DEFAULT_STRING;
        int position = 0;
        if(myList.size() != 0){
            for(int i=0;i<myList.size();i++){
                if(myList.get(i).isFlag()){
                    position = i;
                    addressId = myList.get(i).getId();
                    city_name = myList.get(i).getLocality();
                }
            }
        }
        if(addressId.equals(Constants.DEFAULT_STRING)){
            BuyChat.showAToast("Please select address");
        }else if(city_name.equals(BuyChat.readFromPreferences(getActivity(),Keys.city_name,Constants.DEFAULT_STRING))) {
            DataSingleton.getInstance().setAddressData(myList,position);
            startActivity(new Intent(getActivity(), PaymentActivity.class)
                    .putExtra(Keys.address_id, addressId)
                    .putExtra(Keys.position,getArguments().getInt(Keys.position)));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            BuyChat.showAToast("In selected city that order will not delivered");
        }
    }

    @Override
    public void clickListner(View view, int position) {
        DataSingleton.getInstance().setAddressData(myList,position);
        editNextFragment(addAddress,false);
    }

    @Override
    public void clickDeleteListner(View view, int position) {
        dialog = new ProgressDialog(getActivity(),R.style.MyTheme);
        AlertDialogManager.showDialog(dialog);
        BuyChat.dbHelper.DeleteAddress(myList.get(position).getId());
        AlertDialogManager.dismissDialog(dialog);
        getFragmentManager().popBackStack();
        mListener.onAddAddress();
//        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
//        Call<JsonObject> call = service.deleteAddress(Parse.getAddressID(myList.get(position).getId()));
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    AlertDialogManager.dismissDialog(dialog);
//                if(response.code() == Constants.SUCCESS){
//                    if(Parse.checkStatus(response.body().toString()).equals(Keys.success)){
//                        getFragmentManager().popBackStack();
//                        mListener.onAddAddress();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
    }

    @Override
    public void clickCheckListner(View view, int position) {
           for(int i=0;i<myList.size();i++){
              myList.get(i).setFlag(false);
           }
            myList.get(position).setFlag(true);
            adapter.notifyDataSetChanged();
    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        AlertDialogManager.dismissDialog(dialog);
        if(response.code() == Constants.SUCCESS){
            if(Parse.checkStatus(response.body().toString()).equals(Keys.success)){
               ArrayList arrayList = new ArrayList<>();
                arrayList= Parse.parseAddress(response.body().toString());
                if(arrayList.size() != 0) {
                    myList.addAll(arrayList);
                    adapter.notifyDataSetChanged();
                    if(myList.size()>0){
                        continueButton.setVisibility(View.VISIBLE);
                    }else
                        continueButton.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        AlertDialogManager.dismissDialog(dialog);
    }
}
