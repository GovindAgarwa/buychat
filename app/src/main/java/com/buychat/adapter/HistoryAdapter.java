package com.buychat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.CustomClickListner;
import com.buychat.extras.Validater;
import com.buychat.pojos.ProductPojos;
import com.buychat.pojos.ShopsPojo;
import com.buychat.utils.fonts.TextViewRegular;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<ProductPojos> mListData;
    ImageLoader imageLoader;
    private ImageLoadingListener imageListener;
    CustomClickListner clickClistner;


    public HistoryAdapter(Context context, List<ProductPojos> mListData) {
        this.mListData = mListData;
        this.context = context;
    //    this.clickClistner = onItemClickClistner;
        imageLoader = ImageLoader.getInstance();
    }

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





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if(viewType==VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_items, parent, false);

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

                if(position == 0) {
                    ((MyViewHolder) holder).previous_order.setVisibility(View.VISIBLE);
                }else{
                    ((MyViewHolder) holder).previous_order.setVisibility(View.GONE);
                }

                ((MyViewHolder) holder).name.setText(mListData.get(position).getBusiness_name());
                ((MyViewHolder) holder).address.setText(mListData.get(position).getProduct_name());
                ((MyViewHolder) holder).date.setText(Validater.ReviewFormatConvert(mListData.get(position).getOrder_date()));
                ((MyViewHolder) holder).total_price.setText(Constants.MONEY_ICON+mListData.get(position).getOrder_amount());
                if(!mListData.get(position).getMerchant_name().equals(Constants.DEFAULT_STRING))
                ((MyViewHolder) holder).startingLetter.setText(mListData.get(position).getMerchant_name().charAt(0) + "");


                imageLoader.displayImage(BuyChat.replace(mListData.get(position).getMerchant_image()), ((MyViewHolder) holder).profile_image, new SimpleImageLoadingListener() {

                    final List<String> displayedImages = Collections
                            .synchronizedList(new LinkedList<String>());

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                        ((MyViewHolder) holder).startingLetter.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        if (loadedImage != null) {
                            ((MyViewHolder) holder).startingLetter.setVisibility(View.GONE);
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


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.profile_image) CircleImageView profile_image;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.address) TextView address;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.total_price) TextView total_price;
        @BindView(R.id.starting_letter1) TextView startingLetter;
        @BindView(R.id.previous_order) TextView previous_order;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
          //  itemView.setOnClickListener(this);
         //   profile_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.profile_image){
                if(clickClistner != null)
                clickClistner.imageClickListner(getAdapterPosition(),view);
            }else{
                if(clickClistner != null)
                clickClistner.itemClickListner(getAdapterPosition(),view);
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
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        }
    }




}
