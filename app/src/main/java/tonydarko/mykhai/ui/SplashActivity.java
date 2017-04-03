package tonydarko.mykhai.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.preference.PreferenceManager;

import tonydarko.mykhai.R;
import tonydarko.mykhai.Utils.PrefManager;


public class SplashActivity extends Activity {
    Intent mainIntent;
    private PrefManager prefManager;
    boolean displayNotifications = true;
    boolean displayIntro = true;
    SharedPreferences prefsIntro, prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String displayNotificationKey = this.getString(R.string.pref_enable_splash);
        displayNotifications = prefs.getBoolean(displayNotificationKey,
                Boolean.parseBoolean(this.getString(R.string.pref_enable_splash_default)));
        prefManager = new PrefManager(this);

        SharedPreferences prefsIntro = PreferenceManager.getDefaultSharedPreferences(this);
        String displayIntroKey = this.getString(R.string.pref_enable_intro);
        displayIntro = prefsIntro.getBoolean(displayIntroKey,
                Boolean.parseBoolean(this.getString(R.string.pref_enable_intro_default)));
        if (displayIntro){
            prefManager.setFirstTimeLaunch(true);
            System.out.println(prefManager.isFirstTimeLaunch());
        }

        if (displayNotifications) {
            setContentView(R.layout.activity_splash);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (prefManager.isFirstTimeLaunch()) {
                        mainIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();
                    } else {
                        mainIntent = new Intent(SplashActivity.this, LogginActivity.class);
                        overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();
                    }
                }
            }, 3000);
        } else {
            if (prefManager.isFirstTimeLaunch()) {
                mainIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }else{
                mainIntent = new Intent(SplashActivity.this, LogginActivity.class);
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }
    }
}