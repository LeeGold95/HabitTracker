package listapp.habittracker.settingsscreen;

import java.util.Date;

import listapp.habittracker.utils.DateManipulations;

/*
 This class sets validation functions to validate new habit fields input
 Each function returns error string if input is invalid, or null if valid.
 */

public class SettingsValidation {

    //for start date only condition is that it's a valid date.
    public static String startDateValid(String startDate){
        if(startDate.isEmpty()) //allow field to be left empty
            return null;
        return dateValidMsg(DateManipulations.dateValid(startDate,"dd-MM-yyyy"));
    }

    public static String endDateValid(String endDate, String startDate){
        if(endDate.isEmpty()) //allow field to be left empty
            return null;
        Date parsedEndDate = DateManipulations.dateValid(endDate, "dd-MM-yyyy");
        String dateValid = dateValidMsg(parsedEndDate);
        if(dateValid!=null)
            return dateValid;
        //if no start date was entered, check if end date is after today.
        if(startDate.isEmpty()){
            Date today = new Date();
            if(parsedEndDate.before(today))
                return "habit must end after today";
        }

        //if a start date was entered make sure it's valid and that it's before end date
        Date parsedStartDate = DateManipulations.dateValid(startDate, "dd-MM-yyyy");
        if(parsedStartDate==null)
            return "invalid start date";
        if(parsedStartDate.after(parsedEndDate))
            return "habit ends before it starts";


        return null;
    }

    public static String habitNameValid(String name){
        if(name.isEmpty())
            return "must enter habit name";
        return null;
    }


    private static String dateValidMsg(Date date){
        if(date==null)
            return "date format must be dd-mm-yyyy";
        return null;
    }


    public static Boolean validateAll(String habitName ,String startDate, String endDate){
        return (habitNameValid(habitName)==null && startDateValid(startDate)==null && endDateValid(endDate, startDate)==null);
    }



}
