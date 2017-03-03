package tonydarko.mykhai;

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

import tonydarko.mykhai.Adapters.SchedulerAdapter;
import tonydarko.mykhai.Items.SchedulerItem;

public class SchedulerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    String url;
    SchedulerAdapter adapter;
    public Elements title;
    private ListView lv;
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
        ParseTable parseTable = new ParseTable();

        parseTable.execute();

        try {
            final HashMap<String, String> hashMap = parseTable.get();
            for (int i = 1; i < newTableFinal.length; i++) {
                data.add(new SchedulerItem(
                        newTableFinal[i][0],//fio
                        newTableFinal[i][1],//group
                        newTableFinal[i][2],//para
                        newTableFinal[i][3],//type
                        newTableFinal[i][4],//date
                        newTableFinal[i][5]));//fio_prepod
            }
            adapter = new SchedulerAdapter(this, data);
            lv.setAdapter(adapter);

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
        adapter.getFilter().filter(newText);
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
                        newTableFinal[t] = new String[tds.size()+1];
                        for (int j = 0; j < tds.size(); j++) {
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
            return hashMap;
        }

    }
}