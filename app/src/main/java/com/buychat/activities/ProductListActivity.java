package com.buychat.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.adapter.ViewPagerAdapter;
import com.buychat.app.BuyChat;
import com.buychat.extras.AddData;
import com.buychat.extras.AddToList;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.fragments.BlankFragment;
import com.buychat.fragments.ElectronicsAddToCartDialog;
import com.buychat.fragments.ElectronicsFragment;
import com.buychat.fragments.EventsFragment;
import com.buychat.fragments.FashionFragment;
import com.buychat.fragments.FlightFragment;
import com.buychat.fragments.GreryFashBeautyElecFragment;
import com.buychat.fragments.GroceryFragment;
import com.buychat.fragments.MovieRoomFragment;
import com.buychat.fragments.MovieTaxiHotelFragment;
import com.buychat.fragments.PayBillFragment;
import com.buychat.fragments.RestBukLadEduFragment;
import com.buychat.fragments.RestaurantFragment;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.pojos.ProductPojos;
import com.buychat.singleton.DataSingleton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductListActivity extends AppCompatActivity implements AddToList,AddData,SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.checkout_button)
    RelativeLayout checkOutLayout;
    @BindViews({R.id.count,R.id.total_price})
    List<TextView> textViewList;
    ArrayList<ProductPojos> arrayproduct;
    ArrayList<CategoriesPojos> arraylistCats;
    ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        Intent intent = getIntent();
        arraylistCats = new ArrayList<>();
        arrayproduct = new ArrayList<>();
        if(intent != null) {
            Bundle args = intent.getBundleExtra("BUNDLE");
            arraylistCats = (ArrayList<CategoriesPojos>) args.getSerializable("ARRAY");
        }
        setSupportActionBar(toolbar);
        setTitle(DataSingleton.getInstance().getCategoriesData().getSubcategory_name());
        getSupportActionBar().setSubtitle(DataSingleton.getInstance().getData().getBusiness_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        setupViewPager(viewPager);

        if(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).isEmpty()) {
            checkOutLayout.setVisibility(View.GONE);
        }else {
            setCheckOutLayout();
        }
    }

    private void setCheckOutLayout(){
        int item = 0;
        float price = 0.0f;
        for (int i = 0; i < BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).size(); i++) {
            item += BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(i).getQuantity();
            price += (BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(i).getQuantity()
                    * Float.valueOf(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(i).getProduct_price()));
        }
        textViewList.get(Constants.DEFAULT_INT).setText(String.valueOf(item));
        textViewList.get(Constants.INT_ONE).setText(Constants.MONEY_ICON + String.valueOf(price));
        checkOutLayout.setVisibility(View.VISIBLE);
    }
    private int getCategoryPos() {
        for(CategoriesPojos _item : arraylistCats)
        {
            if(_item.getId().equals(DataSingleton.getInstance().getCategoriesData().getId()))
                return arraylistCats.indexOf(_item);
        }
        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.inside_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.history:
                startActivity(new Intent(ProductListActivity.this,HistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.help:
                startActivity(new Intent(ProductListActivity.this,FAQActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.profile:
                startActivity(new Intent(ProductListActivity.this,UserProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.share:
                share();
                break;

        }
        return true;
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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void setupViewPager(ViewPager viewPager) {
        System.out.println("position "+getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT));
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if(arraylistCats.size() != 0) {
            for(int i=0;i<arraylistCats.size();i++){
                if(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_ONE ||
                    getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_TWO ||
                        getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_EIGHT){
                Fragment fragment = new FashionFragment();
                Bundle args = new Bundle();
                args.putInt(Keys.position, getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT));
                args.putInt(Keys.position1, i);
                args.putSerializable("ARRAY",(Serializable)arraylistCats);
                fragment.setArguments(args);
                    adapter.addFragment(fragment, arraylistCats.get(i).getSubcategory_name());
            }else if(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_THREE){
                Fragment fragment = new GroceryFragment();
                Bundle args = new Bundle();
                args.putInt(Keys.position, getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT));
                args.putInt(Keys.position1, i);
                args.putSerializable("ARRAY",(Serializable)arraylistCats);
                fragment.setArguments(args);
                    adapter.addFragment(fragment, arraylistCats.get(i).getSubcategory_name());
            }else if(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_FIVE ||
                    getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_SIX){
                Fragment fragment = new RestaurantFragment();
                Bundle args = new Bundle();
                args.putInt(Keys.position, getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT));
                args.putInt(Keys.position1, i);
                args.putSerializable("ARRAY",(Serializable)arraylistCats);
                fragment.setArguments(args);
                    adapter.addFragment(fragment, arraylistCats.get(i).getSubcategory_name());
            }else if(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_TWELVE ||
                    getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_ELEVEN){
                Fragment fragment = new MovieRoomFragment();
                Bundle args = new Bundle();
                args.putInt(Keys.position, getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT));
                args.putInt(Keys.position1, i);
                args.putSerializable("ARRAY",(Serializable)arraylistCats);
                fragment.setArguments(args);
                    adapter.addFragment(fragment, arraylistCats.get(i).getSubcategory_name());

            }else if(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_FOUR ||
                        getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_FIFTEEN){
                Fragment fragment = new ElectronicsFragment();
                Bundle args = new Bundle();
                args.putInt(Keys.position, getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT));
                args.putInt(Keys.position1, i);
                args.putSerializable("ARRAY",(Serializable)arraylistCats);
                fragment.setArguments(args);
                    adapter.addFragment(fragment, arraylistCats.get(i).getSubcategory_name());
            }

            }
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(getCategoryPos());
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setOffscreenPageLimit(arraylistCats.size());
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    setTitle(arraylistCats.get(position).getSubcategory_name());
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @OnClick({R.id.checkout_button}) void clickCheckOut(){
        startActivity(new Intent(getApplicationContext(), CheckoutOptionsActivity.class).putExtra(Keys.position,getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void addList(int items_no,int position) {
        Fragment fragment =  adapter.getRegisteredFragment(viewPager.getCurrentItem());
        if(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_ONE ||
                getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_TWO ||
                getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_EIGHT ){
            if(fragment instanceof FashionFragment){
                ((FashionFragment) fragment).addData(items_no,position);
            }
        }else if(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_THREE){
            if(fragment instanceof GroceryFragment){
                ((GroceryFragment) fragment).addData(items_no,position);
            }
        }else if(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_FIVE ||
                getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_SIX){
            if(fragment instanceof RestaurantFragment){
                ((RestaurantFragment) fragment).addData(items_no,position);
            }
        }else if(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_TWELVE ||
                getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_ELEVEN){
            if(fragment instanceof MovieRoomFragment){
                ((MovieRoomFragment) fragment).addData(items_no,position);
            }

        }else if(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_FOUR ||
                getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT) == Constants.INT_FIFTEEN){
            if(fragment instanceof ElectronicsFragment){
                ((ElectronicsFragment) fragment).addData(items_no,position);
            }
        }
    }

    @Override
    public void showCheckout(ArrayList<ProductPojos> arrayList) {
        arrayproduct = new ArrayList<>();
        arrayproduct = arrayList;
        Log.d("Size",""+arrayList.size());
        if(arrayproduct.size() !=0){
            for(int i=0;i<arrayproduct.size();i++){
                if(arrayproduct.get(i).isFlag()) {
                    if (BuyChat.dbHelper.getCartProductByProductId(DataSingleton.getInstance().getData().getId(), arrayproduct.get(i).getId()).isEmpty()) {
                        BuyChat.dbHelper.InsertFeeds(arrayproduct.get(i));
                    } else {
                        BuyChat.dbHelper.UpdateFeeds(arrayproduct.get(i));
                    }
                }
            }
        }

        setCheckOutLayout();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        System.out.print("xx"+query);
        Fragment fragment =  adapter.getRegisteredFragment(viewPager.getCurrentItem());
        if(fragment instanceof FashionFragment){
            ((FashionFragment) fragment).filter(query);
        }else if(fragment instanceof GroceryFragment){
            ((GroceryFragment) fragment).filter(query);
        }else if(fragment instanceof RestaurantFragment){
            ((RestaurantFragment) fragment).filter(query);
        }else if(fragment instanceof MovieRoomFragment){
            ((MovieRoomFragment) fragment).filter(query);
        }else if(fragment instanceof ElectronicsFragment){
            ((ElectronicsFragment) fragment).filter(query);
        }

        return false;
    }






}
