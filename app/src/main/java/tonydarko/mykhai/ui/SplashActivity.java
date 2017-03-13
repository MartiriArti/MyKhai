package tonydarko.mykhai.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import tonydarko.mykhai.R;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,LogginActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
                SplashActivity.this.finish();
            }
        }, 2000);
    }
}
