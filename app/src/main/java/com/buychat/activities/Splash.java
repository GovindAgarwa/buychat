package com.buychat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.buychat.BaseActivity;
import com.buychat.BuildConfig;
import com.buychat.Manifest;
import com.buychat.R;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.fragments.DemoFragment;
import com.buychat.fragments.IntroView;
import com.buychat.fragments.SplashView;
import com.buychat.greatjob.GreateJob;
import com.buychat.register.RegisterView;
import com.google.firebase.iid.FirebaseInstanceId;

public class Splash extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private View mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        mLayout = findViewById(R.id.container);
        initializeView();

    }

    private void initializeView() {
        BuyChat.saveToPreferences(getApplicationContext(), Keys.device_token, FirebaseInstanceId.getInstance().getToken());
        if(getIntent() != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SplashView().newInstance(getIntent().getIntExtra(Keys.position,Constants.DEFAULT_INT)), Constants.Splash)
                    .commitAllowingStateLoss();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SplashView().newInstance(Constants.DEFAULT_INT), Constants.Splash)
                    .commitAllowingStateLoss();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    private static final int REQUEST_STROGE = 0;
    private static final int REQUEST_CAMERA = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Log.i("TAG", requestCode+" "+grantResults.length+" "+grantResults[0]);

        if (requestCode == REQUEST_STROGE) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i("TAG", "Storage permission.");

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
            } else {
                Log.i("TAG", "Permissions were not granted.");
                Snackbar.make(mLayout, "Permissions were not granted.",
                        Snackbar.LENGTH_SHORT).show();

            }
        }else if (requestCode == REQUEST_CAMERA) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            // Check if the only required permission has been granted
            Log.i("TAG", "Camera Permission ");
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i("TAG", "Camera Permission has been granted. Preview can now be opened");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constants.REQUEST_CAMERA);

//                Snackbar.make(mLayout, "Camera Permission has been granted. Preview can now be opened",
//                        Snackbar.LENGTH_SHORT).show();
            } else {
                Log.i("TAG", "Permissions were not granted.");
                Snackbar.make(mLayout, "Permissions were not granted.",
                        Snackbar.LENGTH_SHORT).show();

            }
            // END_INCLUDE(permission_result)

        } else if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            // Check if the only required permission has been granted
            Log.i("TAG", "Phone Permission");
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Phone Permission has been granted");

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                if(fragment instanceof SplashView){
                    ((SplashView) fragment).callApi();
                }
            } else {
                Log.i("TAG", "Permissions were not granted.");
                Snackbar.make(mLayout, "Permissions were not granted.",
                        Snackbar.LENGTH_SHORT).show();

            }
            // END_INCLUDE(permission_result)

        }else{
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
                ((GreateJob)fragment).onSelectFromGalleryResult(data);
            else if (requestCode == Constants.REQUEST_CAMERA)
                ((GreateJob)fragment).onCaptureImageResult(data);
        }
    }




}
