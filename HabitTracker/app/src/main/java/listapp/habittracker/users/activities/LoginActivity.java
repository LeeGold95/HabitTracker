package listapp.habittracker.users.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import listapp.habittracker.drawer.IntentManager;
import listapp.habittracker.utils.ViewManager;
import listapp.habittracker.dataconnections.GetLogin;
import listapp.habittracker.R;

/*
This class implements the Login screen.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login, signUp, passReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.createAccount);
        signUp = findViewById(R.id.signUpButton);
        passReset = findViewById(R.id.passResetButton);

        login.setOnClickListener(v -> loginAttempt());
        signUp.setOnClickListener(v-> IntentManager.changeIntent(this, RegisterActivity.class));
        passReset.setOnClickListener(v->IntentManager.changeIntent(this, ResetPasswordActivity.class));
    }

    private void loginAttempt(){
        String inputName = ViewManager.getString(username);
        String inputPass = ViewManager.getString(password);

        if(inputName.isEmpty() || inputPass.isEmpty()){ //make sure all necessary fields were filled
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_LONG).show();
        }
        else{
            String sql = "SELECT * FROM users WHERE username = ? AND userpass = ? ";
            GetLogin conn = new GetLogin(sql, inputName, inputPass, this);
            conn.execute();
        }
    }

}