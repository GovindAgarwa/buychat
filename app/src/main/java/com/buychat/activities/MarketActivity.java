package com.buychat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.adapter.BrandsAdapter;
import com.buychat.adapter.MarketAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.listners.EndlessRecyclerOnScrollListener;
import com.buychat.listners.RecyclerItemClickListener;
import com.buychat.pojos.City;
import com.buychat.singleton.SocketSingleton;
import com.buychat.utils.AlertDialogManager;
import com.buychat.utils.GradientHalfoverImageDrawable;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nihas-mac on 18/08/2016.
 */
public class MarketActivity extends AppCompatActivity implements Callback<JsonObject> {

    @BindView(R.id.recycler_views)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)Toolbar toolbar;
    LinearLayoutManager linearLayoutManager;
    ImageLoader imageLoader;
    ImageDisplayListener imageListener;
    DisplayImageOptions options;
    ArrayList<City> arrayList;
    String city_id;
    private ProgressDialog dialog;
    private EndlessRecyclerOnScrollListener scrolllistner;
    @BindView(R.id.empty)
    TextView empty;
    MarketAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

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
                .cacheOnDisc(true).resetViewBeforeLoading(true).build();

        initializeRecyclerView();
    }

    private void initializeRecyclerView(){
        dialog = new ProgressDialog(MarketActivity.this, R.style.MyTheme);
        AlertDialogManager.showDialog(dialog);
        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.getCityMarket(Parse.getCityIdWithLimitOffset(Constants.DEFAULT_INT,Constants.INT_TEN));
        call.enqueue(this);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(arrayList.get(position).getCategory_id().equals(Constants.Fashion_CategryId)){
                    startActivityForResult(new Intent(getApplicationContext(), MarketCategoryActivity.class)
                                    .putExtra(Keys.position,Constants.INT_ONE)
                                    .putExtra(Keys.business_image,arrayList.get(position).getMarket_image())
                                    .putExtra(Keys.market_name,arrayList.get(position).getMarket_name())
                                    .putExtra(Keys.category_id,Constants.Fashion_CategryId)
                                    .putExtra(Keys.city_market_id,arrayList.get(position).getId())
                            ,Constants.INT_TWO);
                   overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else if(arrayList.get(position).getCategory_id().equals(Constants.Beauty_CategoryId)){

                    startActivityForResult(new Intent(getApplicationContext(), MarketCategoryActivity.class)
                                    .putExtra(Keys.position,Constants.INT_TWO)
                                    .putExtra(Keys.business_image,arrayList.get(position).getMarket_image())
                                    .putExtra(Keys.market_name,arrayList.get(position).getMarket_name())
                                    .putExtra(Keys.category_id,Constants.Beauty_CategoryId)
                                    .putExtra(Keys.city_market_id,arrayList.get(position).getId())
                            ,Constants.INT_TWO);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else if(arrayList.get(position).getCategory_id().equals(Constants.Groceries_CategoryId)){
                    startActivityForResult(new Intent(getApplicationContext(), MarketCategoryActivity.class)
                                    .putExtra(Keys.position,Constants.INT_THREE)
                                    .putExtra(Keys.business_image,arrayList.get(position).getMarket_image())
                                    .putExtra(Keys.market_name,arrayList.get(position).getMarket_name())
                                    .putExtra(Keys.category_id,Constants.Groceries_CategoryId)
                                    .putExtra(Keys.city_market_id,arrayList.get(position).getId())
                            ,Constants.INT_TWO);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else if(arrayList.get(position).getCategory_id().equals(Constants.Electronics_CategoryId)){
                    startActivityForResult(new Intent(getApplicationContext(), MarketCategoryActivity.class)
                                    .putExtra(Keys.position,Constants.INT_FOUR)
                                    .putExtra(Keys.business_image,arrayList.get(position).getMarket_image())
                                    .putExtra(Keys.market_name,arrayList.get(position).getMarket_name())
                                    .putExtra(Keys.category_id,Constants.Electronics_CategoryId)
                                    .putExtra(Keys.city_market_id,arrayList.get(position).getId())
                            ,Constants.INT_TWO);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }


            }
        }));
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        arrayList = new ArrayList<>();
        AlertDialogManager.dismissDialog(dialog);
        if(response.code() == Constants.SUCCESS) {
            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                arrayList = new ArrayList<>();
                arrayList = Parse.parseCityMarket(response.body().toString());
            }
        }else {
            BuyChat.showAToast(Constants.Some_Went_Wrong);
        }

        scrolllistner =  new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d(Keys.position," "+current_page);
                ScrollApiCall(current_page);
            }
        };

        recyclerView.addOnScrollListener(scrolllistner);
        adapter = new MarketAdapter(getApplicationContext(),arrayList,imageLoader,imageListener,options);
        recyclerView.setAdapter(adapter);
        if(arrayList.size() != 0) {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            adapter.enableFooter(false);
            adapter.notifyDataSetChanged();
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    private void ScrollApiCall(int current_page) {
        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.getCityMarket(Parse.getCityIdWithLimitOffset(Constants.INT_TEN*current_page+Constants.INT_ONE,Constants.INT_TEN));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ArrayList<City> array = new ArrayList<>();
                if(response.code() == Constants.SUCCESS) {
                    if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                        array = Parse.parseCityMarket(response.body().toString());
                    }
                }
                if(array.size() != 0) {
                    arrayList.addAll(array);
                    adapter.notifyDataSetChanged();
                }else{
                    adapter.enableFooter(false);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {

    }


    public static class ImageDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
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
        setResult(Constants.INT_THREE);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @Override
    protected void onResume() {
        super.onResume();
        BuyChat.saveToPreferences(getApplicationContext(),Keys.flat_no_floor_name,Constants.False);
    }

}
