package com.buychat.activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.adapter.CustomViewPagerAdapter;
import com.buychat.adapter.ViewPagerAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Communicate;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.fragments.BillPayFragment;
import com.buychat.fragments.ChatFragment;
import com.buychat.fragments.ChatView;
import com.buychat.fragments.EmptyOrdersFragment;
import com.buychat.fragments.EventsFragment;
import com.buychat.fragments.ExploreView;
import com.buychat.fragments.FeePaymentFragment;
import com.buychat.fragments.FlightFragment;
import com.buychat.fragments.GreryFashBeautyElecFragment;
import com.buychat.fragments.ItemsFragment;
import com.buychat.fragments.MovieTaxiHotelFragment;
import com.buychat.fragments.OffersFragment;
import com.buychat.fragments.PayBillFragment;
import com.buychat.fragments.RestBukLadEduFragment;
import com.buychat.pojos.Chat;
import com.buychat.singleton.DataSingleton;
import com.buychat.singleton.ISocketEvents;
import com.buychat.singleton.SocketHandler;
import com.buychat.singleton.SocketSingleton;
import com.buychat.utils.AlertDialogManager;
import com.buychat.utils.chats.Status;
import com.buychat.utils.chats.UserType;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainCategoryDetailActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,Communicate, Callback<JsonObject>{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    CustomViewPagerAdapter adapter;
    private View mLayout;
    Animator pagerAnimation;
    private int oldDragPosition = 0;
    private int type=0;
    static final int TYPE_PROGRAMMATICALLY = 0;
 //   SocketHandler socketHandler;
 private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
    //    socketHandler = new SocketHandler(this);
    //    socketHandler.connectToSocket();
      //  SocketSingleton.get(getApplicationContext()).getSocket().on(Keys.socketId,onMessageEvent);
    //    SocketSingleton.get(getApplicationContext()).getSocket().connect();
        mLayout = findViewById(R.id.viewPager);
        ButterKnife.bind(this);
        initialize();
    }

//    private Emitter.Listener onMessageEvent = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            try {
//                JSONObject data = (JSONObject) args[0];
//                Log.d(Keys.socket_id, data.getString(Keys.id));
//                BuyChat.saveToPreferences(getApplicationContext(), Keys.socket_id, data.getString(Keys.id));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    };

    private void initialize() {
        BuyChat.saveToPreferences(getApplicationContext(),Keys.from,Constants.MERCHANT);
        setSupportActionBar(toolbar);
        setTitle(DataSingleton.getInstance().getData().getBusiness_name());
        getSupportActionBar().setSubtitle(DataSingleton.getInstance().getData().getMerchant_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        ApiCall();


    }

    @OnClick(R.id.toolbar) void clickToolbar(){
        if( getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_FIFTEEN){

        }else {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class).putExtra(Keys.position, getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT)));
            overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
        }
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
                startActivity(new Intent(MainCategoryDetailActivity.this,HistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.help:
                startActivity(new Intent(MainCategoryDetailActivity.this,FAQActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.profile:
                startActivity(new Intent(MainCategoryDetailActivity.this,UserProfileActivity.class));
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

    private void setupViewPager(final ViewPager viewPager) {
        System.out.println("position "+getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT));
        adapter = new CustomViewPagerAdapter(getSupportFragmentManager(),getApplicationContext());
        if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_ONE ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_TWO ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_FOUR ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_FIVE ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_SIX){


            if(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) == Constants.DEFAULT_INT) {

                adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT)), Constants.Chats, "" + Constants.DEFAULT_INT);
                adapter.addFragment(new EmptyOrdersFragment().newInstance(getIntent().getIntExtra(Keys.position, Constants.INT_ONE)), Constants.Orders, "" + Constants.DEFAULT_INT);
                adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT)), Constants.Offers, "" + Constants.DEFAULT_INT);

             }else{
                adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT)), Constants.AskQuestion, "" + Constants.DEFAULT_INT);
                adapter.addFragment(new EmptyOrdersFragment().newInstance(getIntent().getIntExtra(Keys.position, Constants.INT_ONE)), Constants.Shop, "" + Constants.DEFAULT_INT);
                adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position, Constants.DEFAULT_INT)), Constants.News, "" + Constants.DEFAULT_INT);

            }

        }else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_THREE){

            if(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) == Constants.DEFAULT_INT) {
                adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Chats,""+Constants.DEFAULT_INT);
                adapter.addFragment(new EmptyOrdersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.INT_THREE)), Constants.My_List,""+Constants.DEFAULT_INT);
                adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Offers,""+Constants.DEFAULT_INT);

            }else{
                adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.AskQuestion,""+Constants.DEFAULT_INT);
                adapter.addFragment(new EmptyOrdersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.INT_THREE)), Constants.Shop,""+Constants.DEFAULT_INT);
                adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.News,""+Constants.DEFAULT_INT);

            }

        }else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_SEVEN){
            adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Chats,""+Constants.DEFAULT_INT);
            adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Offers,""+Constants.DEFAULT_INT);

            //adapter.addFragment(new BillPayFragment(), Constants.Bill_Pay,""+Constants.DEFAULT_INT);
        }else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_EIGHT ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_FIFTEEN){

            if(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) == Constants.DEFAULT_INT) {
                adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Chats,""+Constants.DEFAULT_INT);
                adapter.addFragment(new EmptyOrdersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.INT_ONE)), Constants.Orders,""+Constants.DEFAULT_INT);
                adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Offers,""+Constants.DEFAULT_INT);

            }else{
                adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.AskQuestion,""+Constants.DEFAULT_INT);
                adapter.addFragment(new EmptyOrdersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.INT_ONE)), Constants.Shop,""+Constants.DEFAULT_INT);
                adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.News,""+Constants.DEFAULT_INT);

            }

        } else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_NINE ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_TEN ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_THIRTEEN ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_FOURTEEN){

            adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Chats,""+Constants.DEFAULT_INT);
            adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Offers,""+Constants.DEFAULT_INT);

        }else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_ELEVEN){

            if(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) == Constants.DEFAULT_INT) {
                adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Chats,""+Constants.DEFAULT_INT);
                adapter.addFragment(new EmptyOrdersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.INT_ELEVEN)), Constants.Rooms,""+Constants.DEFAULT_INT);
                adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Offers,""+Constants.DEFAULT_INT);

            }else{
                adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.AskQuestion,""+Constants.DEFAULT_INT);
                adapter.addFragment(new EmptyOrdersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.INT_ELEVEN)), Constants.Shop,""+Constants.DEFAULT_INT);
                adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.News,""+Constants.DEFAULT_INT);

            }

        }else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_TWELVE){

            if(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) == Constants.DEFAULT_INT) {
                adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Chats,""+Constants.DEFAULT_INT);
                adapter.addFragment(new EmptyOrdersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.INT_TWELVE)), Constants.Movies,""+Constants.DEFAULT_INT);
                adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Offers,""+Constants.DEFAULT_INT);

            }else{
                adapter.addFragment(new ChatFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.AskQuestion,""+Constants.DEFAULT_INT);
                adapter.addFragment(new EmptyOrdersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.INT_TWELVE)), Constants.Shop,""+Constants.DEFAULT_INT);
                adapter.addFragment(new OffersFragment().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.News,""+Constants.DEFAULT_INT);

            }

        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
//        if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_SEVEN){
//            viewPager.setCurrentItem(Constants.DEFAULT_INT);
//        }else{
//            viewPager.setCurrentItem(Constants.INT_ONE);
//        }

        if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_ONE ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_TWO ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_FOUR ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_FIVE ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_SIX ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_FIFTEEN){
                    viewPager.setOffscreenPageLimit(Constants.INT_TWO);
            if(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) == Constants.DEFAULT_INT) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(Constants.INT_ONE);
                    }
                }, 200);
            }else{
                viewPager.setCurrentItem(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) - 1);
            }

            }else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_THREE){

            viewPager.setOffscreenPageLimit(Constants.INT_TWO);
            if(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) == Constants.DEFAULT_INT) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        viewPager.setCurrentItem(Constants.INT_ONE);
                    }
                }, 200);
            }else{
                viewPager.setCurrentItem(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) - 1);
            }
        }else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_SEVEN){
            viewPager.setOffscreenPageLimit(Constants.INT_ONE);
        }else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_EIGHT){
            viewPager.setOffscreenPageLimit(Constants.INT_TWO);
            if(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) == Constants.DEFAULT_INT) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(Constants.INT_ONE);
                    }
                }, 200);
            }else{
                viewPager.setCurrentItem(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) - 1);
            }
        }else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_NINE ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_TEN ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_THIRTEEN ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_FOURTEEN){
            viewPager.setOffscreenPageLimit(Constants.INT_ONE);
        }else if(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_ELEVEN ||
                getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT) == Constants.INT_TWELVE){

            viewPager.setOffscreenPageLimit(Constants.INT_TWO);
            if(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) == Constants.DEFAULT_INT) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(Constants.INT_ONE);
                    }
                }, 200);
            }else{
                viewPager.setCurrentItem(getIntent().getIntExtra(Keys.brandposition,Constants.DEFAULT_INT) - 1);
            }
        }
    }

    /*
    *animatePagerTransition Params
    * forward - boolean value to start the animation
    * pageCount
    * specificPageNumber- the specific page it will scroll
    *
     */




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("xx",""+resultCode);
        Fragment fragment =  adapter.getRegisteredFragment(viewPager.getCurrentItem());
        if(fragment instanceof EmptyOrdersFragment){
            ((EmptyOrdersFragment) fragment).initialize();
        }
        Log.d(getLocalClassName(),resultCode+" resultCode");
        Log.d(getLocalClassName(),requestCode+" requestCode");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE)
                ((ChatFragment)fragment).onSelectFromGalleryResult(data);
            else if (requestCode == Constants.REQUEST_CAMERA)
                ((ChatFragment)fragment).onCaptureImageResult(data);
        }

        Log.d(getLocalClassName(),"onActivityResult");
    }



    private static final int REQUEST_STROGE = 0;
    private static final int REQUEST_CAMERA = 1;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_STROGE) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i("TAG", "Received response for Camera permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i("TAG", "Storage permission has now been granted. Showing preview.");

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), Constants.SELECT_FILE);


                Log.i("TAG", "Storage permission has now been granted. Showing preview.");
//                Snackbar.make(mLayout, "Storage Permission has been granted",
//                        Snackbar.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_CAMERA) {
                // BEGIN_INCLUDE(permission_result)
                // Received permission result for camera permission.
                // Check if the only required permission has been granted
                Log.i("TAG", "Camera Permission has been granted. Preview can now be opened");
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission has been granted, preview can be displayed

                    Snackbar.make(mLayout, "Camera Permission has been granted. Preview can now be opened",
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    Log.i("TAG", "Permissions were not granted.");
                    Snackbar.make(mLayout, "Permissions were not granted.",
                            Snackbar.LENGTH_SHORT).show();

                }
                // END_INCLUDE(permission_result)

            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getLocalClassName(),"onResume");
        if(BuyChat.readFromPreferences(getApplicationContext(),Keys.tag_address,Constants.DEFAULT_STRING).equals(Constants.False)){

        }else {
            Log.d(getLocalClassName(),"connect");
            SocketSingleton.get(getApplicationContext()).getSocket().connect();
            SocketSingleton.get(getApplicationContext()).getSocket().on(Keys.user, onMessageEvent);
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d(getLocalClassName(),"onStop");
        SocketSingleton.get(getApplicationContext()).getSocket().off(Keys.user,onMessageEvent);
        SocketSingleton.get(getApplicationContext()).getSocket().disconnect();
    }



    private Emitter.Listener onMessageEvent = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {


                final JSONObject data = (JSONObject) args[0];
                Log.d(getClass()+" "+Keys.socket_id, data.toString());
                if(DataSingleton.getInstance().getData().getId().equals(data.getString(Keys.merchant_id))) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(Constants.DEFAULT_INT);
                            Fragment fragment =  adapter.getRegisteredFragment(0);
                            if(fragment instanceof ChatFragment) {
                                ((ChatFragment) fragment).setEmitData(MainCategoryDetailActivity.this,data);
                            }
                        }
                    });






                } else{
                    final Chat message = new Chat();
                    message.setMessageStatus(Status.SENT);
                    if (data.has(Keys.message)) {
                        if(data.getString(Keys.message).equals(Constants.DEFAULT_STRING)){
                            message.setMessageText("Image");
                            message.setImage(data.getString(Keys.image));
                        }else {
                            message.setMessageText(data.getString(Keys.message));

                        }
                    }
                   // message.setMessageText(data.getString(Keys.message));
                    message.setImage(data.getString(Keys.image));
                    message.setFrom(data.getString(Keys.from));
                    message.setUserType(UserType.SELF);
                    message.setMessageTime(new Date().getTime());
                    message.setMerchant_id(data.getString(Keys.merchant_id));
                    message.setBusiness_name(data.getString(Keys.buisness_name));
                    message.setMerchant_image(data.getString(Keys.merchant_image));
                    message.setFlag("1");
                    message.setCount(0);
                    BuyChat.dbHelper.InsertChat(message);
                    InboxNotification();
                }


            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };


    private void InboxNotification(){
        //set intents and pending intents to call activity on click of "show activity" action button of notification
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent resultIntent = new Intent(this, Splash.class);
        resultIntent.putExtra(Keys.position,Constants.DEFAULT_INT);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent piResult = PendingIntent.getActivity(this,
                (int) Calendar.getInstance().getTimeInMillis(), resultIntent, 0);

//Assign inbox style notification
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        inboxStyle.setBigContentTitle("BuyChat");
ArrayList<Chat> arrayList = new ArrayList<>();
        arrayList = BuyChat.dbHelper.getAllChat();
        int message_count=0;
        int merchant_count=0;
        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getFlag().equals("1")) {
                merchant_count++;
            }
            for(int j=0;j<BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).size();j++){
                if(BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getFlag().equals("1")){
                    message_count++;
                    inboxStyle.addLine(BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getBusiness_name()+" : "
                            +BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getMessageText());

                    BuyChat.dbHelper.UpdateCountChat(BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getMerchant_id(),(j+1));
                }
            }

        }

        inboxStyle.setSummaryText(merchant_count+" conversations "+message_count+" messages");

//build notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(getNotificationIcon())
                        .setSound(sound)
                        .setContentTitle("BuyChat Notification")
                        .setContentText("Touch to see the notification")
                        .setStyle(inboxStyle)
                        .setAutoCancel(true)
                        .setContentIntent(piResult)
                        .setColor(getResources().getColor(R.color.colorPrimaryDark));

// Gets an instance of the NotificationManager service
        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//to post your notification to the notification bar
        notificationManager.notify(0, mBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_transparent : R.mipmap.ic_launcher;
    }

    @Override
    public void callChatFragment() {

        viewPager.setCurrentItem(Constants.DEFAULT_INT);
        Fragment fragment1 =  adapter.getRegisteredFragment(viewPager.getCurrentItem());
        if(fragment1 instanceof ChatFragment){
            ((ChatFragment) fragment1).sendData();
        }

        Fragment fragment =  adapter.getRegisteredFragment(Constants.INT_ONE);
        if(fragment instanceof EmptyOrdersFragment){
            ((EmptyOrdersFragment) fragment).initialize();
        }
    }

    private void ApiCall(){
        dialog= new ProgressDialog(MainCategoryDetailActivity.this,R.style.MyTheme);
        AlertDialogManager.showDialog(dialog);
        WebRequests service1 = BuyChat.initializeRetrofit().create(WebRequests.class);
        Call<JsonObject> call1 = service1.getOrdersMerchantList(Parse.getAccessTokenWithMerchantIDLimitOffset(DataSingleton.getInstance().getData().getId(),Constants.INT_TEN,Constants.DEFAULT_INT));
        call1.enqueue(this);
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if(response.code() == Constants.SUCCESS) {
//            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                AlertDialogManager.dismissDialog(dialog);
                BuyChat.saveToPreferences(getApplicationContext(),Keys.histroy_data,response.body().toString());
                setupViewPager(viewPager);
      //      }
        }else {
            BuyChat.showAToast(Constants.Some_Went_Wrong);
        }

    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {

    }

    private void setupWindowAnimations() {
        Transition transition;

        if (Build.VERSION.SDK_INT >= 21 ) {
            if (type == TYPE_PROGRAMMATICALLY) {
                transition = buildEnterTransition();
            } else {
                transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_from_bottom);
            }
            getWindow().setEnterTransition(transition);
        }
    }

    private Visibility buildEnterTransition() {

        if (Build.VERSION.SDK_INT >= 21 ) {
            Slide enterTransition = new Slide();
            enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            enterTransition.setSlideEdge(Gravity.TOP);
            return enterTransition;
        }else
            return null;
    }
}
