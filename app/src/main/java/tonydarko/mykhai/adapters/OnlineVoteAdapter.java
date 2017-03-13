package tonydarko.mykhai.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import tonydarko.mykhai.Filters.VoteFilter;
import tonydarko.mykhai.R;
import tonydarko.mykhai.items.OnlineVoteItem;


public class OnlineVoteAdapter extends RecyclerView.Adapter<OnlineVoteAdapter.ExtraBallHolder> implements Filterable{

    private VoteFilter filter;
    private ArrayList<OnlineVoteItem> data;
    private ArrayList<OnlineVoteItem> filteredList;

    public OnlineVoteAdapter(ArrayList<OnlineVoteItem> data) {
        this.data = data;
        this.filteredList = data;
        getFilter();
    }

    @Override
    public ExtraBallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.online_vote_item, parent, false);
        return new ExtraBallHolder(itemView);
    }

    public ArrayList<OnlineVoteItem> getData() {
        return data;
    }

    public void setData(ArrayList<OnlineVoteItem> data) {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(ExtraBallHolder holder, int position) {
        OnlineVoteItem onlineVoteItem = data.get(position);

        holder.numZayava.setText(onlineVoteItem.getNumZayava());
        holder.fioStudent.setText(onlineVoteItem.getFioStudent());
        holder.fioPrepod.setText(onlineVoteItem.getFioPrepod());
        holder.predmet.setText(onlineVoteItem.getPredmet());
        holder.group.setText(onlineVoteItem.getGroup());
        holder.date.setText(onlineVoteItem.getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ExtraBallHolder extends RecyclerView.ViewHolder {

        private TextView numZayava;
        private TextView fioStudent;
        private TextView fioPrepod;
        private TextView predmet;
        private TextView group;
        private TextView date;

        private ExtraBallHolder(View itemView) {
            super(itemView);
            numZayava = (TextView) itemView.findViewById(R.id.num_zayava);
            fioStudent = (TextView) itemView.findViewById(R.id.fio);
            fioPrepod = (TextView) itemView.findViewById(R.id.fio_prepod);
            predmet = (TextView) itemView.findViewById(R.id.para);
            group = (TextView) itemView.findViewById(R.id.group);
            date = (TextView) itemView.findViewById(R.id.data);
        }
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new VoteFilter(data, this);
        }

        return filter;
    }
}