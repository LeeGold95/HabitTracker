package listapp.habittracker.settingsscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import listapp.habittracker.R;

/*
This class connects the SettingsItem with it's design and functionality.
Set up a habit settings object for a habit in settings view.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>{

    private ArrayList<SettingsItem> habitList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onRemoveClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public static class SettingsViewHolder extends RecyclerView.ViewHolder{
        public TextView vTitle, vFrequency, vStartDate, vEndDate;
        public Button vEditHabit, vDeleteHabit;
        public SettingsViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            vTitle = itemView.findViewById(R.id.habitName);
            vFrequency = itemView.findViewById(R.id.habitFreq);
            vStartDate = itemView.findViewById(R.id.startDate);
            vEndDate = itemView.findViewById(R.id.endDate);

            vEditHabit = itemView.findViewById(R.id.editItem);
            vDeleteHabit = itemView.findViewById(R.id.deleteItem);

            vEditHabit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                            listener.onEditClick(position);
                    }
                }
            });
            vDeleteHabit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                            listener.onRemoveClick(position);
                    }
                }
            });
        }
    }

    public SettingsAdapter(ArrayList<SettingsItem> habitList) {
        this.habitList = habitList;
    }


    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item_layout, parent, false);
        return new SettingsViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        SettingsItem currentHabit = habitList.get(position);

        holder.vTitle.setText(currentHabit.getTitle());
        holder.vFrequency.setText(currentHabit.getFrequency());
        holder.vStartDate.setText(currentHabit.getStartDateDisplay());
        if(currentHabit.getEndDate()==null) //if no end date is set
            holder.vEndDate.setText("unlimited");
        else
            holder.vEndDate.setText(currentHabit.getEndDateDisplay());
    }

    @Override
    public int getItemCount() {
        return habitList.size(); //get number of settingsItems in current view
    }




}
