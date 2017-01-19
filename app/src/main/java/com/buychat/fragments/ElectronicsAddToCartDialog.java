package com.buychat.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.extras.AddData;
import com.buychat.extras.AddToList;
import com.buychat.extras.Constants;
import com.buychat.pojos.ShopPojo;
import com.buychat.singleton.DataSingleton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class ElectronicsAddToCartDialog extends DialogFragment  {




    @BindViews({R.id.product_name,R.id.product_description,R.id.product_price,R.id.offer_price,R.id.discount,R.id.quantity})
    List<TextView> textViewList;
    @BindView(R.id.product_image)
    ImageView imageView;

    int items_no=0;

    public ElectronicsAddToCartDialog() {

    }
    private AddData mListener;
    @Override
    public void onAttach(Activity activity) {
        mListener = (AddData) activity;
        super.onAttach(activity);
    }


    ImageLoader imageLoader;
    DisplayImageOptions options;

    private void initializeImageLoader(){
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();

    }
    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }
    static  int position;
    public static ElectronicsAddToCartDialog newInstance(int positio) {
        position = positio;
        Bundle args = new Bundle();
        ElectronicsAddToCartDialog fragment = new ElectronicsAddToCartDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.dialog_electronic_veiw, container,false);
        ButterKnife.bind(this,itemView);
        return itemView;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeImageLoader();
        items_no = DataSingleton.getInstance().getCartArray().getQuantity();
        textViewList.get(Constants.DEFAULT_INT).setText(DataSingleton.getInstance().getCartArray().getProduct_name());
        textViewList.get(Constants.INT_ONE).setText(DataSingleton.getInstance().getCartArray().getProduct_short_description());
        textViewList.get(Constants.INT_TWO).setText(Constants.MONEY_ICON+Float.valueOf(DataSingleton.getInstance().getCartArray().getProduct_price()));
        textViewList.get(Constants.INT_FIVE).setText(""+DataSingleton.getInstance().getCartArray().getQuantity());
        if(DataSingleton.getInstance().getCartArray().getIs_discount() != null) {
            if (DataSingleton.getInstance().getCartArray().getIs_discount().equals("0")) {
                textViewList.get(Constants.INT_THREE).setVisibility(View.GONE);
                textViewList.get(Constants.INT_FOUR).setVisibility(View.GONE);
            } else {
                textViewList.get(Constants.INT_THREE).setPaintFlags(textViewList.get(Constants.INT_THREE).getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                textViewList.get(Constants.INT_FOUR).setText(DataSingleton.getInstance().getCartArray().getDiscount() + Constants.DISCOUNT);
                textViewList.get(Constants.INT_THREE).setText(Constants.MONEY_ICON + Float.valueOf(DataSingleton.getInstance().getCartArray().getOfferprice()));
                textViewList.get(Constants.INT_THREE).setVisibility(View.VISIBLE);
                textViewList.get(Constants.INT_FOUR).setVisibility(View.VISIBLE);
            }
        }
        imageLoader.displayImage(BuyChat.replace(DataSingleton.getInstance().getCartArray().getProduct_image()),
                imageView, options);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @OnClick(R.id.popup_close)
    public void close_click(){
        getDialog().dismiss();
    }

    @OnClick(R.id.minus)
    public void minusQty(){
        if (items_no > 1) {
            items_no--;
            textViewList.get(Constants.INT_FIVE).setText(String.valueOf(items_no));
        }

    }

    @OnClick(R.id.plus)
    public void addQty(){
        items_no++;
        textViewList.get(Constants.INT_FIVE).setText(String.valueOf(items_no) );
    }

    @OnClick(R.id.add)
    public void addToList(){
        mListener.addList(items_no,position);
        getDialog().dismiss();
    }




}
