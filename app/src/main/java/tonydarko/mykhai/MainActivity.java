package tonydarko.mykhai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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

public class MainActivity extends AppCompatActivity {

    public Elements title;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> urls;
    private ListView lv;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        urls = new ArrayList<>();

        toolbar.setLogo(R.mipmap.logo);

        lv = (ListView) findViewById(R.id.listView1);

        ParseTitle parseTitle = new ParseTitle();
        parseTitle.execute();

        try {
            final HashMap<String, String> hashMap = parseTitle.get();
            final ArrayList<String> arrayList = new ArrayList<>();
            for (Map.Entry entry : hashMap.entrySet()) {
                arrayList.add(entry.getKey().toString());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayList);

            lv.setAdapter(arrayAdapter);
            final Intent intent = new Intent(this,TableActivity.class);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    intent.putExtra("URL", urls.get(position));
                    System.out.println("PutExtra " + urls.get(position));
                   startActivity(intent);
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class ParseTitle extends AsyncTask<String, Void, HashMap<String, String>> {
        HashMap<String, String> hashMap = new LinkedHashMap<>();

        @Override
        protected HashMap<String, String> doInBackground(String... arg) {

            Document doc;
            try {
                doc = Jsoup.connect("http://my.khai.edu").get();
                title = doc.select(".blackLink");
                for (Element titles : title) {
                    Element element = titles.select("a[href]").first();
                    hashMap.put(titles.text(), element.attr("abs:href"));
                    urls.add(element.attr("abs:href"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hashMap;
        }

    }
}