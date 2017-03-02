package tonydarko.mykhai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import tonydarko.mykhai.Items.ExtraBallItem;
import tonydarko.mykhai.R;
import tonydarko.mykhai.Utils.CustomFilter;


public class ExtraBallAdapter extends BaseAdapter implements Filterable {

    CustomFilter filter;
    ArrayList<ExtraBallItem> data;
    ArrayList<ExtraBallItem> filteredList;
    Context context;

    public ExtraBallAdapter(Context context, ArrayList<ExtraBallItem> data) {
        this.data = data;
        this.context = context;
        this.filteredList = data;
        getFilter();
    }

    public void setData(ArrayList<ExtraBallItem> data) {
        this.data = data;
    }

    public ArrayList<ExtraBallItem> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Получение объекта inflater из контекста
        LayoutInflater inflater = LayoutInflater.from(context);
        //Если someView (View из ListView) вдруг оказался равен
        //null тогда мы загружаем его с помошью inflater
        if (view == null) {
            view = inflater.inflate(R.layout.extra_ball_item, viewGroup, false);
        }
        //Обявляем наши текствьюшки и связываем их с разметкой
        TextView group = (TextView) view.findViewById(R.id.group);
        TextView fio = (TextView) view.findViewById(R.id.fio);

        TextView full = (TextView) view.findViewById(R.id.full_ball);
        TextView ball = (TextView) view.findViewById(R.id.ball);

        //Устанавливаем в каждую текствьюшку соответствующий текст
        group.setText(data.get(i).group);
        fio.setText(data.get(i).fio);
        full.setText(data.get(i).fullBall);
        ball.setText(data.get(i).ball);
        return view;
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new CustomFilter(data, this);
        }

        return filter;
    }
}

