package tonydarko.mykhai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tonydarko.mykhai.Items.BallStudentItem;
import tonydarko.mykhai.R;

public class BallStudentAdapter extends BaseAdapter {
    ArrayList<BallStudentItem> data;
    Context context;

    public BallStudentAdapter(ArrayList<BallStudentItem> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public ArrayList<BallStudentItem> getData() {
        return data;
    }

    public void setData(ArrayList<BallStudentItem> data) {
        this.data = data;
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
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (view == null) {
            view = inflater.inflate(R.layout.ball_student_item, viewGroup, false);
        }
        TextView subject = (TextView) view.findViewById(R.id.subject);

        TextView otcenka = (TextView) view.findViewById(R.id.otcenka);
        TextView ball = (TextView) view.findViewById(R.id.ball_student);
        TextView otcs = (TextView) view.findViewById(R.id.tv_esct);
        TextView tormConrl = (TextView) view.findViewById(R.id.tv_form_ctrl);


        subject.setText(data.get(i).subject);
        otcenka.setText(data.get(i).otcenka);
        ball.setText(data.get(i).ball);
        otcs.setText(data.get(i).ESTC);
        tormConrl.setText(data.get(i).formContrl);
        return view;
    }
}
