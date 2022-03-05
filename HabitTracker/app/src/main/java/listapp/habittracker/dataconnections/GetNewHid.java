package listapp.habittracker.dataconnections;

import java.sql.SQLException;

import listapp.habittracker.settingsscreen.SettingsItem;

/*
This class creates a connection to the database
in order to get the key (hid) for the latest habit created by the user.
 */

public class GetNewHid extends ConnectionHelper {

    private SettingsItem item;

    public GetNewHid(String sqlQuery, SettingsItem item) {
        super(sqlQuery, null);
        this.item = item;
    }

    @Override
    public void queryAction() throws SQLException{
        if(!res.next()) //if resultSet is empty --> no habits for user found
            item.setHid(res.getInt("max"));
    }

}
