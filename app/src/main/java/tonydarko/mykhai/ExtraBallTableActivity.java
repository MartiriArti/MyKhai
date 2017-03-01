package tonydarko.mykhai;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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

public class ExtraBallTableActivity extends AppCompatActivity{

    String url;
    public Elements title;
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
            lv.setAdapter(new ExtraBallAdapter(this, data));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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