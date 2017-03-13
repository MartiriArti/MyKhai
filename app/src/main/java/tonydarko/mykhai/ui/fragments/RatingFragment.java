package tonydarko.mykhai.ui.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

import tonydarko.mykhai.R;
import tonydarko.mykhai.Utils.Constant;
import tonydarko.mykhai.adapters.RatingAdapter;
import tonydarko.mykhai.items.RatingItem;

public class RatingFragment extends Fragment {

    ProgressDialog progressDialog;
    String token;
    ArrayList<String> text;
    ArrayList<String> value;
    RatingAdapter ratingAdapter;
    private RecyclerView recyclerView;
    CoordinatorLayout rootLayout;
    ArrayList<RatingItem> data = new ArrayList<>();
    String url = "http://my.khai.edu/my/student_rating";
    String[][] newTableFinal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.rating_fragment, container, false);
        token = Constant.getToken();
        // getActivity().setTitle("Rating");
        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.rating_coordinator);
        setHasOptionsMenu(true);

        new ParserBigData().execute();
        initRecycleView(rootView);
        return rootView;
    }

    private void initRecycleView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_of_rating);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // ExtraBallAdapter expensesAdapter = new ExtraBallAdapter(getExpenses());
        //   recyclerView.setAdapter(expensesAdapter);
    }

    private class ParserBigData extends AsyncTask<String, Integer, Void> {
        Elements title;
        HashMap<String, String> hashMap = new LinkedHashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Загрузка страницы");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg) {
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
                assert resp1 != null;
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
            assert doc3 != null;
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
            System.out.println(Arrays.deepToString(newTableFinal));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            text = new ArrayList<>();
            value = new ArrayList<>();
            for (int i = 0; i < newTableFinal[0].length; i++){
                text.add(newTableFinal[0][i]);
                value.add(newTableFinal[1][i]);
            }
            for (int i = 0; i < newTableFinal[2].length; i++){
                text.add(newTableFinal[2][i]);
                value.add(newTableFinal[3][i]);
            }

            ratingAdapter = new RatingAdapter(text,value);
            ratingAdapter.notifyDataSetChanged();

            progressDialog.dismiss();
            recyclerView.setAdapter(ratingAdapter);
        }

    }
}