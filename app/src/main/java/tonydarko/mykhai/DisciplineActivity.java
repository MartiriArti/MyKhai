package tonydarko.mykhai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
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

import tonydarko.mykhai.Adapters.OnlineVoteAdapter;
import tonydarko.mykhai.Items.OnlineVoteItem;

public class DisciplineActivity extends AppCompatActivity {

    TextView danger, warning;
    String url;
    String[] infos;
    OnlineVoteAdapter adapter;
    public Elements title;
    private ListView lv;
    private ArrayList<OnlineVoteItem> data = new ArrayList<>();
    String[] newTableFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discipline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setTitle("Дисципліни");

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");

        infos = new String[3];
        warning = (TextView) findViewById(R.id.informDiscipline);
        danger = (TextView) findViewById(R.id.dangerDiscipline);
        lv = (ListView) findViewById(R.id.listViewDiscipline);

        ParseTable parseTable = new ParseTable();
        parseTable.execute();

        warning.setText(infos[0]);
        danger.setText(infos[1]);

        final ArrayList<String> arrayList = new ArrayList<>();

        try {
            final HashMap<String, String> hashMap = parseTable.get();

            arrayList.addAll(Arrays.asList(newTableFinal).subList(1, newTableFinal.length));

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                    R.layout.main_item, R.id.tv_main_item, arrayList);

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
                title = doc.select("h4");
                int z = 0;
                for (Element titles : title) {
                    infos[z] = titles.text();
                    System.out.println(infos[z]);
                    z++;
                }

                doc = Jsoup.connect(url).get();
                title = doc.select("option");
                int t = 0;
                newTableFinal = new String[title.size()];
                for (Element titles : title) {
                    newTableFinal[t] = titles.text();
                    t++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hashMap;
        }
    }
}