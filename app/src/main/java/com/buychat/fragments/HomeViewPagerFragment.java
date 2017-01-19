package com.buychat.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buychat.R;
import com.buychat.activities.MainCategoryActivity;
import com.buychat.activities.MarketCategoryActivity;
import com.buychat.activities.Splash;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.City;
import com.buychat.utils.AutoScrollViewPager;
import com.buychat.utils.CirclePageIndicator;
import com.buychat.utils.GradientHalfoverImageDrawable;
import com.buychat.utils.ImageDisplayListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by nihas-mac on 25/08/2016.
 */
public class HomeViewPagerFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.pager)
    AutoScrollViewPager viewPager;
    @BindView(R.id.indicator)
    CirclePageIndicator mIndicator;
    ViewPagerAdapter adapter;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    private ImageLoadingListener imageListener;
    private GestureDetector gestureDetector;

    private int dragThreshold = 10; int downX = 0; int downY = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_viewpager_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageLoader = ImageLoader.getInstance();
        imageListener=new ImageDisplayListener();
        gestureDetector = new GestureDetector(getActivity(), new SingleTapConfirm());
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .displayer(new BitmapDisplayer() {
                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                        if(getActivity() != null) {
                            int gradientStartColor = Color.parseColor("#00000000");//argb(0, 0, 0, 0);
                            int gradientEndColor = Color.parseColor("#88000000");//argb(255, 0, 0, 0);
                            GradientHalfoverImageDrawable gradientDrawable = new GradientHalfoverImageDrawable(getResources(), bitmap);
                            gradientDrawable.setGradientColors(gradientStartColor, gradientEndColor);
                            imageAware.setImageDrawable(gradientDrawable);
                        }
                    }
                })
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo).build();

    }


    public  void settingCategoryData(final ArrayList<City> arrayList, final String category){
//        imgArray=new ArrayList<>();
//
//        imgArray.add(new Image("FASHION ONE","http://cdn.playbuzz.com/cdn/2bff0e00-cbe8-49e5-85d4-7e4c052df449/f097abfe-d3d6-42c5-9768-11616bc985e2.jpg"));
//        imgArray.add(new Image("FASHION TWO","http://www.fashiondesignerjobs.org/wp-content/uploads/2016/07/fashion-for-men.jpg"));
//        imgArray.add(new Image("FASHION THREE","http://fullhdpictures.com/wp-content/uploads/2016/01/Fashion-Wallpaper.jpg"));


        viewPager.setInterval(Constants.SPLASH_TIME_OUT);
        viewPager.startAutoScroll();
        adapter = new ViewPagerAdapter(arrayList);
        viewPager.setAdapter(adapter);
        mIndicator.setViewPager(viewPager);

//        viewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BuyChat.showAToast("jhiii");
//            }
//        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (gestureDetector.onTouchEvent(event)) {
                    // single tap
                    if(category.equals(Constants.Fashion_CategryId)){
                        startActivityForResult(new Intent(getActivity(), MarketCategoryActivity.class)
                                        .putExtra(Keys.position,Constants.INT_ONE)
                                        .putExtra(Keys.business_image,arrayList.get(viewPager.getCurrentItem()).getMarket_image())
                                        .putExtra(Keys.market_name,arrayList.get(viewPager.getCurrentItem()).getMarket_name())
                                        .putExtra(Keys.category_id,Constants.Fashion_CategryId)
                                        .putExtra(Keys.city_market_id,arrayList.get(viewPager.getCurrentItem()).getId())
                                ,Constants.INT_TWO);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else if(category.equals(Constants.Beauty_CategoryId)){
                        startActivityForResult(new Intent(getActivity(), MarketCategoryActivity.class)
                                        .putExtra(Keys.position,Constants.INT_TWO)
                                        .putExtra(Keys.business_image,arrayList.get(viewPager.getCurrentItem()).getMarket_image())
                                        .putExtra(Keys.market_name,arrayList.get(viewPager.getCurrentItem()).getMarket_name())
                                        .putExtra(Keys.category_id,Constants.Beauty_CategoryId)
                                        .putExtra(Keys.city_market_id,arrayList.get(viewPager.getCurrentItem()).getId())
                                ,Constants.INT_TWO);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else if(category.equals(Constants.Groceries_CategoryId)){
                        startActivityForResult(new Intent(getActivity(), MarketCategoryActivity.class)
                                .putExtra(Keys.position,Constants.INT_THREE)
                                .putExtra(Keys.business_image,arrayList.get(viewPager.getCurrentItem()).getMarket_image())
                                .putExtra(Keys.market_name,arrayList.get(viewPager.getCurrentItem()).getMarket_name())
                                .putExtra(Keys.city_market_id,arrayList.get(viewPager.getCurrentItem()).getId())
                                .putExtra(Keys.category_id,Constants.Groceries_CategoryId),Constants.INT_TWO);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else if(category.equals(Constants.Electronics_CategoryId)){
                        startActivityForResult(new Intent(getActivity(), MarketCategoryActivity.class)
                                .putExtra(Keys.position,Constants.INT_FOUR)
                                .putExtra(Keys.business_image,arrayList.get(viewPager.getCurrentItem()).getMarket_image())
                                .putExtra(Keys.market_name,arrayList.get(viewPager.getCurrentItem()).getMarket_name())
                                .putExtra(Keys.city_market_id,arrayList.get(viewPager.getCurrentItem()).getId())
                                .putExtra(Keys.category_id,Constants.Electronics_CategoryId),Constants.INT_TWO);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                } else {
                    // your code for move and drag
                    // How far the user has to scroll before it locks the parent vertical scrolling.
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP){
                        downX = (int) event.getRawX();
                        downY = (int) event.getRawY();
                        return false;
                    }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                        int distanceX = Math.abs((int) event.getRawX() - downX);
                        int distanceY = Math.abs((int) event.getRawY() - downY);

                        Log.e("OnTouchListener", "distance X : " + distanceX + " , distance Y : " + distanceY);

                        if(distanceY > distanceX && distanceY > dragThreshold){
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                        }else if(distanceX==0 && distanceY==0){

                        }
                    }
                }

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


    }

    public class ViewPagerAdapter extends PagerAdapter {

        private ArrayList<City> iconResId;
//        private String cats;

        public ViewPagerAdapter(ArrayList<City> iconResId) {

            this.iconResId = iconResId;
//            this.cats = category;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return iconResId.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View itemView = getActivity().getLayoutInflater().inflate(R.layout.home_viewpager_item, container, false);

            ImageView iconView = (ImageView) itemView.findViewById(R.id.landing_img_slide);
            TextView title = (TextView)itemView.findViewById(R.id.title_market);

            imageLoader.displayImage(iconResId.get(position).getMarket_image(), iconView, options,imageListener);

            title.setText(iconResId.get(position).getMarket_name());
            itemView.setId(position);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);

        }
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
}
