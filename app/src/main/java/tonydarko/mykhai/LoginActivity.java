package tonydarko.mykhai;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tonydarko.mykhai.Utils.NetworkStatusChecker;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
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
        inputLogin.setError("Логин пуст");
        inputLogin.setHint("Введите логин");

        inputPass = (TextInputLayout) findViewById(R.id.input_pass);
        inputPass.setError("Пароль пуст");
        inputLogin.setHint("Введите пароль");
        pass = (TextInputEditText) findViewById(R.id.password);

        btn = (Button) findViewById(R.id.btn);

        mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        networkStatusChecker = new NetworkStatusChecker();

        setInputText();
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (NetworkStatusChecker.isNetworkAvailable(LoginActivity.this)) {
            LoginActivity.this.startActivity(mainIntent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            LoginActivity.this.finish();
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void setInputText(){
        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0 ) {
                    btn.setEnabled(false);
                    inputLogin.setError("Логин пуст");
                } else {
                    btn.setEnabled(true);
                    inputLogin.setErrorEnabled(false);
                }
                if (charSequence.length() >= 14 ) {
                    btn.setEnabled(false);
                    inputLogin.setError("Не допустимая длина логина");
                } else {
                    btn.setEnabled(true);
                    inputLogin.setErrorEnabled(false);
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
                    inputPass.setError("Пароль пуст");
                } else {
                    btn.setEnabled(true);
                    inputPass.setErrorEnabled(false);
                }
                if (charSequence.length() >= 14) {
                    btn.setEnabled(false);
                    inputPass.setError("Не допустимая длина пароля");
                } else {
                    btn.setEnabled(true);
                    inputPass.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
