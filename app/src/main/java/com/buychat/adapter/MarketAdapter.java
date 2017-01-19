package com.buychat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.buychat.activities.MarketActivity;
import com.buychat.R;
import com.buychat.pojos.City;
import com.buychat.utils.fonts.TextViewBold;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class MarketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    ImageLoader loader;
    MarketActivity.ImageDisplayListener listener;
    DisplayImageOptions options;
    String randomColor[]={"#fd59a7","#3286c1","#03b7b7","#ff545f","#3c5896","#d7b8ff","#aef5cd","#a692ff","#f5aeae"};


    private ArrayList<City> mListData;
    private Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public boolean isFooterEnabled() {
        return isFooterEnabled;
    }

    public void setIsFooterEnabled(boolean isFooterEnabled) {
        this.isFooterEnabled = isFooterEnabled;
    }

    private boolean isFooterEnabled = true;




    public MarketAdapter(Context context,ArrayList<City> mListData, ImageLoader imageLoader, MarketActivity.ImageDisplayListener imageListener, DisplayImageOptions options) {
        this.mListData = mListData;
        this.listener=imageListener;
        this.loader=imageLoader;
        this.options=options;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if(viewType==VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.city_market_item, parent, false);

            vh = new MyViewHolder(v);
        }else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }

        return vh;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProgressViewHolder) {

            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);

        } else if (mListData.size() > 0 && position < mListData.size()) {
            if (holder instanceof MyViewHolder) {

                ((MyViewHolder) holder).titleMarket.setText(mListData.get(position).getMarket_name());

                loader.displayImage(mListData.get(position).getMarket_image(), ((MyViewHolder) holder).image, options, new SimpleImageLoadingListener() {

                    final List<String> displayedImages = Collections
                            .synchronizedList(new LinkedList<String>());

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                        ((MyViewHolder) holder).topRelative.setBackgroundColor(Color.parseColor(randomColor[randInt(0, randomColor.length - 1)]));
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        if (loadedImage != null) {
                            ImageView imageView = (ImageView) view;
                            boolean firstDisplay = !displayedImages.contains(imageUri);
                            if (firstDisplay) {
                                FadeInBitmapDisplayer.animate(imageView, 500);
                                displayedImages.add(imageUri);
                            }
                        }
                    }
                });
            }
        }
    }



    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.landing_img_slide)ImageView image;
        @BindView(R.id.title_market)TextViewBold titleMarket;
        @BindView(R.id.top_relative)RelativeLayout topRelative;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
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
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        }
    }
}
