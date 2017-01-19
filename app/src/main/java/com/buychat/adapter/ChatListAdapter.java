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
import com.buychat.pojos.Chat;
import com.buychat.pojos.ProductPojos;
import com.buychat.pojos.ShopsPojo;
import com.buychat.utils.fonts.TextViewRegular;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Chat> mListData;
    ImageLoader imageLoader;
    private ImageLoadingListener imageListener;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("h:mm a", Locale.US);


    CustomClickListner clickClistner;

    public ChatListAdapter(Context context, List<Chat> mListData) {
        this.mListData = mListData;
        this.context = context;
        imageLoader = ImageLoader.getInstance();
    }

    private Context context;
    private final int VIEW_ITEM = 1;
    private final int VIEW_ADD = 0;





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_ADD:
                ViewGroup vGroupImage = (ViewGroup) mInflater.inflate(R.layout.add_list_item, parent, false);
                AddViewHolder image = new AddViewHolder(vGroupImage);
                return image;
            case VIEW_ITEM:
                ViewGroup empty = (ViewGroup) mInflater.inflate(R.layout.chat_list_item, parent, false);
                MyViewHolder images = new MyViewHolder(empty);
                return images;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof AddViewHolder) {



        } else if (holder instanceof MyViewHolder) {

                ((MyViewHolder) holder).message.setText(mListData.get(position-1).getMessageText());
                ((MyViewHolder) holder).buisness_name.setText(mListData.get(position-1).getBusiness_name());
                ((MyViewHolder) holder).time.setText(Validater.getDisplayableTime(mListData.get(position-1).getMessageTime()));

                if(!mListData.get(position-1).getFlag().equals(""+Constants.DEFAULT_INT)) {
                    ((MyViewHolder) holder).count.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).count.setText(""+mListData.get(position-1).getCount());
                    ((MyViewHolder) holder).message.setTextColor(BuyChat.getAppContext().getResources().getColor(R.color.green));
                    ((MyViewHolder) holder).time.setTextColor(BuyChat.getAppContext().getResources().getColor(R.color.green));
                }else{
                    ((MyViewHolder) holder).count.setVisibility(View.INVISIBLE);
                    ((MyViewHolder) holder).time.setTextColor(BuyChat.getAppContext().getResources().getColor(android.R.color.darker_gray));
                    ((MyViewHolder) holder).message.setTextColor(BuyChat.getAppContext().getResources().getColor(android.R.color.darker_gray));
                }

                if(!mListData.get(position-1).getBusiness_name().equals(Constants.DEFAULT_STRING))
                    ((MyViewHolder) holder).startingLetter.setText(mListData.get(position-1).getBusiness_name().charAt(0) + "");

                if(mListData.get(position-1).getMerchant_image() != null) {
                    if (mListData.get(position-1).getMerchant_image().equals(Constants.DEFAULT_STRING)) {

                    } else {
                        imageLoader.displayImage(BuyChat.replace(mListData.get(position-1).getMerchant_image()), ((MyViewHolder) holder).profile_image, new SimpleImageLoadingListener() {

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
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.profile_image)
        CircleImageView profile_image;
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.buisness_name)
        TextView buisness_name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.count)
        TextView count;

        @BindView(R.id.starting_letter1) TextView startingLetter;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
        return null != mListData ? mListData.size()+1 : Constants.DEFAULT_INT+1;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (position ==  VIEW_ADD) {

                viewType = VIEW_ADD;

        } else {
            viewType = VIEW_ITEM;
        }
        return viewType;

    }



    public static class AddViewHolder extends RecyclerView.ViewHolder {

        public AddViewHolder(View v) {
            super(v);

        }
    }
}
