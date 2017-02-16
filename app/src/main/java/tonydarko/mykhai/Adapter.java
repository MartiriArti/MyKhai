package tonydarko.mykhai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class Adapter extends BaseAdapter {

    ArrayList<Item> data = new ArrayList<Item>();
    Context context;

    public Adapter( Context context, ArrayList<Item> data) {
        this.data = data;
        this.context = context;
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
            view = inflater.inflate(R.layout.item, viewGroup, false);
        }
        //Обявляем наши текствьюшки и связываем их с разметкой
        TextView group = (TextView) view.findViewById(R.id.group);
        TextView fio = (TextView) view.findViewById(R.id.fio);
        TextView ball = (TextView) view.findViewById(R.id.ball);

        //Устанавливаем в каждую текствьюшку соответствующий текст
       group.setText(data.get(i).group);
       fio.setText(data.get(i).fio);
       ball.setText(data.get(i).ball);


        return view;
    }
}
