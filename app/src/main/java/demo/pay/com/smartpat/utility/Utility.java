package demo.pay.com.smartpat.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {

    private static String TAG=Utility.class.getSimpleName();

    public static SharedPreferences getSharedPref(Context mContext){
        return mContext.getSharedPreferences("mypref",0);
    }

    public static String getUserProfile(Context mContext){
        return getSharedPref(mContext).getString("UserProfile","");
    }
    public static String getEmail(Context mContext){
        return getSharedPref(mContext).getString("UserEmail","");
    }
    public static String getEncryptedEmail(Context mContext){
        String mail =  getSharedPref(mContext).getString("UserEmail","");
        if(!TextUtils.isEmpty(mail)){
            int indexofSeperator = mail.indexOf("@");
            String subString = mail.substring(1,indexofSeperator);
            mail.replaceAll(subString,"*");
            return  mail;
        }
        return mail;
    }

    public static boolean isValidMobileNo(String str){

        String mobilepattern = "^(?:0091|\\\\+91|0)[7-9][0-9]{9}$";

        Pattern p = Pattern.compile(mobilepattern);//. represents single character
        Matcher m = p.matcher(str);
        boolean b = m.matches();
        return  b;
    }
    public static String loadLocalJson(Context mContext,String jsonfilePath) throws IOException {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open(jsonfilePath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static boolean isEmpty(String str){
        boolean flag = true;
        if(str != null && !str.isEmpty()){
            flag = false;

        }
        return flag;
    }
    public static Date getDateFromString(String str){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            Date date =dateFormat.parse(str);
            return  date;

        }
        catch (ParseException e){

        }
        return new Date();
    }

    public static boolean isFutureDate(String data){
        //String dateFormat = ""//12-03-2016
       // AppLog.d(TAG,"date to check@@@@@@@@@@@@@@@@"+data);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            Date date =dateFormat.parse(data);
         //   AppLog.d(TAG,"date is @@@@@@@@@@@@@@@@@@@@"+date.toString());

            // reuse the calendar to set user specified date
            Calendar c = Calendar.getInstance();
            // set the calendar to start of today
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

         //   AppLog.d("Utility","Todayz time@@@@"+c.toString());
            // and get that as a Date
            Date today = c.getTime();

            if(date.before(today)){
                return false;
            }
            else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public  static boolean isConnectedToNetwork(Context mContext) {
        int[] NETWORK_TYPES = {ConnectivityManager.TYPE_WIFI,
                ConnectivityManager.TYPE_ETHERNET};
        ConnectivityManager connManager =
                (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        for (int networkType : NETWORK_TYPES) {
            NetworkInfo info = connManager.getNetworkInfo(networkType);
            if (info != null && info.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGPSEnabled(Context mContext){
        LocationManager service = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return  enabled;
        // Check if enabled and if not send user to the GPS settings
       /* if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
        }*/
    }
    /**
     * Method to verify google play services on the device
     * */
   /* public static boolean checkPlayServices(Activity mContext) {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, mContext,
                        1000).show();
            } else {
                Toast.makeText(mContext,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();

            }
            return false;
        }
        return true;
    }*/

    public static String replaceNullString(String str){

        if(TextUtils.isEmpty(str)){
            return "";
        }
        return str;
    }


    public static String convert12HoursTo24Hrs(String time){
        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat date24Format = new SimpleDateFormat("HH:mm");

        String newTime = "";
        try {
            newTime = date24Format.format(date12Format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newTime;

    }


}
