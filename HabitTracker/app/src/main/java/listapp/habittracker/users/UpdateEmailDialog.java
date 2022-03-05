package listapp.habittracker.users;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import listapp.habittracker.utils.DialogPopUp;
import listapp.habittracker.R;
import listapp.habittracker.utils.ViewManager;
import listapp.habittracker.utils.WarningManager;
import listapp.habittracker.dataconnections.UpdateDb;

/*
This class creates a dialog to edit an current user's email address.

This class is implemented as a dialog and not an Activity to minimize unnecessary database access and Activity changes.
After change of email, the database and view are updated accordingly.
That way, we don't need to reload user details from database every time a change is made.
 */

public class UpdateEmailDialog extends AppCompatDialogFragment {

    private int uid;
    private TextView mailTitle;

    private EditText newEmail;
    private TextView newEmailWarn;

    AlertDialog dialog;

    public UpdateEmailDialog(int uid, TextView mailTitle) {
        this.uid = uid;
        this.mailTitle = mailTitle;
    }

    public void show(FragmentManager manager){
        this.show(manager, "update email");
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.profile_email_dialog, null);

        //set positive button action to null, and define it after dialog creation,
        // in order to override default on-click dismiss() function
        // and allow error dialog to appear without interfering this dialog.
        builder.setView(view)
                .setNegativeButton("cancel", (dialog, which) -> negButtonAction())
                .setPositiveButton("apply changes", null);

        setViewVars(view);
        setViewListeners();

        dialog = builder.create();
        dialog.show();

        Button posButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        posButton.setOnClickListener(v-> posButtonAction());

        return dialog;
    }

    private void setViewVars(View view){
        newEmail = view.findViewById(R.id.newEmail);
        newEmailWarn = view.findViewById(R.id.newEmail_warn);

        String currentMail = ViewManager.getString(mailTitle);
        if(!currentMail.equals("no email address"))
            newEmail.setText(currentMail);
    }

    private void setViewListeners(){

        newEmail.addTextChangedListener(new ViewManager.newTextWatcher() {
            @Override
            public void doOnTextChange(String s) {
                ViewManager.changeWarnFlag(newEmailWarn, RegValidation.mailValid(s));
            }
        }.getTextWatcher());
    }

    private void negButtonAction(){
        this.dismiss();
    }
    private void posButtonAction(){
        String newEmailString = ViewManager.getString(newEmail);
        if(newEmailString.isEmpty()){
            new WarningManager.MissingEmail(this.getContext()) {
                @Override
                protected void doOnOk() {
                    updateEmail(null);
                }
            };
        }
        else{
            if(RegValidation.mailValid(newEmailString)!=null)
                new DialogPopUp(this.getContext(), "invalid address", "please enter a valid email address").show();
            else updateEmail(newEmailString);
        }
    }

    private void updateEmail(String email){
        //update db
        String emailQuery = email;
        if(email!=null) {
            String sql = "UPDATE users SET usermail = ? WHERE uid="+uid;
            new UpdateDb(sql, new String[]{emailQuery}).execute();
        }
        else {
            String sql = "UPDATE users SET usermail = null WHERE uid="+uid;
            new UpdateDb(sql).execute();
            email = "no email address";
        }

        //update view
        mailTitle.setText(email);

        //close dialog
        this.dismiss();
    }

}
