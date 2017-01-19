package com.buychat.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.buychat.R;
import com.buychat.adapter.ViewPagerAdapter;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.fragments.CashOnDelivery;
import com.buychat.fragments.CheckOutFragment;
import com.buychat.fragments.HistoryDetailFragment;
import com.buychat.singleton.DataSingleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderHistroyDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.submit)
    Button submit;
    ViewPagerAdapter adapter;
    @BindView(R.id.container)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        submit.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        setTitle("Order History");
        getSupportActionBar().setSubtitle(DataSingleton.getInstance().getCartArray().getBusiness_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        if(DataSingleton.getInstance().getCartArray().getOrder_type().equals(""+Constants.INT_FOUR)){
            HistoryDetailFragment cashOnDelivery = new HistoryDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Keys.position,Constants.INT_THREE);
            cashOnDelivery.setArguments(bundle);
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,cashOnDelivery,Constants.CheckOutFragment)
                    .commit();
        }else {
            setupViewPager(viewPager);
        }
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
            String order_type = null;
            int position = 0;
            if(DataSingleton.getInstance().getCartArray().getOrder_type().equals(""+Constants.INT_ONE)){
                order_type = Constants.Home_Delivery;
                position = 0;
            }else if(DataSingleton.getInstance().getCartArray().getOrder_type().equals(""+Constants.INT_TWO)){
                order_type = Constants.Take_Away;
                position = 1;
            }else if(DataSingleton.getInstance().getCartArray().getOrder_type().equals(""+Constants.INT_THREE)){
                order_type = Constants.Pre_Order;
                position = 2;
            }
            HistoryDetailFragment cashOnDelivery = new HistoryDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Keys.position,position);
            cashOnDelivery.setArguments(bundle);
            adapter.addFragment(cashOnDelivery, order_type);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setOnPageChangeListener(this);
        }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
