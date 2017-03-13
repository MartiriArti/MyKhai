package tonydarko.mykhai.Filters;

import android.widget.Filter;


import java.util.ArrayList;

import tonydarko.mykhai.adapters.SchedullerAdapter;
import tonydarko.mykhai.items.SchedullerItem;

public class SchedullerFilter extends Filter {

    private  ArrayList<SchedullerItem> filterList;
    private SchedullerAdapter adapter;

    public SchedullerFilter(ArrayList<SchedullerItem> filterList, SchedullerAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toLowerCase();

            ArrayList<SchedullerItem> filteredMovies = new ArrayList<>();

            for (SchedullerItem item : filterList) {
                if (item.getGroup().toLowerCase().contains(constraint.toString().toLowerCase())
                        | item.getType().toLowerCase().contains(constraint.toString())
                        | item.getFio().toLowerCase().contains(constraint.toString())
                        | item.getFioPrepod().toLowerCase().contains(constraint.toString())
                        | item.getPara().toLowerCase().contains(constraint.toString())) {
                    filteredMovies.add(item);
                }
            }

            results.count = filteredMovies.size();
            results.values = filteredMovies;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setData((ArrayList<SchedullerItem>) results.values);
        adapter.notifyDataSetChanged();

    }
}
