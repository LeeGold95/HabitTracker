package listapp.habittracker.dataconnections;

import java.sql.SQLException;

/*
This class creates a connection to the database and executes a manipulation query (inset, delete,...)
This class can be used for any query that does not have a result or ignores result.
 */

public class UpdateDb extends ConnectionHelper {

    //get query with parameters
    public UpdateDb(String sqlQuery, String[] params) {
        super(sqlQuery, params);
    }
    //get query that doesn't have parameters
    public UpdateDb(String sql){
        this(sql, null);
    }

    //no action is needed on query result --> set empty method
    @Override
    public void queryAction() throws SQLException {

    }

}
