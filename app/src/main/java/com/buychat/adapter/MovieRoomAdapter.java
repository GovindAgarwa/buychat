package com.buychat.adapter;

import android.content.Context;
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
public class MovieRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

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

    public MovieRoomAdapter(Context context,List<ProductPojos> mListData) {
        this.mListData = mListData;
        mContext = context;
        initializeImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if(viewType==VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_room_item, parent, false);

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
                ((MyViewHolder) holder).product_name.setText(mListData.get(position).getProduct_name());
                ((MyViewHolder) holder).product_price.setText(Constants.MONEY_ICON+mListData.get(position).getProduct_price());
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


        TextView product_name;
        TextView product_price;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.product_image);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_price = (TextView) itemView.findViewById(R.id.product_price);

        }
    }
}
