package com.buychat.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buychat.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class BillPayFragment extends Fragment {

    private ProgressDialog dialog;
    private Unbinder unbinder;

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.billpay_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
}
