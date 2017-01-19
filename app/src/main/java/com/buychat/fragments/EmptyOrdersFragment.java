package com.buychat.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.buychat.activities.CategoryListActivity;
import com.buychat.R;
import com.buychat.api.Parse;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.AlertDialogManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class EmptyOrdersFragment extends Fragment {
    private ProgressDialog dialog;
    private Unbinder unbinder;
    @BindView(R.id.float_button)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.neworder)
    Button button;

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    static int positions;
    public static EmptyOrdersFragment newInstance(int position) {
        positions = position;
        Bundle args = new Bundle();
        EmptyOrdersFragment fragment = new EmptyOrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("position1 "+positions);
        initialize();
        if(DataSingleton.getInstance().getData().getProduct_count() == Constants.DEFAULT_INT){
            floatingActionButton.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
        }else {

            if (positions == Constants.INT_THREE) {
                button.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
            } else if (positions == Constants.INT_FIFTEEN) {
                button.setVisibility(View.VISIBLE);
                button.setText("DISCOVER");

            } else if (positions == Constants.INT_ELEVEN) {
                button.setVisibility(View.VISIBLE);
                button.setText("ROOMS");

            } else if (positions == Constants.INT_FIFTEEN) {
                button.setVisibility(View.VISIBLE);
                button.setText("SHOWS");

            } else {
                button.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.GONE);
            }

        }
    }

    public void initialize(){
        try {
            String string = BuyChat.readFromPreferences(getActivity(),Keys.histroy_data,Constants.DEFAULT_STRING);
            if (BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).isEmpty() && Parse.parseHistory(string).isEmpty()) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.container, BlankFragment.newInstance(positions), Constants.Empty_Order)
                        .commit();
            } else {
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.container, ItemsFragment.newInstance(positions), Constants.ItemsFragment)
                        .commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.neworder) void neworder(){
        if(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).isEmpty()) {
            startActivityForResult(new Intent(getActivity(), CategoryListActivity.class)
                    .putExtra(Keys.position, positions),Constants.DEFAULT_INT);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else {
            BuyChat.dbHelper.DeleteMerchant(DataSingleton.getInstance().getData().getId());
            startActivityForResult(new Intent(getActivity(), CategoryListActivity.class).putExtra(Keys.position, positions),Constants.DEFAULT_INT);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }

    @OnClick(R.id.float_button) void floatButton(){
        if(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).isEmpty()) {
            startActivityForResult(new Intent(getActivity(), CategoryListActivity.class)
                    .putExtra(Keys.position, positions),Constants.DEFAULT_INT);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else {
            BuyChat.dbHelper.DeleteMerchant(DataSingleton.getInstance().getData().getId());
            startActivityForResult(new Intent(getActivity(), CategoryListActivity.class).putExtra(Keys.position, positions),Constants.DEFAULT_INT);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
