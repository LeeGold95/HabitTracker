package listapp.habittracker.mainscreen;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import listapp.habittracker.R;

/*
This class connects the MainItem with it's design and functionality.
Set up a checkbox object for a habit in main view.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainBoxViewHolder> {

    private ArrayList<MainItem> habitBoxList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void doOnClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class MainBoxViewHolder extends RecyclerView.ViewHolder{
        public CheckBox vCheckBox;
        public MainBoxViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            vCheckBox = itemView.findViewById(R.id.checkBox);
            vCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                            listener.doOnClick(position);
                    }
                }
            });
        }
    }

    public MainAdapter(ArrayList<MainItem> habitBoxList) {
        this.habitBoxList = habitBoxList;
    }

    @NonNull
    @Override
    public MainBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_layout, parent, false);
        return new MainBoxViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainBoxViewHolder holder, int position) {
        MainItem currentHabitBox = habitBoxList.get(position);

        holder.vCheckBox.setText(currentHabitBox.getHabitName());
        if(currentHabitBox.isChecked()){
            holder.vCheckBox.setChecked(true);
            holder.vCheckBox.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.vCheckBox.setChecked(false);
            holder.vCheckBox.setPaintFlags(0);
        }
    }

    @Override
    public int getItemCount() {
        return habitBoxList.size(); //get number of mainItems in current view
    }


}
