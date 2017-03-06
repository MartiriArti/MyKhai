package tonydarko.mykhai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

import tonydarko.mykhai.Adapters.OnlineVoteAdapter;
import tonydarko.mykhai.Adapters.SchedulerAdapter;
import tonydarko.mykhai.Items.OnlineVoteItem;
import tonydarko.mykhai.Items.SchedulerItem;

public class SchedulerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    String url;
    ProgressDialog progressDialog;
    SchedulerAdapter adapter;
    public Elements title;
    private ListView lv;
    int t = 0;
    private ArrayList<SchedulerItem> data = new ArrayList<>();
    String[][] newTableFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setTitle("Розклад занять за вибором");
        lv = (ListView) findViewById(R.id.listViewScheduler);

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");

        new ParserBigData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }


    private class ParserBigData extends AsyncTask<String, Integer, Void> {
        Elements title;
        HashMap<String, String> hashMap = new LinkedHashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SchedulerActivity.this);
            progressDialog.setTitle("Загрузка страницы");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... block) {
            progressDialog.setMessage("Получение данных");
            progressDialog.setIndeterminate(false);
            Document doc;
            try {
                doc = Jsoup
                        .connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
                        .referrer("http://google.com")
                        .timeout(1000 * 8)
                        .ignoreContentType(true).get();

                title = doc.select("tr");
                newTableFinal = new String[title.size()][];
                progressDialog.setMax(newTableFinal.length);
                for (Element titles : title) {
                    hashMap.put(titles.text(), titles.attr("td"));
                    Elements trs = titles.select("tr");
                    for (int i = 0; i < trs.size(); i++) {
                        Elements tds = trs.get(i).select("td");
                        newTableFinal[t] = new String[tds.size()+1];
                        for (int j = 0; j < tds.size(); j++) {
                            progressDialog.setProgress(t);
                            newTableFinal[t][j] = tds.get(j).text();
                            if (j==2) {
                                String[] str = newTableFinal[t][2].split("\\|\\|");
                                newTableFinal[t][j] = str[0];
                                newTableFinal[t][5] = str[1];
                            }
                        }
                    }
                    t++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 1; i < newTableFinal.length; i++) {
                data.add(new SchedulerItem(
                        newTableFinal[i][0],//fio
                        newTableFinal[i][1],//group
                        newTableFinal[i][2],//para
                        newTableFinal[i][3],//type
                        newTableFinal[i][4],//date
                        newTableFinal[i][5]));//fio_prepod
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            adapter = new SchedulerAdapter(SchedulerActivity.this, data);
            adapter.notifyDataSetChanged();
            lv.setAdapter(adapter);
        }

    }
}