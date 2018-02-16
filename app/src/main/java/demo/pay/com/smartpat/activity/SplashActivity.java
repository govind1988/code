package demo.pay.com.smartpat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import demo.pay.com.smartpat.R;
import demo.pay.com.smartpat.utility.AppConstant;
import demo.pay.com.smartpat.utility.Utility;


public class SplashActivity extends AppCompatActivity {
Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_splash);
        mContext=this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(Utility.getSharedPref(mContext).getBoolean(AppConstant.isLoggedIn,false)) {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
                else{

                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }

            }
        }, 1000);
    }




}
