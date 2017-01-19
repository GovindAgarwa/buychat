package com.buychat.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.buychat.R;
import com.buychat.extras.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class FeePaymentFragment extends Fragment {

    private ProgressDialog dialog;
    private Unbinder unbinder;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindViews({R.id.fee_layout_content,R.id.newReg_layout_content})
    List<LinearLayout> arrayLinear;
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fee_payment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radioButton1){
                    arrayLinear.get(Constants.DEFAULT_INT).setVisibility(View.VISIBLE);
                    arrayLinear.get(Constants.INT_ONE).setVisibility(View.GONE);
                }else if(i == R.id.radioButton2){
                    arrayLinear.get(Constants.DEFAULT_INT).setVisibility(View.GONE);
                    arrayLinear.get(Constants.INT_ONE).setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
