package tonydarko.mykhai.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import tonydarko.mykhai.R;
import tonydarko.mykhai.items.BallStudentItem;

public class StudentBallsAdapter extends RecyclerView.Adapter<StudentBallsAdapter.ExtraBallHolder> {


    private  ArrayList<BallStudentItem> data;


    public StudentBallsAdapter(ArrayList<BallStudentItem> data) {
        this.data = data;
    }

    @Override
    public ExtraBallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ball_student_item, parent, false);
        return new ExtraBallHolder(itemView);
    }

    public ArrayList<BallStudentItem> getData() {
        return data;
    }

    public void setData(ArrayList<BallStudentItem> data) {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(ExtraBallHolder holder, int position) {
        BallStudentItem ballStudentItem = data.get(position);
        holder.subject.setText(ballStudentItem.getSubject());
        holder.otcenka.setText(ballStudentItem.getOtcenka());
        holder.ball.setText(ballStudentItem.getBall());
        holder.etcs.setText(ballStudentItem.getESTC());
        holder.formContr.setText(ballStudentItem.getFormContrl());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ExtraBallHolder extends RecyclerView.ViewHolder {

        private TextView subject;
        private TextView otcenka;
        private TextView ball;
        private TextView etcs;
        private TextView formContr;

        private ExtraBallHolder(View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.subject);
            otcenka = (TextView) itemView.findViewById(R.id.otcenka);
            ball = (TextView) itemView.findViewById(R.id.ball_student);
            etcs = (TextView) itemView.findViewById(R.id.tv_esct);
            formContr = (TextView) itemView.findViewById(R.id.tv_form_ctrl);

        }
    }


}