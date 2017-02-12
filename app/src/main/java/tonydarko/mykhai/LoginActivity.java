package tonydarko.mykhai;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tonydarko.mykhai.Utils.NetworkStatusChecker;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn;
    EditText login, password;
    Intent mainIntent;
    NetworkStatusChecker networkStatusChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password) ;
        btn = (Button) findViewById(R.id.btn);

        mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        networkStatusChecker = new NetworkStatusChecker();

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (NetworkStatusChecker.isNetworkAvailable(LoginActivity.this)){
            LoginActivity.this.startActivity(mainIntent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            LoginActivity.this.finish();
        }else{
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }
}
