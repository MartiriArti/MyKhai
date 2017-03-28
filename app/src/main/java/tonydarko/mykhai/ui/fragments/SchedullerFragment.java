package tonydarko.mykhai.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import tonydarko.mykhai.R;
import tonydarko.mykhai.adapters.ExtraBallAdapter;
import tonydarko.mykhai.adapters.SchedullerAdapter;
import tonydarko.mykhai.items.SchedullerItem;

public class SchedullerFragment extends Fragment {

    private SearchView searchView;
    private MenuItem searchMenuItem;
    ProgressDialog progressDialog;
    String url = "http://my.khai.edu/my/scheduler";
    int t = 0;
    Document doc;
    SchedullerAdapter schedullerAdapter;
    ArrayList<SchedullerItem> data = new ArrayList<>();
    String[][] newTableFinal;
    private RecyclerView recyclerView;
    CoordinatorLayout rootLayout;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scheduller_fragment, container, false);
        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.scheduller_coordinator);
        setHasOptionsMenu(true);
        context = getActivity();
        if (data.size() == 0) {
            new ParserBigData().execute();
        } else {
            schedullerAdapter = new SchedullerAdapter(data);
            schedullerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(schedullerAdapter);
        }
        initRecycleView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        context = getActivity();
        schedullerAdapter = new SchedullerAdapter(data);
        schedullerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(schedullerAdapter);
    }

    private void initRecycleView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_of_schedullers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setQueryHint(getString(R.string.search_s_input));
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
                schedullerAdapter.getFilter().filter(newText);
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
                            Elements tds = trs.get(i).select("td");
                            newTableFinal[t] = new String[tds.size() + 1];
                            for (int j = 0; j < tds.size(); j++) {
                                progressDialog.setProgress(t);
                                newTableFinal[t][j] = tds.get(j).text();
                                if (j == 2) {
                                    String[] str = newTableFinal[t][2].split("\\|\\|");
                                    newTableFinal[t][j] = str[0];
                                    newTableFinal[t][5] = str[1];
                                }
                            }
                        }
                        System.out.println(Arrays.deepToString(newTableFinal));
                        t++;
                    }
                    for (int i = 1; i < newTableFinal.length; i++) {
                        data.add(new SchedullerItem(
                                newTableFinal[i][0],//fio
                                newTableFinal[i][1],//group
                                newTableFinal[i][2],//para
                                newTableFinal[i][3],//type
                                newTableFinal[i][4],//date
                                newTableFinal[i][5]));//fio_prepod
                    }
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
                schedullerAdapter = new SchedullerAdapter(data);
                schedullerAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(schedullerAdapter);
                t = 0;
            } else {
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
}