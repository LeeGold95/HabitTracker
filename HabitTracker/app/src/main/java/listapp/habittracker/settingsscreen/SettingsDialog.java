package listapp.habittracker.settingsscreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import listapp.habittracker.utils.DateManipulations;
import listapp.habittracker.utils.DialogPopUp;
import listapp.habittracker.R;
import listapp.habittracker.utils.ViewManager;
import listapp.habittracker.utils.WarningManager;
import listapp.habittracker.dataconnections.GetNewHid;
import listapp.habittracker.dataconnections.UpdateDb;

/*
This class creates a dialog to edit an existing habit of user or create new one.

This class is implemented as a dialog and not an Activity to minimize unnecessary database access and Activity changes.
After change in habit/ new habit creation, the database and view are updated accordingly.
that way, we don't need to reload all habits from database to Activity every time a change is made.
 */

public class SettingsDialog extends AppCompatDialogFragment {

    private Boolean newEntity;
    private int uid, position;
    private SettingsAdapter adapter;
    private ArrayList<SettingsItem> habitList;
    private SettingsItem habit;

    private TextView start_date, end_date, name;
    private RadioButton daily, weekday;
    private Switch[] days = new Switch[7];
    private TextView startDateWarn, endDateWarn, habitNameWarn;

    private String positiveButton, negativeButton;
    AlertDialog dialog;

    String[] dayStrings = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public SettingsDialog(int uid, SettingsAdapter adapter, ArrayList<SettingsItem> habitList, int position) {
        this.uid = uid;
        this.adapter = adapter;
        this.habitList = habitList;
        this.position = position;

        if(position==-1){
            this.habit = null;
            this.newEntity = true;
        }
        else{
            this.habit = habitList.get(position);
            this.newEntity = false;
        }

        setButtons();
    }
    public SettingsDialog(int uid, SettingsAdapter adapter, ArrayList<SettingsItem> habitList) {
        this(uid, adapter, habitList, -1);
    }

    public void show(FragmentManager manager){
        this.show(manager, "settings");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_dialog, null);

        //set positive button action to null, and define it after dialog creation,
        // in order to override default on-click dismiss() function
        builder.setView(view)
                .setNegativeButton(negativeButton, (dialog, which) -> negButtonAction())
                .setPositiveButton(positiveButton,null);

        setViewVars(view);
        setViewListeners();
        setOnClicks();

        fillForm();

        dialog = builder.create();
        dialog.show();

        Button posButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        posButton.setOnClickListener(v-> posButtonAction());

        return dialog;
    }

    private void setViewVars(View view){
        start_date = view.findViewById(R.id.newStartDate);
        end_date = view.findViewById(R.id.newEndDate);
        name = view.findViewById(R.id.newHabitName);

        daily = view.findViewById(R.id.dailyCheckBox);
        weekday = view.findViewById(R.id.weekdayCheckBox);

        days[0] = view.findViewById(R.id.su);
        days[1] = view.findViewById(R.id.mo);
        days[2] = view.findViewById(R.id.tu);
        days[3] = view.findViewById(R.id.we);
        days[4] = view.findViewById(R.id.th);
        days[5] = view.findViewById(R.id.fr);
        days[6] = view.findViewById(R.id.sa);

        startDateWarn = view.findViewById(R.id.startDateWarn);
        endDateWarn = view.findViewById(R.id.endDateWarn);
        habitNameWarn = view.findViewById(R.id.habitNameWarn);
    }
    private void setButtons(){
        if(newEntity){
            positiveButton = "create new";
        }
        else{
            positiveButton = "apply changes";
        }
        negativeButton = "cancel";
    }

    //set listeners to detect invalid input. change warn views accordingly.
    private void setViewListeners(){
        name.addTextChangedListener(new ViewManager.newTextWatcher() {
            @Override
            public void doOnTextChange(String s) {
                ViewManager.changeWarnFlag(habitNameWarn, SettingsValidation.habitNameValid(s));
            }
        }.getTextWatcher());
        start_date.addTextChangedListener(new ViewManager.newTextWatcher() {
            @Override
            public void doOnTextChange(String s) {
                ViewManager.changeWarnFlag(startDateWarn, SettingsValidation.startDateValid(s));
            }
        }.getTextWatcher());
        end_date.addTextChangedListener(new ViewManager.newTextWatcher() {
            @Override
            public void doOnTextChange(String s) {
                ViewManager.changeWarnFlag(endDateWarn, SettingsValidation.endDateValid(s, ViewManager.getString(start_date)));
            }
        }.getTextWatcher());
    }
    private void setOnClicks(){
        daily.setOnClickListener(v -> pickDaily(true));
        weekday.setOnClickListener(v -> pickDaily(false));
    }

    private void posButtonAction(){
        if(SettingsValidation.validateAll(ViewManager.getString(name),ViewManager.getString(start_date),ViewManager.getString(end_date))) {
            String[] inputVars = getDialogValues();
            if(inputVars[2]==null) //if no repetition was picked
                new DialogPopUp(this.getContext(), "missing information", "please mark at least one day or check Daily button").show();
            else{
                if (!newEntity) {
                    updateHabit(inputVars[0], inputVars[1], inputVars[2]);
                } else {
                    createHabit(inputVars[0], inputVars[1], inputVars[2]);
                }
                this.dismiss();
            }
        }
        else
            new DialogPopUp(this.getContext(), "missing information", WarningManager.MISSING_FIELDS).show();
    }
    private void negButtonAction(){
        this.dismiss();
    }

    private void updateHabit(String startDate, String endDate, String repetition){
        startDate = getQueryStartDate(startDate);
        updateHabitDb(startDate, endDate, repetition);
        updateHabitView(startDate, endDate, repetition);
    }
    private void createHabit(String startDate, String endDate, String repetition){
        startDate = getQueryStartDate(startDate);
        newHabitDb(startDate, endDate, repetition);
        addHabitView(startDate, endDate, repetition);
    }



    private String[] getDialogValues(){
        String startDate = DateManipulations.displayToSqlFormat(ViewManager.getString(start_date));
        String endDate = DateManipulations.displayToSqlFormat(ViewManager.getString(end_date));

        StringBuilder repetition = null;
        if (daily.isChecked())
            repetition = new StringBuilder("Daily");
        for(int i=0; i<days.length; i++){
            if(days[i].isChecked()){
                if(repetition==null)
                    repetition = new StringBuilder(dayStrings[i]);
                else
                    repetition.append(",").append(dayStrings[i]);
            }
        }

        if(repetition!=null)
            return new String[] {startDate, endDate, repetition.toString()};
        else
            return new String[] {startDate, endDate, null};
    }

    private void updateHabitDb(String startDate, String endDate, String repetition){
        //String[] dateStrings = getQueryEndDate(startDate, endDate);
        String[] params;
        String query;
        if(endDate==null){
            query = "UPDATE habits " +
                    "SET hname = ? , start_date = ? , end_date = null , repetition = ? " +
                    "WHERE hid = " + habit.getHid();
            params = new String[] {ViewManager.getString(name), startDate, repetition};
        }
        else{
            query = "UPDATE habits " +
                    "SET hname = ? , start_date = ? , end_date = ? , repetition = ? " +
                    "WHERE hid = " + habit.getHid();
            params = new String[] {ViewManager.getString(name), startDate, startDate, repetition};
        }

        new UpdateDb(query, params).execute();
    }
    private void newHabitDb(String startDate, String endDate, String repetition){
        //String[] dateStrings = getQueryEndDate(startDate, endDate);
        String[] params;
        String query;
        if(endDate==null) {
            query = "INSERT INTO habits " +
                    "VALUES (null, ? , ? ,null, ? , "+uid+")";
            params = new String[]{ViewManager.getString(name), startDate, repetition};
        }
        else{
            query = "INSERT INTO habits " +
                    "VALUES (null, ? , ? , ? , ? , "+uid+")";
            params = new String[]{ViewManager.getString(name), startDate, endDate, repetition};
        }

        new UpdateDb(query, params).execute();
    }


    private String getQueryStartDate(String startDate){
        if(startDate==null)
            return DateManipulations.toSqlFormat(DateManipulations.getToday());
        return  startDate;
    }

    private void updateHabitView(String startDate, String endDate, String repetition){
        habit.setTitle(ViewManager.getString(name));
        habit.setFrequency(repetition);

        habit.setStartDate(startDate);
        habit.setEndDate(endDate);
        adapter.notifyItemChanged(position);
    }
    private void addHabitView(String startDate, String endDate, String repetition){
        SettingsItem newHabit = new SettingsItem(ViewManager.getString(name), repetition, startDate, endDate, -1);
        habitList.add(newHabit);
        adapter.notifyItemInserted(habitList.size());

        //update new hid after updating view in order not to wait for database access
        String sql = "SELECT MAX(hid) AS max FROM habits WHERE uid="+uid;
        new GetNewHid(sql, newHabit);
    }

    //
    //next functions handle functionality of single items in dialog.
    //

    //This function makes sure a user can only pick one type of repetition (Daily\pick days)
    //We allow picking both types by picking days and then marking Daily.
    //That is done to make the transformation from daily to pick days smoother,
    //in cases where users want to go back to days they picked before marking daily.
    private void pickDaily(Boolean picked){
        if(picked){
            daily.setChecked(true);
            weekday.setChecked(false);
            disableWeekdays();
        }
        else{
            daily.setChecked(false);
            weekday.setChecked(true);
            enableWeekdays();
        }
    }
    private void disableWeekdays(){
        for(Switch day: days){
            day.setClickable(false);
        }
    }
    private void enableWeekdays(){
        for(Switch day: days){
            day.setClickable(true);
        }
    }

    private void fillForm(){
        //if this form is for editing.
        if(!newEntity){
            String hrepetition = habit.getFrequency();
            name.setText(habit.getTitle());
            this.start_date.setText(habit.getStartDateDisplay());
            if(end_date!=null)
                this.end_date.setText(habit.getEndDateDisplay());
            markDays(hrepetition);
            pickDaily(hrepetition.contains("Daily"));
        }
        else{ //if this form is for new habit --> pick Daily as default.
            pickDaily(true);
        }
    }
    private void markDays(String marked){ //mark all days that are in habit's repetition as checked
        for(int i = 0; i< dayStrings.length; i++){
            days[i].setChecked(marked.contains(dayStrings[i]));
        }
    }

}
