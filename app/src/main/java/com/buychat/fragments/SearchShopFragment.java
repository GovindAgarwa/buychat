package com.buychat.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.activities.MainCategoryActivity;
import com.buychat.activities.MainCategoryDetailActivity;
import com.buychat.activities.ProfileActivity;
import com.buychat.adapter.MovieRoomAdapter;
import com.buychat.adapter.MovieTaxiHotelAdapter;
import com.buychat.adapter.RestBukasLaundGroceryFashionBeautyAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.CustomClickListner;
import com.buychat.extras.Keys;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.listners.RecyclerItemClickListener;
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
public class SearchShopFragment extends Fragment implements CustomClickListner,Callback<JsonObject>,SwipeRefreshLayout.OnRefreshListener {

    private ProgressDialog dialog;
    private Unbinder unbinder;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener scrolllistner;
    List<ShopsPojo> arraylistCats;
    TextView empty;
    MovieTaxiHotelAdapter adapter;
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    GetKeyboard callGetKeyboard;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callGetKeyboard = (GetKeyboard) activity;
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
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            ApiCall(view);
    }





    private void ApiCall(View view){
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        TypedValue typed_value = new TypedValue();
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typed_value, true);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        if(!mSwipeRefreshLayout.isEnabled())
            mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        dialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        AlertDialogManager.showDialog(dialog);
        empty = (TextView)view.findViewById(R.id.empty);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_views);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(linearLayoutManager);
        arraylistCats = new ArrayList<>();
        WebRequests service1 = BuyChat.initializeRetrofit().create(WebRequests.class);
        callGetKeyboard.setData();
        Call<JsonObject> call1 = service1.getShopListBySearch(Parse.getCityIdWithQueryWithLimitOffset(
                BuyChat.readFromPreferences(getActivity(),Keys.keyword,Constants.DEFAULT_STRING),
                Constants.DEFAULT_INT,Constants.INT_TEN));
        call1.enqueue(this);

    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
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
        adapter = new MovieTaxiHotelAdapter(getActivity(),arraylistCats,SearchShopFragment.this);
        recyclerView.setAdapter(adapter);
        if(arraylistCats.size() != 0) {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            adapter.enableFooter(false);
            adapter.notifyDataSetChanged();
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {

    }

    private void ScrollApiCall(int current_page) {
        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.getShopListBySearch(Parse.getCityIdWithQueryWithLimitOffset(
                BuyChat.readFromPreferences(getActivity(),Keys.keyword,Constants.DEFAULT_STRING),
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
        DataSingleton.getInstance().setData(arraylistCats,position);
        if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Fashion_CategryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_ONE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Beauty_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_TWO));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Groceries_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_THREE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Electronics_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_FOUR));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Restaurants_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_FIVE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Bukas_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_SIX));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Pay_Bill_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_SEVEN));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Education_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_EIGHT));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Taxis_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_NINE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Flights_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_TEN));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Hotels_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_ELEVEN));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Movie_Tickets_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_TWELVE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Event_Tickets_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_THIRTEEN));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Laundry_CategoryId)){
            startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra(Keys.position,Constants.INT_FOURTEEN));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void itemClickListner(int position, View view) {


        BuyChat.saveToPreferences(getActivity(),Keys.merchant_id,arraylistCats.get(position).getId());
        BuyChat.saveToPreferences(getActivity(),Keys.business_name,arraylistCats.get(position).getBusiness_name());
        BuyChat.saveToPreferences(getActivity(),Keys.merchant_image,arraylistCats.get(position).getBusiness_image());


        System.out.println("package_value : "+arraylistCats.get(position).getPackage_value());
        System.out.println("delivery_charge : "+arraylistCats.get(position).getDelivery_charge());
        BuyChat.saveToPreferences(BuyChat.getAppContext(), Keys.package_value, (arraylistCats.get(position).getPackage_value()));
        BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.delivery_charge,(arraylistCats.get(position).getDelivery_charge()));

        DataSingleton.getInstance().setData(arraylistCats,position);
       // startActivity(new Intent(getActivity(),MainCategoryDetailActivity.class)
         //       .putExtra(Keys.position,getArguments().getInt(Keys.position)));
       // getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Fashion_CategryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_ONE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Beauty_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_TWO));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Groceries_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_THREE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Electronics_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FOUR));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Restaurants_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FIVE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Bukas_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_SIX));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Pay_Bill_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_SEVEN));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Education_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_EIGHT));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Taxis_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_NINE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Flights_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_TEN));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Hotels_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_ELEVEN));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Movie_Tickets_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_TWELVE));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Event_Tickets_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_THIRTEEN));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Laundry_CategoryId)){
            startActivity(new Intent(getActivity(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FOURTEEN));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
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
            adapter = new MovieTaxiHotelAdapter(getActivity(), filteredList, SearchShopFragment.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void ApiCallWithData(String query){
        query = query.toString().toLowerCase();
        if(query.equals(Constants.DEFAULT_STRING)){
            WebRequests service1 = BuyChat.initializeRetrofit().create(WebRequests.class);
            Call<JsonObject> call1 = service1.getShopListBySearch(Parse.getCityIdWithQueryWithLimitOffset(query,
                    Constants.DEFAULT_INT,Constants.INT_TEN));
            call1.enqueue(this);
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    public interface GetKeyboard{
        void setData();
    }
}
