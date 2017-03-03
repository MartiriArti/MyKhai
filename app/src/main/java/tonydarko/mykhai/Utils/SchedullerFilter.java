package tonydarko.mykhai.Utils;

import android.widget.Filter;

import java.util.ArrayList;

import tonydarko.mykhai.Adapters.SchedulerAdapter;
import tonydarko.mykhai.Items.SchedulerItem;

public class SchedullerFilter extends Filter {

    ArrayList<SchedulerItem> filterList;
    SchedulerAdapter adapter;

    public SchedullerFilter(ArrayList<SchedulerItem> filterList, SchedulerAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toLowerCase();

            ArrayList<SchedulerItem> filteredMovies = new ArrayList<>();

            for (SchedulerItem item : filterList) {
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
        adapter.setData((ArrayList<SchedulerItem>) results.values);
        adapter.notifyDataSetChanged();

    }
}
