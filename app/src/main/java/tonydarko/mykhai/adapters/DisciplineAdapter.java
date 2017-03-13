package tonydarko.mykhai.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tonydarko.mykhai.R;
import tonydarko.mykhai.items.DisciplineItem;


public class DisciplineAdapter extends RecyclerView.Adapter<DisciplineAdapter.ExtraBallHolder> {


    private ArrayList<DisciplineItem> data;


    public DisciplineAdapter(ArrayList<DisciplineItem> data) {
        this.data = data;
    }

    @Override
    public ExtraBallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discipline_item, parent, false);
        return new ExtraBallHolder(itemView);
    }

    public ArrayList<DisciplineItem> getData() {
        return data;
    }

    public void setData(ArrayList<DisciplineItem> data) {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(ExtraBallHolder holder, int position) {
        DisciplineItem disciplineItem = data.get(position);
        holder.subject.setText(disciplineItem.getSubject());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ExtraBallHolder extends RecyclerView.ViewHolder {

        private TextView subject;

        private ExtraBallHolder(View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.subject);

        }
    }


}