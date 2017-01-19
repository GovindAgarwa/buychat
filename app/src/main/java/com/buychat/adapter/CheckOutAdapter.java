package com.buychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.ProductPojos;
import com.buychat.pojos.ShopPojo;
import com.buychat.singleton.DataSingleton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by amanjham on 16/05/16.
 */
public class CheckOutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    final int TYPE_HEADER = 0;
    final int TYPE_MIDDLE = 1;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    ArrayList<ProductPojos> arrayList;
    int items_no=0;
    Calculation calculation;
    int flag;
    public CheckOutAdapter(ArrayList<ProductPojos> arrayList, Calculation calculation,int pos) {
        this.arrayList = arrayList;
        this.calculation = calculation;
        flag = pos;
        initializeImageLoader();
    }


    private void initializeImageLoader(){
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_logo_image)
                .showImageOnFail(R.drawable.empty_logo_image)
                .showImageOnLoading(R.drawable.empty_logo_image).build();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HEADER:
                ViewGroup vGroupImage = (ViewGroup) mInflater.inflate(R.layout.checkout_items, parent, false);
                HeaderHolder image = new HeaderHolder(vGroupImage);
                return image;
            case TYPE_MIDDLE:
                ViewGroup vGroupText = (ViewGroup) mInflater.inflate(R.layout.checkout_items_bottom, parent, false);
                MiddleHolder text = new MiddleHolder(vGroupText);
                return text;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).textViewList.get(Constants.DEFAULT_INT).setText(arrayList.get(position).getProduct_name());
            ((HeaderHolder) holder).textViewList.get(Constants.INT_ONE).setText(Constants.MONEY_ICON+Float.valueOf(arrayList.get(position).getProduct_price()));
            ((HeaderHolder) holder).textViewList.get(Constants.INT_TWO).setText(""+arrayList.get(position).getQuantity());
        } else if (holder instanceof MiddleHolder) {
            ((MiddleHolder) holder).textViewList.get(Constants.DEFAULT_INT).setText("("+DataSingleton.getInstance().getTotalItems(arrayList)+" items)");
            ((MiddleHolder) holder).textViewList.get(Constants.INT_ONE).setText(Constants.MONEY_ICON+DataSingleton.getInstance().getSubTotalData(arrayList));

            if(flag == Constants.INT_TWO){
                 ((MiddleHolder) holder).textViewList.get(Constants.INT_THREE).setText(Constants.MONEY_ICON+ DataSingleton.getInstance().getTotalData(arrayList));
            }else if(flag == Constants.INT_ONE){
                ((MiddleHolder) holder).viewList.get(Constants.DEFAULT_INT).setVisibility(View.VISIBLE);
                ((MiddleHolder) holder).relativeLayoutList.get(Constants.DEFAULT_INT).setVisibility(View.VISIBLE);
                ((MiddleHolder) holder).textViewList.get(Constants.INT_TWO).setText(Constants.MONEY_ICON+Float.valueOf(BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.package_value,Constants.DEFAULT_STRING)));
                ((MiddleHolder) holder).textViewList.get(Constants.INT_THREE).setText(Constants.MONEY_ICON+DataSingleton.getInstance().getTotalWithPackageChargeData(arrayList));
            }else if(flag == Constants.DEFAULT_INT) {
                ((MiddleHolder) holder).viewList.get(Constants.DEFAULT_INT).setVisibility(View.VISIBLE);
                ((MiddleHolder) holder).relativeLayoutList.get(Constants.DEFAULT_INT).setVisibility(View.VISIBLE);
                ((MiddleHolder) holder).viewList.get(Constants.INT_ONE).setVisibility(View.VISIBLE);
                ((MiddleHolder) holder).relativeLayoutList.get(Constants.INT_ONE).setVisibility(View.VISIBLE);
                ((MiddleHolder) holder).textViewList.get(Constants.INT_TWO).setText(Constants.MONEY_ICON+ Float.valueOf(BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.package_value,Constants.DEFAULT_STRING)));
                ((MiddleHolder) holder).textViewList.get(Constants.INT_FOUR).setText(Constants.MONEY_ICON+ Float.valueOf(BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.delivery_charge,Constants.DEFAULT_STRING)));
                ((MiddleHolder) holder).textViewList.get(Constants.INT_THREE).setText(Constants.MONEY_ICON+DataSingleton.getInstance().getTotalWithPackageChargeWithDeliveryData(arrayList));
            }else if(flag == Constants.INT_THREE){
                ((MiddleHolder) holder).viewList.get(Constants.INT_ONE).setVisibility(View.VISIBLE);
                ((MiddleHolder) holder).relativeLayoutList.get(Constants.INT_ONE).setVisibility(View.VISIBLE);
                ((MiddleHolder) holder).textViewList.get(Constants.INT_FOUR).setText(Constants.MONEY_ICON+Float.valueOf(BuyChat.readFromPreferences(BuyChat.getAppContext(), Keys.delivery_charge,Constants.DEFAULT_STRING)));
                ((MiddleHolder) holder).textViewList.get(Constants.INT_THREE).setText(Constants.MONEY_ICON+DataSingleton.getInstance().getTotalWithDeliveryChargeData(arrayList));

            }

        }
    }

    @Override
    public int getItemCount() {
        return null != arrayList ? arrayList.size()+1 : Constants.DEFAULT_INT+1;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (position ==  arrayList.size()) {
            viewType = TYPE_MIDDLE;
        } else {
            viewType = TYPE_HEADER;
        }
        return viewType;
    }

    class HeaderHolder extends RecyclerView.ViewHolder{

        @BindViews({R.id.product_name,R.id.product_price,R.id.quantity})
        List<TextView> textViewList;

        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.minus) void minus(){
            calculation.clickMinus(getAdapterPosition());
        }

        @OnClick(R.id.plus) void plus(){
            calculation.clickPlus(getAdapterPosition());
        }


    }


    class MiddleHolder extends RecyclerView.ViewHolder {


        @BindViews({R.id.item_count,R.id.subtotal,R.id.package_charge,R.id.grand_total,R.id.delivery_charge})
        List<TextView> textViewList;

        @BindViews({R.id.package_view,R.id.delivery_view})
        List<View> viewList;
        @BindViews({R.id.package_layout,R.id.delivery_layout})
        List<RelativeLayout> relativeLayoutList;

        public MiddleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @OnClick(R.id.addMoreItems) void addMoreItems()
        {
            calculation.addMoreItems();
        }
    }

    public interface Calculation{
        void addMoreItems();
        void clickMinus(int position);
        void clickPlus(int position);
    }


}
