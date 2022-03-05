package listapp.habittracker.mainscreen;

import java.util.Date;

import listapp.habittracker.utils.DateManipulations;

/*
This class holds a single habit-checkbox item in main view checkbox list.
 */

public class MainItem {
    private int hid;
    private String habitName;
    private Boolean checked;
    private Date date;


    public MainItem(int hid, String habitName, Boolean checked, Date date) {
        this.hid = hid;
        this.habitName = habitName;
        this.checked = checked;
        this.date = date;
    }

    public String getHabitName() {
        return habitName;
    }
    public Boolean isChecked() {
        return checked;
    }
    public int getHid(){
        return hid;
    }
    public Date getDate() {
        return date;
    }
    public String getDateString(){
        return DateManipulations.toSqlFormat(date);
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }
}
