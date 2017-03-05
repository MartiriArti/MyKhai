package tonydarko.mykhai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

import tonydarko.mykhai.Adapters.ExtraBallAdapter;
import tonydarko.mykhai.Items.ExtraBallItem;
import tonydarko.mykhai.Parsers.ExtraParser;

public class ExtraBallTableActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ProgressDialog progressDialog;
    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    String url;
    ExtraBallAdapter extraBallAdapter;
    ListView lv;
    ArrayList<ExtraBallItem> data = new ArrayList<>();
    String[][] newTableFinal;
    TextView info;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_ball);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setTitle("Додаткові бали");
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");

        lv = (ListView) findViewById(R.id.listViewTable);

        info = (TextView) findViewById(R.id.inform);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Title");
        progressDialog.setMessage("Message");
        // меняем стиль на индикатор
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // устанавливаем максимум
        progressDialog.setMax(1407);
        // включаем анимацию ожидания
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                // выключаем анимацию ожидания
                progressDialog.setIndeterminate(false);
                if (progressDialog.getProgress() < progressDialog.getMax()) {
                    // увеличиваем значения индикаторов
                    progressDialog.incrementProgressBy(60);
                    progressDialog.incrementSecondaryProgressBy(75);
                    handler.sendEmptyMessageDelayed(0, 100);
                } else {
                    progressDialog.dismiss();
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 2000);

        ExtraParser parser = new ExtraParser(url);
        parser.execute();
        try {
            parser.get();
          data = parser.getData();
           newTableFinal = parser.getNewTableFinal();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        info.setText(newTableFinal[0][0]);//info message

        extraBallAdapter = new ExtraBallAdapter(this, data);
        extraBallAdapter.notifyDataSetChanged();

        lv.setAdapter(extraBallAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setBackgroundColor(Color.WHITE);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String queryText) {
        extraBallAdapter.getFilter().filter(queryText);
        return false;
    }
}