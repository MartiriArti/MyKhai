package tonydarko.mykhai.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import tonydarko.mykhai.R;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ExtraBallHolder> {


    private  ArrayList<String> text;
    private  ArrayList<String> value;

    public RatingAdapter( ArrayList<String> text, ArrayList<String> value) {
        this.text = text;
        this.value = value;
    }

    @Override
    public ExtraBallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rating_item, parent, false);
        return new ExtraBallHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExtraBallHolder holder, int position) {
        String text_t = text.get(position);
        String value_t = value.get(position);

        holder.text.setText(text_t);
        holder.value.setText(value_t);
    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    class ExtraBallHolder extends RecyclerView.ViewHolder {

        private TextView text;
        private TextView value;

        private ExtraBallHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.tv_text);
            value = (TextView) itemView.findViewById(R.id.tv_value);

        }
    }


}