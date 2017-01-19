package com.buychat.utils;

/**
 * Created by Snyxius Technologies on 6/9/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.buychat.R;
import com.buychat.app.BuyChat;



public  class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     *                want icon
     */
    public void showAlertDialog(final Context context, String title,
                                String message) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(message);


        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        //alertDialog.setMessage(message);

//		TextView tvMesg = new TextView(context);//(TextView)alertDialog.findViewById(android.R.id.message);
//		tvMesg.setText(message);
//		int px = RoadLikeUtilities.dpToPx(context, 5);
//		tvMesg.setPadding(px, px, px, px);
//		tvMesg.setTextSize(RoadLikeUtilities.spToPixels(context, 6.5f));
//		alertDialog.setView(tvMesg);
//


                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });



        // Showing Alert Message
        alertDialog.create();
        alertDialog.show();
    }


    public static void showCurrentLocAlertDialog(final Context context,
                                           String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage(message);


            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });


        alertDialog.create();
        alertDialog.show();
    }


    public static void showDialog(ProgressDialog dialogs){
        dialogs.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialogs.setIndeterminate(true);
     //   dialogs.setCancelable(false);
    //    dialogs.setIndeterminateDrawable(BuyChat.getAppContext().getResources().getDrawable(R.drawable.progress));
        dialogs.show();

    }

    public static void showDialog(ProgressDialog dialogs,boolean flag){
        dialogs.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialogs.setIndeterminate(true);
        dialogs.setCancelable(flag);
        //    dialogs.setIndeterminateDrawable(BuyChat.getAppContext().getResources().getDrawable(R.drawable.progress));
        dialogs.show();

    }

    public static void dismissDialog(ProgressDialog dialogs){
        if (dialogs != null && dialogs.isShowing()) {
            dialogs.dismiss();
            dialogs = null;
        }
    }
}
