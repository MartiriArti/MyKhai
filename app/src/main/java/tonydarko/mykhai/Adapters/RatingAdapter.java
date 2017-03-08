package tonydarko.mykhai.Adapters;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import tonydarko.mykhai.R;

public class RatingAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> text;
    ArrayList<String> value;

    public RatingAdapter(Context context, ArrayList<String> text, ArrayList<String> value) {
        this.context = context;
        this.text = text;
        this.value = value;
    }


    @Override
    public int getCount() {
        return text.size();
    }

    @Override
    public Object getItem(int i) {
        return text.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (view == null) {
            view = inflater.inflate(R.layout.rating_item, viewGroup, false);
        }
        TextView tvText = (TextView) view.findViewById(R.id.tv_text);
        TextView tvValue = (TextView) view.findViewById(R.id.tv_value);

        tvText.setText(text.get(i));
        tvValue.setText(value.get(i));
        return view;
    }
}
