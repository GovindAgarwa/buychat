package com.buychat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.adapter.RestBukasLaundGroceryFashionBeautyAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.CustomClickListner;
import com.buychat.extras.Keys;
import com.buychat.extras.LocationSelecter;
import com.buychat.fragments.ChatView;
import com.buychat.fragments.ExploreView;
import com.buychat.fragments.LocationFragment;
import com.buychat.fragments.SearchShopFragment;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.pojos.City;
import com.buychat.pojos.ShopsPojo;
import com.buychat.singleton.DataSingleton;
import com.buychat.singleton.SocketSingleton;
import com.buychat.utils.AlertDialogManager;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,LocationSelecter,SearchShopFragment.GetKeyboard {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.city_name)
    TextView city_name;
    ArrayList<City> arrayList;

String QString = Constants.DEFAULT_STRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        search.setOnQueryTextListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        city_name.setText(BuyChat.readFromPreferences(getApplicationContext(), Keys.city_name,Constants.DEFAULT_STRING));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,new SearchShopFragment(),Constants.SearchShopFragment)
                .commit();
    }

    @OnClick(R.id.secondTab)
    public void secondTabClick(){
        arrayList = new ArrayList<>();
        arrayList = Parse.parseCity(DataSingleton.getInstance().getCityData());
        DialogFragment dialogFragment = LocationFragment.newInstance(arrayList);
        //dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager().beginTransaction(), Constants.LocationFragment);
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
        setResult(Constants.INT_ONE);
        overridePendingTransition(R.anim.push_down_in,R.anim.fade_out);
        super.onBackPressed();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String query) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(fragment instanceof SearchShopFragment){
            ((SearchShopFragment) fragment).filter(query);
        }
        QString = query;
        new android.os.Handler().removeCallbacksAndMessages(null);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment frg = null;
                frg = getSupportFragmentManager().findFragmentById(R.id.container);
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        }, 700);
        return true;

    }

    @Override
    public void setLocationText(int position) {

        if(!city_name.getText().toString().equals(arrayList.get(position).getCity_name())) {
            BuyChat.saveToPreferences(getApplicationContext(), Keys.city_id, arrayList.get(position).getId());
            BuyChat.saveToPreferences(getApplicationContext(), Keys.city_name, arrayList.get(position).getCity_name());
            BuyChat.saveToPreferences(getApplicationContext(), Keys.keyword,Constants.DEFAULT_STRING);
            city_name.setText(arrayList.get(position).getCity_name());
            Fragment frg = null;
            frg = getSupportFragmentManager().findFragmentById(R.id.container);
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }

    }


    @Override
    public void setData() {
        BuyChat.saveToPreferences(getApplicationContext(),Keys.keyword,search.getQuery().toString());
    }


    @Override
    protected void onResume() {
        super.onResume();
        BuyChat.saveToPreferences(getApplicationContext(),Keys.flat_no_floor_name,Constants.False);
    }
}
