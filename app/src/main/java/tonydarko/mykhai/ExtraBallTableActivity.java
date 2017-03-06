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
import tonydarko.mykhai.Utils.Cache;

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

        new checkAuth().execute();

    }

    private class checkAuth extends AsyncTask<String, Void, Void> {
        Elements title;
        HashMap<String, String> hashMap = new LinkedHashMap<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ExtraBallTableActivity.this,
                    "Ожидайте", "Выполняю соединение с ресурсом...", true, false);
        }

        @Override
        protected Void doInBackground(String... blockRega) {
            Document doc;
            try {
                doc = Jsoup
                        .connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
                        .referrer("http://google.com").timeout(1000 * 5)
                        .ignoreContentType(true).get();

                title = doc.select("tr");
                int t = 0;
                newTableFinal = new String[title.size()][];
                for (Element titles : title) {
                    hashMap.put(titles.text(), titles.attr("td"));
                    Elements trs = titles.select("tr");
                    for (int i = 0; i < trs.size(); i++) {
                        Elements tds = trs.get(i).select("td");
                        newTableFinal[t] = new String[tds.size()];
                        for (int j = 0; j < tds.size(); j++) {
                            newTableFinal[t][j] = tds.get(j).text();
                        }
                    }
                    t++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 2; i < newTableFinal.length; i++) {
                newTableFinal[i][2] += " " + newTableFinal[i][3] + " " + newTableFinal[i][4];
                data.add(new ExtraBallItem(
                        newTableFinal[i][1],//group
                        newTableFinal[i][2], //fio
                        newTableFinal[i][5],//full ball
                        newTableFinal[i][6]));//ball

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            info.setText(newTableFinal[0][0]);//info message

            extraBallAdapter = new ExtraBallAdapter(ExtraBallTableActivity.this, data);
            extraBallAdapter.notifyDataSetChanged();

            lv.setAdapter(extraBallAdapter);
        }

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