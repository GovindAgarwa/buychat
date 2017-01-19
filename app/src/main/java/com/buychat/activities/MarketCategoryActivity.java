package com.buychat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.adapter.ViewPagerAdapter;
import com.buychat.api.Parse;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.extras.LocationSelecter;
import com.buychat.fragments.EventsFragment;
import com.buychat.fragments.FlightFragment;
import com.buychat.fragments.GreryFashBeautyElecFragment;
import com.buychat.fragments.LocationFragment;
import com.buychat.fragments.MovieTaxiHotelFragment;
import com.buychat.fragments.PayBillFragment;
import com.buychat.fragments.RestBukLadEduFragment;
import com.buychat.pojos.City;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.BlurBuilder;
import com.buychat.utils.GradientHalfoverImageDrawable;
import com.buychat.utils.ImageDisplayListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarketCategoryActivity extends AppCompatActivity implements LocationSelecter,SearchView.OnQueryTextListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.city_name)
    TextView city_name;
    ArrayList<City> arrayList;
    private ProgressDialog dialog;
    ViewPagerAdapter adapter;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    private ImageLoadingListener imageListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketcategory);
        ButterKnife.bind(this);
        initialize();

    }

    @OnClick(R.id.secondTab)
    public void secondTabClick(){
        arrayList = new ArrayList<>();
        arrayList = Parse.parseCity(DataSingleton.getInstance().getCityData());
        DialogFragment dialogFragment = LocationFragment.newInstance(arrayList);
        //dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager().beginTransaction(), Constants.LocationFragment);
    }


    private void initialize() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        city_name.setText(BuyChat.readFromPreferences(getApplicationContext(), Keys.city_name,Constants.DEFAULT_STRING));
        imageLoader = ImageLoader.getInstance();
        imageListener=new ImageDisplayListener();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .displayer(new BitmapDisplayer() {
                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                        int gradientStartColor = Color.parseColor("#00000000");//argb(0, 0, 0, 0);
                        int gradientEndColor = Color.parseColor("#88000000");//argb(255, 0, 0, 0);
                        GradientHalfoverImageDrawable gradientDrawable = new GradientHalfoverImageDrawable(getResources(), bitmap);
                        gradientDrawable.setGradientColors(gradientStartColor, gradientEndColor);
                        imageAware.setImageDrawable(gradientDrawable);
                    }
                })
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_logo_image)
                .showImageOnFail(R.drawable.empty_logo_image)
                .showImageOnLoading(R.drawable.empty_logo_image).build();

        ImageView iconView = (ImageView) findViewById(R.id.landing_img_slide);

        TextView title = (TextView) findViewById(R.id.title_market);

        imageLoader.displayImage(getIntent().getStringExtra(Keys.business_image), iconView, options,imageListener);


        title.setText(getIntent().getStringExtra(Keys.market_name));

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment fragment = new GreryFashBeautyElecFragment();
        Bundle args = new Bundle();
        args.putInt(Keys.position, Constants.INT_ONE);
        args.putString(Keys.category_id, Constants.Fashion_CategryId);
        args.putString(Keys.city_market_id, getIntent().getStringExtra(Keys.city_market_id));
        fragment.setArguments(args);
        adapter.addFragment(fragment,Constants.Fashion);

        Fragment fragment1 = new GreryFashBeautyElecFragment();
        Bundle args1 = new Bundle();
        args1.putInt(Keys.position, Constants.INT_TWO);
        args1.putString(Keys.category_id, Constants.Beauty_CategoryId);
        args1.putString(Keys.city_market_id, getIntent().getStringExtra(Keys.city_market_id));
        fragment1.setArguments(args1);
        adapter.addFragment(fragment1, Constants.Beauty);

        Fragment fragment2 = new GreryFashBeautyElecFragment();
        Bundle args2 = new Bundle();
        args2.putInt(Keys.position, Constants.INT_THREE);
        args2.putString(Keys.category_id, Constants.Groceries_CategoryId);
        args2.putString(Keys.city_market_id, getIntent().getStringExtra(Keys.city_market_id));
        fragment2.setArguments(args2);
        adapter.addFragment(fragment2, Constants.Groceries);

        Fragment fragment3 = new GreryFashBeautyElecFragment();
        Bundle args3 = new Bundle();
        args3.putInt(Keys.position, Constants.INT_FOUR);
        args3.putString(Keys.category_id, Constants.Electronics_CategoryId);
        args3.putString(Keys.city_market_id, getIntent().getStringExtra(Keys.city_market_id));
        fragment3.setArguments(args3);
        adapter.addFragment(fragment3, Constants.Electronics);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) - 1);
        viewPager.setOffscreenPageLimit(Constants.INT_THREE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.category_menu, menu);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public void setLocationText(int position) {

        if(!city_name.getText().toString().equals(arrayList.get(position).getCity_name())) {
            BuyChat.saveToPreferences(getApplicationContext(),Keys.city_name,arrayList.get(position).getCity_name());
            BuyChat.saveToPreferences(getApplicationContext(),Keys.city_id,arrayList.get(position).getId());
            city_name.setText(arrayList.get(position).getCity_name());
            reloadViewPager(viewPager.getCurrentItem());
        }
    }

    private void reloadViewPager(int position) {
         adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment fragment = new GreryFashBeautyElecFragment();
        Bundle args = new Bundle();
        args.putInt(Keys.position, Constants.INT_ONE);
        args.putString(Keys.category_id, Constants.Fashion_CategryId);
        fragment.setArguments(args);
        adapter.addFragment(fragment,Constants.Fashion);

        Fragment fragment1 = new GreryFashBeautyElecFragment();
        Bundle args1 = new Bundle();
        args1.putInt(Keys.position, Constants.INT_TWO);
        args1.putString(Keys.category_id, Constants.Beauty_CategoryId);
        fragment1.setArguments(args1);
        adapter.addFragment(fragment1, Constants.Beauty);

        Fragment fragment2 = new GreryFashBeautyElecFragment();
        Bundle args2 = new Bundle();
        args2.putInt(Keys.position, Constants.INT_THREE);
        args2.putString(Keys.category_id, Constants.Groceries_CategoryId);
        fragment2.setArguments(args2);
        adapter.addFragment(fragment2, Constants.Groceries);

        Fragment fragment3 = new GreryFashBeautyElecFragment();
        Bundle args3 = new Bundle();
        args3.putInt(Keys.position, Constants.INT_FOUR);
        args3.putString(Keys.category_id, Constants.Electronics_CategoryId);
        fragment3.setArguments(args3);
        adapter.addFragment(fragment3, Constants.Electronics);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.setOffscreenPageLimit(Constants.INT_THREE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.search:
                startActivityForResult(new Intent(getApplicationContext(),SearchListActivity.class),Constants.DEFAULT_INT);
                overridePendingTransition(R.anim.push_up_in,R.anim.fade_out);
                break;
            case R.id.histroy:
                startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.help:
                startActivity(new Intent(getApplicationContext(),FAQActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(),UserProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.share:
                share();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void share() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this app!");
            startActivity(shareIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }





    @Override
    public void onBackPressed() {
        setResult(Constants.INT_TWO);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {


        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        System.out.print("xx"+query);
        Fragment fragment =  adapter.getRegisteredFragment(viewPager.getCurrentItem());
        if(fragment instanceof RestBukLadEduFragment){
            ((RestBukLadEduFragment) fragment).filter(query);
        }else if(fragment instanceof GreryFashBeautyElecFragment){
            ((GreryFashBeautyElecFragment) fragment).filter(query);
        }else if(fragment instanceof PayBillFragment){
            ((PayBillFragment) fragment).filter(query);
        }else if(fragment instanceof MovieTaxiHotelFragment){
            ((MovieTaxiHotelFragment) fragment).filter(query);
        }else if(fragment instanceof EventsFragment){
            ((EventsFragment) fragment).filter(query);
        }else if(fragment instanceof FlightFragment){
            ((FlightFragment) fragment).filter(query);
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        BuyChat.saveToPreferences(getApplicationContext(),Keys.flat_no_floor_name,Constants.False);
       // SocketSingleton.get(getApplicationContext()).getSocket().disconnect();
    }


}
