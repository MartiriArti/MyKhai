package tonydarko.mykhai.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import java.util.ArrayList;

import tonydarko.mykhai.Filters.SchedullerFilter;
import tonydarko.mykhai.R;
import tonydarko.mykhai.items.SchedullerItem;

public class SchedullerAdapter extends RecyclerView.Adapter<SchedullerAdapter.SchedullerHolder> implements Filterable {

    private SchedullerFilter filter;
    private ArrayList<SchedullerItem> data;
    private ArrayList<SchedullerItem> filteredList;

    public SchedullerAdapter(ArrayList<SchedullerItem> data) {
        this.data = data;
        this.filteredList = data;
        getFilter();
    }

    @Override
    public SchedullerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scheduller_item, parent, false);
        return new SchedullerHolder(itemView);
    }

    public ArrayList<SchedullerItem> getData() {
        return data;
    }

    public void setData(ArrayList<SchedullerItem> data) {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(SchedullerHolder holder, int position) {
        SchedullerItem schedullerItem = data.get(position);

        holder.group.setText(schedullerItem.getGroup());
        holder.fio.setText(schedullerItem.getFio());
        holder.para.setText(schedullerItem.getPara());
        holder.type.setText(schedullerItem.getType());
        holder.date.setText(schedullerItem.getDate());
        holder.fioPrepod.setText(schedullerItem.getFioPrepod());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class SchedullerHolder extends RecyclerView.ViewHolder {

        private TextView fio;
        private TextView group;
        private TextView para;
        private TextView type;
        private TextView date;
        private TextView fioPrepod;


        private SchedullerHolder(View itemView) {
            super(itemView);
            group = (TextView) itemView.findViewById(R.id.group);
            fio = (TextView) itemView.findViewById(R.id.fio);
            para = (TextView) itemView.findViewById(R.id.para);
            type = (TextView) itemView.findViewById(R.id.type);
            date = (TextView) itemView.findViewById(R.id.data);
            fioPrepod = (TextView) itemView.findViewById(R.id.fio_prepod);

        }
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new SchedullerFilter(data, this);
        }

        return filter;
    }
}