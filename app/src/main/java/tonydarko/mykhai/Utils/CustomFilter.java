package tonydarko.mykhai.Utils;

import android.widget.Filter;

import java.util.ArrayList;

import tonydarko.mykhai.Adapters.ExtraBallAdapter;
import tonydarko.mykhai.Items.ExtraBallItem;

public class CustomFilter extends Filter {

    ArrayList<ExtraBallItem> filterList;
    ExtraBallAdapter adapter;

    public CustomFilter(ArrayList<ExtraBallItem> filterList, ExtraBallAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    //FILTERING
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        //RESULTS
        FilterResults results = new FilterResults();

        //VALIDATION
        if (constraint != null && constraint.length() > 0) {

            //CHANGE TO UPPER FOR CONSISTENCY
            constraint = constraint.toString().toUpperCase();

            ArrayList<ExtraBallItem> filteredMovies = new ArrayList<>();

            //LOOP THRU FILTER LIST
            for (int i = 0; i < filterList.size(); i++) {
                //FILTER
                if (filterList.get(i).getGroup().contains(constraint)) {
                    filteredMovies.add(filterList.get(i));
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


    //PUBLISH RESULTS

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.setData((ArrayList<ExtraBallItem>) results.values);
        adapter.notifyDataSetChanged();

    }
}