package tonydarko.mykhai.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import tonydarko.mykhai.R;
import tonydarko.mykhai.Utils.Constant;
import tonydarko.mykhai.Utils.NetworkStatusChecker;

public class LogginActivity extends Activity implements View.OnClickListener {

    ParserToken parserToken;
    ProgressDialog progressDialog;
    Boolean noOrYes = false;
    boolean displayNotifications = false;
    static String info = "";
    static String danger = "";
    Map<String, String> common;
    FloatingActionButton btn;
    Button noRegBtn;
    String savedLogin, savedPass;
    SharedPreferences setting;
    String myLogin, myPassword;
    TextInputLayout inputLogin, inputPass;
    TextInputEditText login, pass;
    Intent mainIntent;
    Document doc3, doc;
    NetworkStatusChecker networkStatusChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loggin);

        inputLogin = (TextInputLayout) findViewById(R.id.input_login);
        login = (TextInputEditText) findViewById(R.id.login);

        inputPass = (TextInputLayout) findViewById(R.id.input_pass);
        pass = (TextInputEditText) findViewById(R.id.password);

        noRegBtn = (Button) findViewById(R.id.no_reg_btn);
        btn = (FloatingActionButton) findViewById(R.id.btn);

        networkStatusChecker = new NetworkStatusChecker();

        mainIntent = new Intent(this, MainActivity.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String displayNotificationKey = this.getString(R.string.pref_enable_save_password);
        displayNotifications = prefs.getBoolean(displayNotificationKey,
                Boolean.parseBoolean(this.getString(R.string.pref_enable_save_password_default)));
        setInputText();
        noRegBtn.setOnClickListener(this);
        btn.setOnClickListener(this);

        if (NetworkStatusChecker.isNetworkAvailable(LogginActivity.this)) {
            if (displayNotifications) {
                setting = getSharedPreferences("LogPass", Context.MODE_PRIVATE);
                savedLogin = setting.getString("Login", "");
                savedPass = setting.getString("Password", "");
                if (savedLogin.length() != 0 & savedPass.length() != 0) {
                    Boolean noOrYes = true;
                    Constant.setNoOrYes(noOrYes);
                    final LogginWithSaved logginWithSaved = new LogginWithSaved();
                    logginWithSaved.execute();
                }
            }
        } else {
            Constant.setInfo("");
            Snackbar.make(findViewById(android.R.id.content), R.string.login_no_intenet, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_reg_btn:
                noOrYes = false;
                Constant.setNoOrYes(noOrYes);
                if (NetworkStatusChecker.isNetworkAvailable(LogginActivity.this)) {
                    LogginActivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    LogginActivity.this.finish();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.login_no_intenet, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                }
                break;

            case R.id.btn:
                if (NetworkStatusChecker.isNetworkAvailable(LogginActivity.this)) {
                    myLogin = login.getText().toString();
                    myPassword = pass.getText().toString();
                    if (myLogin.length() != 0 & myPassword.length() != 0) {
                        parserToken = new ParserToken();
                        try {
                            parserToken.execute().get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (Constant.getInfo().length() != 0) {
                            noOrYes = true;
                            Constant.setNoOrYes(noOrYes);
                            if (displayNotifications) {
                                    setting = getSharedPreferences("LogPass", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = setting.edit();
                                    editor.putString("Login", myLogin);
                                    editor.putString("Password", myPassword);
                                    editor.apply();
                            }
                            LogginActivity.this.startActivity(mainIntent);
                            startActivity(mainIntent);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            LogginActivity.this.finish();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), R.string.not_corr_logpass, Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.RED)
                                    .show();
                        }
                    } else
                        Snackbar.make(findViewById(android.R.id.content), R.string.logorpass_empty, Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED)
                                .show();
                } else
                    Snackbar.make(findViewById(android.R.id.content), R.string.login_no_intenet, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                break;
        }
    }

    private class ParserToken extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg) {
            Connection.Response resp1 = null;
            String token = null;
            try {
                resp1 = Jsoup.connect(Constant.getUrl())
                        .method(Connection.Method.GET)
                        .execute();
                assert resp1 != null;
                doc = resp1.parse();
                token = resp1.parse().getElementsByTag("div").first().val().trim();
                for (Element meta : doc.select("input")) {
                    if (meta.attr("name").equals("_csrf")) {
                        token = meta.attr("value");
                    }
                }

                // Упаковываю все в пост и отправляю
                Connection.Response resp2 = null;
                resp2 = Jsoup.connect(Constant.getUrl())
                        .referrer("http://www.google.com")
                        .userAgent(Constant.getUserAgent())
                        .data("username", myLogin)
                        .data("password", myPassword)
                        .data("_csrf", token)
                        .cookies(resp1.cookies())
                        .method(Connection.Method.POST).timeout(10000).execute();

                assert resp2 != null;
                common = resp2.cookies();
                Constant.setCommon(common);
                Constant.setToken(token);

                Constant.setMyLogin(myLogin);
                Constant.setMyPassword(myPassword);

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
                if (clas.text().startsWith("Шановний (а),")) {
                    info = clas.text();
                    Constant.setInfo(info);
                }
            }
            String danger;
            for (Element clas : doc3.getElementsByTag("div")) {
                if (clas.getElementsByClass("alert").size() != 0) {
                    danger = clas.getElementsByClass("alert").text();
                    Constant.setDanger(danger);
                    break;
                }
            }

            return info;
        }
    }

    private class LogginWithSaved extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LogginActivity.this);
            progressDialog.setTitle("Авторизація");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... arg) {
            Connection.Response resp1 = null;
            String token = null;
            try {
                resp1 = Jsoup.connect(Constant.getUrl())
                        .method(Connection.Method.GET)
                        .execute();
                assert resp1 != null;
                doc = resp1.parse();
                if (doc != null) {
                    token = resp1.parse().getElementsByTag("div").first().val().trim();
                    for (Element meta : doc.select("input")) {
                        if (meta.attr("name").equals("_csrf")) {
                            token = meta.attr("value");
                        }
                    }
                    // Упаковываю все в пост и отправляю
                    Connection.Response resp2 = null;

                    resp2 = Jsoup.connect(Constant.getUrl())
                            .referrer("http://www.google.com")
                            .userAgent(Constant.getUserAgent())
                            .data("username", savedLogin)
                            .data("password", savedPass)
                            .data("_csrf", token)
                            .cookies(resp1.cookies())
                            .method(Connection.Method.POST).timeout(10000).execute();

                    assert resp2 != null;
                    common = resp2.cookies();
                    Constant.setCommon(common);
                    Constant.setToken(token);

                    Constant.setMyLogin(savedLogin);
                    Constant.setMyPassword(savedPass);

                    doc3 = Jsoup.connect("http://my.khai.edu/my/")
                            .referrer("http://www.google.com")
                            .userAgent(Constant.getUserAgent())
                            .data("_csrf", token)
                            .cookies(Constant.getCommon())
                            .timeout(10000)
                            .get();

                    assert doc3 != null;
                    for (Element clas : doc3.getElementsByClass("lead")) {
                        if (clas.text().startsWith("Шановний (а),")) {
                            String info = clas.text();
                            Constant.setInfo(info);
                        }
                    }
                    String danger;
                    for (Element clas : doc3.getElementsByTag("div")) {
                        if (clas.getElementsByClass("alert").size() != 0) {
                            danger = clas.getElementsByClass("alert").text();
                            Constant.setDanger(danger);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return info;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            progressDialog.dismiss();
            if (doc != null) {
                LogginActivity.this.startActivity(mainIntent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                LogginActivity.this.finish();
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LogginActivity.this);
                alertDialog.setTitle(R.string.login_no_auth);

                alertDialog.setMessage(R.string.AD_check_internet);

                alertDialog.setPositiveButton(R.string.AD_repeat, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final LogginWithSaved logginWithSaved = new LogginWithSaved();
                        logginWithSaved.execute();
                    }
                });

                alertDialog.setNegativeButton(R.string.AD_exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }
        }
    }


    public void setInputText() {
        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    btn.setEnabled(false);
                    inputLogin.setError(getString(R.string.login_login_empty));
                } else if (charSequence.length() != 0) {
                    btn.setEnabled(true);
                    inputLogin.setErrorEnabled(false);
                }
                if (charSequence.length() >= 14) {
                    btn.setEnabled(false);
                    inputLogin.setError(getString(R.string.login_login_large));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    btn.setEnabled(false);
                    inputPass.setError(getString(R.string.login_pass_empty));
                } else if (charSequence.length() != 0) {
                    btn.setEnabled(true);
                    inputPass.setErrorEnabled(false);
                }
                if (charSequence.length() >= 14) {
                    btn.setEnabled(false);
                    inputPass.setError(getString(R.string.login_pass_large));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LogginActivity.this);
        alertDialog.setTitle(R.string.login_q_exit);

        alertDialog.setMessage(R.string.login_exit_quest);

        alertDialog.setPositiveButton(R.string.login_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                LogginActivity.this.finish();
            }
        });

        alertDialog.setNegativeButton(R.string.login_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

}
