package com.buychat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.BrandsPojo;
import com.buychat.pojos.CategoriesPojos;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.AlertDialogManager;
import com.buychat.utils.BlurBuilder;
import com.buychat.utils.fonts.TextViewRegular;
import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Bundle extras;
    @BindView(R.id.lite_listrow_map)MapView mapView;
    @BindView(R.id.categories)LinearLayout catsLayout;
    @BindView(R.id.buychat_id)TextViewRegular buyId;
    @BindView(R.id.work_hours)TextViewRegular workHours;
    @BindView(R.id.profile_address)TextViewRegular profileAddress;
    @BindView(R.id.service_area)TextViewRegular serviceArea;
    @BindView(R.id.service_area_layout)LinearLayout serviceAreaLayout;
    @BindView(R.id.cityshop_homedelivery)LinearLayout homeDelivery;
    @BindView(R.id.cityshop_pre_Order)LinearLayout preOrder;
    @BindView(R.id.cityshop_parcel)LinearLayout parcel;
    @BindView(R.id.order_type_layout)LinearLayout orderTypeLayout;
    @BindView(R.id.payment_layout)LinearLayout paymentLayout;

    @BindView(R.id.description_layout)LinearLayout descriptionLayout;
    @BindView(R.id.business_description)TextViewRegular descriptionText;
    @BindView(R.id.business_image)ImageView businessImage;
 //   @BindView(R.id.business_background)ImageView business_background;
 //   @BindView(R.id.progress_bar_image)ProgressBar progressBarImage;
    @BindView(R.id.relative)
    LinearLayout relative;
    private ProgressDialog dialog;
    ImageLoader imageLoader;
    ArrayList<CategoriesPojos> arraylistCats;
    @BindView(R.id.brandss) LinearLayout linear;
//    @BindView(R.id.linear) LinearLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_actvity);
        ButterKnife.bind(this);

        initialize();
        extras=getIntent().getExtras();
        imageLoader=ImageLoader.getInstance();

        if(extras!=null) {
            setHideorShowMapView(extras.getInt(Keys.position, 1000));
            Log.e("POS",extras.getInt(Keys.position, 1000)+"");
        }else
            setHideorShowMapView(1000);

        homeDelivery.setVisibility(View.GONE);
        preOrder.setVisibility(View.GONE);
        parcel.setVisibility(View.GONE);


    }

    private void setHideorShowMapView(int anInt) {
        if(anInt==6 || anInt==11 || anInt==12 || anInt==13) {
            mapView.setVisibility(View.VISIBLE);
            catsLayout.setVisibility(View.GONE);
        } else if (anInt==1 || anInt==2 || anInt==3 || anInt==4 ||anInt==5) {
            mapView.setVisibility(View.GONE);
            catsLayout.setVisibility(View.VISIBLE);
        } else {
            mapView.setVisibility(View.GONE);
            catsLayout.setVisibility(View.GONE);
        }

    }


    private void ApiCategoryCall(){
        dialog = new ProgressDialog(ProfileActivity.this, R.style.MyTheme);
        AlertDialogManager.showDialog(dialog);

        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call = service.getShopDetails(Parse.getMerchantId(DataSingleton.getInstance().getData().getId()));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                AlertDialogManager.dismissDialog(dialog);
                if(response.code() == Constants.SUCCESS) {
                    if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                        relative.setVisibility(View.VISIBLE);
                        buyId.setText(Parse.checkMessage(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.mobile));
                        workHours.setText(Parse.checkMessage(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.business_hours));
                        profileAddress.setText(Parse.checkMessage(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.business_address));
                        if(!TextUtils.isEmpty(Parse.checkMessage(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.business_description)))
                            descriptionText.setText(Parse.checkMessage(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.business_description));
                        else
                            descriptionLayout.setVisibility(View.GONE);

                        //SERVISES
                        manageServices(response);

                        // CATEGORIES
                        manageCategories(response);

                        // BUSINESS IMAGE
                        manageTopImage(response);



                        // PAYMENT LAYOUT
                        managePaymentLayout(response);


                        //ORDER TYPE
                        manageOrderTypes(response);


                    }
                }else {
                    BuyChat.showAToast(Constants.Some_Went_Wrong);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AlertDialogManager.dismissDialog(dialog);
                BuyChat.showAToast(Constants.Internet);
            }
        });
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
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int w1 = (int) (metrics.widthPixels / metrics.density), h1 = w1 * 3 / 4;
        android.view.ViewGroup.LayoutParams layoutParams = businessImage.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = h1;
        businessImage.setLayoutParams(layoutParams);
        businessImage.setAdjustViewBounds(true);
        businessImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        ApiCategoryCall();
    }



    /**
     * .
     *
     * ORDER TYPES LAYOUT.
     *
     * .
     */
    public void manageOrderTypes(Response<JsonObject> response){
        String HOME_DELIVERY="Home Delivery";
        String PREORDER="Pre-Order";
        String TAKEAWAY="Take Away";
        homeDelivery.setVisibility(View.GONE);
        preOrder.setVisibility(View.GONE);
        parcel.setVisibility(View.GONE);
        JSONArray orderTypeArray=Parse.checkMessageJsonArray(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.order_type);
        if(orderTypeArray.length()>0) {
            if (orderTypeArray.toString().contains(HOME_DELIVERY)) {
                homeDelivery.setVisibility(View.VISIBLE);
            }
            if (orderTypeArray.toString().contains(PREORDER)) {
                preOrder.setVisibility(View.VISIBLE);
            }
            if (orderTypeArray.toString().contains(TAKEAWAY)) {
                parcel.setVisibility(View.VISIBLE);
            }
        }else{
            orderTypeLayout.setVisibility(View.GONE);
        }
    }


    /**
     * .
     *
     * SERVICES LAYOUT.
     *
     * .
     */
    public void manageServices(Response<JsonObject> response){
        if(!TextUtils.isEmpty(Parse.checkMessage(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.service_area))) {
            serviceArea.setText(Parse.checkMessage(Parse.parseProfileDetail(response.body().toString(), Keys.shopdetails), Keys.service_area));
        }else{
            serviceAreaLayout.setVisibility(View.GONE);
        }
    }

    /**
     * .
     *
     * CATEGORIES LAYOUT.
     *
     * .
     */
    public void manageCategories(Response<JsonObject> response){
        JSONArray catsArray=Parse.checkMessageJsonArray(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.subcategory);
        if(catsArray.length()>0){
            arraylistCats=new ArrayList<CategoriesPojos>();
            arraylistCats=Parse.parseProfileCategories(catsArray.toString());
            settingCategoryData(arraylistCats);
        }else{
            catsLayout.setVisibility(View.GONE);
        }
    }


    /**
     * .
     *
     * TOP IMAGE.
     *
     * .
     */
    public void manageTopImage(Response<JsonObject> response){
        if(!Parse.checkMessage(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.business_image).equals("")){
            imageLoader.displayImage(Parse.checkMessage(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.business_image), businessImage, new SimpleImageLoadingListener() {

                final List<String> displayedImages = Collections
                        .synchronizedList(new LinkedList<String>());

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    super.onLoadingStarted(imageUri, view);
                    businessImage.setImageResource(R.drawable.empty_logo_image);
                   // progressBarImage.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    if (loadedImage != null) {
                      //  progressBarImage.setVisibility(View.GONE);
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




        }else{
            businessImage.setVisibility(View.GONE);
            businessImage.setImageResource(R.drawable.empty_photo);
          //  progressBarImage.setVisibility(View.GONE);
        }
    }


    /**
     * .
     *
     * PAYMENT LAYOUT.
     *
     * .
     */
    public void managePaymentLayout(Response<JsonObject> response){
        ImageView simplePayImage,codImage;
        simplePayImage=(ImageView)findViewById(R.id.card1);
        codImage=(ImageView)findViewById(R.id.card2);
        simplePayImage.setVisibility(View.GONE);
        codImage.setVisibility(View.GONE);

        String CASH_ON_DELIVERY="Cash On Delivery";
        String SIMPLE_PAY="SimplePay";
        JSONArray paymentArray=Parse.checkMessageJsonArray(Parse.parseProfileDetail(response.body().toString(),Keys.shopdetails),Keys.payment_type);
        if(paymentArray.length()>0){

            paymentLayout.setVisibility(View.VISIBLE);
            if(paymentArray.toString().contains(CASH_ON_DELIVERY))
                codImage.setVisibility(View.VISIBLE);
            if(paymentArray.toString().contains(SIMPLE_PAY))
                simplePayImage.setVisibility(View.VISIBLE);
        }else{
            paymentLayout.setVisibility(View.GONE);
        }
    }


    private void settingCategoryData(final ArrayList<CategoriesPojos> arrayList) {


        linear.removeAllViews();
        if(arrayList.size() != 0){
//            main.setVisibility(View.VISIBLE);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

            for (int i = 0; i < arrayList.size(); i++) {
                LinearLayout layout2 = new LinearLayout(this);
                layout2.setLayoutParams(new LinearLayout.LayoutParams(width/3, width/3+10));
                layout2.setOrientation(LinearLayout.VERTICAL);
                layout2.setPadding(5, 5, 5, 5);
                layout2.setGravity(Gravity.CENTER);
                layout2.setId(i);
                layout2.setBackgroundColor(Color.parseColor("#ffffff"));
                layout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        CategoriesPojos pojo = arrayList.get(v.getId());
//                        Gson gson = new Gson();
//                        String myJson = gson.toJson(pojo);
//                        startActivityForResult(new Intent(ProfileActivity.this, BrandProfileActivity.class).putExtra(Keys.data,myJson),Constants.INT_TWO);
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });

                final ImageView imageView = new ImageView(this);

                LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(140,140);
                imageView.setPadding(20, 10, 20, 5);
                imageLoader.displayImage(arrayList.get(i).getSubcategory_image(),imageView,new SimpleImageLoadingListener(){
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

                TextViewRegular tv = new TextViewRegular(this);
                LinearLayout.LayoutParams vps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                vps.gravity = Gravity.CENTER;

                tv.setText(arrayList.get(i).getSubcategory_name());
                tv.setTextSize(12);
                tv.setTextColor(getResources().getColor(R.color.black));
                tv.setLayoutParams(vps);
                layout2.addView(tv);

                linear.addView(layout2);

                View view = new View(this);
                view.setBackgroundColor(getResources().getColor(R.color.greytext));
                LinearLayout.LayoutParams viewvp = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(viewvp);
                linear.addView(view);
            }

        }else{
//            main.setVisibility(View.GONE);

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.history:
                startActivity(new Intent(ProfileActivity.this,HistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.help:
                startActivity(new Intent(ProfileActivity.this,FAQActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.profile:
                startActivity(new Intent(ProfileActivity.this,UserProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.inside_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



}
