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
    SharedPreferences setting;
    boolean displayNotifications = false;
    ProgressDialog progressDialog;
    String savedLogin, savedPass;
    Intent mainIntent;
    Map<String, String> common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        mainIntent = new Intent(this, MainActivity.class);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String displayNotificationKey = this.getString(R.string.pref_enable_save_password);
        displayNotifications = prefs.getBoolean(displayNotificationKey,
                Boolean.parseBoolean(this.getString(R.string.pref_enable_save_password_default)));
        System.out.println("display " + displayNotifications);
        if (displayNotifications){
        setting = getSharedPreferences("LogPass", Context.MODE_PRIVATE);
        savedLogin = setting.getString("Login", "");
        savedPass = setting.getString("Password", "");
        if (savedLogin.length() != 0) {
            Boolean noOrYes = true;
            Constant.setNoOrYes(noOrYes);
            final ParserToken parserToken = new ParserToken();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        parserToken.execute().get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }, 3000);
        }
        } else {
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

        System.out.println(savedLogin);
        System.out.println(savedPass);

    }

    private class ParserToken extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog = new ProgressDialog(SplashActivity.this);
            progressDialog.setTitle("Отправка запроса");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... arg) {
            Connection.Response resp1 = null;
            String token = null;
            try {
                resp1 = Jsoup.connect(Constant.getUrl())
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert resp1 != null;
                Document doc = resp1.parse();
                token = resp1.parse().getElementsByTag("div").first().val().trim();
                for (Element meta : doc.select("input")) {
                    if (meta.attr("name").equals("_csrf")) {
                        token = meta.attr("value");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Упаковываю все в пост и отправляю
            Connection.Response resp2 = null;
            try {
                resp2 = Jsoup.connect(Constant.getUrl())
                        .referrer("http://www.google.com")
                        .userAgent(Constant.getUserAgent())
                        .data("username", savedLogin)
                        .data("password", savedPass)
                        .data("_csrf", token)
                        .cookies(resp1.cookies())
                        .method(Connection.Method.POST).timeout(10000).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert resp2 != null;
            common = resp2.cookies();
            Constant.setCommon(common);
            Constant.setToken(token);

            Constant.setMyLogin(savedLogin);
            Constant.setMyPassword(savedPass);

            Document doc3 = null;
            try {
                doc3 = Jsoup.connect("http://my.khai.edu/my/")
                        .referrer("http://www.google.com")
                        .userAgent(Constant.getUserAgent())
                        .data("_csrf", token)
                        .cookies(Constant.getCommon())
                        .timeout(10000)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert doc3 != null;
            for (Element clas : doc3.getElementsByClass("lead")) {
                System.out.println(clas);
                if (clas.text().startsWith("Шановний (а),")) {
                    String info = clas.text();
                    System.out.println(info);
                    Constant.setInfo(info);
                }
            }
            return info;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            //     progressDialog.dismiss();
            SplashActivity.this.startActivity(mainIntent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);

        }
    }

}
