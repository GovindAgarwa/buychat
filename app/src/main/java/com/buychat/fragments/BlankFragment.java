package com.buychat.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buychat.R;
import com.buychat.extras.Constants;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.fonts.TextViewRegular;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class BlankFragment extends Fragment {

    private ProgressDialog dialog;
    private Unbinder unbinder;
    @BindView(R.id.empty_message)TextViewRegular emptyMessage;
    @BindView(R.id.image)ImageView image;

    static int positions;
    public static BlankFragment newInstance(int position) {
        positions = position;
        Bundle args = new Bundle();
        BlankFragment fragment = new BlankFragment();
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
        View view = inflater.inflate(R.layout.empty_order_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        emptyMessage.setText(positions+"");

        selectEmptyImage(positions);
    }

    public void selectEmptyImage(int pos){
        emptyMessage.setText(Constants.Empty_Message(DataSingleton.getInstance().getData().getBusiness_name()));
        switch (pos){
            case Constants.INT_ONE:
                image.setImageResource(R.drawable.fashion_empty);
                break;
            case Constants.INT_TWO:
                image.setImageResource(R.drawable.beauty_empty);
                break;
            case Constants.INT_THREE:
                image.setImageResource(R.drawable.grocery_empty);
                break;
            case Constants.INT_FOUR:
                image.setImageResource(R.drawable.elecs_empty);
                break;
            case Constants.INT_FIVE:
                image.setImageResource(R.drawable.order_icon);
                break;
            case Constants.INT_SIX:
                image.setImageResource(R.drawable.buka_empty);
                break;
            case Constants.INT_ELEVEN:
                image.setImageResource(R.drawable.hotel_empty);
                break;
            case Constants.INT_TWELVE:
                    image.setImageResource(R.drawable.movie_empty);
                break;
            case Constants.INT_FIFTEEN:
                image.setImageResource(R.drawable.shop_icon);
                break;
            case Constants.INT_EIGHT:
                image.setImageResource(R.drawable.boutique_empty);
                break;
        }

    }
}
