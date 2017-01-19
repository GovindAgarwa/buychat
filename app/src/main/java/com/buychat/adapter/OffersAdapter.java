package com.buychat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.pojos.Offers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class OffersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Offers> mListData;

    public OffersAdapter(ArrayList<Offers> mListData) {
        this.mListData = mListData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offers_items,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MyViewHolder){
                ((MyViewHolder) holder).title.setText(""+mListData.get(position).getOffer_name());
                ((MyViewHolder) holder).description.setText(""+mListData.get(position).getOffer_description());
                ((MyViewHolder) holder).date.setText("Ends on "+mListData.get(position).getOffer_end_date());
            }
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        TextView date;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            description = (TextView)itemView.findViewById(R.id.description);
            date = (TextView)itemView.findViewById(R.id.date);
        }
    }

}
