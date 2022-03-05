package listapp.habittracker.dataconnections;

import java.sql.SQLException;
import java.util.ArrayList;

import listapp.habittracker.settingsscreen.SettingsItem;

/*
This class creates a connection to the database
to get all habits that are defined by this user.
The class produces a list of SettingsItems with the matching data to show in SettingsActivity
 */

public class GetHabitSettings extends ConnectionHelper {

    private ArrayList<SettingsItem> habitList = new ArrayList<>();

    public GetHabitSettings(String sqlQuery) {
        super(sqlQuery, null);
    }

    @Override
    public void queryAction() throws SQLException{
        while (res.next()){
            int hid = res.getInt("hid");
            String title = res.getString("hname");
            String startDate = res.getString("start_date");
            String endDate = res.getString("end_date");
            String frequency = res.getString("repetition");
            habitList.add(new SettingsItem(title,frequency,startDate,endDate,hid));
        }
    }

    //get list of habits for today
    public ArrayList<SettingsItem> getHabitList() {
        return habitList;
    }

    //if an error occurred set list to be null.
    @Override
    public void catchDbException(Exception e) {
        super.catchDbException(e);
        habitList = null;
    }
}
