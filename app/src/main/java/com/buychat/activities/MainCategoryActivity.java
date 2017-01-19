package com.buychat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.adapter.ViewPagerAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
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
import com.buychat.singleton.SocketSingleton;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainCategoryActivity extends AppCompatActivity implements LocationSelecter,SearchView.OnQueryTextListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
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

        Fragment fragment4 = new RestBukLadEduFragment();
        Bundle args4 = new Bundle();
        args4.putInt(Keys.position, Constants.INT_FIVE);
        args4.putString(Keys.category_id, Constants.Restaurants_CategoryId);
        args4.putString(Keys.city_market_id, getIntent().getStringExtra(Keys.city_market_id));
        fragment4.setArguments(args4);
        adapter.addFragment(fragment4, Constants.Restaurants);

        Fragment fragment5 = new RestBukLadEduFragment();
        Bundle args5 = new Bundle();
        args5.putInt(Keys.position, Constants.INT_SIX);
        args5.putString(Keys.category_id, Constants.Bukas_CategoryId);
        args5.putString(Keys.city_market_id, getIntent().getStringExtra(Keys.city_market_id));
        fragment5.setArguments(args5);
        adapter.addFragment(fragment5, Constants.Bukas);


        Fragment fragment12 = new EventsFragment();
        Bundle args12 = new Bundle();
        args12.putInt(Keys.position, Constants.INT_SEVEN);
        args12.putString(Keys.category_id, Constants.Event_Tickets_CategoryId);
        fragment12.setArguments(args12);
        adapter.addFragment(fragment12, Constants.Event_Tickets);

        Fragment fragment7 = new RestBukLadEduFragment();
        Bundle args7 = new Bundle();
        args7.putInt(Keys.position, Constants.INT_EIGHT);
        args7.putString(Keys.category_id, Constants.Education_CategoryId);
        args7.putString(Keys.city_market_id, getIntent().getStringExtra(Keys.city_market_id));
        fragment7.setArguments(args7);
        adapter.addFragment(fragment7, Constants.Boutiques);

        Fragment fragment8 = new MovieTaxiHotelFragment();
        Bundle args8 = new Bundle();
        args8.putInt(Keys.position, Constants.INT_NINE);
        args8.putString(Keys.category_id, Constants.Taxis_CategoryId);
        fragment8.setArguments(args8);
        adapter.addFragment(fragment8, Constants.Taxis);

        Fragment fragment9 = new FlightFragment();
        Bundle args9 = new Bundle();
        args9.putInt(Keys.position, Constants.INT_TEN);
        args9.putString(Keys.category_id, Constants.Flights_CategoryId);
        fragment9.setArguments(args9);
        adapter.addFragment(fragment9, Constants.Flights);

        Fragment fragment10 = new MovieTaxiHotelFragment();
        Bundle args10 = new Bundle();
        args10.putInt(Keys.position, Constants.INT_ELEVEN);
        args10.putString(Keys.category_id, Constants.Hotels_CategoryId);
        fragment10.setArguments(args10);
        adapter.addFragment(fragment10, Constants.Hotels);


        Fragment fragment11 = new MovieTaxiHotelFragment();
        Bundle args11 = new Bundle();
        args11.putInt(Keys.position, Constants.INT_TWELVE);
        args11.putString(Keys.category_id, Constants.Movie_Tickets_CategoryId);
        fragment11.setArguments(args11);
        adapter.addFragment(fragment11, Constants.Movie_Tickets);

        /*
          Fragment fragment6 = new PayBillFragment();
        Bundle args6 = new Bundle();
        args6.putInt(Keys.position, Constants.INT_SEVEN);
        args6.putString(Keys.category_id, Constants.Pay_Bill_CategoryId);
        fragment6.setArguments(args6);
        adapter.addFragment(fragment6, Constants.Pay_Bill);


        Fragment fragment13 = new RestBukLadEduFragment();
        Bundle args13 = new Bundle();
        args13.putInt(Keys.position, Constants.INT_FOURTEEN);
        args13.putString(Keys.category_id, Constants.Laundry_CategoryId);
        args13.putString(Keys.city_market_id, getIntent().getStringExtra(Keys.city_market_id));
        fragment13.setArguments(args13);
        adapter.addFragment(fragment13, Constants.Laundry);

*/
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) - 1);
        viewPager.setOffscreenPageLimit(Constants.INT_THIRTEEN);

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

        Fragment fragment4 = new RestBukLadEduFragment();
        Bundle args4 = new Bundle();
        args4.putInt(Keys.position, Constants.INT_FIVE);
        args4.putString(Keys.category_id, Constants.Restaurants_CategoryId);
        fragment4.setArguments(args4);
        adapter.addFragment(fragment4, Constants.Restaurants);

        Fragment fragment5 = new RestBukLadEduFragment();
        Bundle args5 = new Bundle();
        args5.putInt(Keys.position, Constants.INT_SIX);
        args5.putString(Keys.category_id, Constants.Bukas_CategoryId);
        fragment5.setArguments(args5);
        adapter.addFragment(fragment5, Constants.Bukas);


        Fragment fragment12 = new EventsFragment();
        Bundle args12 = new Bundle();
        args12.putInt(Keys.position, Constants.INT_SEVEN);
        args12.putString(Keys.category_id, Constants.Event_Tickets_CategoryId);
        fragment12.setArguments(args12);
        adapter.addFragment(fragment12, Constants.Event_Tickets);

        Fragment fragment7 = new GreryFashBeautyElecFragment();
        Bundle args7 = new Bundle();
        args7.putInt(Keys.position, Constants.INT_EIGHT);
        args7.putString(Keys.category_id, Constants.Education_CategoryId);
        fragment7.setArguments(args7);
        adapter.addFragment(fragment7, Constants.Boutiques);

        Fragment fragment8 = new MovieTaxiHotelFragment();
        Bundle args8 = new Bundle();
        args8.putInt(Keys.position, Constants.INT_NINE);
        args8.putString(Keys.category_id, Constants.Taxis_CategoryId);
        fragment8.setArguments(args8);
        adapter.addFragment(fragment8, Constants.Taxis);

        Fragment fragment9 = new FlightFragment();
        Bundle args9 = new Bundle();
        args9.putInt(Keys.position, Constants.INT_TEN);
        args9.putString(Keys.category_id, Constants.Flights_CategoryId);
        fragment9.setArguments(args9);
        adapter.addFragment(fragment9, Constants.Flights);

        Fragment fragment10 = new MovieTaxiHotelFragment();
        Bundle args10 = new Bundle();
        args10.putInt(Keys.position, Constants.INT_ELEVEN);
        args10.putString(Keys.category_id, Constants.Hotels_CategoryId);
        fragment10.setArguments(args10);
        adapter.addFragment(fragment10, Constants.Hotels);


        Fragment fragment11 = new MovieTaxiHotelFragment();
        Bundle args11 = new Bundle();
        args11.putInt(Keys.position, Constants.INT_TWELVE);
        args11.putString(Keys.category_id, Constants.Movie_Tickets_CategoryId);
        fragment11.setArguments(args11);
        adapter.addFragment(fragment11, Constants.Movie_Tickets);
/*
        Fragment fragment6 = new PayBillFragment();
        Bundle args6 = new Bundle();
        args6.putInt(Keys.position, Constants.INT_SEVEN);
        args6.putString(Keys.category_id, Constants.Pay_Bill_CategoryId);
        fragment6.setArguments(args6);
        adapter.addFragment(fragment6, Constants.Pay_Bill);

        Fragment fragment13 = new RestBukLadEduFragment();
        Bundle args13 = new Bundle();
        args13.putInt(Keys.position, Constants.INT_FOURTEEN);
        args13.putString(Keys.category_id, Constants.Laundry_CategoryId);
        fragment13.setArguments(args13);
        adapter.addFragment(fragment13, Constants.Laundry);
*/
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.setOffscreenPageLimit(Constants.INT_THIRTEEN);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.histroy:
                startActivity(new Intent(MainCategoryActivity.this,HistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.help:
                startActivity(new Intent(MainCategoryActivity.this,FAQActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.profile:
                startActivity(new Intent(MainCategoryActivity.this,UserProfileActivity.class));
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
