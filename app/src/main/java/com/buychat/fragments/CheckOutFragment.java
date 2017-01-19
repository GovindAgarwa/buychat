package com.buychat.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buychat.R;
import com.buychat.activities.CategoryListActivity;
import com.buychat.adapter.CheckOutAdapter;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.pojos.ProductPojos;
import com.buychat.pojos.ShopPojo;
import com.buychat.singleton.DataSingleton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Snyxius Technologies on 8/16/2016.
 */
public class CheckOutFragment extends Fragment implements CheckOutAdapter.Calculation {

    private ProgressDialog dialog;
    private Unbinder unbinder;
    @BindView(R.id.recycler_views)
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener scrolllistner;
    ArrayList<ProductPojos> cartPOJOs;
    CheckOutAdapter adapter;
    int items_no=0;
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
//    static int positions;
//    static  int flag;
    public static CheckOutFragment newInstance(int position,int fla) {
//        flag = fla;
//        positions = position;
        Bundle args = new Bundle();
        CheckOutFragment fragment = new CheckOutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkout_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecyclerView();
    }

    public void initializeRecyclerView(){
        System.out.println("position : "+getArguments().getInt(Keys.position1));
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        cartPOJOs = new ArrayList<>();
        cartPOJOs = BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId());
        adapter = new CheckOutAdapter(cartPOJOs,this,getArguments().getInt(Keys.position1)) ;
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void addMoreItems() {
        startActivity(new Intent(getActivity(), CategoryListActivity.class).putExtra(Keys.position, getArguments().getInt(Keys.position)));
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        getActivity().finish();
    }

    @Override
    public void clickMinus(int position) {
        if (cartPOJOs.get(position).getQuantity() > 1) {
            int quantity = cartPOJOs.get(position).getQuantity();
            quantity--;
            cartPOJOs.get(position).setQuantity(quantity);
            BuyChat.dbHelper.UpdateFeeds(cartPOJOs.get(position));
        }else{
            BuyChat.dbHelper.DeleteItems(cartPOJOs.get(position).getId());
            cartPOJOs.remove(position);
        }

        if(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).isEmpty()){
            startActivity(new Intent(getActivity(), CategoryListActivity.class).putExtra(Keys.position, getArguments().getInt(Keys.position)));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            getActivity().finish();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clickPlus(int position) {
        int quantity = cartPOJOs.get(position).getQuantity();
        quantity++;
        cartPOJOs.get(position).setQuantity(quantity);
        BuyChat.dbHelper.UpdateFeeds(cartPOJOs.get(position));
        adapter.notifyDataSetChanged();
    }
}
