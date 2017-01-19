package com.buychat.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.api.Parse;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.BrandsPojo;
import com.buychat.pojos.ShopsPojo;
import com.buychat.singleton.DataSingleton;
import com.buychat.singleton.SocketSingleton;
import com.buychat.utils.BlurBuilder;
import com.buychat.utils.fonts.TextViewRegular;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nihas-mac on 19/08/2016.
 */
public class BrandProfileActivity extends AppCompatActivity
        {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.buy_chat_id)TextViewRegular buyId;
    @BindView(R.id.desc)TextViewRegular desc;
    @BindView(R.id.imageView)ImageView imageMain;
    @BindView(R.id.background_image)ImageView backImage;
    @BindView(R.id.facebook)ImageView facebook;
    @BindView(R.id.twitter)ImageView twitter;
    @BindView(R.id.linkedin)ImageView linkedin;
    @BindView(R.id.social_media)LinearLayout socialLayout;
    @BindView(R.id.video_layout) TextView videoLayout;
    @BindView(R.id.player_view)
    WebView webView;
    @BindView(R.id.description_layout)LinearLayout descLayout;
    @BindView(R.id.website_layout)LinearLayout websiteLayout;

    @BindView(R.id.social_view)View socialView;
    @BindView(R.id.video_view )View videoView;
    @BindView(R.id.description_view)View descView;
    @BindView(R.id.website_view)View websiteView;


    @BindView(R.id.official_website)TextViewRegular officialWebsite;
    String fb,twt,linkd;
    String youtubeUrl;
    ImageLoader imageLoader;
    BrandsPojo pojo;

//    @BindView(R.id.player_view)
//    YouTubePlayerView playerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brand_profile_layout);
        ButterKnife.bind(this);

        imageLoader = ImageLoader.getInstance();
        // initializes the YouTube player view
//        playerView.initialize(Constants.YOUTUBE_API_KEY, this);

//        YouTubePlayerSupportFragment frag =
//                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
//        frag.initialize(Constants.YOUTUBE_API_KEY, this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int w1 = (int) (metrics.widthPixels / metrics.density), h1 = w1 * 3 / 6;
//        android.view.ViewGroup.LayoutParams layoutParams = imageMain.getLayoutParams();
//        layoutParams.width = w1;
//        layoutParams.height = h1;
//        imageMain.setLayoutParams(layoutParams);
//        imageMain.setAdjustViewBounds(true);
//        imageMain.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if(getIntent() != null){

            Gson gson = new Gson();
            pojo = gson.fromJson(getIntent().getStringExtra(Keys.data), BrandsPojo.class);
            Log.i("Ok",pojo.getBusiness_name());
            setYoutube();
            setTitle(pojo.getBusiness_name());
            getSupportActionBar().setSubtitle(pojo.getBusiness_address());
            buyId.setText(pojo.getMobile());
            desc.setText(pojo.getBusiness_description());
            officialWebsite.setText(pojo.getWebsite_url());
            fb=pojo.getFacebook_url();
            twt=pojo.getTwitter_url();
            linkd=pojo.getLinkedin_url();
            youtubeUrl=pojo.getVideo_url();

            if(TextUtils.isEmpty(fb)){
                facebook.setVisibility(View.GONE);
            }

            if(TextUtils.isEmpty(twt)){
                twitter.setVisibility(View.GONE);
            }

            if(TextUtils.isEmpty(linkd)){
                linkedin.setVisibility(View.GONE);
            }

            if(TextUtils.isEmpty(pojo.getVideo_url())) {
                videoLayout.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
            }
            if(TextUtils.isEmpty(pojo.getWebsite_url())) {
                websiteView.setVisibility(View.GONE);
                websiteLayout.setVisibility(View.GONE);
            }
            if(TextUtils.isEmpty(pojo.getBusiness_description())) {
                descView.setVisibility(View.GONE);
                descLayout.setVisibility(View.GONE);
            }
            if(TextUtils.isEmpty(fb)&&TextUtils.isEmpty(twt)&&TextUtils.isEmpty(twt)) {
                socialLayout.setVisibility(View.GONE);
                socialView.setVisibility(View.GONE);
            }

            imageLoader.displayImage(pojo.getBusiness_image(),imageMain,new SimpleImageLoadingListener(){
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    super.onLoadingStarted(imageUri, view);
                    imageMain.setImageResource(R.drawable.empty_logo_image);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    FadeInBitmapDisplayer.animate(imageMain, 500);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    super.onLoadingFailed(imageUri, view, failReason);
                    imageMain.setImageResource(R.drawable.empty_logo_image);
                }
            });

            imageLoader.displayImage(pojo.getBusiness_image(), backImage, new SimpleImageLoadingListener() {

                final List<String> displayedImages = Collections
                        .synchronizedList(new LinkedList<String>());

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    super.onLoadingStarted(imageUri, view);
                    imageMain.setImageResource(R.drawable.empty_photo);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    if (loadedImage != null) {
                        ImageView imageView = (ImageView) view;
                        boolean firstDisplay = !displayedImages.contains(imageUri);
                        if (firstDisplay) {
                            FadeInBitmapDisplayer.animate(imageView, 500);
                            displayedImages.add(imageUri);

//                                            Bitmap image = BlurBuilder.blur(view);
//                                            business_background.setImageDrawable(new BitmapDrawable(getResources(), image));
                        }
                    }
                }
            });

        }
    }

//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
//        if (!wasRestored) {
//            player.cueVideo(youtubeUrl);
//            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//        }
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorResult) {
//// shows dialog if user interaction may fix the error
//        if (errorResult.isUserRecoverableError()) {
//            errorResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
//        }
//        else {
//            // displays the error occured during the initialization
//            String error = String.format(
//                    getString(R.string.error_string), errorResult.toString());
//            BuyChat.showAToast(error);
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RECOVERY_DIALOG_REQUEST) {
//
//            // initializes the YouTube player view
//            getYouTubePlayerProvider().initialize(Constants.YOUTUBE_API_KEY, this);
//        }
//    }
//
//    // Returns Player view defined in xml file
//    private YouTubePlayer.Provider getYouTubePlayerProvider() {
//        return (YouTubePlayerView) findViewById(R.id.player_view);
//    }

    /**
     *
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }

    @OnClick(R.id.facebook)
    public void fbClick(){
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(fb));
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.twitter)
    public void twitClick(){
    try{
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(twt));
        startActivity(i);
    }catch (Exception e){
        e.printStackTrace();
    }
    }

    @OnClick(R.id.linkedin)
    public void linkedClick(){
    try{
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(linkd));
        startActivity(i);
    }catch (Exception e){
        e.printStackTrace();
    }
    }


    @OnClick(R.id.shop_layout)
    public void shopClick(){

        ArrayList<ShopsPojo> shopsPojos= new ArrayList<>();
        ShopsPojo pojo1 = new ShopsPojo();
        pojo1.setId(pojo.getId());
        pojo1.setBusiness_name(pojo.getBusiness_name());
        pojo1.setMerchant_name(pojo.getMerchant_name());
        pojo1.setCategory_master_id(Constants.Brands_CategryId);
        pojo1.setPackage_value("0");
        pojo1.setDelivery_charge("0");
        pojo1.setProduct_count(pojo.getProduct_count());
        pojo1.setMin_order_value("0");
        pojo1.setPayment_type(new ArrayList<String>());
        pojo1.setOrder_type(new ArrayList<String>());
        shopsPojos.add(pojo1);

        System.out.println("package_value : 0");
        System.out.println("delivery_charge : 0");
        BuyChat.saveToPreferences(BuyChat.getAppContext(), Keys.package_value, "0");
        BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.delivery_charge,"0");

        BuyChat.saveToPreferences(getApplicationContext(),Keys.merchant_id,pojo.getId());
        BuyChat.saveToPreferences(getApplicationContext(),Keys.business_name,pojo.getBusiness_name());
        BuyChat.saveToPreferences(getApplicationContext(),Keys.merchant_image,pojo.getBusiness_image());


        DataSingleton.getInstance().setData(shopsPojos,Constants.DEFAULT_INT);
        if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Fashion_CategryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_ONE)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Beauty_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_TWO)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Groceries_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_THREE)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Electronics_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FOUR)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Restaurants_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FIVE)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Bukas_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_SIX)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Pay_Bill_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_SEVEN)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Education_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_EIGHT)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Taxis_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_NINE)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Flights_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_TEN)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Hotels_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_ELEVEN)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Movie_Tickets_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_TWELVE)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Event_Tickets_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_THIRTEEN)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Laundry_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FOURTEEN)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Brands_CategryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_FIFTEEN)
                    .putExtra(Keys.brandposition,Constants.INT_TWO));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        
    }


    private void setYoutube(){
        try {
            String item = "http://www.youtube.com/embed/";
            System.out.println("Youtube Url "+pojo.getVideo_url());

            String ss = pojo.getVideo_url();
            ss = ss.substring(ss.indexOf("v=") + 2);
            item += ss;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int w1 = (int) (metrics.widthPixels / metrics.density), h1 = w1 * 3 / 5;

            String frameVideo = "<html><body><iframe class=\"youtube-player\" type=\"text/html5\" width=\""
                    + (w1 - 20)
                    + "\" height=\""
                    + h1 + "\" src=\""
                    + item
                    + "\" frameborder=\"0\"\"allowfullscreen\"></iframe>";

            //String frameVideo = "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/_TGl-r8_uUw\" frameborder=\"0\" allowfullscreen></iframe>";
            webView.loadData(frameVideo, "text/html", "utf-8");
            webView.getSettings().setJavaScriptEnabled(true);
//        String item = "http://www.youtube.com/embed/";
//
//        String ss = "https://www.youtube.com/watch?v=3ZKzByjI7uM";
//        ss = ss.substring(ss.indexOf("v=") + 2);
//        item += ss;
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int w1 = (int) (metrics.widthPixels / metrics.density), h1 = w1 * 3 / 5;
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebChromeClient(chromeClient);
//      //  webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//
//        try {
//            webView.loadData(
//                    ,
//                    "text/html5", "utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (view instanceof FrameLayout) {
                FrameLayout frame = (FrameLayout) view;
                if (frame.getFocusedChild() instanceof VideoView) {
                    VideoView video = (VideoView) frame.getFocusedChild();
                    frame.removeView(video);
                    video.start();
                }
            }

        }
    };

    @OnClick(R.id.news_layout)
    public void newsClick(){
        ArrayList<ShopsPojo> shopsPojos= new ArrayList<>();
        ShopsPojo pojo1 = new ShopsPojo();
        pojo1.setId(pojo.getId());
        pojo1.setBusiness_name(pojo.getBusiness_name());
        pojo1.setMerchant_name(pojo.getMerchant_name());
        pojo1.setCategory_master_id(Constants.Brands_CategryId);
        pojo1.setPackage_value("0");  pojo1.setDelivery_charge("0");
        pojo1.setProduct_count(pojo.getProduct_count());

        pojo1.setMin_order_value("0");
        pojo1.setPayment_type(new ArrayList<String>());
        pojo1.setOrder_type(new ArrayList<String>());
        shopsPojos.add(pojo1);

        System.out.println("package_value : 0");
        System.out.println("delivery_charge : 0");
        BuyChat.saveToPreferences(BuyChat.getAppContext(), Keys.package_value, "0");
        BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.delivery_charge,"0");

        BuyChat.saveToPreferences(getApplicationContext(),Keys.merchant_id,pojo.getId());
        BuyChat.saveToPreferences(getApplicationContext(),Keys.business_name,pojo.getBusiness_name());
        BuyChat.saveToPreferences(getApplicationContext(),Keys.merchant_image,pojo.getBusiness_image());

        DataSingleton.getInstance().setData(shopsPojos,Constants.DEFAULT_INT);


        if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Fashion_CategryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_ONE)
                    .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Beauty_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_TWO)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Groceries_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_THREE)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Electronics_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FOUR)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Restaurants_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FIVE)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Bukas_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_SIX)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Pay_Bill_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_SEVEN)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Education_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_EIGHT)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Taxis_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_NINE)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Flights_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_TEN)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Hotels_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_ELEVEN)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Movie_Tickets_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_TWELVE)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Event_Tickets_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_THIRTEEN)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Laundry_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FOURTEEN)
                   .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Brands_CategryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_FIFTEEN)
                    .putExtra(Keys.brandposition,Constants.INT_THREE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @OnClick(R.id.ask_layout)
    public void askClick(){

        ArrayList<ShopsPojo> shopsPojos= new ArrayList<>();
        ShopsPojo pojo1 = new ShopsPojo();
        pojo1.setId(pojo.getId());
        pojo1.setBusiness_name(pojo.getBusiness_name());
        pojo1.setMerchant_name(pojo.getMerchant_name());
        pojo1.setCategory_master_id(Constants.Brands_CategryId);
        pojo1.setPackage_value("0");
        pojo1.setDelivery_charge("0");
        pojo1.setProduct_count(pojo.getProduct_count());
        pojo1.setMin_order_value("0");
        pojo1.setPayment_type(new ArrayList<String>());
        pojo1.setOrder_type(new ArrayList<String>());
        shopsPojos.add(pojo1);


        System.out.println("package_value : 0");
        System.out.println("delivery_charge : 0");
        BuyChat.saveToPreferences(BuyChat.getAppContext(), Keys.package_value, "0");
        BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.delivery_charge,"0");

        BuyChat.saveToPreferences(getApplicationContext(),Keys.merchant_id,pojo.getId());
        BuyChat.saveToPreferences(getApplicationContext(),Keys.business_name,pojo.getBusiness_name());
        BuyChat.saveToPreferences(getApplicationContext(),Keys.merchant_image,pojo.getBusiness_image());





        DataSingleton.getInstance().setData(shopsPojos,Constants.DEFAULT_INT);
        if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Fashion_CategryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_ONE)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Beauty_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_TWO)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Groceries_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_THREE)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Electronics_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FOUR)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Restaurants_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FIVE)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Bukas_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_SIX)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Pay_Bill_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_SEVEN)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Education_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_EIGHT)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Taxis_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_NINE)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Flights_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_TEN)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Hotels_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_ELEVEN)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Movie_Tickets_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_TWELVE)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Event_Tickets_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_THIRTEEN)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Laundry_CategoryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class).putExtra(Keys.position,Constants.INT_FOURTEEN)
                   .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(DataSingleton.getInstance().getData().getCategory_master_id().equals(Constants.Brands_CategryId)){
            startActivity(new Intent(getApplicationContext(), MainCategoryDetailActivity.class)
                    .putExtra(Keys.position,Constants.INT_FIFTEEN)
                    .putExtra(Keys.brandposition,Constants.INT_ONE));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }


    private final class EventListener implements YouTubePlayer.PlaybackEventListener {


        /**
         * Called when video starts playing
         */
        @Override
        public void onPlaying() {
            Log.e("Status", "Playing");
        }


        /**
         * Called when video stops playing
         */
        @Override
        public void onPaused() {
            Log.e("Status", "Paused");
        }


        /**
         * Called when video stopped for a reason other than paused
         */
        @Override
        public void onStopped() {
            Log.e("Status", "Stopped");
        }


        /**
         * Called when buffering of video starts or ends
         * @param b True if buffering is on, else false
         */
        @Override
        public void onBuffering(boolean b) {
        }


        /**
         * Called when jump in video happens. Reason can be either user scrubbing
         * or seek method is called explicitely
         * @param i
         */
        @Override
        public void onSeekTo(int i) {
        }
    }

    private final class StateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        /**
         * Called when player begins loading a video. During this duration, player
         * won't accept any command that may affect the video playback
         */
        @Override
        public void onLoading() {

        }

        /**
         * Called when video is loaded. After this player can accept
         * the playback affecting commands
         * @param s Video Id String
         */
        @Override
        public void onLoaded(String s) {
        }


        /**
         * Called when YouTube ad is started
         */
        @Override
        public void onAdStarted() {
        }


        /**
         * Called when video starts playing
         */
        @Override
        public void onVideoStarted() {
        }


        /**
         * Called when video is ended
         */
        @Override
        public void onVideoEnded() {
        }


        /**
         * Called when any kind of error occurs
         * @param errorReason Error string showing the reason behind it
         */
        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BuyChat.saveToPreferences(getApplicationContext(),Keys.flat_no_floor_name,Constants.False);
    }
}
