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
import com.buychat.pojos.ShopsPojo;
import com.buychat.utils.RoundedImageView;
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
public class PayBillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<ShopsPojo> mListData;
    ImageLoader imageLoader;
    private ImageLoadingListener imageListener;
    CustomClickListner clickClistner;
    public PayBillAdapter(Context context, List<ShopsPojo> mListData, CustomClickListner onItemClickClistner) {
        this.mListData = mListData;
        this.context = context;
        this.clickClistner = onItemClickClistner;
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
                    .inflate(R.layout.paybill_items, parent, false);

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
                ((MyViewHolder) holder).name.setText(mListData.get(position).getBusiness_name());
                ((MyViewHolder) holder).type.setText(mListData.get(position).getMerchant_name());
                ((MyViewHolder) holder).ratingBar.setRating(Float.valueOf(mListData.get(position).getRating()));
                ((MyViewHolder) holder).map_address.setText(mListData.get(position).getBusiness_address());
                if(!mListData.get(position).getBusiness_name().equals(Constants.DEFAULT_STRING))
                    ((MyViewHolder) holder).startingLetter.setText(mListData.get(position).getBusiness_name().charAt(0) + "");

                imageLoader.displayImage(BuyChat.replace(mListData.get(position).getBusiness_image()), ((MyViewHolder) holder).profile_image, new SimpleImageLoadingListener() {

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

        @BindView(R.id.profile_image)
        RoundedImageView profile_image;
        @BindView(R.id.logos_name)
        TextViewRegular name;
        @BindView(R.id.logos_type) TextViewRegular type;
        @BindView(R.id.map_address) TextViewRegular map_address;
        @BindView(R.id.order_time) TextViewRegular order_time;
        @BindView(R.id.starting_letter1) TextView startingLetter;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        protected LayerDrawable avg_stars;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.avg_stars = (LayerDrawable) this.ratingBar.getProgressDrawable();
            this.avg_stars.getDrawable(2).setColorFilter(BuyChat.getAppContext().getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
            itemView.setOnClickListener(this);
            profile_image.setOnClickListener(this);
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
