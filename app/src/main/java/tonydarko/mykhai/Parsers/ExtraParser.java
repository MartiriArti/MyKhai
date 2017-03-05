package tonydarko.mykhai.Parsers;

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

public class ExtraParser extends AsyncTask<String, Void, HashMap<String, String>> {

    private ArrayList<ExtraBallItem> data = new ArrayList<>();
    String[][] newTableFinal;
    String url;
    Elements title;
    HashMap<String, String> hashMap = new LinkedHashMap<>();

    public ExtraParser(String url) {
        this.url = url;
    }

    public ArrayList<ExtraBallItem> getData() {
        return data;
    }

    public String[][] getNewTableFinal() {
        return newTableFinal;
    }

    @Override
    protected HashMap<String, String> doInBackground(String... arg) {
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(5000).get();
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
        afterParsing();

        return hashMap;
    }

    public void afterParsing() {
        for (int i = 2; i < newTableFinal.length; i++) {
            newTableFinal[i][2] += " " + newTableFinal[i][3] + " " + newTableFinal[i][4];
            data.add(new ExtraBallItem(
                    newTableFinal[i][1],//group
                    newTableFinal[i][2], //fio
                    newTableFinal[i][5],//full ball
                    newTableFinal[i][6]));//ball

        }
    }
}