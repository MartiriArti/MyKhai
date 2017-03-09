package tonydarko.mykhai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import tonydarko.mykhai.Utils.Constant;
import tonydarko.mykhai.Utils.NetworkStatusChecker;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ParserToken parserToken;
    ProgressDialog progressDialog;
    Boolean noOrYes = false;
    static String info = "";
    Map<String, String> common;
    FloatingActionButton btn;
    Button noRegBtn;
    String myLogin, myPassword;
    TextInputLayout inputLogin, inputPass;
    TextInputEditText login, pass;
    Intent mainIntent;
    NetworkStatusChecker networkStatusChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLogin = (TextInputLayout) findViewById(R.id.input_login);
        login = (TextInputEditText) findViewById(R.id.login);

        inputPass = (TextInputLayout) findViewById(R.id.input_pass);
        pass = (TextInputEditText) findViewById(R.id.password);

        noRegBtn = (Button) findViewById(R.id.no_reg_btn);
        btn = (FloatingActionButton) findViewById(R.id.btn);

        networkStatusChecker = new NetworkStatusChecker();

        mainIntent = new Intent(this, MainActivity.class);

        setInputText();
        noRegBtn.setOnClickListener(this);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_reg_btn:
                noOrYes = false;
                Constant.setNoOrYes(noOrYes);
                if (NetworkStatusChecker.isNetworkAvailable(LoginActivity.this)) {
                    LoginActivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(this, R.string.login_no_intenet, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btn:
                if (NetworkStatusChecker.isNetworkAvailable(LoginActivity.this)) {
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
                            LoginActivity.this.startActivity(mainIntent);
                            startActivity(mainIntent);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            LoginActivity.this.finish();
                        } else {
                            Toast.makeText(this, "Логин или пароль не верны", Toast.LENGTH_LONG).show();
                        }
                    } else
                        Toast.makeText(this, "Логин или пароль пуст", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(this, R.string.login_no_intenet, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public class ParserToken extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Отправка запроса");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... arg) {
            Connection.Response resp1 = null;
            try {
                resp1 = Jsoup.connect(Constant.getUrl())
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String token = null;
            try {
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
                        .data("username", myLogin)
                        .data("password", myPassword)
                        .data("_csrf", token)
                        .cookies(resp1.cookies())
                        .method(Connection.Method.POST).timeout(10000).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            common = resp2.cookies();
            Constant.setCommon(common);
            Constant.setToken(token);

            Constant.setMyLogin(myLogin);
            Constant.setMyPassword(myPassword);

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

            for (Element clas : doc3.getElementsByClass("lead")) {
                System.out.println(clas);
                if (clas.text().startsWith("Шановний (а),")) {
                    info = clas.text();
                    System.out.println(info);
                    Constant.setInfo(info);
                }
            }
            return info;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            progressDialog.dismiss();

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

}
