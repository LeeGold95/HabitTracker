package listapp.habittracker.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateManipulations {

    public static Date getToday(){
        return new Date();
    }

    public static String toDisplayString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        //Date today = new Date();
        if(dateFormat.format(getToday()).equals(dateFormat.format(date)))
            dateFormat = new SimpleDateFormat("EEE, d MMM");
        else
            dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        return dateFormat.format(date);
    }
    public static String toSqlFormat(Date date){
        DateFormat queryFormat = new SimpleDateFormat("yyyy-MM-dd");
        return queryFormat.format(date);
    }
    public static String toDisplayFormat(Date date){
        DateFormat queryFormat = new SimpleDateFormat("dd-MM-yyyy");
        return queryFormat.format(date);
    }


    //takes string date in format yyyy-mm-dd and returns date String in dd-mm-yyyy format
    public static String sqlToDisplayFormat(String originDate){
        if(originDate==null || originDate.equals("null"))
            return null;
        try{
            Date newDate = dateValid(originDate, "yyyy-MM-dd");
            return toDisplayFormat(newDate);
        }
        catch (Exception e){ //catch invalid string input
            e.printStackTrace();
        }
        return null;
    }

    //takes string date in format dd-mm-yyyy and returns date String in yyyy-mm-dd format
    public static String displayToSqlFormat(String originDate){
        if(originDate==null || originDate.equals("null"))
            return null;

        try{
            Date newDate = dateValid(originDate, "dd-MM-yyyy");
            return toSqlFormat(newDate);
        }
        catch (Exception e){ //catch invalid string input
            e.printStackTrace();
        }

        return null;
    }


    public static Date getNext(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
    public static Date getPrevious(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static String getDayOfWeek(Date date){
        DateFormat queryFormat = new SimpleDateFormat("EEEE");
        return queryFormat.format(date);
    }

    public static Date dateValid(String date, String pattern){
        if(date == null)
            return null;
        Date parsedDate;
        DateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        try {
            parsedDate = format.parse(date);
        }
        catch (ParseException e) {
            return null;
        }
        return parsedDate;
    }



}
