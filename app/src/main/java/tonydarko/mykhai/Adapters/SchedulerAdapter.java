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

import tonydarko.mykhai.Items.SchedulerItem;
import tonydarko.mykhai.R;
import tonydarko.mykhai.Utils.ExtraBallFilter;
import tonydarko.mykhai.Utils.SchedullerFilter;

public class SchedulerAdapter extends BaseAdapter implements Filterable {

    SchedullerFilter filter;
    private ArrayList<SchedulerItem> data_scheduler = new ArrayList<>();
    private Context context;

    public SchedulerAdapter(Context context, ArrayList<SchedulerItem> dat) {
        this.data_scheduler = dat;
        this.context = context;
        getFilter();
    }

    @Override
    public int getCount() {
        return data_scheduler.size();
    }

    @Override
    public Object getItem(int i) {
        return data_scheduler.get(i);
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
            view = inflater.inflate(R.layout.scheduler_item, viewGroup, false);
        }
        //Обявляем наши текствьюшки и связываем их с разметкой
        TextView fio = (TextView) view.findViewById(R.id.fio);
        TextView group = (TextView) view.findViewById(R.id.group);
        TextView para = (TextView) view.findViewById(R.id.para);
        TextView type = (TextView) view.findViewById(R.id.type);
        TextView data = (TextView) view.findViewById(R.id.data);
        TextView fio_prep = (TextView) view.findViewById(R.id.fio_prepod);


        //Устанавливаем в каждую текствьюшку соответствующий текст
        fio.setText(data_scheduler.get(i).fio);
        group.setText(data_scheduler.get(i).group);
        para.setText(data_scheduler.get(i).para);
        type.setText(data_scheduler.get(i).type);
        data.setText(data_scheduler.get(i).date);
        fio_prep.setText(data_scheduler.get(i).fioPrepod);


        return view;
    }

    public ArrayList<SchedulerItem> getData() {
        return data_scheduler;
    }

    public void setData(ArrayList<SchedulerItem> data_scheduler) {
        this.data_scheduler = data_scheduler;
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new SchedullerFilter(data_scheduler, this);
        }

        return filter;
    }

}