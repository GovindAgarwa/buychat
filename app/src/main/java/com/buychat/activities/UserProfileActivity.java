package com.buychat.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.buychat.BaseActivity;
import com.buychat.R;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.greatjob.GreateJob;
import com.buychat.utils.AlertDialogManager;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nihas-mac on 25/08/2016.
 */
public class UserProfileActivity extends AppCompatActivity implements Callback<JsonObject>,ActivityCompat.OnRequestPermissionsResultCallback {
    private ProgressDialog dialog;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.notification)
    CheckBox notification;
    @BindView(R.id.profileImage)
    CircleImageView imageView;
    private String uploadPicture = "";
    private View mLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initialize();
        mLayout = findViewById(R.id.container);
        uploadPicture = BuyChat.readFromPreferences(getApplicationContext(),Keys.user_image,Constants.DEFAULT_STRING);
    }

    private void initialize() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        setData();
    }

    @OnClick(R.id.profileImage) void clickImageView(){
        selectImage();
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    showCamera();
                } else if (items[item].equals("Choose from Library")) {
                    requestStoragePermission();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

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

    private static final int REQUEST_STROGE = 0;
    private static final int REQUEST_CAMERA = 1;
    private void requestStoragePermission() {


        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i("TAG",
                    "Displaying storage permission rationale to provide additional context.");

            Snackbar.make(mLayout, "Storage permission is needed to fetch the photo.",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(UserProfileActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_STROGE);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(UserProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STROGE);
        }
        // END_INCLUDE(camera_permission_request)
    }


    public void showCamera() {
        if (ActivityCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            requestCameraPermission();

        } else {


            showCameraPreview();
        }
        // END_INCLUDE(camera_permission)

    }

    private void showCameraPreview() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }


    private void requestCameraPermission() {


        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(UserProfileActivity.this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i("TAG", "Camera permission is needed to show the camera preview.");
            Snackbar.make(mLayout, "Camera permission is needed to show the camera preview.",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(UserProfileActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {
            Log.i("TAG", "Camera permission is needed");
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(UserProfileActivity.this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
        // END_INCLUDE(camera_permission_request)
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == Constants.REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    public void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(thumbnail);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        sendImages(thumbnail);
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//        try {
////            destination.createNewFile();
////            fo = new FileOutputStream(destination);
////            fo.write(bytes.toByteArray());
////            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    public void onSelectFromGalleryResult(Intent data) {

        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);


        Bitmap bm;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(selectedImagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap bmRotated = rotateBitmap(bm, orientation);
            imageView.setImageBitmap(bmRotated);
            sendImages(bmRotated);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        try{
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    return bitmap;
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }
            try {
                Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return bmRotated;
            }
            catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }


    }

    private void sendImages(Bitmap bitmaps){
        int nh = (int) ( bitmaps.getHeight() * (256.0 / bitmaps.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bitmaps, 256, nh, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        uploadPicture = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    private void setData(){
        imageView.setImageBitmap(BuyChat.base64ToBitmap(BuyChat.readFromPreferences(getApplicationContext(),Keys.user_image,Constants.DEFAULT_STRING)));
        email.setText(BuyChat.readFromPreferences(getApplicationContext(), Keys.email,Constants.DEFAULT_STRING));
        mobile.setText(BuyChat.readFromPreferences(getApplicationContext(), Keys.mobile,Constants.DEFAULT_STRING));
        name.setText(BuyChat.readFromPreferences(getApplicationContext(), Keys.name,Constants.DEFAULT_STRING));
        if(BuyChat.readFromPreferences(getApplicationContext(),Keys.notification,Constants.DEFAULT_STRING).equals(Constants.True)){
            notification.setChecked(true);
        }else{
            notification.setChecked(false);
        }
    }
    @OnClick(R.id.update) void update(){
        if(name.getText().toString().isEmpty()){
            BuyChat.showAToast(Constants.Validate_name);
        }else{
            String notify;
            if(notification.isChecked()){
                notify = Constants.True;
            }else{
                notify = Constants.False;
            }
            dialog = new ProgressDialog(UserProfileActivity.this,R.style.MyTheme);
            AlertDialogManager.showDialog(dialog);
            WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
            Call<JsonObject> call = service.updateProfile(Parse.updateProfileData(name.getText().toString(),notify,uploadPicture));
            call.enqueue(this);
        }
    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        AlertDialogManager.dismissDialog(dialog);
        if(response.code() == Constants.SUCCESS){
            if(Parse.checkStatus(response.body().toString()).equals(Keys.success)){
                BuyChat.showAToast("Profile Updated Successfully");
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.name,Parse.checkMessage(response.body().toString(),Keys.name));
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.notification,Parse.checkMessage(response.body().toString(),Keys.notification));
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.user_image,uploadPicture);
                setData();
            }else{
                BuyChat.showAToast(Parse.checkMessage(response.body().toString(),Keys.message));
            }
        }
    }

      @Override
      public void onFailure(Call<JsonObject> call, Throwable t) {
          AlertDialogManager.dismissDialog(dialog);
          BuyChat.showAToast(Constants.Internet);
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


}
