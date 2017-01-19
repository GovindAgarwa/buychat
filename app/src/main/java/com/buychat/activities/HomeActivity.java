package com.buychat.activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buychat.BaseActivity;
import com.buychat.MyFirebaseInstanceIDService;
import com.buychat.MyFirebaseMessagingService;
import com.buychat.R;
import com.buychat.adapter.CustomViewPagerAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.extras.LocationSelecter;
import com.buychat.fragments.ChatView;
import com.buychat.fragments.ExploreView;
import com.buychat.fragments.LocationFragment;
import com.buychat.fragments.RestBukLadEduFragment;
import com.buychat.pojos.Chat;
import com.buychat.pojos.City;
import com.buychat.singleton.DataSingleton;
import com.buychat.singleton.IMerchantEvents;
import com.buychat.singleton.ISocketEvents;
import com.buychat.singleton.MerchantHandler;
import com.buychat.singleton.SocketHandler;
import com.buychat.singleton.SocketSingleton;
import com.buychat.utils.chats.Status;
import com.buychat.utils.chats.UserType;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements LocationSelecter,Callback<JsonObject>,ChatView.CallSecondPage {

    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.firstTab) LinearLayout firstTab;
    @BindView(R.id.secondTab) LinearLayout secondTab;
    @BindView(R.id.city_name) TextView city_name;
    ArrayList<City> arrayList;
    private int type=0;
    static final int TYPE_PROGRAMMATICALLY = 0;
    Animator pagerAnimation;
    private int oldDragPosition = 0;
    private ProgressDialog dialog;
    CustomViewPagerAdapter adapter;
     int count = 0;
//SocketHandler socketHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        SocketSingleton.get(getApplicationContext()).getSocket().on(Keys.socketId,onMessageEvent);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        firstTab.setVisibility(View.VISIBLE);
        secondTab.setVisibility(View.GONE);

        city_name.setText(BuyChat.readFromPreferences(getApplicationContext(), Keys.city_name,Constants.DEFAULT_STRING));

        setupWindowAnimations();
        System.out.println(DataSingleton.getInstance().getCityArray());
        setMainTabCount();
        adapter = new CustomViewPagerAdapter(getSupportFragmentManager(),getApplicationContext());
        adapter.addFragment(new ChatView(), "CHAT",""+BuyChat.readFromPreferences(getApplicationContext(),Keys.count,Constants.DEFAULT_INT));
        adapter.addFragment(new ExploreView(), "EXPLORE",""+Constants.DEFAULT_INT);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    getSupportActionBar().setTitle("");
                    firstTab.setVisibility(View.VISIBLE);
                    secondTab.setVisibility(View.GONE);
                }else{
                    getSupportActionBar().setTitle("");
                    firstTab.setVisibility(View.GONE);
                    secondTab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setCustomView(adapter.getTabView(0));

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                animatePagerTransition(true,2);
//            }
//        }, 1000);


    }



//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d(Keys.socket_id,"connect");
//        SocketSingleton.get(getApplicationContext()).getSocket().on(Keys.user,onUserEvent);
//        SocketSingleton.get(getApplicationContext()).getSocket().connect();
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        SocketSingleton.get(getApplicationContext()).getSocket().off(Keys.user,onUserEvent);
//        //SocketSingleton.get(getApplicationContext()).getSocket().disconnect();
//
//    }

    public Emitter.Listener onUserEvent = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                try {
                    Uri notifications = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notifications);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final JSONObject data = (JSONObject) args[0];
                Log.d(getLocalClassName()+""+Keys.socket_id, data.toString());
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
                message.setImage(data.getString(Keys.image));
                message.setUserType(UserType.SELF);
                message.setFrom(data.getString(Keys.from));
                message.setMessageTime(new Date().getTime());
                message.setMerchant_id(data.getString(Keys.merchant_id));
                message.setBusiness_name(data.getString(Keys.buisness_name));
                message.setMerchant_image(data.getString(Keys.merchant_image));
                message.setFlag("1");
                message.setCount(0);
                BuyChat.dbHelper.InsertChat(message);
                setMainTabCount();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CustomViewPagerAdapter(getSupportFragmentManager(),getApplicationContext());
                        adapter.addFragment(new ChatView(), "CHAT",""+BuyChat.readFromPreferences(getApplicationContext(),Keys.count,Constants.DEFAULT_INT));
                        adapter.addFragment(new ExploreView(), "EXPLORE",""+Constants.DEFAULT_INT);
                        viewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewPager);
                        for (int i = 0; i < tabLayout.getTabCount(); i++) {
                            TabLayout.Tab tab = tabLayout.getTabAt(i);
                            tab.setCustomView(adapter.getTabView(i));
                        }
                        tabLayout.requestFocus();
                        viewPager.setCurrentItem(Constants.DEFAULT_INT);

                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };


    private void setMainTabCount(){
        ArrayList<Chat> arrayLists = new ArrayList<>();
        arrayLists = BuyChat.dbHelper.getAllChat();
        int message_count=0;
        int merchant_count=0;
        for(int i=0;i<arrayLists.size();i++){
            if(arrayLists.get(i).getFlag().equals("1")) {
                merchant_count++;
            }
            for(int j=0;j<BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayLists.get(i).getMerchant_id()).size();j++){
                if(BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayLists.get(i).getMerchant_id()).get(j).getFlag().equals("1")){
                    BuyChat.dbHelper.UpdateCountChat(BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayLists.get(i).getMerchant_id()).get(j).getMerchant_id(),(j+1));
                }
            }
        }
        BuyChat.saveToPreferences(getApplicationContext(),Keys.count,merchant_count);
    }

    private void InboxNotification(){
        //set intents and pending intents to call activity on click of "show activity" action button of notification
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent resultIntent = new Intent(this, Splash.class);
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

    private Emitter.Listener onMessageEvent = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject data = (JSONObject) args[0];
                Log.d(Keys.socket_id, data.getString(Keys.id));
                BuyChat.saveToPreferences(getApplicationContext(), Keys.socket_id, data.getString(Keys.id));
                if(!BuyChat.readFromPreferences(getApplicationContext(), Keys.socket_id,Constants.DEFAULT_STRING).equals(Constants.DEFAULT_STRING)) {
                    WebRequests service1 = BuyChat.initializeRetrofit().create(WebRequests.class);
                    Call<JsonObject> call1 = service1.update_socket(Parse.updateSocket());
                    call1.enqueue(HomeActivity.this);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                startActivityForResult(new Intent(getApplicationContext(),SearchListActivity.class),Constants.DEFAULT_INT);
                overridePendingTransition(R.anim.push_up_in,R.anim.fade_out);
                break;
            case R.id.history:
                startActivity(new Intent(HomeActivity.this,HistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.help:
                startActivity(new Intent(HomeActivity.this,FAQActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.profile:
                startActivity(new Intent(HomeActivity.this,UserProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.share:
                share();
                break;

        }
        return super.onOptionsItemSelected(item);
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
    public void setLocationText(int position) {
        if(!city_name.getText().toString().equals(arrayList.get(position).getCity_name())) {
            BuyChat.saveToPreferences(getApplicationContext(), Keys.city_id, arrayList.get(position).getId());
            BuyChat.saveToPreferences(getApplicationContext(), Keys.city_name, arrayList.get(position).getCity_name());
            city_name.setText(arrayList.get(position).getCity_name());
            adapter = new CustomViewPagerAdapter(getSupportFragmentManager(),getApplicationContext());
            adapter.addFragment(new ChatView(), "CHAT",""+BuyChat.readFromPreferences(getApplicationContext(),Keys.count,Constants.DEFAULT_INT));
            adapter.addFragment(new ExploreView(), "EXPLORE",""+""+Constants.DEFAULT_INT);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(Constants.INT_ONE);

        }
    }



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

    @Override
    public void setPage() {
        viewPager.setCurrentItem(Constants.INT_ONE);
    }






    @OnClick(R.id.secondTab)
    public void secondTabClick(){
        arrayList = new ArrayList<>();
        arrayList = Parse.parseCity(DataSingleton.getInstance().getCityData());
        DialogFragment dialogFragment = LocationFragment.newInstance(arrayList);
        //dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager().beginTransaction(), Constants.LocationFragment);
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
            enterTransition.setSlideEdge(Gravity.RIGHT);
            return enterTransition;
        }else
            return null;
    }



    private void animatePagerTransition(final boolean forward, int pageCount) {
        // if previous animation have not finished we can get exception
        if (pagerAnimation != null) {
            pagerAnimation.cancel();
        }
        pagerAnimation = getPagerTransitionAnimation(forward, pageCount);
        if (viewPager.beginFakeDrag()) {    // checking that started drag correctly
            pagerAnimation.start();
        }
    }

    private Animator getPagerTransitionAnimation(final boolean forward, int pageCount) {
        ValueAnimator animator = ValueAnimator.ofInt(0, viewPager.getWidth() - 1);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewPager.endFakeDrag();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                viewPager.endFakeDrag();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                    viewPager.endFakeDrag();
                oldDragPosition = 0;
                viewPager.beginFakeDrag();
            }
        });

        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(  new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int dragPosition = (Integer) animation.getAnimatedValue();
                int dragOffset = dragPosition - oldDragPosition;
                oldDragPosition = dragPosition;
//                viewPager.fakeDragBy(dragOffset * (forward ? -1 : 1));
                if (viewPager.beginFakeDrag() || viewPager.isFakeDragging()) {
                    viewPager.fakeDragBy(dragOffset * (forward ? -1 : 1));
                }
            }
        });

        animator.setDuration(1500 / pageCount); // remove divider if you want to make each transition have the same speed as single page transition
        animator.setRepeatCount(pageCount);

        return animator;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println(getLocalClassName()+" "+resultCode);
            int current_item = viewPager.getCurrentItem();
            if(!city_name.getText().toString().equals(BuyChat.readFromPreferences(getApplicationContext(),Keys.city_name,Constants.DEFAULT_STRING))){
                city_name.setText(BuyChat.readFromPreferences(getApplicationContext(), Keys.city_name,Constants.DEFAULT_STRING));
                adapter = new CustomViewPagerAdapter(getSupportFragmentManager(),getApplicationContext());
                adapter.addFragment(new ChatView(), "CHAT",""+BuyChat.readFromPreferences(getApplicationContext(),Keys.count,Constants.DEFAULT_INT));
                adapter.addFragment(new ExploreView(), "EXPLORE",""+Constants.DEFAULT_INT);
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(Constants.INT_ONE);
            }else {
                System.out.println("count1  "+count);
                setMainTabCount();
                adapter = new CustomViewPagerAdapter(getSupportFragmentManager(),getApplicationContext());
                adapter.addFragment(new ChatView(), "CHAT",""+BuyChat.readFromPreferences(getApplicationContext(),Keys.count,Constants.DEFAULT_INT));
                adapter.addFragment(new ExploreView(), "EXPLORE",""+Constants.DEFAULT_INT);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    tab.setCustomView(adapter.getTabView(i));
                }
                tabLayout.requestFocus();
                viewPager.setCurrentItem(current_item);
            }

        SocketSingleton.get(getApplicationContext()).getSocket().connect();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketSingleton.get(getApplicationContext()).getSocket().disconnect();
        SocketSingleton.get(getApplicationContext()).getSocket().off(Keys.socketId,onMessageEvent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BuyChat.saveToPreferences(getApplicationContext(),Keys.tag_address,Constants.True);
        BuyChat.saveToPreferences(getApplicationContext(),Keys.flat_no_floor_name,Constants.False);
        BuyChat.saveToPreferences(getApplicationContext(),Keys.fields,Constants.True);
        SocketSingleton.get(getApplicationContext()).getSocket().connect();
        SocketSingleton.get(getApplicationContext()).getSocket().on(Keys.user,onUserEvent );
    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d(getLocalClassName(),"onStop");
        SocketSingleton.get(getApplicationContext()).getSocket().off(Keys.user,onUserEvent);
        if(BuyChat.readFromPreferences(getApplicationContext(),Keys.flat_no_floor_name,Constants.DEFAULT_STRING).equals(Constants.False)){
            Log.d(getLocalClassName(),"flag");
            SocketSingleton.get(getApplicationContext()).getSocket().disconnect();
        }
    }





}
