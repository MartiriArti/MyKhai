package tonydarko.mykhai.Utils;

import android.widget.Filter;

import java.util.ArrayList;

import tonydarko.mykhai.Adapters.OnlineVoteAdapter;
import tonydarko.mykhai.Items.OnlineVoteItem;

public class VoteFilter extends Filter {

    ArrayList<OnlineVoteItem> filterList;
    OnlineVoteAdapter adapter;

    public VoteFilter(ArrayList<OnlineVoteItem> filterList, OnlineVoteAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toLowerCase();

            ArrayList<OnlineVoteItem> filteredMovies = new ArrayList<>();

            for (OnlineVoteItem item : filterList) {
                if (item.getGroup().toLowerCase().contains(constraint.toString().toLowerCase())
                        | item.getFioStudent().toLowerCase().contains(constraint.toString())
                        | item.getFioPrepod().toLowerCase().contains(constraint.toString())
                        | item.getPredmet().toLowerCase().contains(constraint.toString())) {
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
        adapter.setData((ArrayList<OnlineVoteItem>) results.values);
        adapter.notifyDataSetChanged();

    }
}