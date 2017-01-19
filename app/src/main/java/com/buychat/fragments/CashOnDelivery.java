package com.buychat.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.fonts.TextViewRegular;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class CashOnDelivery extends Fragment {

    private ProgressDialog dialog;
    private Unbinder unbinder;
    @BindView(R.id.total_price)
    TextView total_price;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.image)ImageView image;

    static int positions;
    public static CashOnDelivery newInstance(int position) {
        positions = position;
        Bundle args = new Bundle();
        CashOnDelivery fragment = new CashOnDelivery();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cash_on_delivery, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            if(getArguments().getInt(Keys.position1) == Constants.DEFAULT_INT){
                message.setText("Please pay cash at the time of delivery");
            }else{
                message.setText("Please pay by simplepay");
            }
            total_price.setText(Constants.MONEY_ICON+getArguments().getString(Keys.order_amount,Constants.DEFAULT_STRING));
        }



}
