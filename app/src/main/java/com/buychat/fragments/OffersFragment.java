package com.buychat.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.adapter.ElectronicsAdapter;
import com.buychat.adapter.OffersAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.AddToList;
import com.buychat.extras.Communicate;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.listners.RecyclerItemClickListener;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.pojos.Offers;
import com.buychat.pojos.ProductPojos;
import com.buychat.singleton.DataSingleton;
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
public class OffersFragment extends Fragment implements Callback<JsonObject>,SwipeRefreshLayout.OnRefreshListener,RecyclerItemClickListener.OnItemClickListener {


    public static OffersFragment newInstance(int position) {
        Bundle args = new Bundle();
        OffersFragment fragment = new OffersFragment();
        fragment.setArguments(args);
        return fragment;
    }


    Communicate communicate;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicate = (Communicate) activity;
    }

    private ProgressDialog dialog;
    private Unbinder unbinder;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener scrolllistner;
    OffersAdapter adapter;
//    private static int mainposition;
//    private static int position;
    int position;
    int mainposition;
    ArrayList<Offers> arrayList;
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
        Call<JsonObject> call1 = service1.getOffers(Parse.getMerchantId(
                DataSingleton.getInstance().getData().getId()));
        call1.enqueue(this);
    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        mSwipeRefreshLayout.setRefreshing(false);
        arrayList = new ArrayList<>();
        if(response.code() == Constants.SUCCESS) {
            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                arrayList = new ArrayList<>();
                arrayList = Parse.parseOffers(response.body().toString());
            }
        }else {
            BuyChat.showAToast(Constants.Some_Went_Wrong);
        }

//        scrolllistner =  new EndlessRecyclerOnScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int current_page) {
//                Log.d(Keys.position," "+current_page);
//                ScrollApiCall(current_page);
//            }
//        };

//        recyclerView.addOnScrollListener(scrolllistner);
        adapter = new OffersAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        if(arrayList.size() != 0) {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        mSwipeRefreshLayout.setRefreshing(false);
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
        linearLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),this));
    }


    @Override
    public void onRefresh() {
        if(scrolllistner != null) {
            recyclerView.removeOnScrollListener(scrolllistner);
            recyclerView.invalidate();
        }
        ApiCall();
    }

    @Override
    public void onItemClick(View view, int position) {
        ProductPojos cartPOJO = new ProductPojos();
        cartPOJO.setId(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));
        cartPOJO.setMerchant_id(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));
        cartPOJO.setProduct_name(arrayList.get(position).getOffer_name());
        cartPOJO.setProduct_description(arrayList.get(position).getOffer_description());
        cartPOJO.setProduct_short_description(arrayList.get(position).getOffer_end_date());
        cartPOJO.setProduct_sku(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));
        cartPOJO.setProduct_price(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));
        cartPOJO.setProduct_image("");
        cartPOJO.setProduct_categories(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));
        cartPOJO.setQuantity(Integer.valueOf("1"));
        cartPOJO.setOfferprice(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));
        BuyChat.dbHelper.InsertFeeds(cartPOJO);
        communicate.callChatFragment();
    }
}
