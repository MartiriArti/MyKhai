package tonydarko.mykhai.Parsers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import tonydarko.mykhai.Items.ExtraBallItem;
import tonydarko.mykhai.Utils.Cache;

public class ExtraParser extends AsyncTask<String, Integer, String[][]> {
    Elements title;
    HashMap<String, String> hashMap = new LinkedHashMap<>();
    ArrayList<ExtraBallItem> data = new ArrayList<>();
    String url;
    String[][] newTableFinal;
    Context context;

   public ExtraParser(String url, Context context){
       this.context = context;
        this.url = url;
    }

    @Override
    protected String[][] doInBackground(String... arg) {
        Document doc;
        try {
            doc = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
                    .get();

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

        for (int i = 2; i < newTableFinal.length; i++) {
            newTableFinal[i][2] += " " + newTableFinal[i][3] + " " + newTableFinal[i][4];
            data.add(new ExtraBallItem(
                    newTableFinal[i][1],//group
                    newTableFinal[i][2], //fio
                    newTableFinal[i][5],//full ball
                    newTableFinal[i][6]));//ball

        }

        Cache.setNewTableFinal(newTableFinal);
        System.out.println("Chase MTF" + newTableFinal.length);
        Cache.setData(data);
        System.out.println("DATA " + data);

        return newTableFinal;
    }


}