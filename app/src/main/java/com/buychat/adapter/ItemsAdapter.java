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
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.fonts.TextViewRegular;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<ProductPojos> mListData;
    ArrayList<ProductPojos> cartListData;
    Context mContext;
    String name;
    ImageLoader imageLoader;
    private ImageLoadingListener imageListener;
    int count;
    private final int VIEW_ITEM = 1;
    private final int VIEW_ADD = 0;

    public ItemsAdapter(Context context,ArrayList<ProductPojos> mListData,int data_count,ArrayList<ProductPojos> cartListData,String string) {
        count = data_count;
        name = string;
       if(data_count >=Constants.INT_ONE){
            count = Constants.INT_ONE;
       }
        System.out.println("Item Holder count "+count);
        this.mListData = mListData;
        this.cartListData =cartListData;
        mContext = context;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_ADD:
                ViewGroup vGroupImage = (ViewGroup) mInflater.inflate(R.layout.cart_items, parent, false);
                CartViewHolder image = new CartViewHolder(vGroupImage);
                return image;
            case VIEW_ITEM:
                ViewGroup empty = (ViewGroup) mInflater.inflate(R.layout.history_items, parent, false);
                MyViewHolder images = new MyViewHolder(empty);
                return images;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
           if (holder instanceof CartViewHolder) {
                ((CartViewHolder) holder).textViewList.get(Constants.DEFAULT_INT)
                        .setText(name);
               ((CartViewHolder) holder).textViewList.get(Constants.INT_ONE).setText(Constants.MONEY_ICON+ DataSingleton.getInstance().getSubTotalData(cartListData));
            }else  if (holder instanceof MyViewHolder) {

               if(count >= Constants.INT_ONE){
                   position = position-1;
               }
               if(position == 0){
                   ((MyViewHolder) holder).previous_order.setVisibility(View.VISIBLE);
               }else{
                   ((MyViewHolder) holder).previous_order.setVisibility(View.GONE);
               }
               System.out.println("Item Holder array size "+mListData.size());
               System.out.println("Item Holder position "+position);
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




    @Override
    public int getItemCount() {
        return null != mListData ? mListData.size()+count :1;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (position ==  VIEW_ADD && count >= Constants.INT_ONE) {
                viewType = VIEW_ADD;
        } else {
            if(mListData.size() != 0) {
                viewType = VIEW_ITEM;
            }
        }
        return viewType;

    }

    class CartViewHolder extends RecyclerView.ViewHolder  {


        @BindViews({R.id.title,R.id.offer_price})
        List<TextView> textViewList;

        public CartViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textViewList.get(Constants.DEFAULT_INT).setSelected(true);
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

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
        }


    }

}
