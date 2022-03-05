package listapp.habittracker.utils;

import android.app.AlertDialog;
import android.content.Context;

import listapp.habittracker.utils.DialogPopUp;

public class WarningManager {

    public static final String MISSING_EMAIL = "Missing an email address will prevent you from resetting your password in the future.\nAre you sure?";
    public static final String DB_ISSUE = "Failed to connect to database.\nPlease try again later.\nContact us if this problem proceeds.";
    public static final String MISSING_FIELDS = "Some fields are missing or incorrect.";


    public static void dbConnectionError(Context context){
        new DialogPopUp(context, "CONNECTION ISSUE", DB_ISSUE).show();
    }

    public static abstract class MissingEmail extends WarningAlert {
        public MissingEmail(Context context) {
            super(context, MISSING_EMAIL);
        }
    }

    public static abstract class WarningAlert{
        //AlertDialog alertDialog;
        public WarningAlert(Context context, String msg) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
            alertDialog.setTitle("WARNING");
            alertDialog.setMessage(msg);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    (dialog, which) -> {
                        dialog.dismiss();
                        doOnOk();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }
        protected abstract void doOnOk();
    }


}
