package tonydarko.mykhai.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tonydarko.mykhai.Items.ExtraBallItem;
import tonydarko.mykhai.R;


public class ExtraBallAdapter extends BaseAdapter {

    ArrayList<ExtraBallItem> data = new ArrayList<ExtraBallItem>();
    Context context;

    public ExtraBallAdapter(Context context, ArrayList<ExtraBallItem> data) {
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
            view = inflater.inflate(R.layout.extra_ball_item, viewGroup, false);
        }
        //Обявляем наши текствьюшки и связываем их с разметкой
        TextView group = (TextView) view.findViewById(R.id.group);
        TextView lN = (TextView) view.findViewById(R.id.last_name);
        TextView fN = (TextView) view.findViewById(R.id.first_name);
        TextView otch = (TextView) view.findViewById(R.id.otchistvo);
        TextView ball = (TextView) view.findViewById(R.id.ball);

        //Устанавливаем в каждую текствьюшку соответствующий текст
       group.setText(data.get(i).group);
       lN.setText(data.get(i).lastName);
       fN.setText(data.get(i).firstName);
       otch.setText(data.get(i).otchistvo);
       ball.setText(data.get(i).ball);


        return view;
    }
}
