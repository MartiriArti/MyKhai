package tonydarko.mykhai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.select.Elements;

import java.util.ArrayList;

import tonydarko.mykhai.Utils.Constant;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Boolean noOrYes;
    public Elements title;
    private ArrayList<String> urls;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        urls = new ArrayList<>();

        toolbar.setLogo(R.mipmap.logo);

        lv = (ListView) findViewById(R.id.listView1);
        noOrYes = Constant.getNoOrYes();
        final ArrayList<String> arrayList = new ArrayList<>();

        urls.add("http://my.khai.edu/my/student_marks");
        arrayList.add("Оцінки студента");
        urls.add("http://my.khai.edu/my/scolarship_ball");
        arrayList.add("Додаткові бали");
        urls.add("http://my.khai.edu/my/student_rating");
        arrayList.add("Рейтинги на стипендію");
        urls.add("http://my.khai.edu/my/discipline");
        arrayList.add("Вибір дисципліни");
        urls.add("http://my.khai.edu/my/stats");
        arrayList.add("Онлайн вибір");
        urls.add("http://my.khai.edu/my/scheduler");
        arrayList.add("Розклад занять за вибором");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.main_item, R.id.tv_main_item, arrayList);

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        Intent intent;
        switch (pos) {
            case 0:
                if (noOrYes) {
                    intent = new Intent(this, BallStudentActivity.class);
                    intent.putExtra("URL", urls.get(pos));
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }else {
                    Toast.makeText(this, "Вход не выполнен", Toast.LENGTH_LONG).show();
                }
                break;
            case 1:
                intent = new Intent(this, ExtraBallTableActivity.class);
                intent.putExtra("URL", urls.get(pos));
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case 2:
                if (noOrYes) {
                    intent = new Intent(this, RatingActivity.class);
                    intent.putExtra("URL", urls.get(pos));
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    Toast.makeText(this, "Вход не выполнен", Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                intent = new Intent(this, DisciplineActivity.class);
                intent.putExtra("URL", urls.get(pos));
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case 4:
                intent = new Intent(this, OnlineVoteActivity.class);
                intent.putExtra("URL", urls.get(pos));
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case 5:
                intent = new Intent(this, SchedulerActivity.class);
                intent.putExtra("URL", urls.get(pos));
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
    }
}