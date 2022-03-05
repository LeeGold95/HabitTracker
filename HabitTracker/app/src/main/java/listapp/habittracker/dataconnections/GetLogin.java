package listapp.habittracker.dataconnections;

import android.app.Activity;

import java.sql.SQLException;

import listapp.habittracker.utils.DialogPopUp;
import listapp.habittracker.drawer.IntentManager;
import listapp.habittracker.utils.WarningManager;
import listapp.habittracker.mainscreen.activities.MainActivity;

/*
This class creates a connection to the database to attempt a user's login.
The class checks database against user-provided username and password
and shows error \ logs-in accordingly.
 */

public class GetLogin extends ConnectionHelper {
    private Activity activity;
    private Boolean match;
    private int uid;


    public GetLogin(String sqlQuery, String username, String password, Activity activity) {
        super(sqlQuery, new String[] {username, password});
        this.activity = activity;
        this.match = false;
    }

    @Override
    public void queryAction() throws SQLException{
        if(!res.next()) //if resultSet is empty --> no user with that name found or password incorrect
            wrongInput();
        else{
            // successful login --> save user id
            match = true;
            uid = res.getInt("uid");
        }
    }

    @Override
    protected void onPostExecute(String msg) {
        if(match){ //successful login --> send user id and move to MainActivity
            IntentManager.changeIntent(activity,MainActivity.class,uid);
        }
    }

    private void wrongInput(){ //unsuccessful login --> show error to user
        new DialogPopUp(activity, "failed to login", "Username or password are incorrect").show();
    }

    @Override
    public void catchDbException(Exception e) {
        super.catchDbException(e);
        WarningManager.dbConnectionError(activity);
    }
}
