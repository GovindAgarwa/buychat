package com.buychat.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.adapter.ViewPagerAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.fragments.ChatFragment;
import com.buychat.fragments.CheckOutFragment;
import com.buychat.greatjob.GreateJob;
import com.buychat.singleton.DataSingleton;
import com.buychat.singleton.ISocketEvents;
import com.buychat.singleton.SocketHandler;
import com.buychat.singleton.SocketSingleton;
import com.buychat.utils.AlertDialogManager;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMainActivity extends AppCompatActivity  {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
  //SocketHandler socketHandler;

    ProgressDialog dialog;

    private View mLayout;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
      //  socketHandler = new SocketHandler(this);
      //  socketHandler.connectToSocket();
        Log.d(getLocalClassName(),"onCreate");
        dialog = new ProgressDialog(ChatMainActivity.this,R.style.MyTheme);
        mLayout = findViewById(R.id.container);
        ButterKnife.bind(this);
        initialize();
        if(BuyChat.readFromPreferences(getApplicationContext(),Keys.chatlist,Constants.DEFAULT_STRING).equals(Constants.True)) {
            Log.d(getLocalClassName(),"onCreate connect");
            AlertDialogManager.showDialog(dialog);
            SocketSingleton.get(getApplicationContext()).getSocket().on(Keys.socketId, onMessageEvent);
            SocketSingleton.get(getApplicationContext()).getSocket().connect();
        }else{
            Log.d(getLocalClassName(),"onCreate normal");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,new ChatFragment(),Constants.Chats)
                    .commit();
        }


    }

    private Emitter.Listener onMessageEvent = new Emitter.Listener() {
        @Override
       public void call(Object... args) {
            try {
                count++;
                JSONObject data = (JSONObject) args[0];
                Log.d(Keys.socket_id, data.getString(Keys.id));
                BuyChat.saveToPreferences(getApplicationContext(), Keys.socket_id, data.getString(Keys.id));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(getLocalClassName(),"onCreate emit");
                        if(getApplicationContext() != null) {
                            new Handler().post(new Runnable() {
                                public void run() {
                                    if (count == Constants.INT_ONE) {
                                        if(!isFinishing()) {
                                            getSupportFragmentManager().beginTransaction()
                                                    .add(R.id.container, new ChatFragment(), Constants.Chats)
                                                    .commitAllowingStateLoss();
                                            AlertDialogManager.dismissDialog(dialog);
                                        }
                                   } else {
                                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                                        if(fragment instanceof ChatFragment){
                                            ((ChatFragment) fragment).setData();
                                        }
                                        AlertDialogManager.dismissDialog(dialog);
                                        Log.d(getLocalClassName(),"onCreate emit fragment false");
                                    }
                                }
                            });
                        }
                    }
                });

            }catch (Exception e){
                AlertDialogManager.dismissDialog(dialog);
                BuyChat.showAToast(Constants.Internet);
                e.printStackTrace();
            }

        }
    };


    private void initialize() {
        setSupportActionBar(toolbar);
        String title = BuyChat.readFromPreferences(getApplicationContext(),Keys.business_name,Constants.DEFAULT_STRING);
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
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
        Log.d(getLocalClassName(),"onBackPressed");
        Intent intent = new Intent(getApplicationContext(),
                HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();

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
            }  else {
                    Log.i("TAG", "Permissions were not granted.");
                    Snackbar.make(mLayout, "Permissions were not granted.",
                            Snackbar.LENGTH_SHORT).show();

                }
                // END_INCLUDE(permission_result)

        } else if (requestCode == REQUEST_CAMERA) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            // Check if the only required permission has been granted
            Log.i("TAG", "Camera Permission has been granted. Preview can now be opened");
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constants.REQUEST_CAMERA);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

        System.out.println(requestCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE)
                ((ChatFragment)fragment).onSelectFromGalleryResult(data);
            else if (requestCode == Constants.REQUEST_CAMERA)
                ((ChatFragment)fragment).onCaptureImageResult(data);
        }
        Log.d(getLocalClassName(),"onActivityResult");

    }


    private Emitter.Listener onUserEvent = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject data = (JSONObject) args[0];
                Log.d(getClass()+""+Keys.socket_id, data.toString());
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                        ((ChatFragment)fragment).setEmitData(ChatMainActivity.this,data);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getLocalClassName(),"onResume");
        BuyChat.saveToPreferences(getApplicationContext(),Keys.flat_no_floor_name,Constants.True);
        BuyChat.saveToPreferences(getApplicationContext(),Keys.fields,Constants.False);

        if(BuyChat.readFromPreferences(getApplicationContext(),Keys.tag_address,Constants.DEFAULT_STRING).equals(Constants.False)){
            Log.d(getLocalClassName(),"onResume not connect");
        }else {
            Log.d(getLocalClassName(),"onResume connect");
            SocketSingleton.get(getApplicationContext()).getSocket().connect();
            SocketSingleton.get(getApplicationContext()).getSocket().on(Keys.user, onUserEvent);
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d(getLocalClassName(),"onStop");
        SocketSingleton.get(getApplicationContext()).getSocket().off(Keys.user,onUserEvent);
        if(BuyChat.readFromPreferences(getApplicationContext(),Keys.fields,Constants.DEFAULT_STRING).equals(Constants.False)){
            Log.d(getLocalClassName(),"onStop disconnect");
            SocketSingleton.get(getApplicationContext()).getSocket().disconnect();
        }
    }





}
