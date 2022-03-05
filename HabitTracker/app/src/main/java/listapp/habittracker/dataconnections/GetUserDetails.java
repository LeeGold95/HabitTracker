package listapp.habittracker.dataconnections;

import android.content.Context;

import java.sql.SQLException;

import listapp.habittracker.utils.WarningManager;

/*
This class creates a connection to the database to get user row by uid.
 */

public class GetUserDetails extends ConnectionHelper{

    private String username = null;
    private String email = null;
    private Context context;

    public GetUserDetails(String sqlQuery, Context context) {
        super(sqlQuery, null);
        this.context = context;
    }

    @Override
    public void queryAction() throws SQLException{
        if(res.next()) {
            username = res.getString("username");
            email = res.getString("usermail");
        }
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }

    @Override
    public void catchDbException(Exception e) {
        super.catchDbException(e);
        WarningManager.dbConnectionError(context);
    }
}
