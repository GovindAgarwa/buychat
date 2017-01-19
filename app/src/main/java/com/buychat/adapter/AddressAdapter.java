package com.buychat.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.pojos.Address;
import com.buychat.utils.fonts.TextViewRegular;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder>{

    List<Address> mListData;
    Activity activity;
    clickEdit click;
    public AddressAdapter(List<Address> mListData, Activity activity,clickEdit clicks) {
        click = clicks;
        this.mListData = mListData;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.address_item,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressAdapter.MyViewHolder holder, int position) {

        holder.locName.setText(mListData.get(position).getTag_address());
        if(mListData.get(position).getTag_address().equals("HOME"))
            holder.itemPicture.setImageResource(R.drawable.home_grey);
        else if(mListData.get(position).getTag_address().equals("WORK"))
            holder.itemPicture.setImageResource(R.drawable.work_grey);
        else
            holder.itemPicture.setImageResource(R.drawable.location_grey);
        holder.buildingName.setText(mListData.get(position).getFlat_no_floor_name());
        holder.localityName.setText(mListData.get(position).getLocality());
        holder.landmarkName.setText(mListData.get(position).getLandmark());
        if(mListData.get(position).isFlag()){
                holder.checkBox.setChecked(true);
        }else{

            holder.checkBox.setChecked(false);
        }
    }

    public void notifyData(ArrayList<Address> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.mListData = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.locality_name)TextView localityName;
        @BindView(R.id.build_name)TextView buildingName;
        @BindView(R.id.landmark_name)TextView landmarkName;
        @BindView(R.id.loc_Name)TextView locName;
        @BindView(R.id.item_picture)ImageView itemPicture;
        @BindView(R.id.selected_checkbox)CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick(R.id.edit) void click(View view){
            click.clickListner(view,getAdapterPosition());
        }

        @OnClick(R.id.delete) void clickDelete(View view){
            click.clickDeleteListner(view,getAdapterPosition());
        }

        @OnClick(R.id.selected_checkbox) void clickCheck(View view){
            click.clickCheckListner(view,getAdapterPosition());
        }
    }

    public interface clickEdit{
        void clickListner(View view,int position);
        void clickDeleteListner(View view,int position);
        void clickCheckListner(View view,int position);
    }
}
