package tonydarko.mykhai;

import android.content.Intent;
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
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

import tonydarko.mykhai.Utils.NetworkStatusChecker;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    final static String URL = "http://my.khai.edu/my/login";
    final static String MyLogin = "martishkov_a";
    final static String MyPassword = "ant641448";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";
    // credentials
    FloatingActionButton btn;
    Button noRegBtn;
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

        mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        networkStatusChecker = new NetworkStatusChecker();

        setInputText();
        noRegBtn.setOnClickListener(this);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_reg_btn:
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
                    if (login.getText().length() != 0 & pass.getText().length() != 0) {
                       /* LoginActivity.this.startActivity(mainIntent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        LoginActivity.this.finish();*/


                        Toast.makeText(this, "Логин или пароль пуст", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, R.string.login_no_intenet, Toast.LENGTH_LONG).show();
                    }
                    break;
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

}
