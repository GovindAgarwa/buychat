package com.buychat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.extras.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amanjham on 13/02/16.
 */
public class CustomViewPagerAdapter extends FragmentPagerAdapter {

    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final List<String> mFragmentCountList = new ArrayList<>();
    Context context;
    public CustomViewPagerAdapter(FragmentManager manager,Context context1) {
        super(manager);
        context = context1;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title,String count) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        mFragmentCountList.add(count);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setText(mFragmentTitleList.get(position));
        tv.setTextColor(Color.parseColor("#ffffff"));
        TextView tv1 = (TextView) v.findViewById(R.id.count);
        System.out.println("count2  "+mFragmentCountList.get(position));
        if(mFragmentCountList.get(position).equals(""+ Constants.DEFAULT_INT)){
            tv1.setVisibility(View.GONE);
        }else {
            tv1.setVisibility(View.VISIBLE);
            tv1.setText(mFragmentCountList.get(position));
        }
        return v;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mFragment.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mFragment.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return mFragment.get(position);
    }

    private SparseArray<Fragment> mFragment = new SparseArray<>();

    private final List<Fragment> mFragmentList = new ArrayList<>();




}
