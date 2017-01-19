package com.buychat.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buychat.activities.MainCategoryDetailActivity;
import com.buychat.activities.ProfileActivity;
import com.buychat.R;
import com.buychat.adapter.FlightAdapter;
import com.buychat.adapter.RestBukasLaundGroceryFashionBeautyAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.CustomClickListner;
import com.buychat.extras.Keys;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.pojos.ProductPojos;
import com.buychat.pojos.ShopsPojo;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.AlertDialogManager;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class FlightFragment extends Fragment implements CustomClickListner,Callback<JsonObject>,SwipeRefreshLayout.OnRefreshListener {
    private ProgressDialog dialog;
    private Unbinder unbinder;

    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    List<CategoriesPojos> listCats;
    FlightAdapter adapter;
    private EndlessRecyclerOnScrollListener scrolllistner;
    List<ShopsPojo> arraylistCats;
    TextView empty;

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRefreshView(view);
        ApiCall();
    }


    private void ApiCall(){
        WebRequests service1 = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call1 = service1.getShopList(Parse.getCityIdAndCategoryIdWithLimitOffset(getArguments().getString(Keys.category_id),
                Constants.INT_TEN,Constants.DEFAULT_INT));
        call1.enqueue(this);
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        mSwipeRefreshLayout.setRefreshing(false);
        arraylistCats = new ArrayList<>();
        AlertDialogManager.dismissDialog(dialog);
        if(response.code() == Constants.SUCCESS) {
            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                arraylistCats = new ArrayList<>();
                arraylistCats = Parse.parseShopList(response.body().toString());
            }
        }else {
            BuyChat.showAToast(Constants.Some_Went_Wrong);
        }

        scrolllistner =  new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d(Keys.position," "+current_page);
                ScrollApiCall(current_page);
            }
        };

        recyclerView.addOnScrollListener(scrolllistner);
        adapter = new FlightAdapter(getActivity(),arraylistCats,FlightFragment.this);
        recyclerView.setAdapter(adapter);
        if(arraylistCats.size() != 0) {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.enableFooter(false);
            adapter.notifyDataSetChanged();
        }else{
            adapter.enableFooter(false);
            adapter.notifyDataSetChanged();
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        mSwipeRefreshLayout.setRefreshing(false);
        empty.setText(Constants.Internet);
        empty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
    private void ScrollApiCall(int current_page) {
        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.getShopList(Parse.getCityIdAndCategoryIdWithLimitOffset(getArguments().getString(Keys.category_id),
                Constants.INT_TEN*current_page+Constants.INT_ONE,Constants.INT_TEN));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ArrayList<ShopsPojo> array = new ArrayList<>();
                if(response.code() == Constants.SUCCESS) {
                    if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                        array = Parse.parseShopList(response.body().toString());
                    }
                }
                if(array.size() != 0) {
                    arraylistCats.addAll(array);
                    adapter.notifyDataSetChanged();
                }else{
                    adapter.enableFooter(false);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void imageClickListner(int position, View view) {
        BuyChat.saveToPreferences(BuyChat.getAppContext(), Keys.package_value, (arraylistCats.get(position).getPackage_value()));
        BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.delivery_charge,(arraylistCats.get(position).getDelivery_charge()));
        DataSingleton.getInstance().setData(arraylistCats,position);
        startActivity(new Intent(getActivity(),ProfileActivity.class)
                .putExtra(Keys.position,getArguments().getInt(Keys.position)));
        getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public void itemClickListner(int position, View view) {
        BuyChat.saveToPreferences(BuyChat.getAppContext(), Keys.package_value, (arraylistCats.get(position).getPackage_value()));
        BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.delivery_charge,(arraylistCats.get(position).getDelivery_charge()));

        BuyChat.saveToPreferences(getActivity(),Keys.merchant_id,arraylistCats.get(position).getId());
        BuyChat.saveToPreferences(getActivity(),Keys.business_name,arraylistCats.get(position).getBusiness_name());
        BuyChat.saveToPreferences(getActivity(),Keys.merchant_image,arraylistCats.get(position).getBusiness_image());



        DataSingleton.getInstance().setData(arraylistCats,position);
        startActivity(new Intent(getActivity(),MainCategoryDetailActivity.class)
                .putExtra(Keys.position,getArguments().getInt(Keys.position)));
        getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void filter(String query){
        query = query.toString().toLowerCase();

        final List<ShopsPojo> filteredList = new ArrayList<>();
        if(arraylistCats.size() != 0) {
            for (int i = 0; i < arraylistCats.size(); i++) {

                final String text = arraylistCats.get(i).getBusiness_name().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(arraylistCats.get(i));
                }
            }

            linearLayoutManager = new LinearLayoutManager(getActivity());
            adapter = new FlightAdapter(getActivity(), filteredList, FlightFragment.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        if(scrolllistner != null) {
            recyclerView.removeOnScrollListener(scrolllistner);
            recyclerView.invalidate();
        }
        ApiCall();
    }

    private void initializeRefreshView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        TypedValue typed_value = new TypedValue();
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typed_value, true);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        if(!mSwipeRefreshLayout.isEnabled())
            mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        empty = (TextView)view.findViewById(R.id.empty);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_views);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
