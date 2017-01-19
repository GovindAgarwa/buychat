package com.buychat.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.pojos.ProductPojos;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class FashionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    List<ProductPojos> mListData;
    Context mContext;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public boolean isFooterEnabled() {
        return isFooterEnabled;
    }

    public void setIsFooterEnabled(boolean isFooterEnabled) {
        this.isFooterEnabled = isFooterEnabled;
    }

    private boolean isFooterEnabled = true;


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
    public FashionAdapter(Context context,List<ProductPojos> mListData) {
        this.mListData = mListData;
        mContext = context;
        this.mListData = mListData;
        initializeImageLoader();
    }




    class MyViewHolder extends RecyclerView.ViewHolder  {

        @BindViews({R.id.product_name,R.id.product_price,R.id.offer_price,R.id.discount,R.id.description})
        List<TextView> textViewList;
        @BindView(R.id.product_image)
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if(viewType==VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fashion_items, parent, false);

            vh = new MyViewHolder(v);
        }else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ProgressViewHolder){

            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);

        }else if (mListData.size() > 0 && position < mListData.size()) {
            if (holder instanceof MyViewHolder) {
                ((MyViewHolder) holder).textViewList.get(Constants.DEFAULT_INT).setText(mListData.get(position).getProduct_name());
                ((MyViewHolder) holder).textViewList.get(Constants.INT_FOUR).setText(mListData.get(position).getProduct_short_description());
                ((MyViewHolder) holder).textViewList.get(Constants.INT_ONE).setText(Constants.MONEY_ICON+Float.valueOf(mListData.get(position).getProduct_price()));
                if(mListData.get(position).getIs_discount().equals("0")){
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_TWO).setVisibility(View.GONE);
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_THREE).setVisibility(View.GONE);
                }else{
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_TWO).setPaintFlags(((MyViewHolder) holder).textViewList.get(Constants.INT_TWO).getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

                    ((MyViewHolder) holder).textViewList.get(Constants.INT_THREE).setText(mListData.get(position).getDiscount()+Constants.DISCOUNT);
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_TWO).setText(Constants.MONEY_ICON+Float.valueOf(mListData.get(position).getOfferprice()));
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_TWO).setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_THREE).setVisibility(View.VISIBLE);
                }

                imageLoader.displayImage(BuyChat.replace(mListData.get(position).getProduct_image()),
                        ((MyViewHolder) holder).imageView, options);
            }
        }
    }




    @Override
    public int getItemCount() {
        return  (isFooterEnabled) ? mListData.size() + 1 : mListData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (isFooterEnabled && position >= mListData.size() ) ? VIEW_PROG : VIEW_ITEM;
    }

    public void enableFooter(boolean isEnabled){
        this.isFooterEnabled = isEnabled;
    }



    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
