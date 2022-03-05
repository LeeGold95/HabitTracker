package listapp.habittracker.dataconnections;

import java.sql.SQLException;

/*
This class creates a connection to the database to check if username exists in the system
 */

public class FindUsername extends ConnectionHelper {

    private Boolean userExists;

    public FindUsername(String sqlQuery, String userName) {
        super(sqlQuery, new String[]{userName});
    }

    @Override
    public void queryAction() throws SQLException{
            userExists = res.next(); //if query returned a result, username exists in system.
    }

    public Boolean getUserExists() {
        return userExists;
    }

}
