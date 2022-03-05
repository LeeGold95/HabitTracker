package listapp.habittracker.settingsscreen;

import listapp.habittracker.utils.DateManipulations;

/*
This class holds a single habit-settings item in settings view's habits list.
 */

public class SettingsItem {

    private String title, frequency, startDate, endDate;
    private int hid;

    public SettingsItem(String title, String frequency, String startDate, String endDate, int hid) {
        this.title = title;
        this.frequency = frequency;
        //dates are held in sql format
        this.startDate = startDate;
        this.endDate = endDate;
        this.hid = hid;
    }

    public String getTitle() {
        return title;
    }
    public String getFrequency() {
        return frequency;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getStartDateDisplay(){
        return DateManipulations.sqlToDisplayFormat(startDate);
    }
    public String getEndDate() {
        return endDate;
    }
    public String getEndDateDisplay(){
        return DateManipulations.sqlToDisplayFormat(endDate);
    }
    public int getHid() {
        return hid;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public void setHid(int hid){
        this.hid = hid;
    }
}
