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
import com.buychat.utils.fonts.TextViewRegular;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


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

    public RestaurantAdapter(Context context,List<ProductPojos> mListData) {
        this.mListData = mListData;
        mContext = context;
        initializeImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if(viewType==VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.restaurant_item, parent, false);

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
                ((MyViewHolder) holder).textViewList.get(Constants.INT_ONE).setText(Constants.MONEY_ICON+Float.valueOf(mListData.get(position).getProduct_price()));
                if(mListData.get(position).getIs_discount().equals("0")){
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_FIVE).setVisibility(View.GONE);
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_FOUR).setVisibility(View.GONE);
                }else{
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_FIVE).setPaintFlags(((MyViewHolder) holder).textViewList.get(Constants.INT_FIVE).getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

                    ((MyViewHolder) holder).textViewList.get(Constants.INT_FOUR).setText(mListData.get(position).getDiscount()+Constants.DISCOUNT);
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_FIVE).setText(Constants.MONEY_ICON+Float.valueOf(mListData.get(position).getOfferprice()));
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_FIVE).setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).textViewList.get(Constants.INT_FOUR).setVisibility(View.VISIBLE);
                }
                ((MyViewHolder) holder).textViewList.get(Constants.INT_TWO).setText(mListData.get(position).getProduct_prep_time()+Constants.PREPARATION);


                imageLoader.displayImage(BuyChat.replace(mListData.get(position).getProduct_image()),
                        ((MyViewHolder) holder).imageView, options);
            }
        }
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




    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  {


        @BindViews({R.id.product_name,R.id.product_price,R.id.product_time,R.id.product_rating,R.id.discount,R.id.offer_price})
        List<TextView> textViewList;
        @BindView(R.id.product_image)
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
