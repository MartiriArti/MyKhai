package tonydarko.mykhai;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private ArrayList<String> table;
    private ListView lv;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        table = new ArrayList<>();

        toolbar.setLogo(R.mipmap.logo);

        lv = (ListView) findViewById(R.id.listView1);
        tv = (TextView) findViewById(R.id.textView);

        ParseTitle parseTitle = new ParseTitle();
        parseTitle.execute();

        try {
            final HashMap<String,String> hashMap = parseTitle.get();
            final ArrayList<String> arrayList = new ArrayList<>();
            for (Map.Entry entry: hashMap.entrySet()){
                arrayList.add(entry.getKey().toString());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, arrayList);

            lv.setAdapter(arrayAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    ParseText parseText = new ParseText();
                    parseText.execute(hashMap.get(arrayList.get(position)));
                    try {
                        lv.setVisibility(View.GONE);
                        tv.setText(parseText.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
      lv.setVisibility(View.VISIBLE);
        tv.setVisibility(View.GONE);
    }

    public class ParseText extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String str = " ";
            try {
                Document document = Jsoup.connect(params[0]).get();
                Element element = document.select(".header").first();
                str = element.text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    public class ParseTitle extends AsyncTask<String, Void, HashMap<String,String>> {
            HashMap<String,String> hashMap = new LinkedHashMap<>();
        @Override
        protected HashMap<String,String> doInBackground(String... arg) {

            Document doc;
            try {
                doc = Jsoup.connect("http://my.khai.edu").get();
                title = doc.select(".blackLink");
                for (Element titles : title) {
                    Element element = titles.select("a[href]").first();
                    hashMap.put(titles.text(), element.attr("abs:href"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hashMap;
        }

    }
}