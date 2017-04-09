package tonydarko.mykhai.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import tonydarko.mykhai.R;
import tonydarko.mykhai.adapters.ExtraBallAdapter;
import tonydarko.mykhai.items.ExtraBallItem;
import tonydarko.mykhai.ui.MainActivity;

public class ExtraBallFragment extends Fragment {

    private SearchView searchView;
    private MenuItem searchMenuItem;
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    Document doc;
    String info = null;
    CoordinatorLayout rootLayout;
    String url = "http://my.khai.edu/my/scolarship_ball";
    int t = 0;
    ExtraBallAdapter extraBallAdapter;
    ArrayList<ExtraBallItem> data = new ArrayList<>();
    String[][] newTableFinal;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.extra_ball_fragment, container, false);
        context = getActivity();
        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.extra_ball_coordinator);
        setHasOptionsMenu(true);

        if (data.size() == 0) {
            new ParserBigData().execute();
        } else {
            extraBallAdapter = new ExtraBallAdapter(data);
            extraBallAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(extraBallAdapter);
            infoDialog();
        }

        initRecycleView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        context = getActivity();
        extraBallAdapter = new ExtraBallAdapter(data);
        extraBallAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(extraBallAdapter);
    }

    private void initRecycleView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_of_balls);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onDestroy() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setQueryHint(getString(R.string.search_ebt_input));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                extraBallAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private class ParserBigData extends AsyncTask<String, Integer, Void> {
        Elements title;
        HashMap<String, String> hashMap = new LinkedHashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getString(R.string.pre_exec_loading));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... block) {
            progressDialog.setMessage(getString(R.string.pre_exec_get));
            progressDialog.setIndeterminate(false);
            try {
                doc = Jsoup
                        .connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
                        .referrer("http://google.com")
                        .timeout(1000 * 8)
                        .ignoreContentType(true).get();
                if (doc != null) {
                    title = doc.select("tr");
                    newTableFinal = new String[title.size()][];
                    progressDialog.setMax(newTableFinal.length);
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
                }
                info = newTableFinal[0][0];
                for (int i = 2; i < newTableFinal.length; i++) {
                    newTableFinal[i][2] += " " + newTableFinal[i][3] + " " + newTableFinal[i][4];
                    data.add(new ExtraBallItem(
                            newTableFinal[i][1],//group
                            newTableFinal[i][2], //fio
                            newTableFinal[i][5],//full ball
                            newTableFinal[i][6]));//ball
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (doc != null) {
                extraBallAdapter = new ExtraBallAdapter(data);
                extraBallAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(extraBallAdapter);
               infoDialog();

                newTableFinal = null;
                t = 0;
            }else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle(R.string.AD_err_dow_page);

                alertDialog.setMessage(R.string.AD_check_internet);

                alertDialog.setPositiveButton(R.string.AD_repeat, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        new ParserBigData().execute();
                    }
                });

                alertDialog.setNegativeButton(R.string.AD_exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }
        }
    }

    public void infoDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Увага!");

        alertDialog.setMessage(info);

        alertDialog.setPositiveButton("Зрозуміло", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}