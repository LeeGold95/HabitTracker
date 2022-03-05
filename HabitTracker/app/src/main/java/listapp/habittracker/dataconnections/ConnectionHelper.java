package listapp.habittracker.dataconnections;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
This class creates a connection to the database server using JDBC.
All classes that handle connections will inherit from this class.
This allows child classes to implement the result action only.
That way we make sure all connections work as they should and save repetition of the connection setup part of the code.
 */

public abstract class ConnectionHelper extends AsyncTask<String,String,String> {

    String DB_URL = "jdbc:mysql://"+ DbVars.DATABASE_URL+"/"+DbVars.DATABASE_NAME; //import jdbc package for MySql
    Connection connection;
    PreparedStatement statement;
    ResultSet res;

    String query;
    String[] params;

    protected ConnectionHelper(String sqlQuery, String[] params) {
        super();
        this.query = sqlQuery;
        this.params = params;
    }

    @Override
    protected String doInBackground(String... strings) {
        try{
            Class.forName("com.mysql.jdbc.Driver");

            //create connection and prepared statement from string query
            connection = DriverManager.getConnection(DB_URL, DbVars.USERNAME, DbVars.PASSWORD);
            statement = connection.prepareStatement(query);

            //get variables for query.
            //we don't check input type because all user inputs are strings (uid & hid are not provided by users)
            //if at some point other type of input will be added:
            //change list type to Object & check input type before adding it to statement.

            if(params!=null) { //process only queries with parameters
                for (int i = 0; i < params.length; i++) {
                    statement.setString(i+1 , params[i]);
                }
            }

            //run query on connection and get result
            if(statement.execute())
                res = statement.getResultSet();

            queryAction(); //let child classes process result

            //close connection
            if(res!=null)
                res.close();
            statement.close();
            connection.close();

        }
        catch (SQLException e) {
            Log.e("SQL STATE", e.getSQLState());
            catchDbException(e);
        }
        catch (Exception e){
            catchDbException(e);
        }
        finally {
            try {
                if(statement != null)
                    statement.close();
                if(connection != null)
                    connection.close();
            }
            catch (SQLException e){
                catchDbException(e);
            }
        }

        return null;
    }

    //lets child classes decide how to handle exceptions.
    protected abstract void queryAction() throws SQLException;

    // handle exceptions in a function so that child classes are able to handle exceptions individually.
    public void catchDbException(Exception e){
        //log exception to developers
        Log.e("LOG!!", "IN DB CATCH");
        Log.e("SQLException", e.getMessage());
    }

}
