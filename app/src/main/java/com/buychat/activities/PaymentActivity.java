package com.buychat.activities;

import android.app.Activity;
import android.app.FragmentManager;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.adapter.ViewPagerAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.fragments.BillPayFragment;
import com.buychat.fragments.BlankFragment;
import com.buychat.fragments.CashOnDelivery;
import com.buychat.fragments.ChatFragment;
import com.buychat.fragments.CheckOutFragment;
import com.buychat.fragments.ElectronicsAddToCartDialog;
import com.buychat.fragments.EmptyOrdersFragment;
import com.buychat.fragments.FeePaymentFragment;
import com.buychat.fragments.OffersFragment;
import com.buychat.fragments.ThankYouDialog;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.AlertDialogManager;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.simplepay.gateway.fragments.CreditCardPaymentFragment;
import ng.simplepay.gateway.fragments.GatewayLayoutFragment;
import ng.simplepay.gateway.utils.Texts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements Callback<JsonObject> {

    private ProgressDialog dialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.submit)
    Button submit;
    String total_amount;
    int order_type;
    ViewPagerAdapter adapter;
    String payment_type = "";
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

        if(DataSingleton.getInstance().getData().getOrder_type().size() != 0){
            for(int i=0;i<DataSingleton.getInstance().getData().getOrder_type().size();i++){

                if(DataSingleton.getInstance().getData().getOrder_type().get(i).equalsIgnoreCase(Constants.Home_Delivery)){
                        if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == i){
                            order_type =  Constants.INT_ONE;
                            total_amount =  DataSingleton.getInstance()
                                    .getTotalWithPackageChargeWithDeliveryData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
                                            .getData().getId()));
                        }
                }else if(DataSingleton.getInstance().getData().getOrder_type().get(i).equalsIgnoreCase(Constants.Take_Away)){
                    if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == i){
                        order_type =  Constants.INT_TWO;
                        total_amount = DataSingleton.getInstance()
                                .getTotalWithPackageChargeData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
                                        .getData().getId()));
                    }
                }else if(DataSingleton.getInstance().getData().getOrder_type().get(i).equalsIgnoreCase(Constants.Pre_Order)){
                    if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == i){
                        order_type =  Constants.INT_THREE;
                        total_amount = DataSingleton.getInstance()
                                .getTotalData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
                                        .getData().getId()));
                    }
                }
            }

        }else{
       //     if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.DEFAULT_INT){
                order_type =  Constants.INT_FOUR;
                total_amount =  DataSingleton.getInstance()
                        .getTotalWithDeliveryChargeData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
                                .getData().getId()));
//            }
//            if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_ONE){
//                order_type =  Constants.INT_TWO;
//                total_amount = DataSingleton.getInstance()
//                        .getTotalWithPackageChargeData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
//                                .getData().getId()));
//            }
//            if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_TWO){
//                order_type =  Constants.INT_THREE;
//                total_amount = DataSingleton.getInstance()
//                        .getTotalData(BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance()
//                                .getData().getId()));
//            }
        }


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if(DataSingleton.getInstance().getData().getPayment_type().size() != 0){
            for(int i=0;i<DataSingleton.getInstance().getData().getPayment_type().size();i++){

                if(DataSingleton.getInstance().getData().getPayment_type().get(i).equalsIgnoreCase(Constants.CashOnDelivery)){
                    CashOnDelivery cashOnDelivery = new CashOnDelivery();
                    Bundle args =new Bundle();
                    args.putInt(Keys.position,getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT));
                    args.putInt(Keys.position1,Constants.DEFAULT_INT);
                    args.putString(Keys.address_id,getIntent().getStringExtra(Keys.address_id));
                    args.putString(Keys.order_amount,total_amount);
                    cashOnDelivery.setArguments(args);
                    adapter.addFragment(cashOnDelivery, Constants.CashOnDelivery);
                }else if(DataSingleton.getInstance().getData().getPayment_type().get(i).equalsIgnoreCase(Constants.Simplepay)){
                    CashOnDelivery cashOnDelivery1 = new CashOnDelivery();
                    Bundle args1=new Bundle();
                    args1.putInt(Keys.position,getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT));
                    args1.putInt(Keys.position1,Constants.INT_ONE);
                    args1.putString(Keys.address_id,getIntent().getStringExtra(Keys.address_id));
                    args1.putString(Keys.order_amount,total_amount);
                    cashOnDelivery1.setArguments(args1);
                    adapter.addFragment(cashOnDelivery1, Constants.Simplepay);
                }
            }
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(DataSingleton.getInstance().getData().getPayment_type().size()-1);

        }else{
            CashOnDelivery cashOnDelivery = new CashOnDelivery();
            Bundle args =new Bundle();
            args.putInt(Keys.position,getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT));
            args.putInt(Keys.position1,Constants.DEFAULT_INT);
            args.putString(Keys.address_id,getIntent().getStringExtra(Keys.address_id));
            args.putString(Keys.order_amount,total_amount);
            cashOnDelivery.setArguments(args);
            adapter.addFragment(cashOnDelivery, Constants.CashOnDelivery);

            CashOnDelivery cashOnDelivery1 = new CashOnDelivery();
            Bundle args1=new Bundle();
            args1.putInt(Keys.position,getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT));
            args1.putInt(Keys.position1,Constants.INT_ONE);
            args1.putString(Keys.address_id,getIntent().getStringExtra(Keys.address_id));
            args1.putString(Keys.order_amount,total_amount);
            cashOnDelivery1.setArguments(args1);
            adapter.addFragment(cashOnDelivery1, Constants.Simplepay);

            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(Constants.INT_ONE);
        }







    }
    private static final Integer SIMPLEPAY_GATEWAY = 0;

    @OnClick(R.id.submit) void submit(){
        dialog = new ProgressDialog(PaymentActivity.this,R.style.MyTheme);
        AlertDialogManager.showDialog(dialog,false);

        if(DataSingleton.getInstance().getData().getPayment_type().size() != 0){
            for(int i=0;i<DataSingleton.getInstance().getData().getPayment_type().size();i++){
                if(DataSingleton.getInstance().getData().getPayment_type().get(i).equals(Constants.CashOnDelivery)){
                    if(viewPager.getCurrentItem() == i){
                        payment_type = Constants.CashOnDelivery;
                        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
                        Call<JsonObject> call = service.createOrders(Parse.getAccessTokenWithOrderDetails(getIntent().getStringExtra(Keys.address_id),total_amount,1,order_type));
                        call.enqueue(this);
                    }
                }else if(DataSingleton.getInstance().getData().getPayment_type().get(i).equals(Constants.Simplepay)){
                    if(viewPager.getCurrentItem() == i){
                        payment_type = Constants.Simplepay;
                        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
                        Call<JsonObject> call = service.createOrders(Parse.getAccessTokenWithOrderDetails(getIntent().getStringExtra(Keys.address_id),total_amount,2,order_type));
                        call.enqueue(this);
                    }
                }
            }

        }else{
            if(viewPager.getCurrentItem() == Constants.DEFAULT_INT){
                payment_type = Constants.CashOnDelivery;
                WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
                Call<JsonObject> call = service.createOrders(Parse.getAccessTokenWithOrderDetails(getIntent().getStringExtra(Keys.address_id),total_amount,1,order_type));
                call.enqueue(this);
            }

            if(viewPager.getCurrentItem() == Constants.INT_ONE){
                payment_type = Constants.Simplepay;
                WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
                Call<JsonObject> call = service.createOrders(Parse.getAccessTokenWithOrderDetails(getIntent().getStringExtra(Keys.address_id),total_amount,2,order_type));
                call.enqueue(this);
            }
        }
    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        AlertDialogManager.dismissDialog(dialog);
        if(response.code() == Constants.SUCCESS){
            if(Parse.checkStatus(response.body().toString()).equals(Keys.success)){
                BuyChat.saveToPreferences(getApplicationContext(),Keys.order_id,Parse.checkMessage(response.body().toString(), Keys.order_id));

                if(viewPager.getCurrentItem() == Constants.DEFAULT_INT) {
                    ThankYouDialog frament = new ThankYouDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString(Keys.order_id, Parse.checkMessage(response.body().toString(), Keys.order_id));
                    bundle.putString(Keys.total_amount, total_amount);
                    bundle.putString(Keys.payment_type,payment_type);
                     frament.setArguments(bundle);
                    DialogFragment dialogFragment = frament;
                    dialogFragment.setCancelable(false);
                    dialogFragment.show(getSupportFragmentManager().beginTransaction(), Constants.ThankYouDialog);
                }else{
                    int amount = (int)Math.round(Float.valueOf(total_amount));
                    Log.d("PaymentData","+"+BuyChat.readFromPreferences(getApplicationContext(),Keys.buychat_id,Constants.DEFAULT_STRING));
                    Intent myIntent = new Intent( PaymentActivity.this,ng.simplepay.gateway.Gateway.class);
                    myIntent.putExtra("description", "Order #"+BuyChat.readFromPreferences(getApplicationContext(),Keys.order_id,Constants.DEFAULT_STRING));
                    myIntent.putExtra("api_key", "pu_922f80ee65b04d3a912142da8979e023");
                            //test_pu_ffef447b7baf4183b83429c4aa23acf6
                    myIntent.putExtra("amount", amount*100);
                    myIntent.putExtra("amount_currency", "NGN");
                    myIntent.putExtra("email", "a@a.com");
                    myIntent.putExtra("phone", "+351911111111");
                    myIntent.putExtra("payment_type", "checkout");
                    myIntent.putExtra("address", "31 Kade St, Abuja, Nigeria");
                    myIntent.putExtra("postal_code", "110001");
                    myIntent.putExtra("state", "Abuja");
                    myIntent.putExtra("country", "NG");
                    PaymentActivity.this.startActivityForResult(myIntent, SIMPLEPAY_GATEWAY);
                }
            }
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        AlertDialogManager.dismissDialog(dialog);
        BuyChat.showAToast(Constants.Internet);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == SIMPLEPAY_GATEWAY && resultCode == Activity.RESULT_OK) {
            Log.d("PaymentData",data.getStringExtra("token"));
            dialog = new ProgressDialog(PaymentActivity.this,R.style.MyTheme);
            AlertDialogManager.showDialog(dialog,false);

            WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
            Call<JsonObject> call = service.updateTransactionId(Parse.getAccessTokenOrderIdTransactionId(data.getStringExtra("token")));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    AlertDialogManager.dismissDialog(dialog);
                    if(response.code() == Constants.SUCCESS) {
                        if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                            ThankYouDialog fragment = new ThankYouDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString(Keys.order_id, BuyChat.readFromPreferences(getApplicationContext(), Keys.order_id, Constants.DEFAULT_STRING));
                            bundle.putString(Keys.total_amount, total_amount);
                            bundle.putString(Keys.payment_type,payment_type);
                            fragment.setArguments(bundle);
                            DialogFragment dialogFragment = fragment;
                            dialogFragment.setCancelable(false);
                            dialogFragment.show(getSupportFragmentManager().beginTransaction(), Constants.ThankYouDialog);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });


        }else{
            WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
            Call<JsonObject> call = service.cancelOrder(Parse.getAccessTokenOrderId());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.code() == Constants.SUCCESS) {
                        if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }
    }
}
