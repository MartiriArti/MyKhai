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
import tonydarko.mykhai.adapters.DisciplineAdapter;
import tonydarko.mykhai.adapters.ExtraBallAdapter;
import tonydarko.mykhai.items.DisciplineItem;

public class DisciplineFragment extends Fragment {

    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    CoordinatorLayout rootLayout;
    int t = 0;
    Context context;
    Document doc;
    ArrayList<DisciplineItem> data = new ArrayList<>();
    String url = "http://my.khai.edu/my/discipline";
    DisciplineAdapter disciplineAdapter;
    String[] newTableFinal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.discipline_fragment, container, false);
        context = getActivity();
        // getActivity().setTitle("ExtraBallFragment");
        rootLayout = (CoordinatorLayout) rootView.findViewById(R.id.discipline_coordinator);
        setHasOptionsMenu(true);
        if (data.size() == 0) {
            new ParserBigData().execute();
        } else {
            disciplineAdapter = new DisciplineAdapter(data);
            disciplineAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(disciplineAdapter);
        }
        initRecycleView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        context = getActivity();
        disciplineAdapter = new DisciplineAdapter(data);
        disciplineAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(disciplineAdapter);
    }

    private void initRecycleView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_of_discipline);
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
            progressDialog.setTitle(getString(R.string.pre_exec_loading));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg) {
            try {
                doc = Jsoup.connect(url).get();
                if (doc != null) {
                    title = doc.select("option");
                    newTableFinal = new String[title.size()];
                    for (Element titles : title) {
                        newTableFinal[t] = titles.text();
                        t++;
                    }
                    for (int i = 1; i < newTableFinal.length; i++) {
                        data.add(new DisciplineItem(newTableFinal[i]));
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
                disciplineAdapter = new DisciplineAdapter(data);
                disciplineAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(disciplineAdapter);
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
}