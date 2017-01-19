package com.buychat.adapter;


import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buychat.R;

import java.util.ArrayList;


/**
 * Created by Eoin on 8/8/16.
 */
public class TextPagerAdapter extends PagerAdapter {

    ArrayList<String> listText;

    public TextPagerAdapter(ArrayList<String> listTexts) {
        listText=new ArrayList<>();
        listText.addAll(listTexts);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        TextView text = (TextView) LayoutInflater.from(collection.getContext())
                .inflate(R.layout.layout_page_tutorial_text, null);

        text.setText(listText.get(position));

//        switch (position) {
//            case 0:
//                text.setText(R.string.write_label);
//                break;
//            case 1:
//                text.setText(R.string.share_label);
//                break;
//            case 2:
//                text.setText(R.string.follow_label);
//                break;
//            default:
//                text.setText("");
//        }

        collection.addView(text);
        return text;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((TextView) view);
    }

    @Override
    public int getCount() {
        return listText.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
