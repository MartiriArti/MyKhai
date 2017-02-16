package tonydarko.mykhai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class TableActivity extends AppCompatActivity {

    String url;
    public Elements title;
    private ListView lv;
    ArrayList<Item> data = new ArrayList<Item>();
    String[][] newTableFinal;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");

        lv = (ListView) findViewById(R.id.listViewTable);

        info = (TextView) findViewById(R.id.inform);


        ParseTable parseTitle = new ParseTable();
        parseTitle.execute();

        try {
            final HashMap<String, String> hashMap = parseTitle.get();


            info.setText(newTableFinal[0][0]);
            // arrayList.remove(0);

            for (int i = 2; i < newTableFinal.length; i++) {
                data.add(new Item(newTableFinal[i][1],
                        newTableFinal[i][2] + " " + newTableFinal[i][3] + " " + newTableFinal[i][4]
                        , newTableFinal[i][6]));
            }

            lv.setAdapter(new Adapter(this, data));

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