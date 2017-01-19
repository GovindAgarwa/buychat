package com.buychat;

/**
 * Created by Snyxius Technologies on 7/18/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.buychat.activities.ChatMainActivity;
import com.buychat.activities.HomeActivity;
import com.buychat.activities.Splash;
import com.buychat.api.Parse;
import com.buychat.app.BuyChat;
import com.buychat.extras.Constants;
import com.buychat.extras.Keys;
import com.buychat.pojos.Chat;
import com.buychat.pojos.ShopsPojo;
import com.buychat.singleton.DataSingleton;
import com.buychat.utils.chats.Status;
import com.buychat.utils.chats.UserType;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    ArrayList<Chat> arrayList = new ArrayList<>();
    String image = "";
    String message_text = "";
    String merchant_name = "";
    String merchant_message = "";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("Push Data "+remoteMessage.getData().toString());
        image = "";
        message_text = "";
        merchant_name = "";
        merchant_message = "";
        BuyChat.saveToPreferences(getApplicationContext(),Keys.from,remoteMessage.getData().get(Keys.from));
        if(!remoteMessage.getData().get(Keys.image).equals(Constants.DEFAULT_STRING)) {
            image = remoteMessage.getData().get(Keys.image);
            message_text =  remoteMessage.getData().get(Keys.message);
        }
        merchant_message =  remoteMessage.getData().get(Keys.message);
        merchant_name = remoteMessage.getData().get(Keys.buisness_name);

        arrayList = new ArrayList<>();
        final Chat message = new Chat();
        message.setMessageStatus(Status.SENT);
            if(remoteMessage.getData().get(Keys.message).equals(Constants.DEFAULT_STRING)){
                message.setMessageText("Image");
                message.setImage(remoteMessage.getData().get(Keys.image));
            }else {
                message.setMessageText(remoteMessage.getData().get(Keys.message));

            }
    //    message.setMessageText(remoteMessage.getData().get(Keys.message));
        message.setImage(remoteMessage.getData().get(Keys.image));
        message.setUserType(UserType.SELF);
        message.setMessageTime(new Date().getTime());
        message.setMerchant_id(remoteMessage.getData().get(Keys.merchant_id));
        message.setBusiness_name(remoteMessage.getData().get(Keys.buisness_name));
        message.setMerchant_image(remoteMessage.getData().get(Keys.merchant_image));
        message.setFrom(remoteMessage.getData().get(Keys.from));
        message.setFlag("1");
        message.setCount(0);
        BuyChat.dbHelper.InsertChat(message);


        InboxNotification();
    }
    // [END receive_message]


    private void showNotification() {
        Intent intent = new Intent(this, Splash.class);
        intent.putExtra(Keys.position,Constants.INT_ONE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
        .setLargeIcon(largeIcon)
                .setSmallIcon(getNotificationIcon())
                .setSound(sound)
                .setContentTitle(DataSingleton.getInstance().getData().getBusiness_name())
                .setContentText(DataSingleton.getInstance().getData().getMessage())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimaryDark));

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_transparent : R.mipmap.ic_launcher;
    }

    private void InboxNotification(){
        //set intents and pending intents to call activity on click of "show activity" action button of notification
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//Assign inbox style notification
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        inboxStyle.setBigContentTitle("BuyChat");
      //  NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();




        arrayList = BuyChat.dbHelper.getAllChat();
        int message_count=0;
        int merchant_count=0;

        String merchant_image = "";
        String business_name = "";
        String merchant_id = "";

        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getFlag().equals("1")) {
                merchant_count++;
                business_name =  arrayList.get(i).getBusiness_name();
                merchant_id = arrayList.get(i).getMerchant_id();
                merchant_image = arrayList.get(i).getMerchant_image();

            }
                for(int j=0;j<BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).size();j++){
                    if(BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getFlag().equals("1")){
                        message_count++;
                      if(!BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getImage().equals(Constants.DEFAULT_STRING)) {
                    //     image = BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getImage();
                    //        message_text =  BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getMessageText();
                      }
                        inboxStyle.addLine(BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getBusiness_name()+" : "
                                +BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getMessageText());

                        BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getBusiness_name();
                        BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getMessageText();
                        BuyChat.dbHelper.UpdateCountChat(BuyChat.dbHelper.getChatByMerchant_idOrderBy(arrayList.get(i).getMerchant_id()).get(j).getMerchant_id(),(j+1));
                    }
                }

        }

        inboxStyle.setSummaryText(merchant_count+" conversations "+message_count+" messages");
        Intent resultIntent;
        PendingIntent piResult;
        if(merchant_count == 1){

            BuyChat.saveToPreferences(getApplicationContext(),Keys.merchant_id,merchant_id);
            BuyChat.saveToPreferences(getApplicationContext(),Keys.business_name,business_name);
            BuyChat.saveToPreferences(getApplicationContext(),Keys.merchant_image,merchant_image);
            BuyChat.saveToPreferences(getApplicationContext(),Keys.chatlist,Constants.True);


            if(image.equals(Constants.DEFAULT_STRING)) {
                resultIntent = new Intent(this, Splash.class);
                resultIntent.putExtra(Keys.position, Constants.INT_ONE);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                piResult = PendingIntent.getActivity(this,
                        (int) Calendar.getInstance().getTimeInMillis(), resultIntent, 0);

                //build notification
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(getNotificationIcon())
                                .setSound(sound)
                                .setContentTitle(merchant_name)
                                .setContentText(merchant_message)
                                .setStyle(inboxStyle)
                                .setAutoCancel(true)
                                .setContentIntent(piResult)
                                .setColor(getResources().getColor(R.color.colorPrimaryDark));
// Gets an instance of the NotificationManager service
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, mBuilder.build());
            }else{

                new generatePictureStyleNotification(this,business_name,message_text , image).execute();

            }

        }else {
             resultIntent = new Intent(this, Splash.class);
            resultIntent.putExtra(Keys.position,Constants.DEFAULT_INT);
             resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             piResult = PendingIntent.getActivity(this,
                    (int) Calendar.getInstance().getTimeInMillis(), resultIntent, 0);

            //build notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(getNotificationIcon())
                            .setSound(sound)
                            .setContentTitle(merchant_name)
                            .setContentText(merchant_message)
                            .setStyle(inboxStyle)
                            .setAutoCancel(true)
                            .setContentIntent(piResult)
                            .setColor(getResources().getColor(R.color.colorPrimaryDark));
// Gets an instance of the NotificationManager service
            NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build());

        }




//
    }

    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
            notiStyle.setBigContentTitle(title);
            notiStyle.setSummaryText(message);
            notiStyle.bigPicture(result);

            Intent intent = new Intent(mContext, Splash.class);
            intent.putExtra(Keys.position,Constants.INT_ONE);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                            .setSmallIcon(getNotificationIcon())
                            .setSound(sound)
                            .setContentTitle(merchant_name)
                            .setContentText(merchant_message)
                            .setStyle(notiStyle)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setColor(getResources().getColor(R.color.colorPrimaryDark));
// Gets an instance of the NotificationManager service
            NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build());
        }
    }
}
