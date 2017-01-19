package com.buychat.greatjob;

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
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.buychat.activities.HomeActivity;
import com.buychat.R;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.City;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.AlertDialogManager;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snyxius Technologies on 8/8/2016.
 */
public class GreateJob extends Fragment implements Callback<JsonObject>,ActivityCompat.OnRequestPermissionsResultCallback {

    private Unbinder unbinder;

    @BindView(R.id.spinner_location)
    Spinner citySpinner;
    @BindView(R.id.buychat_id)
    TextView buychat_id;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.profileImage)
    CircleImageView imageView;

    @BindView(R.id.relative)
    RelativeLayout relative;

    ArrayList<City> arrayList;
    String city_id;
    private ProgressDialog dialog;
    private String uploadPicture = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_location_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.profileImage) void clickImageView(){
        selectImage();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        dialog = new ProgressDialog(getActivity(), R.style.MyTheme);
//        AlertDialogManager.showDialog(dialog);
//        WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
//        Call<JsonObject> call = service.get_city(Parse.getAccessToken());
//        call.enqueue(this);
        relative.setVisibility(View.VISIBLE);
        buychat_id.setText(BuyChat.readFromPreferences(getActivity(),Keys.buychat_id,Constants.DEFAULT_STRING));
        arrayList = new ArrayList<>();
        arrayList = Parse.parseCity(DataSingleton.getInstance().getCityData());
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,  Parse.parseCityString(DataSingleton.getInstance().getCityData()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
    }




    @OnClick(R.id.next) void onClickNext(){
        if(uploadPicture.equals(Constants.DEFAULT_STRING)){
            BuyChat.showAToast(Constants.SelectImage);
        }else

        if(name.getText().toString().isEmpty()){
           BuyChat.showAToast(Constants.Validate_name);
       }else if(citySpinner.getSelectedItem().toString().equals("Select City")){
            BuyChat.showAToast(Constants.SelectCity);
        }else{
            dialog = new ProgressDialog(getActivity(), R.style.MyTheme);
            AlertDialogManager.showDialog(dialog);
            String city_name = citySpinner.getSelectedItem().toString();
            for(int i=0;i<arrayList.size();i++){
                if(arrayList.get(i).getCity_name().equals(city_name)){
                    city_id = arrayList.get(i).getId();
                }
            }
            WebRequests service = BuyChat.initializeRetrofit().create(WebRequests.class);
            Call<JsonObject> call = service.save_city(Parse.getAccessTokenAndCityAndImageAndName(citySpinner.getSelectedItem().toString(),uploadPicture,name.getText().toString()));
            call.enqueue(this);
        }

    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        AlertDialogManager.dismissDialog(dialog);
        if(response.code() == Constants.SUCCESS) {
            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.city_name,Parse.checkMessage(response.body().toString(),Keys.city));
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.city_id,city_id);
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.name,name.getText().toString());
                BuyChat.saveToPreferences(BuyChat.getAppContext(),Keys.user_image,uploadPicture);
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                getActivity().finish();
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

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    private static final int REQUEST_STROGE = 0;
    private static final int REQUEST_CAMERA = 1;
    private void requestStoragePermission() {


        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i("TAG",
                    "Displaying storage permission rationale to provide additional context.");

            Snackbar.make(getView(), "Storage permission is needed to fetch the photo.",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_STROGE);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STROGE);
        }
        // END_INCLUDE(camera_permission_request)
    }


    public void showCamera() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i("TAG", "Camera permission is needed to show the camera preview.");
            Snackbar.make(getView(), "Camera permission is needed to show the camera preview.",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {
            Log.i("TAG", "Camera permission is needed");
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
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
        Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null,
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
}
