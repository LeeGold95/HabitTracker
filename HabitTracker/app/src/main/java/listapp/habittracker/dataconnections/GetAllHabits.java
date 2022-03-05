package listapp.habittracker.dataconnections;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import listapp.habittracker.utils.DateManipulations;
import listapp.habittracker.mainscreen.MainItem;

/*
This class creates a connection to the database
to get all habits that are defined to show in this date for this user.
The class produces a list of MainItems with the matching data to show in MainActivity.
 */

public class GetAllHabits extends ConnectionHelper {

    private ArrayList<MainItem> habitBoxList = new ArrayList<>();
    private Date date;
    private int checkedCounter;

    public GetAllHabits(String sqlQuery, Date date) {
        super(sqlQuery, null);
        this.date = date;
        this.checkedCounter = 0; //count how many of today's habits were marked as done.
    }

    @Override
    public void queryAction() throws SQLException{
            String day = DateManipulations.getDayOfWeek(date);
            //loop through results
            while(res.next()) {
                String rep = res.getString("repetition");
                if (rep.contains("Daily") || rep.contains(day)) { //check if date is included in repetition
                    int hid = res.getInt("hid");
                    String hname = res.getString("hname");
                    String mark = res.getString("checked");
                    Boolean checked = mark != null; //check if user marked this habit as done this date
                    habitBoxList.add(new MainItem(hid, hname, checked, date));
                    if(checked)
                        checkedCounter++;
                }
            }
    }
    //get list of habits for today
    public ArrayList<MainItem> getHabitBoxList() {
        return habitBoxList;
    }
    //get number of achieved habits in habitBoxList
    public int getCheckedCounter() {
        return checkedCounter;
    }

    //if an error occurred set list to be null.
    @Override
    public void catchDbException(Exception e) {
        super.catchDbException(e);
        habitBoxList = null;
    }
}
