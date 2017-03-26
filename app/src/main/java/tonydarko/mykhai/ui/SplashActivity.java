package tonydarko.mykhai.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.preference.PreferenceManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import tonydarko.mykhai.R;
import tonydarko.mykhai.Utils.Constant;

import static tonydarko.mykhai.ui.LogginActivity.info;


public class SplashActivity extends Activity {
    Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        mainIntent = new Intent(this, MainActivity.class);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(SplashActivity.this, LogginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                    SplashActivity.this.finish();
                }
            }, 3000);
        }
    }
