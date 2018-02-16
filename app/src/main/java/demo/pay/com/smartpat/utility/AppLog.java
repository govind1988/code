package demo.pay.com.smartpat.utility;

import android.util.Log;


public class AppLog {

    public static String  TAG="SMARTPAY";
    public  static String  ANIMATION=" Base Animation";
    public static String FRAGMENT_ANIM="Fragment Animation";
    static boolean flag=false;

    public static void d(String fTag,String fMsg){
        if(flag){
            Log.d(TAG,String.format("%s ### %s",fTag,fMsg));
        }

    }
    public static void i(String fTag,String fMsg){
        if(flag){
            Log.i(TAG, String.format("%s ### %s", fTag, fMsg));
        }

    }
    public static void e(String fTag,String fMsg){
        if(flag){
            Log.e(TAG,String.format("%s ### %s",fTag,fMsg));
        }

    }
}