package tonydarko.mykhai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class SchedulerActivity extends AppCompatActivity {
    String url;
    public Elements title;
    private ListView lv;
    private ArrayList<SchedulerItem> data = new ArrayList<>();
    String[][] newTableFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_scheduler);
        setSupportActionBar(toolbar);
        setTitle("Розклад занять за вибором");
        lv = (ListView) findViewById(R.id.listViewScheduler);
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");

        ParseTable parseTable = new ParseTable();
        parseTable.execute();
        try {
            final HashMap<String, String> hashMap = parseTable.get();
//[Шелепова Маргарита Олександрівна,
// 116,
// Етика ділового спілкування ,
// ЛЕКЦІЯ,
// вівторок, 1 пара, 427 гк,
// Широка Світлана Іванівна]
            for (int i = 1; i < newTableFinal.length; i++) {
                data.add(new SchedulerItem(
                        newTableFinal[i][0],
                        newTableFinal[i][1],
                        newTableFinal[i][2],
                        newTableFinal[i][3],
                        newTableFinal[i][4],
                        newTableFinal[i][5]));
            }

            lv.setAdapter(new SchedulerAdapter(this, data));

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
                System.out.println(Arrays.deepToString(newTableFinal));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return hashMap;
        }

    }
}