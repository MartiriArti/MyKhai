package tonydarko.mykhai;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import tonydarko.mykhai.Utils.Constant;

public class RatingActivity extends AppCompatActivity {

    String token;
    String url;
    String[][] newTableFinal;

    RatingActivity() {
        this.token = Constant.getToken();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setTitle("Рейтинг на стипендію");

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");

        new ParserToken().execute();
    }

    public class ParserToken extends AsyncTask<String, Void, HashMap<String, String>> {
        Elements title;
        HashMap<String, String> hashMap = new LinkedHashMap<>();

        @Override
        protected HashMap<String, String> doInBackground(String... arg) {
            Connection.Response resp1 = null;
            try {
                resp1 = Jsoup.connect(Constant.getUrl())
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String token = null;
            try {
                Document doc = resp1.parse();
                token = resp1.parse().getElementsByTag("div").first().val().trim();
                for (Element meta : doc.select("input")) {
                    if (meta.attr("name").equals("_csrf")) {
                        token = meta.attr("value");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Document doc3 = null;
            try {
                doc3 = Jsoup.connect(url)
                        .referrer("http://www.google.com")
                        .userAgent(Constant.getUserAgent())
                        .data("_csrf", token)
                        .cookies(Constant.getCommon())
                        .timeout(10000)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int t = 0;
            System.out.println(doc3.body());
            title = doc3.select("tr");
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
            System.out.println(newTableFinal[0].length);
            System.out.println(newTableFinal[2].length);

            System.out.println(Arrays.toString(newTableFinal[0]));
            System.out.println(Arrays.toString(newTableFinal[1]));
            System.out.println(Arrays.toString(newTableFinal[2]));
            System.out.println(Arrays.toString(newTableFinal[3]));
            return null;
        }
    }

}
