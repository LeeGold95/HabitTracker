package listapp.habittracker.users.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import listapp.habittracker.dataconnections.FindUsername;
import listapp.habittracker.dataconnections.UpdateDb;

/*
This class implements the register screen.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password, rePassword, email;
    private Button register, login;
    private TextView wName, wPass, wRePass, wMail;
    private boolean usernameExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.rePassword);
        email = findViewById(R.id.email);

        wName = findViewById(R.id.warnusername);
        wPass = findViewById(R.id.warnPassword);
        wRePass = findViewById(R.id.warnRePassword);
        wMail = findViewById(R.id.warnEmail);

        addTextWatchers();

        register = findViewById(R.id.createAccount);
        usernameExists = false;
        login = findViewById(R.id.login);

        register.setOnClickListener(v -> attemptRegister());
        login.setOnClickListener(v -> IntentManager.changeIntent(this, LoginActivity.class));

    }

    //watch for invalid input and update views accordingly to warn user
    private void addTextWatchers(){

        username.addTextChangedListener(new ViewManager.newTextWatcher() {
            @Override
            public void doOnTextChange(String s) {
                ViewManager.changeWarnFlag(wName, RegValidation.nameValid(s));
            }
        }.getTextWatcher());
        password.addTextChangedListener(new ViewManager.newTextWatcher() {
            @Override
            public void doOnTextChange(String s) {
                ViewManager.changeWarnFlag(wPass, RegValidation.passValid(s));
            }
        }.getTextWatcher());
        rePassword.addTextChangedListener(new ViewManager.newTextWatcher() {
            @Override
            public void doOnTextChange(String s) {
                ViewManager.changeWarnFlag(wRePass, RegValidation.passMatch(ViewManager.getString(password) ,s));
            }
        }.getTextWatcher());
        email.addTextChangedListener(new ViewManager.newTextWatcher() {
            @Override
            public void doOnTextChange(String s) {
                ViewManager.changeWarnFlag(wMail, RegValidation.mailValid(s));
            }
        }.getTextWatcher());

    }

    private void attemptRegister(){
        //check that all fields have valid values
        if(RegValidation.validateAll(username.getText().toString(),
                ViewManager.getString(password),ViewManager.getString(rePassword),ViewManager.getString(email)))
        {
            //check if username already in system
            String sql = "SELECT * FROM users WHERE username = ?";
            FindUsername conn = new FindUsername(sql, ViewManager.getString(username).toLowerCase());
            try {
                conn.execute().get();
                usernameExists = conn.getUserExists();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            if (usernameExists) {
                new DialogPopUp(this, "username taken", "This username already exists. \nPlease pick a different username.").show();
            } else {
                if (ViewManager.getString(email).isEmpty()) {
                    new WarningManager.MissingEmail(this) {
                        @Override
                        protected void doOnOk() {
                            register();
                        }
                    };
                }
                else
                    register();
            }
        }
        else
            new DialogPopUp(this, "missing information", WarningManager.MISSING_FIELDS).show();

    }

    private void register(){
        String query;
        String[] params;
        if(ViewManager.getString(email).isEmpty()) { //register user without email
            query = "INSERT INTO users VALUES (null, ? , ? , null)";
            params = new String[]{ViewManager.getString(username).toLowerCase(), ViewManager.getString(password)};
        }
        else { //register user with email
            query = "INSERT INTO users VALUES (null, ? , ? , ? )";
            params = new String[]{ViewManager.getString(username).toLowerCase(), ViewManager.getString(password), ViewManager.getString(email)};
        }
        new UpdateDb(query, params).execute();

        //alert user for successful account creation
        new DialogPopUp(this, "account created", "new account created successfully.",
                (dialog1, which) -> IntentManager.changeIntent(this,LoginActivity.class)).show();
    }

}