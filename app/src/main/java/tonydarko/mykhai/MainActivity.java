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
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public Elements title;
    private ArrayList<String> urls;
    private ListView lv;

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
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                    R.layout.main_item, R.id.tv_main_item, arrayList);

            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(this);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        Intent intent;
        switch (pos){
            case 0:
                Toast.makeText(this,"Еще в разработке!",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                intent = new Intent(this, ExtraBallTableActivity.class);
                intent.putExtra("URL", urls.get(pos));
                System.out.println("PutExtra " + urls.get(pos));
                startActivity(intent);
                break;
            case 2:
                Toast.makeText(this,"Еще в разработке!",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this,"Еще в разработке!",Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this,"Еще в разработке!",Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(this,"Еще в разработке!",Toast.LENGTH_SHORT).show();
                break;
            case 6:
                intent = new Intent(this, SchedulerActivity.class);
                intent.putExtra("URL", urls.get(pos));
                System.out.println("PutExtra " + urls.get(pos));
                startActivity(intent);
                break;
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
            urls.add("http://my.khai.edu/my/scheduler");
            hashMap.put("Розклад дисциплін за вибором", "http://my.khai.edu/my/scheduler");
            return hashMap;
        }

    }
}