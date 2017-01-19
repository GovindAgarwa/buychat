package com.buychat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.extras.CustomClickListner;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.utils.RoundedImageView;
import com.buychat.utils.fonts.TextViewBold;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<CategoriesPojos> mListData;
    ImageLoader imageLoader;
    DisplayImageOptions options;



    public CategoryAdapter(List<CategoriesPojos> mListData) {
        this.mListData = mListData;
        initializeImageLoader();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_item,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).item_Name.setText(mListData.get(position).getSubcategory_name());
            ((MyViewHolder) holder).item_Qunatity.setText(mListData.get(position).getCount()+" Items");
            imageLoader.displayImage(BuyChat.replace(mListData.get(position).getSubcategory_image()),
                    ((MyViewHolder) holder).image, options);
        }
    }

    private void initializeImageLoader(){
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();

    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.item_Picture)RoundedImageView image;
        @BindView(R.id.item_Name)TextView item_Name;
        @BindView(R.id.item_quantity)TextView item_Qunatity;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }
}
