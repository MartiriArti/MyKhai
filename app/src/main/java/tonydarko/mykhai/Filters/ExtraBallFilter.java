package tonydarko.mykhai.Filters;

import android.widget.Filter;


import java.util.ArrayList;

import tonydarko.mykhai.adapters.ExtraBallAdapter;
import tonydarko.mykhai.items.ExtraBallItem;

public class ExtraBallFilter extends Filter {

    private ArrayList<ExtraBallItem> filterList;
    private ExtraBallAdapter adapter;

    public ExtraBallFilter(ArrayList<ExtraBallItem> filterList, ExtraBallAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toLowerCase();

            ArrayList<ExtraBallItem> filteredMovies = new ArrayList<>();

            for (ExtraBallItem item : filterList) {
                if (item.getGroup().toLowerCase().contains(constraint.toString().toLowerCase())
                        | item.getFio().toLowerCase().contains(constraint.toString()) ) {
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
        adapter.setData((ArrayList<ExtraBallItem>) results.values);
        adapter.notifyDataSetChanged();

    }
}