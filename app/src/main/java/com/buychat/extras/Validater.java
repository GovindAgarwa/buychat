package com.buychat.extras;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AMAN on 06-11-2015.
 */
public class Validater {

//    public static boolean isValidEmail(String email) {
//        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
//    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public static boolean isValidPassword(String password) {
        String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&()~_+]).{8,20})";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidMobile(String mobile) {
        String MOBILE_PATTERN = "^[2-9]{2}[0-9]{8}$";
        Pattern pattern = Pattern.compile(MOBILE_PATTERN);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

//    public static Date GetUTCdatetimeAsDate()
//    {
//        //note: doesn't check for null
//        return StringDateToDate(GetUTCdatetimeAsString());
//    }
//
//    public static String GetUTCdatetimeAsString()
//    {
//        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        final String utcTime = sdf.format(new Date());
//
//        return utcTime;
//    }
//
//    public static Date StringDateToDate(String StrDate)
//    {
//        Date dateToReturn = null;
//        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
//
//        try
//        {
//            dateToReturn = (Date)dateFormat.parse(StrDate);
//        }
//        catch (ParseException e)
//        {
//            e.printStackTrace();
//        }
//
//        return dateToReturn;
//    }
public static String ReviewFormatConvert(String date){
    String startDateString2 = "";
    try {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //df.setTimeZone(TimeZone.getTimeZone("EDT"));
        df.setTimeZone(TimeZone.getTimeZone("GMT-04:00"));
        Date timestamp = null;
        DateFormat df2 = new SimpleDateFormat("dd MMM yyyy HH:mm");
        timestamp = df.parse(date);
        df.setTimeZone(TimeZone.getTimeZone("GMT+05:00"));
        System.out.println("Old = " + date);
        String parsed = df.format(timestamp);
        System.out.println("New = " + parsed);
        startDateString2 = parsed;
    } catch (ParseException e) {
        e.printStackTrace();
    }
    return startDateString2;
}



    public static Date currentDateAndTime() {
        TimeZone.setDefault(TimeZone.getTimeZone("IST"));
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        f.setTimeZone(TimeZone.getTimeZone("GMT"));
        final Date currentDate = new Date();
        f.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        Log.d("CurrentDate", f.format(currentDate));
        return currentDate;

    }

    public static String validateDateAndTime(String date){


        String time = Constants.DEFAULT_STRING;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("EDT"));
            final Date futureDate = dateFormat.parse(date);
           // dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            Log.d("futureDate", dateFormat.format(futureDate));

            TimeZone.setDefault(TimeZone.getTimeZone("IST"));
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            f.setTimeZone(TimeZone.getTimeZone("GMT"));
            final Date currentDate = new Date();
            f.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            Log.d("CurrentDate", dateFormat.format(currentDate));


            if (currentDate.compareTo(futureDate) < 0) {
                long diff = futureDate.getTime()
                        - currentDate.getTime();
                long days = diff / (24 * 60 * 60 * 1000);
                diff -= days * (24 * 60 * 60 * 1000);
                long hours = diff / (60 * 60 * 1000);
                diff -= hours * (60 * 60 * 1000);
                long minutes = diff / (60 * 1000);
                diff -= minutes * (60 * 1000);
                long seconds = diff / 1000;

                Log.v(Keys.locked_on + "Lesser", " " + String.format("%02d", days) + " " + String.format("%02d", hours)
                        + " " + String.format("%02d", minutes) + " " + String.format("%02d", seconds));
                time = Constants.DEFAULT_STRING ;

            } else if (currentDate.compareTo(futureDate) > 0) {

                long diff = futureDate.getTime()
                        - currentDate.getTime();
                long days = diff / (24 * 60 * 60 * 1000);
                diff -= days * (24 * 60 * 60 * 1000);
                long hours = diff / (60 * 60 * 1000);
                diff -= hours * (60 * 60 * 1000);
                long minutes = diff / (60 * 1000);
                diff -= minutes * (60 * 1000);
                long seconds = diff / 1000;
                Log.v(Keys.locked_on + "Greater", " " + String.format("%02d", days) + " " + String.format("%02d", hours)
                        + " " + String.format("%02d", minutes) + " " + String.format("%02d", seconds));
                time = " "+ String.format("%02d", days) + " " + String.format("%02d", hours)
                        + " " + String.format("%02d", minutes) + " " + String.format("%02d", seconds);
            }else{
                long diff = futureDate.getTime()
                        - currentDate.getTime();
                long days = diff / (24 * 60 * 60 * 1000);
                diff -= days * (24 * 60 * 60 * 1000);
                long hours = diff / (60 * 60 * 1000);
                diff -= hours * (60 * 60 * 1000);
                long minutes = diff / (60 * 1000);
                diff -= minutes * (60 * 1000);
                long seconds = diff / 1000;
                Log.v(Keys.locked_on + "Equal", " " + String.format("%02d", days) + " " + String.format("%02d", hours)
                        + " " + String.format("%02d", minutes) + " " + String.format("%02d", seconds));
                time =  " " + String.format("%02d", days) + " " + String.format("%02d", hours)
                        + " " + String.format("%02d", minutes) + " " + String.format("%02d", seconds);
            }



            return time;

        }catch (Exception e){
            e.printStackTrace();
        }
        return time;
    }

    public static String getDisplayableTime(long delta)
    {
        long difference=0;
        Long mDate = java.lang.System.currentTimeMillis();

        if(mDate > delta)
        {
            difference= mDate - delta;
            final long seconds = difference/1000;
            final long minutes = seconds/60;
            final long hours = minutes/60;
            final long days = hours/24;
            final long months = days/31;
            final long years = days/365;

            if (seconds < 0)
            {
                return "not yet";
            }
            else if (seconds < 60)
            {
                return seconds == 1 ? "just now" : "just now";
            }
            else if (seconds < 120)
            {
                return "a min ago";
            }
            else if (seconds < 2700) // 45 * 60
            {
                return minutes + " min ago";
            }
            else if (seconds < 5400) // 90 * 60
            {
                return "an hour ago";
            }
            else if (seconds < 86400) // 24 * 60 * 60
            {
                return hours + " hour ago";
            }
            else if (seconds < 172800) // 48 * 60 * 60
            {
                return "yesterday";
            }
            else if (seconds < 2592000) // 30 * 24 * 60 * 60
            {
                return days + " day ago";
            }
            else if (seconds < 31104000) // 12 * 30 * 24 * 60 * 60
            {

                return months <= 1 ? "1 month ago" : days + " months ago";
            }
            else
            {

                return years <= 1 ? "1 yr ago" : years + " yrs ago";
            }
        }
        return null;
    }
}
