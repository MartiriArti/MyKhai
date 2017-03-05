package tonydarko.mykhai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import tonydarko.mykhai.Adapters.ExtraBallAdapter;
import tonydarko.mykhai.Items.ExtraBallItem;
import tonydarko.mykhai.Parsers.ExtraParser;

public class ExtraBallTableActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    String url;
    ExtraBallAdapter extraBallAdapter;
    private ListView lv;
    private ArrayList<ExtraBallItem> data = new ArrayList<>();
    String[][] newTableFinal;
    TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_ball);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setTitle("Додаткові бали");
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");

        lv = (ListView) findViewById(R.id.listViewTable);

        info = (TextView) findViewById(R.id.inform);

        ExtraParser parser = new ExtraParser(url);
        parser.execute();

        try {
            parser.get();
            newTableFinal = parser.getNewTableFinal();
            info.setText(newTableFinal[0][0]);//info message

            data = parser.getData();

            extraBallAdapter = new ExtraBallAdapter(this, data);
            extraBallAdapter.notifyDataSetChanged();

            lv.setAdapter(extraBallAdapter);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setBackgroundColor(Color.WHITE);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String queryText) {
        extraBallAdapter.getFilter().filter(queryText);
        return false;
    }


}