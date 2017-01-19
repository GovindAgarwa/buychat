package com.buychat.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.buychat.activities.SubCategoryListActivity;
import com.buychat.adapter.CategoryAdapter;
import com.buychat.adapter.MovieTaxiHotelAdapter;
import com.buychat.adapter.RestBukasLaundGroceryFashionBeautyAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.listners.RecyclerItemClickListener;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.pojos.ShopsPojo;
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
public class CategoryFragment extends Fragment implements Callback<JsonObject>,SwipeRefreshLayout.OnRefreshListener,RecyclerItemClickListener.OnItemClickListener {

    private ProgressDialog dialog;
    private Unbinder unbinder;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    List<CategoriesPojos> listCats;
    CategoryAdapter adapter;
    private EndlessRecyclerOnScrollListener scrolllistner;
    List<CategoriesPojos> arraylistCats;
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
        Call<JsonObject> call1 = service1.getSubCategories(Parse.getMerchantId(DataSingleton.getInstance().getData().getId()));
        call1.enqueue(this);
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
        linearLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        mSwipeRefreshLayout.setRefreshing(false);
        if(response.code() == Constants.SUCCESS) {
            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                arraylistCats = new ArrayList<>();
                arraylistCats = Parse.parseCategories(response.body().toString());
                if(arraylistCats.size() != 0) {
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new CategoryAdapter(arraylistCats);
                    recyclerView.setAdapter(adapter);
                }else{
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    public void filter(String query){
        query = query.toString().toLowerCase();
        final List<CategoriesPojos> filteredList = new ArrayList<>();
        if(arraylistCats.size() != 0 || !query.isEmpty()) {
            for (int i = 0; i < arraylistCats.size(); i++) {

                final String text = arraylistCats.get(i).getSubcategory_name().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(arraylistCats.get(i));
                }
            }
            linearLayoutManager = new GridLayoutManager(getActivity(),2);
            adapter = new CategoryAdapter(arraylistCats);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        DataSingleton.getInstance().setCategoriesData(arraylistCats,position);
        if(arraylistCats.get(position).getHas_subcategory() == Constants.DEFAULT_INT){
            ArrayList<CategoriesPojos> arrayList = new ArrayList<CategoriesPojos>();
            for (int i = 0; i<arraylistCats.size();i++){
                if(arraylistCats.get(i).getHas_subcategory() == Constants.DEFAULT_INT){
                    arrayList.add(arraylistCats.get(i));
                }
            }
            Intent intent = new Intent(getActivity(),ProductListActivity.class);
            Bundle args = new Bundle();
            Log.d("size"," "+arrayList.size());
            args.putSerializable("ARRAY",(Serializable)arrayList);
            intent.putExtra("BUNDLE",args);
            intent.putExtra(Keys.position,getArguments().getInt(Keys.position,Constants.DEFAULT_INT));
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            getActivity().finish();
        }else if(arraylistCats.get(position).getHas_subcategory() == Constants.INT_ONE) {
            startActivity(new Intent(getActivity(), SubCategoryListActivity.class)
                    .putExtra(Keys.position, getArguments().getInt(Keys.position, Constants.DEFAULT_INT)));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            getActivity().finish();
        }
    }
}
