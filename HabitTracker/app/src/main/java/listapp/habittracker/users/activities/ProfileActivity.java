package listapp.habittracker.users.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import listapp.habittracker.drawer.DrawerManager;
import listapp.habittracker.drawer.DrawerMethods;
import listapp.habittracker.drawer.IntentManager;
import listapp.habittracker.R;
import listapp.habittracker.mainscreen.activities.MainActivity;
import listapp.habittracker.settingsscreen.activities.SettingsActivity;
import listapp.habittracker.users.UpdateEmailDialog;
import listapp.habittracker.dataconnections.GetUserDetails;

/*
This class implements the profile screen.
 */

public class ProfileActivity extends AppCompatActivity implements DrawerMethods {

    private int uid;

    private DrawerLayout drawer_layout;
    private TextView nameTitle, mailTitle;
    private Button passwordButton, emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //menu setup
        drawer_layout = findViewById(R.id.drawer_layout);
        DrawerManager.changeMenuTitle(drawer_layout, "My Profile");

        uid = IntentManager.getIncomingId(this);

        setViewVars();
        setOnClicks();

    }

    private void setViewVars(){
        nameTitle = findViewById(R.id.nameTitle);
        mailTitle = findViewById(R.id.mailTitle);
        passwordButton = findViewById(R.id.changePassButton);
        emailButton = findViewById(R.id.changeMailButton);

        String[] userDetails = getUserDetails();
        nameTitle.setText(userDetails[0]);
        mailTitle.setText(userDetails[1]);
    }

    private void setOnClicks(){
        passwordButton.setOnClickListener(v-> IntentManager.changeIntent(this, ResetPasswordActivity.class, uid));
        emailButton.setOnClickListener(v->changeEmail());
    }


    private String[] getUserDetails(){
        String name=null, email=null;
        String sql = "SELECT * FROM users WHERE uid="+uid;

        try {
            GetUserDetails conn = new GetUserDetails(sql, this);
            conn.execute().get();
            name = conn.getUsername();
            email = conn.getEmail();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(email==null)
            email = "no email address";

        return new String[] {name, email};
    }

    private void changeEmail(){
        new UpdateEmailDialog(uid, mailTitle).show(getSupportFragmentManager());
    }


    //
    //MENU DRAWER SETUP (implement Drawer methods)
    //
    public void ClickMenu(View view){
        //open drawer
        DrawerManager.openDrawer(drawer_layout);
    }
    public void ClickLogo(View view){
        DrawerManager.closeDrawer(drawer_layout);
    }
    public void ClickHome(View view){
        //GOTO home
        IntentManager.changeIntent(this, MainActivity.class,uid);
    }
    public void ClickHabits(View view){
        //GOTO habit settings
        IntentManager.changeIntent(this, SettingsActivity.class,uid);
    }
    public void ClickLogOut(View view) {
        DrawerManager.LogOut(this);
    }
    public void ClickProfile(View view){
        DrawerManager.closeDrawer(drawer_layout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        DrawerManager.closeDrawer(drawer_layout);
    }
}