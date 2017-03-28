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
import tonydarko.mykhai.adapters.OnlineVoteAdapter;
import tonydarko.mykhai.items.OnlineVoteItem;

public class OnlineVoteFragment extends Fragment {

    private SearchView searchView;
    private MenuItem searchMenuItem;
    ProgressDialog progressDialog;
    Document doc;
    private RecyclerView recyclerView;
    private CoordinatorLayout rootLayout;
    String url = "http://my.khai.edu/my/stats";
    int t = 0;
    OnlineVoteAdapter onlineVoteAdapter;
    ArrayList<OnlineVoteItem> data = new ArrayList<>();
    String[][] newTableFinal;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.online_vote_fragment, container, false);
        context = getActivity();
        // getActivity().setTitle("OnlineVoteFragment");
        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.online_vote_coordinator);
        setHasOptionsMenu(true);

        if (data.size() == 0) {
            new ParserBigData().execute();
        } else {
            onlineVoteAdapter = new OnlineVoteAdapter(data);
            onlineVoteAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(onlineVoteAdapter);
        }
        initRecycleView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        context = getActivity();
        onlineVoteAdapter = new OnlineVoteAdapter(data);
        onlineVoteAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(onlineVoteAdapter);
    }

    private void initRecycleView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_of_votes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setQueryHint(getString(R.string.search_ov_input));
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
                onlineVoteAdapter.getFilter().filter(newText);
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
                    for (int i = 1; i < newTableFinal.length; i++) {
                        data.add(new OnlineVoteItem(
                                newTableFinal[i][0],//num zayava
                                newTableFinal[i][2],//fio student
                                newTableFinal[i][3],//fio prepod
                                newTableFinal[i][4],//predmet
                                newTableFinal[i][5],//group
                                newTableFinal[i][6]));//date
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
                onlineVoteAdapter = new OnlineVoteAdapter(data);
                onlineVoteAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(onlineVoteAdapter);
                t = 0;
            }else{
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