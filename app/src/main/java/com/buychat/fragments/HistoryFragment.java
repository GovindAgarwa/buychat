package com.buychat.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.activities.OrderHistroyDetailsActivity;
import com.buychat.adapter.HistoryAdapter;
import com.buychat.adapter.OffersAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.CustomClickListner;
import com.buychat.extras.Keys;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.listners.RecyclerItemClickListener;
import com.buychat.pojos.Offers;
import com.buychat.pojos.ProductPojos;
import com.buychat.singleton.DataSingleton;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class HistoryFragment extends Fragment implements Callback<JsonObject>,SwipeRefreshLayout.OnRefreshListener,CustomClickListner,RecyclerItemClickListener.OnItemClickListener {


    public static HistoryFragment newInstance(int position) {
        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ProgressDialog dialog;
    private Unbinder unbinder;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener scrolllistner;
    HistoryAdapter adapter;
//    private static int mainposition;
//    private static int position;
    int position;
    int mainposition;
    ArrayList<ProductPojos> arrayList;
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
        Call<JsonObject> call1 = service1.getOrdersList(Parse.getAccessTokenWithLimitOffset(Constants.DEFAULT_INT,Constants.INT_TEN));
        call1.enqueue(this);
    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        mSwipeRefreshLayout.setRefreshing(false);
        arrayList = new ArrayList<>();
        if(response.code() == Constants.SUCCESS) {
            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                arrayList = new ArrayList<>();
                arrayList = Parse.parseHistory(response.body().toString());
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
        adapter = new HistoryAdapter(getActivity(),arrayList);
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
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
    }


    @Override
    public void onRefresh() {
        if(scrolllistner != null) {
            recyclerView.removeOnScrollListener(scrolllistner);
            recyclerView.invalidate();
        }
        ApiCall();
    }

    private void ScrollApiCall(int current_page) {
        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.getOrdersList(Parse.getAccessTokenWithLimitOffset(Constants.INT_TEN*current_page+Constants.INT_ONE,Constants.INT_TEN));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ArrayList<ProductPojos> array = new ArrayList<>();
                if(response.code() == Constants.SUCCESS) {
                    if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                        arrayList = Parse.parseHistory(response.body().toString());
                    }
                }
                if(array.size() != 0) {
                    arrayList.addAll(array);
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

    }

    @Override
    public void itemClickListner(int position, View view) {

    }

    @Override
    public void onItemClick(View view, int position) {
        DataSingleton.getInstance().setCartArray(arrayList,position);
        System.out.println("Package Value : "+DataSingleton.getInstance().getCartArray().getPackage_value());
        System.out.println("Delivery Charge : "+DataSingleton.getInstance().getCartArray().getDelivery_charge());
        BuyChat.saveToPreferences(getActivity(),Keys.package_value,DataSingleton.getInstance().getCartArray().getPackage_value());
        BuyChat.saveToPreferences(getActivity(),Keys.delivery_charge,DataSingleton.getInstance().getCartArray().getDelivery_charge());
        startActivity(new Intent(getActivity(),OrderHistroyDetailsActivity.class));
        getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}
