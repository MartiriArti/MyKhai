package tonydarko.mykhai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import tonydarko.mykhai.Adapters.BallStudentAdapter;
import tonydarko.mykhai.Adapters.ExtraBallAdapter;
import tonydarko.mykhai.Items.BallStudentItem;
import tonydarko.mykhai.Items.ExtraBallItem;
import tonydarko.mykhai.Utils.Constant;

public class BallStudentActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    String token;
    String[][] newTableFinal;
    String url;
    TextView year;
    ListView lv;
    BallStudentAdapter ballStudentAdapter;
    ArrayList<BallStudentItem> data = new ArrayList<>();

    public BallStudentActivity() {
        this.token = Constant.getToken();
        System.out.println(Constant.getToken());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setTitle("Оцінки студента");

        year = (TextView) findViewById(R.id.year);
        lv = (ListView)  findViewById(R.id.listViewBall);
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");

        new ParserToken().execute();

    }

    public class ParserToken extends AsyncTask<String, Void, HashMap<String, String>> {
        Elements title;
        HashMap<String, String> hashMap = new LinkedHashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BallStudentActivity.this);
            progressDialog.setTitle("Загрузка страницы");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

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
            title = doc3.select("tr");
           newTableFinal = new String[title.size()][];
            for (Element titles : title) {
                hashMap.put(titles.text(), titles.attr("td"));
                Elements trs = titles.select("tr");
                for (int i = 0; i < trs.size(); i++) {
                    progressDialog.setProgress(t);
                    Elements tds = trs.get(i).select("td");
                    newTableFinal[t] = new String[tds.size()];
                    for (int j = 0; j < tds.size(); j++) {
                        newTableFinal[t][j] = tds.get(j).text();
                    }
                }
                t++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> stringStringHashMap) {
            super.onPostExecute(stringStringHashMap);
            for (int i = 1; i < newTableFinal.length; i++) {
                data.add(new BallStudentItem(
                        newTableFinal[i][1],//group
                        newTableFinal[i][2], //fio
                        newTableFinal[i][3],//full ball
                        newTableFinal[i][4],//full ball
                        newTableFinal[i][5]));//ball
            }
            progressDialog.dismiss();
            year.setText("Навчальний рік: "+newTableFinal[1][6] +" Семестр: "+ newTableFinal[1][7]);//info message

            ballStudentAdapter = new BallStudentAdapter(data, BallStudentActivity.this);
            ballStudentAdapter.notifyDataSetChanged();

            lv.setAdapter(ballStudentAdapter);
        }
    }

}
