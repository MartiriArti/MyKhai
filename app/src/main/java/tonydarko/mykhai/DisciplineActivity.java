package tonydarko.mykhai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import tonydarko.mykhai.Adapters.OnlineVoteAdapter;
import tonydarko.mykhai.Items.OnlineVoteItem;

public class DisciplineActivity extends AppCompatActivity {

    private static String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";

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

        //infos = new String[3];
       // warning = (TextView) findViewById(R.id.informDiscipline);
       // danger = (TextView) findViewById(R.id.dangerDiscipline);
        lv = (ListView) findViewById(R.id.listViewDiscipline);

        ParseTable parseTable = new ParseTable();
        parseTable.execute();

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
                title = doc.select("option");
                int t = 0;
                newTableFinal = new String[title.size()];
                for (Element titles : title) {
                    newTableFinal[t] = titles.text();
                    t++;
                }

                Map<String, String> loginCookies;

                  /*  Connection.Response res = Jsoup
                            .connect("http://my.khai.edu/my/discipline")
                            .method(Connection.Method.GET)
                            .execute();
                    loginCookies = res.cookies();//заныкали на буд. куки
                    Document doc1 = res.parse();
                    Elements token = doc1.select("[name^=_csrf]");//подняли из парсера token

                System.out.println("Token" + token.text());
                System.out.println(doc1.body().html());

                Connection.Response res_toc = Jsoup.connect("http://my.khai.edu/my/discipline")
                        .data("token", token.toString())
                        .cookies(loginCookies)
                        .method(Connection.Method.POST)
                        .execute();
                Element body = res_toc.parse().body();// вдруг опять чего нибудь "парсануть" придется
                Document doc2 = Jsoup
                        .connect("http://my.khai.edu/my/discipline")
                        .userAgent(userAgent)
                        .get();

                String csrf = doc2.getElementsByTag("input").text();

                System.out.println("SCRF " + csrf);

                Connection.Response res = Jsoup.connect("http://my.khai.edu/my/discipline")
                        .userAgent(userAgent)
                        //.method(Connection.Method.POST)
                        .data("disciplineName", "4")
                        .referrer("http://my.khai.edu/my/discipline")
                        .execute();

                Map<String, String> cookies = res.cookies();
                cookies.put("_csrf", csrf);
                System.out.println(cookies.toString());
                Document doc3 = Jsoup
                        .connect("http://my.khai.edu/my/discipline")
                        .userAgent(userAgent)
                        .cookies(cookies)
                        .get();

               String s = doc3.html();
                System.out.println("HTML|||||||||||||||||" + "\n"+s);
*/
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hashMap;
        }
    }
}