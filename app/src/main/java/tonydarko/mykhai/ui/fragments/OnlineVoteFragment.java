package tonydarko.mykhai.ui.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
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
import tonydarko.mykhai.adapters.OnlineVoteAdapter;
import tonydarko.mykhai.items.OnlineVoteItem;

public class OnlineVoteFragment extends Fragment{

    private SearchView searchView;
    private MenuItem searchMenuItem;
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private CoordinatorLayout rootLayout;
    String url = "http://my.khai.edu/my/stats";
    int t = 0;
    OnlineVoteAdapter onlineVoteAdapter;
    ArrayList<OnlineVoteItem> data = new ArrayList<>();
    String[][] newTableFinal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.online_vote_fragment, container, false);

       // getActivity().setTitle("OnlineVoteFragment");
        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.online_vote_coordinator);
        setHasOptionsMenu(true);

        if (data.size() == 0) {
            new ParserBigData().execute();
        }else {
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
        inflater.inflate( R.menu.menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setQueryHint("Введіть: предмет, фамілію або групу");
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
            Document doc;
            progressDialog.setMessage(getString(R.string.pre_exec_get));
            progressDialog.setIndeterminate(false);
            try {
                doc = Jsoup
                        .connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
                        .referrer("http://google.com")
                        .timeout(1000 * 8)
                        .ignoreContentType(true).get();

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
            } catch (IOException e) {
                e.printStackTrace();
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
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
          progressDialog.dismiss();

          //  info.setText(newTableFinal[0][0]);//info message

            onlineVoteAdapter = new OnlineVoteAdapter(data);
            onlineVoteAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(onlineVoteAdapter);
            t = 0;
        }

    }
}