package tonydarko.mykhai.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import java.util.HashMap;
import java.util.LinkedHashMap;

import tonydarko.mykhai.R;
import tonydarko.mykhai.Utils.Constant;
import tonydarko.mykhai.adapters.ExtraBallAdapter;
import tonydarko.mykhai.adapters.RatingAdapter;
import tonydarko.mykhai.items.RatingItem;

public class RatingFragment extends Fragment {

    ProgressDialog progressDialog;
    String token;
    Document doc3, doc;
    ArrayList<String> text;
    ArrayList<String> value;
    RatingAdapter ratingAdapter;
    private RecyclerView recyclerView;
    CoordinatorLayout rootLayout;
    Context context;
    ArrayList<RatingItem> data = new ArrayList<>();
    String url = "http://my.khai.edu/my/student_rating";
    String[][] newTableFinal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.rating_fragment, container, false);
        token = Constant.getToken();
        context = getActivity();
        // getActivity().setTitle("Rating");
        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.rating_coordinator);
        setHasOptionsMenu(true);

        new ParserBigData().execute();
        initRecycleView(rootView);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        context = getActivity();
    }

    private void initRecycleView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_of_rating);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private class ParserBigData extends AsyncTask<String, Integer, Void> {
        Elements title;
        HashMap<String, String> hashMap = new LinkedHashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getString(R.string.pre_exec_loading));
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
                String token = null;
                assert resp1 != null;
                doc = resp1.parse();
                if (doc != null) {
                    token = resp1.parse().getElementsByTag("div").first().val().trim();
                    for (Element meta : doc.select("input")) {
                        if (meta.attr("name").equals("_csrf")) {
                            token = meta.attr("value");
                        }
                    }

                    doc3 = null;
                    doc3 = Jsoup.connect(url)
                            .referrer("http://www.google.com")
                            .userAgent(Constant.getUserAgent())
                            .data("_csrf", token)
                            .cookies(Constant.getCommon())
                            .timeout(10000)
                            .get();

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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            text = new ArrayList<>();
            value = new ArrayList<>();
            progressDialog.dismiss();
            if (doc3 != null) {
                for (int i = 0; i < newTableFinal[0].length; i++) {
                    text.add(newTableFinal[0][i]);
                    value.add(newTableFinal[1][i]);
                }
                for (int i = 0; i < newTableFinal[2].length; i++) {
                    text.add(newTableFinal[2][i]);
                    value.add(newTableFinal[3][i]);
                }

                ratingAdapter = new RatingAdapter(text, value);
                ratingAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(ratingAdapter);
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Невдалося завантажити сторінку");

                alertDialog.setMessage("Перевірте ваше підключення та спробуйте знову");

                alertDialog.setPositiveButton("Повторити", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        new ParserBigData().execute();
                    }
                });

                alertDialog.setNegativeButton("Вийти", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }
        }

    }
}