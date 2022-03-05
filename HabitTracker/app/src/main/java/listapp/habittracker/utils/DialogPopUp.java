package listapp.habittracker.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class DialogPopUp {

    private Context context;
    private String msg;
    private String title;
    private DialogInterface.OnClickListener listener;

    public DialogPopUp(Context context, String title, String msg) {
        this(context,title,msg,null);
    }

    public DialogPopUp(Context context, String title, String msg, DialogInterface.OnClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.title = title;
        this.msg = msg;
    }

    private AlertDialog createPopUp(){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        if(listener==null) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
        }
        else{
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", listener);
        }
        return alertDialog;
    }

    public void show(){
        ContextCompat.getMainExecutor(context).execute(()-> {
            createPopUp().show();
        });
    }
}
