package tonydarko.mykhai.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import tonydarko.mykhai.Items.ExtraBallItem;
import tonydarko.mykhai.Items.OnlineVoteItem;
import tonydarko.mykhai.R;
import tonydarko.mykhai.Utils.ExtraBallFilter;
import tonydarko.mykhai.Utils.VoteFilter;


public class OnlineVoteAdapter extends BaseAdapter implements Filterable {

    VoteFilter filter;
    ArrayList<ExtraBallItem> filteredList;
    ArrayList<OnlineVoteItem> data_online_vote = new ArrayList<>();
    private Context context;

    public OnlineVoteAdapter(Context context, ArrayList<OnlineVoteItem> dat) {
        this.data_online_vote = dat;
        this.context = context;
        getFilter();
    }

    public ArrayList<OnlineVoteItem> getData() {
        return data_online_vote;
    }

    public void setData(ArrayList<OnlineVoteItem> data_online_vote) {
        this.data_online_vote = data_online_vote;
    }

    @Override
    public int getCount() {
        return data_online_vote.size();
    }

    @Override
    public Object getItem(int i) {
        return data_online_vote.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Получение объекта inflater из контекста
        LayoutInflater inflater = LayoutInflater.from(context);
        //Если someView (View из ListView) вдруг оказался равен
        //null тогда мы загружаем его с помошью inflater
        if (view == null) {
            view = inflater.inflate(R.layout.online_vote_item, viewGroup, false);
        }
        //Обявляем наши текствьюшки и связываем их с разметкой
        TextView fio = (TextView) view.findViewById(R.id.fio);
        TextView num_zayava = (TextView) view.findViewById(R.id.num_zayava);
        TextView group = (TextView) view.findViewById(R.id.group);
        TextView para = (TextView) view.findViewById(R.id.para);
        TextView data = (TextView) view.findViewById(R.id.data);
        TextView fio_prep = (TextView) view.findViewById(R.id.fio_prepod);



        //Устанавливаем в каждую текствьюшку соответствующий текст
        num_zayava.setText("№" + data_online_vote.get(i).numZayava);
        fio.setText(data_online_vote.get(i).fioStudent);
        group.setText(data_online_vote.get(i).group);
        para.setText(data_online_vote.get(i).predmet);
        data.setText(data_online_vote.get(i).date);
        fio_prep.setText(data_online_vote.get(i).fioPrepod);

        if (Objects.equals(data_online_vote.get(i).date, "")){
            data.setText("Без дати");
        }

        return view;
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new VoteFilter(data_online_vote, this);
        }

        return filter;
    }

}