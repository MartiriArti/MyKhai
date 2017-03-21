package tonydarko.mykhai.ui.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
import tonydarko.mykhai.adapters.StudentBallsAdapter;
import tonydarko.mykhai.items.BallStudentItem;

public class StudentBallsFragment extends Fragment {

    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    CoordinatorLayout rootLayout;
    int t = 0;
    ArrayList<BallStudentItem> data = new ArrayList<>();
    String url = "http://my.khai.edu/my/student_marks";
    StudentBallsAdapter studentBallsAdapter;
    String[][] newTableFinal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.ball_studentfragment, container, false);

        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.student_ball_coordinator);
        setHasOptionsMenu(true);

        if (data.size() == 0) {
            new ParserBigData().execute();
        } else {

            studentBallsAdapter = new StudentBallsAdapter(data);
            studentBallsAdapter.notifyDataSetChanged();

            recyclerView.setAdapter(studentBallsAdapter);
        }
        initRecycleView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        studentBallsAdapter = new StudentBallsAdapter(data);
        studentBallsAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(studentBallsAdapter);
    }

    private void initRecycleView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_of_student_balls);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private class ParserBigData extends AsyncTask<String, Integer, Void> {
    Elements title;
        HashMap<String, String> hashMap = new LinkedHashMap<>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Загрузка страницы");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(true);
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
            assert doc3 != null;
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
        protected void onPostExecute(Void stringStringHashMap) {
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

            studentBallsAdapter = new StudentBallsAdapter(data);
            studentBallsAdapter.notifyDataSetChanged();
            Snackbar.make(rootLayout, "Навчальний рік: " +
                    newTableFinal[1][6] + " Семестр: " +
                    newTableFinal[1][7], Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(Color.RED)
                    .show();
            recyclerView.setAdapter(studentBallsAdapter);
            t = 0;
        }
    }
}