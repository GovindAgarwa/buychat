package com.buychat.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.buychat.R;
import com.buychat.activities.HomeActivity;
import com.buychat.activities.Splash;
import com.buychat.adapter.ChatAdapter;
import com.buychat.api.Parse;
import com.buychat.api.WebRequests;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.singleton.DataSingleton;
import com.buychat.singleton.IMerchantEvents;
import com.buychat.singleton.ISocketEvents;
import com.buychat.singleton.MerchantHandler;
import com.buychat.singleton.SocketHandler;
import com.buychat.singleton.SocketSingleton;
import com.buychat.utils.chats.AndroidUtilities;
import com.buychat.pojos.Chat;
import com.buychat.utils.chats.Status;
import com.buychat.utils.chats.UserType;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snyxius Technologies on 8/10/2016.
 */
public class ChatFragment extends Fragment implements Callback<JsonObject> {

    private ProgressDialog dialog;
    private Unbinder unbinder;

    @BindView(R.id.enter_chat1)
    ImageView enterChatView1;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.chat_edit_text1)
    EditText chatEditText1;
    private String uploadPicture = "";
    private ArrayList<Chat> chatMessages;
    @BindView(R.id.chat_list_view)
    ListView chatListView;
    private int keyboardHeight;
    private boolean keyboardVisible;
    private WindowManager.LayoutParams windowLayoutParams;
    private ChatAdapter listAdapter;
   // MerchantHandler merchantSocketHandler;
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static ChatFragment newInstance(int position) {
        Bundle args = new Bundle();
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public void ApiCall(){
        WebRequests service1 = BuyChat.initializeRetrofit().create(WebRequests.class);
//        Call<JsonObject> call1 = service1.chat_view(Parse.getAccessTokenWithMerchantIDLimitOffset(
//                DataSingleton.getInstance().getData().getId()
//                ,100,Constants.DEFAULT_INT));
        Call<JsonObject> call1 = service1.update_socket(Parse.updateSocket());
        call1.enqueue(this);
    }

    @OnClick(R.id.emojiButton) void camera(){
        selectImage();
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     //   merchantSocketHandler = new MerchantHandler(this);
     //   merchantSocketHandler.connectToSocket();
        image.setVisibility(View.GONE);
            setData();
    }

    public void setData(){
        BuyChat.dbHelper.UpdateUnReadChat(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));


        AndroidUtilities.statusBarHeight = getStatusBarHeight();
        chatMessages = new ArrayList<>();
        listAdapter = new ChatAdapter(chatMessages, getActivity());
        chatListView.setAdapter(listAdapter);
        chatEditText1.setOnKeyListener(keyListener);
        enterChatView1.setOnClickListener(clickListener);
        ApiCall();

        if(BuyChat.dbHelper.getChatByMerchant_id(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING)).size() != 0){
            chatMessages.addAll(BuyChat.dbHelper.getChatByMerchant_id(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING)));
            listAdapter.notifyDataSetChanged();
        }
    }






    public void setEmitData(Activity activity,JSONObject data){
        try {

            if(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING).equals(data.getString(Keys.merchant_id))) {
                try {
                    Uri notifications = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(BuyChat.getAppContext(), notifications);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final Chat message = new Chat();
                message.setMessageStatus(Status.SENT);
                message.setUserType(UserType.SELF);
                if (data.has(Keys.message)) {
                    if(data.getString(Keys.message).equals(Constants.DEFAULT_STRING)){
                        message.setMessageText("Image");
                        message.setImage(data.getString(Keys.image));
                    }else {
                        message.setMessageText(data.getString(Keys.message));

                    }
                }
                message.setFrom(data.getString(Keys.from));
                message.setImage(data.getString(Keys.image));
                message.setMessageTime(new Date().getTime());
                message.setMerchant_id(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));
                message.setBusiness_name(BuyChat.readFromPreferences(getActivity(),Keys.business_name,Constants.DEFAULT_STRING));
                message.setMerchant_image(BuyChat.readFromPreferences(getActivity(),Keys.merchant_image,Constants.DEFAULT_STRING));
                message.setFlag("0");
                message.setCount(0);
                chatMessages.add(message);
                BuyChat.dbHelper.InsertChat(message);

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        listAdapter.notifyDataSetChanged();
                    }
                });
            }else{
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
                message.setUserType(UserType.SELF);
                message.setFrom(data.getString(Keys.from));
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

    private void InboxNotification(){
        //set intents and pending intents to call activity on click of "show activity" action button of notification
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent resultIntent = new Intent(getActivity(), Splash.class);
        resultIntent.putExtra(Keys.position,Constants.DEFAULT_INT);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent piResult = PendingIntent.getActivity(getActivity(),
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
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(getNotificationIcon())
                        .setSound(sound)
                        .setContentTitle("BuyChat Notification")
                        .setContentText("Touch to see the notification")
                        .setStyle(inboxStyle)
                        .setAutoCancel(true)
                        .setContentIntent(piResult)
                        .setColor(getResources().getColor(R.color.colorPrimaryDark));

// Gets an instance of the NotificationManager service
        NotificationManager notificationManager =(NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

//to post your notification to the notification bar
        notificationManager.notify(0, mBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_transparent : R.mipmap.ic_launcher;
    }

    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press

                EditText editText = (EditText) v;

                if(v==chatEditText1)
                {
                            final Chat message = new Chat();
                            message.setMessageStatus(Status.SENT);
                            message.setMessageText(editText.getText().toString().trim());
                            message.setUserType(UserType.OTHER);
                            message.setMessageTime(new Date().getTime());
                            chatMessages.add(message);

                            if(listAdapter!=null)
                                listAdapter.notifyDataSetChanged();
                    //merchantSocketHandler.emitToSocket(message);
                }

                chatEditText1.setText("");

                return true;
            }
            return false;

        }
    };

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
        BuyChat.saveToPreferences(getActivity(),Keys.tag_address,Constants.False);

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
        BuyChat.saveToPreferences(getActivity(),Keys.tag_address,Constants.False);
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
        System.out.println("Captured");
        BuyChat.saveToPreferences(getActivity(),Keys.tag_address,Constants.True);
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        image.setVisibility(View.VISIBLE);
        image.setImageBitmap(thumbnail);
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
        BuyChat.saveToPreferences(getActivity(),Keys.tag_address,Constants.True);
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
            image.setImageBitmap(bmRotated);
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
        System.out.println("Show");
        int nh = (int) ( bitmaps.getHeight() * (256.0 / bitmaps.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bitmaps, 256, nh, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        image.setVisibility(View.VISIBLE);
        uploadPicture = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

//    private void sendMessage(final String messageText, final UserType userType)
//    {
//        if(messageText.trim().length()==0)
//            return;
//
//        final Chat message = new Chat();
//        message.setMessageStatus(Status.SENT);
//        message.setMessageText(messageText);
//        message.setUserType(userType);
//        message.setMessageTime(new Date().getTime());
//        chatMessages.add(message);
//
//        if(listAdapter!=null)
//            listAdapter.notifyDataSetChanged();
//
//        // Mark message as delivered after one second
//
//        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
//
//        exec.schedule(new Runnable() {
//            @Override
//            public void run() {
//                message.setMessageStatus(Status.DELIVERED);
//
//                final Chat message = new Chat();
//                message.setMessageStatus(Status.SENT);
//                message.setMessageText(messageText); // 10 spaces;
//                message.setUserType(UserType.SELF);
//                message.setMessageTime(new Date().getTime());
//                chatMessages.add(message);
//
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        listAdapter.notifyDataSetChanged();
//                    }
//                });
//
//
//            }
//        }, 1, TimeUnit.SECONDS);
//
//    }

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if( v == enterChatView1)
            {
                if(uploadPicture.equals(Constants.DEFAULT_STRING)) {
                    if (chatEditText1.getText().toString().isEmpty()) {
                        return;
                    }
                }
                final Chat message = new Chat();
                message.setMessageStatus(Status.SENT);
                message.setImage(uploadPicture);
                message.setMessageText(chatEditText1.getText().toString().trim());
                message.setFrom(BuyChat.readFromPreferences(getActivity(),Keys.from,Constants.DEFAULT_STRING));
                message.setUserType(UserType.OTHER);
                message.setMessageTime(new Date().getTime());
                message.setMerchant_id(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));
                message.setBusiness_name(BuyChat.readFromPreferences(getActivity(),Keys.business_name,Constants.DEFAULT_STRING));
                message.setMerchant_image(BuyChat.readFromPreferences(getActivity(),Keys.merchant_image,Constants.DEFAULT_STRING));
                message.setFlag("0");
                message.setCount(0);
                chatMessages.add(message);

                if(listAdapter!=null)
                    listAdapter.notifyDataSetChanged();
                BuyChat.dbHelper.InsertChat(message);


                System.out.println(Keys.from+" "+BuyChat.readFromPreferences(getActivity(),Keys.from,Constants.DEFAULT_STRING));
                if(BuyChat.readFromPreferences(getActivity(),Keys.from,Constants.DEFAULT_STRING).equalsIgnoreCase(Constants.ADMIN)){
                    System.out.println(Keys.chatMessageUserToAdmin+" "+Keys.socket_id +" "+Parse.getMerchantIdAccessTokenAndMessage(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING),
                            chatEditText1.getText().toString(),""));
                    SocketSingleton.get(getActivity()).getSocket().emit(Keys.chatMessageUserToAdmin, Parse.getMerchantIdAccessTokenAndMessage(BuyChat.readFromPreferences(getActivity(), Keys.merchant_id, Constants.DEFAULT_STRING),
                            chatEditText1.getText().toString(), uploadPicture));
                }else {
                    System.out.println(Keys.chatMessageToMerchant+" "+Keys.socket_id +" "+Parse.getMerchantIdAccessTokenAndMessage(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING),
                            chatEditText1.getText().toString(),""));
                    SocketSingleton.get(getActivity()).getSocket().emit(Keys.chatMessageToMerchant, Parse.getMerchantIdAccessTokenAndMessage(BuyChat.readFromPreferences(getActivity(), Keys.merchant_id, Constants.DEFAULT_STRING),
                            chatEditText1.getText().toString(), uploadPicture));
                }
                uploadPicture="";


            }

            chatEditText1.setText("");
            image.setVisibility(View.GONE);

        }
    };



    public void sendData(){

     //   Log.d("chat",BuyChat.dbHelper.getCartProduct(DataSingleton.getInstance().getData().getId()).get(Constants.DEFAULT_INT).getProduct_image());
        final Chat message = new Chat();
        message.setMessageStatus(Status.SENT);
        message.setImage(BuyChat.dbHelper.getCartProduct(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING)).get(Constants.DEFAULT_INT).getProduct_image());
        message.setMessageText(BuyChat.dbHelper.getCartProduct(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING)).get(Constants.DEFAULT_INT).getProduct_name());
        message.setUserType(UserType.OTHER);
        message.setMessageTime(new Date().getTime());
        message.setFrom(BuyChat.readFromPreferences(getActivity(),Keys.from,Constants.DEFAULT_STRING));
        message.setMerchant_id(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));
        message.setBusiness_name(BuyChat.readFromPreferences(getActivity(),Keys.business_name,Constants.DEFAULT_STRING));
        message.setMerchant_image(BuyChat.readFromPreferences(getActivity(),Keys.merchant_image,Constants.DEFAULT_STRING));
        message.setFlag("0");
        message.setCount(0);
        chatMessages.add(message);
        if(listAdapter!=null)
            listAdapter.notifyDataSetChanged();

        BuyChat.dbHelper.InsertChat(message);

        uploadPicture = BuyChat.dbHelper.getCartProduct(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING)).get(Constants.DEFAULT_INT).getProduct_image();
        String chats = BuyChat.dbHelper.getCartProduct(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING)).get(Constants.DEFAULT_INT).getProduct_name();

        System.out.println(Keys.from+" "+BuyChat.readFromPreferences(getActivity(),Keys.from,Constants.DEFAULT_STRING));

        if(BuyChat.readFromPreferences(getActivity(),Keys.from,Constants.DEFAULT_STRING).equalsIgnoreCase(Constants.ADMIN)){
            System.out.println(Keys.chatMessageUserToAdmin+" "+Keys.socket_id +" "+Parse.getMerchantIdAccessTokenAndMessage(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING),
                    chats,""));
            SocketSingleton.get(getActivity()).getSocket().emit(Keys.chatMessageUserToAdmin, Parse.getMerchantIdAccessTokenAndMessage(BuyChat.readFromPreferences(getActivity(), Keys.merchant_id, Constants.DEFAULT_STRING),
                   chats, uploadPicture));
        }else {
            System.out.println(Keys.chatMessageToMerchant+" "+Keys.socket_id +" "+Parse.getMerchantIdAccessTokenAndMessage(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING),
                    chats,""));
            SocketSingleton.get(getActivity()).getSocket().emit(Keys.chatMessageToMerchant, Parse.getMerchantIdAccessTokenAndMessage(BuyChat.readFromPreferences(getActivity(), Keys.merchant_id, Constants.DEFAULT_STRING),
                    chats, uploadPicture));
        }
        uploadPicture="";
        chats = "";
        BuyChat.dbHelper.DeleteMerchant(BuyChat.readFromPreferences(getActivity(),Keys.merchant_id,Constants.DEFAULT_STRING));

    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if(response.code() == Constants.SUCCESS) {
            if (Parse.checkStatus(response.body().toString()).equals(Keys.success)) {
//                ArrayList arrayLists;
 //              arrayLists = Parse.parseChatView(response.body().toString());
//                if(arrayLists.size() != 0){
//                    chatMessages.addAll(arrayLists);
//                    listAdapter.notifyDataSetChanged();
//                }
            }
        }else {
            BuyChat.showAToast(Constants.Some_Went_Wrong);
        }

    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {

    }



}
