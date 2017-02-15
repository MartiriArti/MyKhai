package tonydarko.mykhai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TableActivity extends AppCompatActivity {

    String url;
    public Elements title;
    private ListView lv;
    ArrayList<String> arrayList;
    String[][] newTableFinal;
    TextView info, table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");

        lv = (ListView) findViewById(R.id.listViewTable);
        info = (TextView) findViewById(R.id.inform);
        table = (TextView) findViewById(R.id.table);

        ParseTable parseTitle = new ParseTable();
        parseTitle.execute();

        try {
            final HashMap<String, String> hashMap = parseTitle.get();
            arrayList = new ArrayList<>();
            for (Map.Entry entry : hashMap.entrySet()) {
                arrayList.add(entry.getKey().toString());
            }

            info.setText(arrayList.get(0));
            table.setText(arrayList.get(1));

            arrayList.remove(0);
            arrayList.remove(0);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                  R.layout.item, R.id.tv_item, arrayList);

          lv.setAdapter(arrayAdapter);

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
                for (Element titles : title) {
                    hashMap.put(titles.text(), titles.attr("td"));

                    Elements trs = titles.select("tr");
                    newTableFinal = new String[trs.size()][];
                    for (int i = 0; i < trs.size(); i++) {
                        Elements tds = trs.get(i).select("td");
                        newTableFinal[i] = new String[tds.size()];
                        for (int j = 0; j < tds.size(); j++) {
                            newTableFinal[i][j] = tds.get(j).text();
                        }
                        // System.out.println(Arrays.toString(newTableFinal[i]));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return hashMap;
        }

    }
}