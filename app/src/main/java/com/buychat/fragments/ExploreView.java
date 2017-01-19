package com.buychat.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buychat.activities.BrandProfileActivity;
import com.buychat.activities.BrandsActivity;
import com.buychat.activities.MainCategoryActivity;
import com.buychat.activities.MarketActivity;
import com.buychat.R;
import com.buychat.adapter.BrandsAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.BrandsPojo;
import com.buychat.pojos.City;
import com.buychat.pojos.Image;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.AutoScrollViewPager;
import com.buychat.utils.CirclePageIndicator;
import com.buychat.utils.GradientHalfoverImageDrawable;
import com.buychat.utils.fonts.TextViewRegular;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.aakira.expandablelayout.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreView extends Fragment{

    private Unbinder unbinder;
//    @BindView(R.id.pager)
//    AutoScrollViewPager viewPager;
//    @BindView(R.id.indicator)
//    CirclePageIndicator mIndicator;
    String GREEN="green";
    String GREY="grey";
//    @BindView(R.id.radioTopGroup)
//    RadioGroup radioTopGroup;
//    @BindView(R.id.fashion_radio) RadioButton fashion;
//    @BindView(R.id.beauty_radio) RadioButton beauty;
//    @BindView(R.id.grocery_radio) RadioButton grocery;
//    @BindView(R.id.electronics_radio) RadioButton electronics;
    @BindView(R.id.relative) RelativeLayout relative;
    @BindView(R.id.down_arrow_beauty) LinearLayout downArrowBeauty;
    @BindView(R.id.down_arrow_electronics) LinearLayout downArrowElectronics;
    @BindView(R.id.down_arrow_fashion) LinearLayout downArrowFashion;
    @BindView(R.id.down_arrow_grocery) LinearLayout downArrowGrocery;
    @BindView(R.id.linear) LinearLayout main;

    @BindView(R.id.fashion_text) TextViewRegular fashionText;
    @BindView(R.id.beauty_text) TextViewRegular beautyText;
    @BindView(R.id.grocery_text) TextViewRegular groceyText;
    @BindView(R.id.elecs_text) TextViewRegular elecsText;
    @BindView(R.id.fashion_img) ImageView fashionImg;
    @BindView(R.id.beauty_img) ImageView beautyImg;
    @BindView(R.id.grocery_img) ImageView groceyImg;
    @BindView(R.id.elecs_img) ImageView elecsImg;
    @BindView(R.id.one) LinearLayout fashionLayout;
    @BindView(R.id.two) LinearLayout beautyLayout;
    @BindView(R.id.three) LinearLayout groceyLayout;
    @BindView(R.id.four) LinearLayout elecsLayout;
    @BindView(R.id.expandableLayout)public ExpandableRelativeLayout expandableLayout;

    @BindView(R.id.brandss) LinearLayout linear;
    ArrayList<BrandsPojo> arrayList;

    ImageLoader imageLoader;
    DisplayImageOptions options;
    private ImageLoadingListener imageListener;
    ArrayList<City> imgArray;
    @BindView(R.id.brand_list)HorizontalScrollView brandList;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.explore_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandableLayout.setInterpolator(Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR));

        if(expandableLayout.isExpanded())
            expandableLayout.collapse();
//        expandableLayout.collapse();
//        relative.setVisibility(View.GONE);
        imageLoader = ImageLoader.getInstance();
        imageListener=new ImageDisplayListener();
        main.setVisibility(View.GONE);
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
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();


        ApiCategoryCall(Constants.Fashion_CategryId);
        downArrowFashion.setVisibility(View.VISIBLE);
        downArrowBeauty.setVisibility(View.INVISIBLE);
        downArrowElectronics.setVisibility(View.INVISIBLE);
        downArrowGrocery.setVisibility(View.INVISIBLE);
        settingMiddleData(DataSingleton.getInstance().getArrayBrandsList());
        if(getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.viewpager_fragment, new HomeViewPagerFragment())
                    .commit();
        }

    }

    private void ApiCategoryCall(final String category){
        expandableLayout.setExpanded(false);
        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.getCityMarket(Parse.getCityIdAndCategoryId(category));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == Constants.SUCCESS) {
                    if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                        imgArray = new ArrayList<>();
                        imgArray = Parse.parseCityMarket(response.body().toString());
                        if(imgArray.size() != 0){


//                            relative.setVisibility(View.VISIBLE);
                            expandableLayout.expand();

                            Log.e("GGG","TOGGLED ONE");
//                            settingCategoryData(imgArray);

                            if( getActivity() != null) {
                                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.viewpager_fragment);
                                if (fragment instanceof HomeViewPagerFragment) {
                                    ((HomeViewPagerFragment) fragment).settingCategoryData(imgArray, category);
                                }
                            }
                        }
                        else{

                            expandableLayout.collapse();
//                            relative.setVisibility(View.GONE);
                            Log.e("GGG","TOGGLED TWO");
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    private void settingMiddleData(final ArrayList<BrandsPojo> arrayList) {


        if(getActivity() != null) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;


            linear.removeAllViews();
            if (arrayList.size() != 0) {
                main.setVisibility(View.VISIBLE);
                for (int i = 0; i < arrayList.size(); i++) {
                    LinearLayout layout2 = new LinearLayout(getActivity());
                    layout2.setLayoutParams(new LinearLayout.LayoutParams(width / 3, width / 3 + 10));
                    layout2.setOrientation(LinearLayout.VERTICAL);
                    layout2.setGravity(Gravity.CENTER);
                    layout2.setPadding(5, 5, 5, 5);
                    layout2.setId(i);
                    layout2.setBackgroundColor(Color.parseColor("#ffffff"));
                    layout2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BrandsPojo pojo = arrayList.get(v.getId());
                            Gson gson = new Gson();
                            String myJson = gson.toJson(pojo);
                            startActivityForResult(new Intent(getActivity(), BrandProfileActivity.class).putExtra(Keys.data, myJson), Constants.INT_TWO);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });

                    final ImageView imageView = new ImageView(getActivity());

                    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(140, 140);
                    imageView.setPadding(20, 10, 20, 5);
                    imageLoader.displayImage(arrayList.get(i).getBusiness_image(), imageView, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            super.onLoadingStarted(imageUri, view);
                            imageView.setImageResource(R.drawable.empty_logo_image);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            FadeInBitmapDisplayer.animate(imageView, 500);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            super.onLoadingFailed(imageUri, view, failReason);
                            imageView.setImageResource(R.drawable.empty_logo_image);
                        }
                    });
//                imageView.setImageResource(R.drawable.airtel);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(vp);
                    layout2.addView(imageView);

                    TextViewRegular tv = new TextViewRegular(getActivity());
                    LinearLayout.LayoutParams vps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    vps.gravity = Gravity.CENTER;

                    tv.setText(arrayList.get(i).getBusiness_name());
                    tv.setTextSize(12);
                    tv.setTextColor(getResources().getColor(R.color.black));
                    tv.setLayoutParams(vps);
                    layout2.addView(tv);

                    linear.addView(layout2);

                    View view = new View(getActivity());
                    view.setBackgroundColor(getResources().getColor(R.color.greytext));
                    LinearLayout.LayoutParams viewvp = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
                    view.setLayoutParams(viewvp);
                    linear.addView(view);
                }

            } else {
                main.setVisibility(View.GONE);

            }
        }
    }


//    @Override
//    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//        if (checkedId == R.id.fashion_radio) {
//            //some code
//            fashion.setTextColor(getResources().getColor(R.color.colorPrimary));
//            grocery.setTextColor(getResources().getColor(R.color.black));
//            beauty.setTextColor(getResources().getColor(R.color.black));
//            electronics.setTextColor(getResources().getColor(R.color.black));
//            downArrowFashion.setVisibility(View.VISIBLE);
//            downArrowBeauty.setVisibility(View.INVISIBLE);
//            downArrowElectronics.setVisibility(View.INVISIBLE);
//            downArrowGrocery.setVisibility(View.INVISIBLE);
////            imgArray.clear();
////            imgArray.add(new Image("FASHION ONE","http://cdn.playbuzz.com/cdn/2bff0e00-cbe8-49e5-85d4-7e4c052df449/f097abfe-d3d6-42c5-9768-11616bc985e2.jpg"));
////            imgArray.add(new Image("FASHION TWO","http://www.fashiondesignerjobs.org/wp-content/uploads/2016/07/fashion-for-men.jpg"));
////            imgArray.add(new Image("FASHION THREE","http://fullhdpictures.com/wp-content/uploads/2016/01/Fashion-Wallpaper.jpg"));
////            viewPager.getAdapter().notifyDataSetChanged();
//              ApiCategoryCall(Constants.Fashion_CategryId);
//
//        } else if (checkedId == R.id.grocery_radio) {
//            //some code
//            fashion.setTextColor(getResources().getColor(R.color.black));
//            grocery.setTextColor(getResources().getColor(R.color.colorPrimary));
//            beauty.setTextColor(getResources().getColor(R.color.black));
//            electronics.setTextColor(getResources().getColor(R.color.black));
//            downArrowFashion.setVisibility(View.INVISIBLE);
//            downArrowBeauty.setVisibility(View.INVISIBLE);
//            downArrowElectronics.setVisibility(View.INVISIBLE);
//            downArrowGrocery.setVisibility(View.VISIBLE);
////            imgArray.clear();
////            imgArray.add(new Image("GROCERY ONE","https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQMo4ABdfi_jV88osqeiEqaUdghLNtiq5ANZfzUPjazBgRyKw-P"));
////            imgArray.add(new Image("GROCERY TWO","https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRLBXaH-Usi9nbB1ySb00X0UfHE-HTd6V6Hwnvk-gtie6ROeFNT"));
////            imgArray.add(new Image("GROCERY THREE","http://1.bp.blogspot.com/-cVw29UUtpg0/Ttd-ur7l9YI/AAAAAAAAABY/cGCGMfolD-I/s1600/product_list_for_website+copy.jpg"));
////            imgArray.add(new Image("GROCERY FOUR","https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQhYqSwrLueE8hWFblA9_jJ6FW74ETu8JMgU2km4jmoqHebA0QewA"));
////            viewPager.getAdapter().notifyDataSetChanged();
//            ApiCategoryCall(Constants.Groceries_CategoryId);
//
//        }else if(checkedId ==R.id.beauty_radio){
//            fashion.setTextColor(getResources().getColor(R.color.black));
//            grocery.setTextColor(getResources().getColor(R.color.black));
//            beauty.setTextColor(getResources().getColor(R.color.colorPrimary));
//            electronics.setTextColor(getResources().getColor(R.color.black));
//            downArrowFashion.setVisibility(View.INVISIBLE);
//            downArrowBeauty.setVisibility(View.VISIBLE);
//            downArrowElectronics.setVisibility(View.INVISIBLE);
//            downArrowGrocery.setVisibility(View.INVISIBLE);
////            imgArray.clear();
////            imgArray.add(new Image("BEAUTY ONE","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQn8U3WHFNtDKl9BjEf4mrTNnWvkppa9M7dvOUSXlqB2pkLENX37w"));
////            imgArray.add(new Image("BEAUTY TWO","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNRy3QOxId_Oa9EhS3adn9ebJp7SmZGSpbJeLYIJzox-yxdn2kTw"));
////            imgArray.add(new Image("BEAUTY THREE","http://blog.getdistributors.com/wp-content/uploads/2014/04/beauty-products-sales-representatives.jpg"));
////            viewPager.getAdapter().notifyDataSetChanged();
//            ApiCategoryCall(Constants.Beauty_CategoryId);
//
//        }else if(checkedId==R.id.electronics_radio){
//            fashion.setTextColor(getResources().getColor(R.color.black));
//            grocery.setTextColor(getResources().getColor(R.color.black));
//            beauty.setTextColor(getResources().getColor(R.color.black));
//            electronics.setTextColor(getResources().getColor(R.color.colorPrimary));
//            downArrowFashion.setVisibility(View.INVISIBLE);
//            downArrowBeauty.setVisibility(View.INVISIBLE);
//            downArrowElectronics.setVisibility(View.VISIBLE);
//            downArrowGrocery.setVisibility(View.INVISIBLE);
//
////            imgArray.clear();
////            imgArray.add(new Image("ELECS ONE","http://ashrafelectronics.com/shop/img/p/20-152-large.jpg"));
////            imgArray.add(new Image("ELECS TWO","http://www.why.do/wp-content/uploads/2013/08/Why-do-people-waste-money-on-apple-products.jpg"));
////            imgArray.add(new Image("ELECS THREE","https://media.licdn.com/mpr/mpr/AAEAAQAAAAAAAARmAAAAJDViYTJjZmM1LTkxNzMtNDRiYS1iNDM0LWU1MDA5MGU3MzFmMQ.png"));
////            viewPager.getAdapter().notifyDataSetChanged();
//            ApiCategoryCall(Constants.Electronics_CategoryId);
//
//        }
//    }


    private static class ImageDisplayListener extends SimpleImageLoadingListener {

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


    @OnClick(R.id.fashion)
    public void FashionClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                        .putExtra(Keys.position,Constants.INT_ONE)
                        .putExtra(Keys.category_id,Constants.Fashion_CategryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.beauty)
    public void BeautyClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                        .putExtra(Keys.position,Constants.INT_TWO)
                        .putExtra(Keys.category_id,Constants.Beauty_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.grocery)
    public void GroceryClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_THREE)
                .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                .putExtra(Keys.category_id,Constants.Groceries_CategoryId),Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.electronics)
    public void ElectronicsClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_FOUR)
                .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                .putExtra(Keys.category_id,Constants.Electronics_CategoryId),Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.restaurants)
    public void RestaurantClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_FIVE)
                .putExtra(Keys.category_id,Constants.Restaurants_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.bukas)
    public void BukasClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_SIX)
                .putExtra(Keys.category_id,Constants.Bukas_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.payBill)
    public void payBillClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_SEVEN)
                .putExtra(Keys.category_id,Constants.Pay_Bill_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.education)
    public void educationClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_EIGHT)
                .putExtra(Keys.category_id,Constants.Education_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.taxis)
    public void taxisClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_NINE)
                .putExtra(Keys.category_id,Constants.Taxis_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.flight)
    public void flightClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_TEN)
                .putExtra(Keys.category_id,Constants.Flights_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.hotels)
    public void hotelsClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_ELEVEN)
                .putExtra(Keys.category_id,Constants.Hotels_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.movieTicket)
    public void movieTicketClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_TWELVE)
                .putExtra(Keys.category_id,Constants.Movie_Tickets_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.event_ticket)
    public void event_ticketClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_SEVEN)
                .putExtra(Keys.category_id,Constants.Event_Tickets_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.laundry)
    public void laundryClick(){
        startActivityForResult(new Intent(getActivity(), MainCategoryActivity.class)
                .putExtra(Keys.position,Constants.INT_FOURTEEN)
                .putExtra(Keys.category_id,Constants.Laundry_CategoryId)
                        .putExtra(Keys.city_market_id,Constants.DEFAULT_STRING)
                ,Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.view_all_market)
    public void viewMarkets(){
        startActivityForResult(new Intent(getActivity(), MarketActivity.class),Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.view_all_brands)
    public void viewBrands(){
        startActivityForResult(new Intent(getActivity(), BrandsActivity.class),Constants.INT_TWO);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

//    @OnClick(R.id.brandss)
//    public void toBrandProfile(){
//        startActivityForResult(new Intent(getActivity(), BrandProfileActivity.class));
//        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//    }

    @OnClick(R.id.one)
    public void fashionClick(){
        clickedCat(1,fashionLayout.getTag().toString());
    }

    @OnClick(R.id.two)
    public void beautyClick(){
       clickedCat(2,beautyLayout.getTag().toString());
    }

    @OnClick(R.id.three)
    public void groceryClick(){
        clickedCat(3,groceyLayout.getTag().toString());
    }

    @OnClick(R.id.four)
    public void elecsClick(){
        clickedCat(4,elecsLayout.getTag().toString());
    }


    public void clickedCat(int clickedpos,String tagname){
        if(clickedpos==1){
            if(tagname.equals(GREY)){
                //1
                fashionImg.setImageResource(R.drawable.fashion_green_new);
                fashionText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                fashionLayout.setTag(GREEN);

                elecsImg.setImageResource(R.drawable.elec_grey_new);
                elecsText.setTextColor(getResources().getColor(R.color.black));
                elecsLayout.setTag(GREY);

                groceyImg.setImageResource(R.drawable.grocery_grey_new);
                groceyText.setTextColor(getResources().getColor(R.color.black));
                groceyLayout.setTag(GREY);

                beautyImg.setImageResource(R.drawable.beauty_grey_new);
                beautyText.setTextColor(getResources().getColor(R.color.black));
                beautyLayout.setTag(GREY);

                downArrowFashion.setVisibility(View.VISIBLE);
                downArrowBeauty.setVisibility(View.INVISIBLE);
                downArrowElectronics.setVisibility(View.INVISIBLE);
                downArrowGrocery.setVisibility(View.INVISIBLE);

                ApiCategoryCall(Constants.Fashion_CategryId);
            }
        }else if(clickedpos==2){

            if(tagname.equals(GREY)){
                fashionImg.setImageResource(R.drawable.fashion_grey_new);
                fashionText.setTextColor(getResources().getColor(R.color.black));
                fashionLayout.setTag(GREY);

                elecsImg.setImageResource(R.drawable.elec_grey_new);
                elecsText.setTextColor(getResources().getColor(R.color.black));
                elecsLayout.setTag(GREY);

                groceyImg.setImageResource(R.drawable.grocery_grey_new);
                groceyText.setTextColor(getResources().getColor(R.color.black));
                groceyLayout.setTag(GREY);

                //2
                beautyImg.setImageResource(R.drawable.beauty_green_new);
                beautyText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                beautyLayout.setTag(GREEN);

                downArrowFashion.setVisibility(View.INVISIBLE);
                downArrowBeauty.setVisibility(View.VISIBLE);
                downArrowElectronics.setVisibility(View.INVISIBLE);
                downArrowGrocery.setVisibility(View.INVISIBLE);

                ApiCategoryCall(Constants.Beauty_CategoryId);
            }

        }else if(clickedpos==3){

            if(tagname.equals(GREY)){
                fashionImg.setImageResource(R.drawable.fashion_grey_new);
                fashionText.setTextColor(getResources().getColor(R.color.black));
                fashionLayout.setTag(GREY);

                elecsImg.setImageResource(R.drawable.elec_grey_new);
                elecsText.setTextColor(getResources().getColor(R.color.black));
                elecsLayout.setTag(GREY);

                //3
                groceyImg.setImageResource(R.drawable.grocery_green_new);
                groceyText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                groceyLayout.setTag(GREEN);

                beautyImg.setImageResource(R.drawable.beauty_grey_new);
                beautyText.setTextColor(getResources().getColor(R.color.black));
                beautyLayout.setTag(GREY);

                downArrowFashion.setVisibility(View.INVISIBLE);
                downArrowBeauty.setVisibility(View.INVISIBLE);
                downArrowElectronics.setVisibility(View.INVISIBLE);
                downArrowGrocery.setVisibility(View.VISIBLE);

                ApiCategoryCall(Constants.Groceries_CategoryId);
            }

        }else if(clickedpos==4){

            if(tagname.equals(GREY)){
                fashionImg.setImageResource(R.drawable.fashion_grey_new);
                fashionText.setTextColor(getResources().getColor(R.color.black));
                fashionLayout.setTag(GREY);

                //4
                elecsImg.setImageResource(R.drawable.elec_green_new);
                elecsText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                elecsLayout.setTag(GREEN);

                groceyImg.setImageResource(R.drawable.grocery_grey_new);
                groceyText.setTextColor(getResources().getColor(R.color.black));
                groceyLayout.setTag(GREY);

                beautyImg.setImageResource(R.drawable.beauty_grey_new);
                beautyText.setTextColor(getResources().getColor(R.color.black));
                beautyLayout.setTag(GREY);

                downArrowFashion.setVisibility(View.INVISIBLE);
                downArrowBeauty.setVisibility(View.INVISIBLE);
                downArrowElectronics.setVisibility(View.VISIBLE);
                downArrowGrocery.setVisibility(View.INVISIBLE);

                ApiCategoryCall(Constants.Electronics_CategoryId);
            }

        }
    }



}