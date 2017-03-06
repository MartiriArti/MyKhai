package tonydarko.mykhai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import tonydarko.mykhai.Adapters.OnlineVoteAdapter;
import tonydarko.mykhai.Items.OnlineVoteItem;

public class OnlineVoteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    String url;
    OnlineVoteAdapter adapter;
    public Elements title;
    private ListView lv;
    private ArrayList<OnlineVoteItem> data = new ArrayList<>();
    String[][] newTableFinal;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_vote);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setTitle("Онлайн вибір");

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");

        lv = (ListView) findViewById(R.id.listViewVote);

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
            progressDialog = new ProgressDialog(OnlineVoteActivity.this);
            progressDialog.setTitle("Загрузка страницы");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... block) {
            Document doc;
            progressDialog.setMessage("Получение данных");
            progressDialog.setIndeterminate(false);
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
                int t = 0;
                newTableFinal = new String[title.size()][];
                for (Element titles : title) {
                    hashMap.put(titles.text(), titles.attr("td"));
                    Elements trs = titles.select("tr");
                    for (int i = 0; i < trs.size(); i++) {
                        progressDialog.setProgress(t);
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

            for (int i = 1; i < newTableFinal.length; i++) {
                data.add(new OnlineVoteItem(
                        newTableFinal[i][0],//num zayava
                        newTableFinal[i][2],//fio student
                        newTableFinal[i][3],//fio prepod
                        newTableFinal[i][4],//predmet
                        newTableFinal[i][5],//group
                        newTableFinal[i][6]));//date
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            adapter = new OnlineVoteAdapter(OnlineVoteActivity.this, data);
            adapter.notifyDataSetChanged();
            lv.setAdapter(adapter);
        }

    }
}