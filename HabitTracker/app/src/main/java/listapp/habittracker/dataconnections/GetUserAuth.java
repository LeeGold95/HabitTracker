package listapp.habittracker.dataconnections;

import android.content.Context;

import java.sql.SQLException;

import listapp.habittracker.utils.DialogPopUp;
import listapp.habittracker.utils.WarningManager;

/*
This class creates a connection to the database
in order to authenticate user's identity by username and email.
 */

public class GetUserAuth extends ConnectionHelper{

    int uid;
    Context context;
    String userEmail;

    public GetUserAuth(String sqlQuery, String username, Context context, String userEmail) {
        super(sqlQuery, new String[]{username});
        this.context = context;
        this.userEmail = userEmail;
        this.uid = -1;
    }

    @Override
    public void queryAction() throws SQLException{
        if(!res.next()) { //if resultSet is empty --> no user with that name found
            wrongInput();
        }
        else{
            String dbMail = res.getString("usermail");
            if(dbMail==null) //only allow reset for rows with email value
                noEmailValue();
            else {
                if (dbMail.equals(userEmail))
                    uid = res.getInt("uid");
                else
                    wrongInput();
            }
        }

    }

    public int getUid(){
        return uid;
    }

    private void wrongInput(){
        new DialogPopUp(context, "Can't reset password", "Username or email are incorrect.").show();
    }
    private void noEmailValue(){
        new DialogPopUp(context, "Can't reset password", "Email value was not provided.").show();
    }

    @Override
    public void catchDbException(Exception e) {
        super.catchDbException(e);
        WarningManager.dbConnectionError(context);
    }
}
