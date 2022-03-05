package listapp.habittracker.mainscreen.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import listapp.habittracker.mainscreen.ProgressBarAnimation;
import listapp.habittracker.users.activities.ProfileActivity;
import listapp.habittracker.settingsscreen.activities.SettingsActivity;
import listapp.habittracker.utils.DateManipulations;
import listapp.habittracker.drawer.DrawerManager;
import listapp.habittracker.drawer.IntentManager;
import listapp.habittracker.mainscreen.MainAdapter;
import listapp.habittracker.mainscreen.MainItem;
import listapp.habittracker.drawer.DrawerMethods;
import listapp.habittracker.utils.WarningManager;
import listapp.habittracker.dataconnections.GetAllHabits;
import listapp.habittracker.R;
import listapp.habittracker.dataconnections.UpdateDb;

/*
This class implements the main screen.
 */

public class MainActivity extends AppCompatActivity implements DrawerMethods {

    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MainItem> habitBoxList;

    private TextView currDate; //current view date, default = today.
    private Date date;
    private int uid;
    private DrawerLayout drawerLayout;
    private Button prevButton, nextButton;
    private ProgressBar progressBar;
    private ProgressBarAnimation progressBarAnimation;
    private int checkedCount, itemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViewVars();

        date = new Date(); //set default date to today
        long incomingDate = IntentManager.getIncomingDate(this);
        uid = IntentManager.getIncomingId(this);
        if(incomingDate!=-1)
            date.setTime(incomingDate);

        currDate.setText(DateManipulations.toDisplayString(date));

        createHabitBoxList();
        buildRecyclerView();
        setProgressBar();

        setOnClicks();
    }

    private void setViewVars(){
        //setup menu
        drawerLayout = findViewById(R.id.drawer_layout);
        DrawerManager.changeMenuTitle(drawerLayout, "My Habit Tracker");

        currDate = findViewById(R.id.dayView);

        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        //progress bar setup and design
        progressBar = findViewById(R.id.progressBar);
        progressBarAnimation = new ProgressBarAnimation(progressBar,0,0);

    }
    private void setOnClicks(){
        //setup buttons
        currDate.setOnClickListener(v->IntentManager.changeIntent(this, CalendarActivity.class, uid));
        nextButton.setOnClickListener(v->IntentManager.changeIntent(this, MainActivity.class, uid, DateManipulations.getNext(date).getTime()));
        prevButton.setOnClickListener(v->IntentManager.changeIntent(this,MainActivity.class, uid, DateManipulations.getPrevious(date).getTime()));
    }


    //handle creation and functions of habit checkbox list
    private void createHabitBoxList(){
        String sql = getHabitsQuery(date, uid);
        GetAllHabits conn = new GetAllHabits(sql, date); //get all habits for this date
        try {
            conn.execute().get();
            habitBoxList = conn.getHabitBoxList();
            if(habitBoxList==null) //if a connection error occurred
                getAllHabitsError();
            else
                checkedCount = conn.getCheckedCounter();
        }
        catch (InterruptedException | ExecutionException e) {
            getAllHabitsError();
            e.printStackTrace();
        }
    }
    private void buildRecyclerView(){
        recyclerView = findViewById(R.id.habitBox);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new MainAdapter(habitBoxList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        itemCount = adapter.getItemCount();

        adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void doOnClick(int position) {
                MainItem habitBox = habitBoxList.get(position);
                //update view
                habitBox.toggleChecked();
                adapter.notifyItemChanged(position);

                //update database
                if(habitBox.isChecked()) {
                    incProgressBar();
                    addProgress(habitBox.getDateString(), habitBox.getHid());
                }
                else {
                    decProgressBar();
                    removeProgress(habitBox.getDateString(), habitBox.getHid());
                }
            }
        });
    }

    private void removeProgress(String sDate, int hid){
        String sql = "DELETE FROM progress WHERE hdate='"+sDate+"' AND hid="+hid;
        new UpdateDb(sql).execute();
    }
    private void addProgress(String sDate, int hid){
        String sql = "INSERT IGNORE INTO progress VALUES ("+hid+",'"+sDate+"')";
        new UpdateDb(sql).execute();
    }

    //set and update progressBar on change
    //progress increment size is calculated by number of habits displayed.
    //in case number of item is not divisible by 100, let the last piece be bigger then the rest
    private void setProgressBar(){
        if(checkedCount==itemCount)
            progressBarAnimation.changeProgress(100);
        else
            progressBarAnimation.changeProgress(checkedCount * (100 / itemCount));
    }
    private void incProgressBar(){
        checkedCount++;
        setProgressBar();
    }
    private void decProgressBar(){
        checkedCount--;
        setProgressBar();
    }

    // Set get all habits query
    private String getHabitsQuery(Date date, int uid){
        String dateQuery = DateManipulations.toSqlFormat(date);
        return "SELECT hid, hname, repetition, checked FROM habits LEFT JOIN (SELECT hid AS checked FROM progress WHERE hdate='"+ dateQuery +"') AS p "
                + "ON p.checked = habits.hid " +
                "WHERE (('" +dateQuery+ "' BETWEEN start_date AND end_date) OR ('"+
                dateQuery+"'>=start_date AND end_date IS NULL)) AND (uid="+uid+")";
    }
    private void getAllHabitsError(){
        habitBoxList = new ArrayList<>();
        WarningManager.dbConnectionError(this);
    }

    //
    //MENU DRAWER SETUP (implement Drawer methods)
    //

    public void ClickMenu(View view){
        //open drawer
        DrawerManager.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        DrawerManager.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        //GOTO home
        IntentManager.changeIntent(this,MainActivity.class,uid);
    }
    public void ClickHabits(View view){
        //GOTO habit settings
        IntentManager.changeIntent(this, SettingsActivity.class,uid);
    }
    public void ClickLogOut(View view) {
        DrawerManager.LogOut(this);
    }
    public void ClickProfile(View view){
        IntentManager.changeIntent(this, ProfileActivity.class,uid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        DrawerManager.closeDrawer(drawerLayout);
    }

}