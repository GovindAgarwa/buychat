package com.buychat.activities;

import android.app.ActivityManager;
import android.content.ComponentName;
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

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.adapter.ViewPagerAdapter;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.fragments.CheckOutFragment;
import com.buychat.fragments.EventsFragment;
import com.buychat.fragments.FlightFragment;
import com.buychat.fragments.GreryFashBeautyElecFragment;
import com.buychat.fragments.MovieTaxiHotelFragment;
import com.buychat.fragments.PayBillFragment;
import com.buychat.fragments.RestBukLadEduFragment;
import com.buychat.singleton.DataSingleton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutOptionsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.container)
    FrameLayout frameLayout;
    @BindView(R.id.submit)
    Button submit;
    ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        setSupportActionBar(toolbar);
        setTitle(DataSingleton.getInstance().getData().getBusiness_name());
        getSupportActionBar().setSubtitle(DataSingleton.getInstance().getData().getMerchant_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        setupViewPager();
        Log.d("OrderType"," "+DataSingleton.getInstance().getData().getOrder_type().size());
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
        setResult(Constants.INT_FOUR);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setupViewPager() {

        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);

         adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(DataSingleton.getInstance().getData().getOrder_type().size() != 0){
               for(int i=0;i<DataSingleton.getInstance().getData().getOrder_type().size();i++){

                    if(DataSingleton.getInstance().getData().getOrder_type().get(i).equalsIgnoreCase(Constants.Home_Delivery)){
                        Fragment fragment = new CheckOutFragment();
                        Bundle args = new Bundle();
                        args.putInt(Keys.position, getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT));
                        args.putInt(Keys.position1, Constants.DEFAULT_INT);
                        fragment.setArguments(args);
                        adapter.addFragment(fragment, Constants.Home_Delivery);
                    }else if(DataSingleton.getInstance().getData().getOrder_type().get(i).equalsIgnoreCase(Constants.Take_Away)){
                        Fragment fragment1 = new CheckOutFragment();
                        Bundle args1 = new Bundle();
                        args1.putInt(Keys.position, getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT));
                        args1.putInt(Keys.position1, Constants.INT_ONE);
                        fragment1.setArguments(args1);
                        adapter.addFragment(fragment1, Constants.Take_Away);
                    }else if(DataSingleton.getInstance().getData().getOrder_type().get(i).equalsIgnoreCase(Constants.Pre_Order)){
                        Fragment fragment2 = new CheckOutFragment();
                        Bundle args2 = new Bundle();
                        args2.putInt(Keys.position, getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT));
                        args2.putInt(Keys.position1, Constants.INT_TWO);
                        fragment2.setArguments(args2);
                        adapter.addFragment(fragment2, Constants.Pre_Order);
                    }
               }
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(DataSingleton.getInstance().getData().getOrder_type().size()-1);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setOnPageChangeListener(this);
        }else{
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            Fragment fragment = new CheckOutFragment();
            Bundle args = new Bundle();
            args.putInt(Keys.position, getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT));
            args.putInt(Keys.position1, Constants.INT_THREE);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,fragment,Constants.CheckOutFragment)
                    .commit();
        }
    }

//    private void reloadViewpager(int position) {
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        Fragment fragment = new CheckOutFragment();
//        Bundle args = new Bundle();
//        args.putInt(Keys.position, getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT));
//        args.putInt(Keys.position1, Constants.DEFAULT_INT);
//        fragment.setArguments(args);
//        adapter.addFragment(fragment,Constants.Home_Delivery);
//
//        Fragment fragment1 = new CheckOutFragment();
//        Bundle args1 = new Bundle();
//        args1.putInt(Keys.position,getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT));
//        args1.putInt(Keys.position1, Constants.INT_ONE);
//        fragment1.setArguments(args1);
//        adapter.addFragment(fragment1, Constants.Take_Away);
//
//        Fragment fragment2 = new CheckOutFragment();
//        Bundle args2 = new Bundle();
//        args2.putInt(Keys.position,getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT));
//        args2.putInt(Keys.position1, Constants.INT_TWO);
//        fragment2.setArguments(args2);
//        adapter.addFragment(fragment2, Constants.Pre_Order);
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(position);
//        viewPager.setOffscreenPageLimit(Constants.INT_TWO);
//    }


    @OnClick(R.id.submit) void submit(){
            String total_amount = "0";
            boolean flag = false;
        if(DataSingleton.getInstance().getData().getOrder_type().size() != 0){
            for(int i=0;i<DataSingleton.getInstance().getData().getOrder_type().size();i++){
                if(DataSingleton.getInstance().getData().getOrder_type().get(i).equalsIgnoreCase(Constants.Home_Delivery)){
                    if(viewPager.getCurrentItem() == i){
                        total_amount =  DataSingleton.getInstance()
                                .getTotalWithPackageChargeWithDeliveryData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
                                        .getData().getId()));
                        flag = false;
                    }
                }else if(DataSingleton.getInstance().getData().getOrder_type().get(i).equalsIgnoreCase(Constants.Take_Away)){
                    if(viewPager.getCurrentItem() == i){
                        total_amount = DataSingleton.getInstance()
                                .getTotalWithPackageChargeData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
                                        .getData().getId()));
                        flag = true;
                    }
                }else if(DataSingleton.getInstance().getData().getOrder_type().get(i).equalsIgnoreCase(Constants.Pre_Order)){
                    if(viewPager.getCurrentItem() == i){
                        total_amount = DataSingleton.getInstance()
                                .getTotalData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
                                        .getData().getId()));
                        flag = true;
                    }
                }
            }

        }else{
        //    if(viewPager.getCurrentItem() == Constants.DEFAULT_INT){
                total_amount =  DataSingleton.getInstance()
                        .getTotalWithPackageChargeWithDeliveryData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
                                .getData().getId()));
                flag = false;
//            }
//            if(viewPager.getCurrentItem() == Constants.INT_ONE){
//                total_amount = DataSingleton.getInstance()
//                        .getTotalWithPackageChargeData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
//                                .getData().getId()));
//                flag = true;
//            }
//            if(viewPager.getCurrentItem() == Constants.INT_TWO){
//                total_amount = DataSingleton.getInstance()
//                        .getTotalData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
//                                .getData().getId()));
//                flag = true;
//            }
        }

        System.out.println("min_order "+DataSingleton.getInstance().getData().getMin_order_value());
        System.out.println("flag "+flag);
        System.out.println("total_amount "+total_amount);

        if(DataSingleton.getInstance().getData().getMin_order_value().equals(Constants.DEFAULT_STRING)){
            if(flag == true){
                startActivity(new Intent(getApplicationContext(), PaymentActivity.class)
                        .putExtra(Keys.address_id, "asd")
                        .putExtra(Keys.position,viewPager.getCurrentItem()));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }else {
                startActivity(new Intent(getApplicationContext(), DeliveryAddressListActivity.class)
                        .putExtra(Keys.position, viewPager.getCurrentItem()));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        } else if(Float.valueOf(DataSingleton.getInstance().getData().getMin_order_value()) <= Float.valueOf(total_amount)){
            if(flag == true){
                startActivity(new Intent(getApplicationContext(), PaymentActivity.class)
                        .putExtra(Keys.address_id, "sd")
                        .putExtra(Keys.position,viewPager.getCurrentItem()));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }else {
                startActivity(new Intent(getApplicationContext(), DeliveryAddressListActivity.class)
                        .putExtra(Keys.position, viewPager.getCurrentItem()));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

        }else {
                BuyChat.showAToast("Minimum Order : "+Constants.MONEY_ICON+DataSingleton.getInstance().getData().getMin_order_value());
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Fragment fragment =  adapter.getRegisteredFragment(viewPager.getCurrentItem());
        if(fragment instanceof CheckOutFragment){
            ((CheckOutFragment) fragment).initializeRecyclerView();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
