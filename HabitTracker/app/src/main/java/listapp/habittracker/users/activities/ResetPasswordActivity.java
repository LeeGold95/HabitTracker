package listapp.habittracker.users.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import listapp.habittracker.utils.DialogPopUp;
import listapp.habittracker.drawer.IntentManager;
import listapp.habittracker.R;
import listapp.habittracker.users.RegValidation;
import listapp.habittracker.utils.ViewManager;
import listapp.habittracker.utils.WarningManager;
import listapp.habittracker.dataconnections.GetUserAuth;
import listapp.habittracker.dataconnections.UpdateDb;

/*
    This class implements the password-reset screen.

    this class holds password reset action.
    user is first being asked to enter username and email.
    if they match database, we reconfigure this activity
    to allow password change without loading a new activity.
    this activity both verifies user's identity and resets password.
 */

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText field1, field2;
    private Button actionButton, cancelButton;
    private TextView warnField1, warnField2, title;

    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        title = findViewById(R.id.resetTitle);
        field1 = findViewById(R.id.r_field1);
        field2 = findViewById(R.id.r_field2);
        actionButton = findViewById(R.id.r_action_button);
        cancelButton = findViewById(R.id.r_cancel);
        warnField1 = findViewById(R.id.field1_warn);
        warnField2 = findViewById(R.id.field2_warn);


        uid = IntentManager.getIncomingId(this);

        if (uid==-1) //show credentials screen
            credentialsViewConfig();
        else //uid received --> request was sent from inside the app --> skip credentials and show password reset
            passwordViewConfig();

        cancelButton.setOnClickListener(v -> IntentManager.changeIntent(this, LoginActivity.class));
    }

    private void credentialsViewConfig(){
        title.setText("Enter credentials");
        field1.setHint("Username");
        field2.setHint("Email address");
        actionButton.setText("forgot password");

        actionButton.setOnClickListener(v -> credentialsOnClick());
    }

    private void passwordViewConfig(){
        title.setText("Enter new password");
        field1.setHint("Password");
        field1.setText(null);
        field1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        field2.setHint("Confirm password");
        field2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        field2.setText(null);
        actionButton.setText("change");

        actionButton.setOnClickListener(v -> passwordOnClick());

        //watch for invalid input and update warning views accordingly.
        field1.addTextChangedListener(new ViewManager.newTextWatcher() {
            @Override
            public void doOnTextChange(String s) {
                ViewManager.changeWarnFlag(warnField1, RegValidation.passValid(s));
            }
        }.getTextWatcher());
        field2.addTextChangedListener(new ViewManager.newTextWatcher() {
            @Override
            public void doOnTextChange(String s) {
                ViewManager.changeWarnFlag(warnField2, RegValidation.passMatch(ViewManager.getString(field1),s));
            }
        }.getTextWatcher());
    }

    private void credentialsOnClick(){
        validateCredentials();
        if(uid!=-1){
            passwordViewConfig();
        }
    }
    private void validateCredentials(){
        if(field1.getText()==null || field2.getText()==null)
            new DialogPopUp(this, "Missing information", WarningManager.MISSING_FIELDS).show();

        else {
            String sql = "SELECT * FROM users WHERE username= ? ";

            GetUserAuth conn = new GetUserAuth(sql,ViewManager.getString(field1), this, ViewManager.getString(field2));
            try {
                conn.execute().get();
                uid = conn.getUid();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void passwordOnClick(){
        String password = ViewManager.getString(field1);
        if(RegValidation.passValid(password)==null && RegValidation.passMatch(password,ViewManager.getString(field2))==null){
            //if password and confirmation are valid
            String sql = "UPDATE users SET userpass = ? WHERE uid = "+uid;
            new UpdateDb(sql, new String[]{password}).execute();

            new DialogPopUp(this, "reset complete", "password successfully changed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    IntentManager.changeIntent(ResetPasswordActivity.this,LoginActivity.class);
                }
            }).show();
        }
        else
            new DialogPopUp(this, "missing information", WarningManager.MISSING_FIELDS).show();
    }

}