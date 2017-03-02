package tonydarko.mykhai;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

import tonydarko.mykhai.Adapters.ExtraBallAdapter;
import tonydarko.mykhai.Adapters.OnlineVoteAdapter;
import tonydarko.mykhai.Items.ExtraBallItem;
import tonydarko.mykhai.Items.OnlineVoteItem;
import tonydarko.mykhai.R;

public class OnlineVoteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    String url;
    public Elements title;
    private ListView lv;
    private ArrayList<OnlineVoteItem> data = new ArrayList<>();
    String[][] newTableFinal;

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


        ParseTable parseTable = new ParseTable();
        parseTable.execute();

        try {
            final HashMap<String, String> hashMap = parseTable.get();
            for (int i = 1; i < newTableFinal.length; i++) {
                data.add(new OnlineVoteItem(
                        newTableFinal[i][0],//num zayava
                        newTableFinal[i][2],//fio student
                        newTableFinal[i][3],//fio prepod
                        newTableFinal[i][4],//predmet
                        newTableFinal[i][5],//group
                        newTableFinal[i][6]));//date
            }
            lv.setAdapter(new OnlineVoteAdapter(this, data));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

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
        return false;
    }

    public class ParseTable extends AsyncTask<String, Void, HashMap<String, String>> {
        HashMap<String, String> hashMap = new LinkedHashMap<>();

        @Override
        protected HashMap<String, String> doInBackground(String... arg) {
            Document doc;
            try {
                doc = Jsoup.connect(url).get();
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
            return hashMap;
        }
    }
}