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
import android.widget.TextView;

import com.buychat.R;
import com.buychat.activities.ProductListActivity;
import com.buychat.adapter.BrandsAdapter;
import com.buychat.adapter.CategoryAdapter;
import com.buychat.adapter.FashionAdapter;
import com.buychat.adapter.GroceryAdapter;
import com.buychat.adapter.RestaurantAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.AddToList;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.listners.RecyclerItemClickListener;
import com.buychat.pojos.BrandsPojo;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.pojos.ProductPojos;
import com.buychat.pojos.ShopPojo;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.AlertDialogManager;
import com.google.gson.JsonObject;

import java.io.Serializable;
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
public class RestaurantFragment extends Fragment implements Callback<JsonObject>,RecyclerItemClickListener.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {



    public static RestaurantFragment newInstance(int positions,ArrayList<CategoriesPojos> categoriesPojoses,int pos) {
        //   position = pos;
        //    arraylistCats = categoriesPojoses;
        //     mainposition = positions;
        Bundle args = new Bundle();
        RestaurantFragment fragment = new RestaurantFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private ProgressDialog dialog;
    private Unbinder unbinder;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private GridLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener scrolllistner;
    RestaurantAdapter adapter;
//    private static int mainposition;
//    private static int position;

    //  static ArrayList<CategoriesPojos> arraylistCats;
    ArrayList<CategoriesPojos> arraylistCats;
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
        position = getArguments().getInt(Keys.position1);
        mainposition = getArguments().getInt(Keys.position);
        arraylistCats = new ArrayList<>();
        arraylistCats = (ArrayList<CategoriesPojos>) getArguments().getSerializable("ARRAY");
        initializeRefreshView(view);
        ApiCall();
    }


    private void ApiCall(){
        WebRequests service1 = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call1 = service1.getProductList(Parse.getMerchantIdAndSubCategoryIdWithLimitOffset(
                DataSingleton.getInstance().getData().getId()
                ,arraylistCats.get(position).getId(),Constants.INT_TEN,Constants.DEFAULT_INT));
        call1.enqueue(this);
    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        mSwipeRefreshLayout.setRefreshing(false);
        arrayList = new ArrayList<>();
        if(response.code() == Constants.SUCCESS) {
            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                arrayList = new ArrayList<>();
                arrayList = Parse.parseProduct(response.body().toString());
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
        adapter = new RestaurantAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);
        if(arrayList.size() != 0) {
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
    }

    private void ScrollApiCall(int current_page) {
        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.getProductList(Parse.getMerchantIdAndSubCategoryIdWithLimitOffset(DataSingleton.getInstance().getData().getId()
                ,arraylistCats.get(position).getId(),Constants.INT_TEN*current_page+Constants.INT_ONE,Constants.INT_TEN));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ArrayList<ProductPojos> array = new ArrayList<>();
                if(response.code() == Constants.SUCCESS) {
                    if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                        array = Parse.parseProduct(response.body().toString());
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

    private AddToList mListener;
    @Override
    public void onAttach(Activity activity) {
        mListener = (AddToList) activity;
        super.onAttach(activity);
    }

    public void addData(int quantity,int position){
        arrayList.get(position).setQuantity(quantity);
        arrayList.get(position).setFlag(true);
        mListener.showCheckout(arrayList);
    }


    @Override
    public void onItemClick(View view, int position) {

        if(BuyChat.dbHelper.getCartProductByProductId(DataSingleton.getInstance().getData().getId(),
                arrayList.get(position).getId()).isEmpty()){
            DataSingleton.getInstance().setCartArray(arrayList,position);
        } else {
            DataSingleton.getInstance().setCartArray(BuyChat.dbHelper.getCartProductByProductId(DataSingleton.getInstance().getData().getId(),
                    arrayList.get(position).getId()),Constants.DEFAULT_INT);
        }
        DialogFragment dialogFragment = ElectronicsAddToCartDialog.newInstance(position);
        dialogFragment.show(getActivity().getSupportFragmentManager().beginTransaction(), Constants.ElectronicsAddToCartDialog);

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
        linearLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
    }

    public void filter(String query){
        query = query.toString().toLowerCase();
        final List<ProductPojos> filteredList = new ArrayList<>();
        if(arrayList.size() != 0 || !query.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                final String text = arrayList.get(i).getProduct_name().toLowerCase();
                if (text.contains(query)) {
                    filteredList.add(arrayList.get(i));
                }
            }
            linearLayoutManager = new GridLayoutManager(getActivity(),1);
            adapter = new RestaurantAdapter(getActivity(),arrayList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
