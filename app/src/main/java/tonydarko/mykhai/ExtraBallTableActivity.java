package tonydarko.mykhai;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import tonydarko.mykhai.Adapters.ExtraBallAdapter;
import tonydarko.mykhai.Items.ExtraBallItem;

public class ExtraBallTableActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    final static String URL = "http://my.khai.edu/my/login";
    final static String MyLogin = "martishkov_a";
    final static String MyPassword = "ant641448";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0";
    // credentials

    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    String url;
    public Elements title;
    ExtraBallAdapter extraBallAdapter;
    private ListView lv;
    private ArrayList<ExtraBallItem> data = new ArrayList<>();
    String[][] newTableFinal;
    TextView info;

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

        ParseTable parseTable = new ParseTable();
        parseTable.execute();

        try {
           parseTable.get();
            info.setText(newTableFinal[0][0]);//info message
            for (int i = 2; i < newTableFinal.length; i++) {
                newTableFinal[i][2] += " " + newTableFinal[i][3] + " " + newTableFinal[i][4];
                data.add(new ExtraBallItem(
                        newTableFinal[i][1],//group
                        newTableFinal[i][2], //fio
                        newTableFinal[i][5],//full ball
                        newTableFinal[i][6]));//ball

            }
            extraBallAdapter = new ExtraBallAdapter(this, data);
            extraBallAdapter.notifyDataSetChanged();
            lv.setAdapter(extraBallAdapter);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
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

            try {
                Connection.Response response = Jsoup
                        .connect(url)
                       // .userAgent(USER_AGENT)
                      //  .data("utf8", "?")
                      //  .data("authenticity_token", "???????????")
                        .data("username", MyLogin)
                        .data("password", MyPassword)
                     //   .data("submit", "true")
                        .execute();


                Map loginCoockies = response.cookies();
                System.out.println(loginCoockies);
                Document doc2 = Jsoup.connect("http://my.khai.edu/my")
                        .cookies(loginCoockies)
                        .get();
                System.out.println(doc2);

            }catch(Exception e){e.printStackTrace();}

            return hashMap;
        }
    }
}