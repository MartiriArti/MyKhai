package tonydarko.mykhai.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import tonydarko.mykhai.Filters.ExtraBallFilter;
import tonydarko.mykhai.R;
import tonydarko.mykhai.items.ExtraBallItem;


public class ExtraBallAdapter extends RecyclerView.Adapter<ExtraBallAdapter.ExtraBallHolder> implements Filterable{

    private  ExtraBallFilter filter;
    private  ArrayList<ExtraBallItem> data;
    private ArrayList<ExtraBallItem> filteredList;

    public ExtraBallAdapter(ArrayList<ExtraBallItem> data) {
        this.data = data;
        this.filteredList = data;
        getFilter();
    }

    @Override
    public ExtraBallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.extra_ball_item, parent, false);
        return new ExtraBallHolder(itemView);
    }

    public ArrayList<ExtraBallItem> getData() {
        return data;
    }

    public void setData(ArrayList<ExtraBallItem> data) {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(ExtraBallHolder holder, int position) {
        ExtraBallItem extraBallItem = data.get(position);
        holder.group.setText(extraBallItem.getGroup());
        holder.fio.setText(extraBallItem.getFio());
        holder.fullBall.setText(extraBallItem.getFullBall());
        holder.ball.setText(extraBallItem.getBall());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ExtraBallHolder extends RecyclerView.ViewHolder {

        private TextView group;
        private TextView fio;
        private TextView fullBall;
        private TextView ball;

        private ExtraBallHolder(View itemView) {
            super(itemView);
            group = (TextView) itemView.findViewById(R.id.group);
            fio = (TextView) itemView.findViewById(R.id.fio);
            fullBall = (TextView) itemView.findViewById(R.id.full_ball);
            ball = (TextView) itemView.findViewById(R.id.ball);
        }
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new ExtraBallFilter(data, this);
        }

        return filter;
    }
}