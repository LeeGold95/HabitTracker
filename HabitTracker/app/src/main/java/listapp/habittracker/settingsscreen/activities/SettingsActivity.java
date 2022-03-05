package listapp.habittracker.settingsscreen.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import listapp.habittracker.users.activities.ProfileActivity;
import listapp.habittracker.drawer.DrawerManager;
import listapp.habittracker.drawer.IntentManager;
import listapp.habittracker.mainscreen.activities.MainActivity;
import listapp.habittracker.settingsscreen.SettingsAdapter;
import listapp.habittracker.settingsscreen.SettingsDialog;
import listapp.habittracker.settingsscreen.SettingsItem;
import listapp.habittracker.R;
import listapp.habittracker.drawer.DrawerMethods;
import listapp.habittracker.utils.WarningManager;
import listapp.habittracker.dataconnections.GetHabitSettings;
import listapp.habittracker.dataconnections.UpdateDb;

/*
This class implements the settings (my habits) screen.
 */

public class SettingsActivity extends AppCompatActivity implements DrawerMethods {

    private int uid;

    private RecyclerView recyclerView;
    private SettingsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<SettingsItem> habitList;
    private DrawerLayout drawerLayout;

    private Button addHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //menu setup
        drawerLayout = findViewById(R.id.drawer_layout);
        DrawerManager.changeMenuTitle(drawerLayout, "My Habits");

        uid = IntentManager.getIncomingId(this);

        createHabitList();
        buildRecyclerView();

        addHabit = findViewById(R.id.addHabit);
        addHabit.setOnClickListener(v -> newHabit());
    }

    private void newHabit(){
        new SettingsDialog(uid,adapter,habitList).show(getSupportFragmentManager());
    }
    private void removeHabitWarning(int position){
        String habitTitle = habitList.get(position).getTitle();
        new WarningManager.WarningAlert(this, "Are you sure you want to delete " + habitTitle + " ?") {
            @Override
            protected void doOnOk() {
                removeHabit(position);
            }
        };
    }
    private void removeHabit(int position){
        //remove from database
        String sql = "DELETE FROM habits WHERE hid="+habitList.get(position).getHid();
        new UpdateDb(sql).execute();

        //remove from view
        habitList.remove(position);
        adapter.notifyItemRemoved(position);
    }
    private void editHabit(int position){
        new SettingsDialog(uid,adapter,habitList,position).show(getSupportFragmentManager());
    }


    private void createHabitList(){
        String sql = "SELECT * FROM habits WHERE uid="+uid;

        //create and add items to list here
        //add user habits settings from db
        GetHabitSettings conn = new GetHabitSettings(sql);
        try {
            conn.execute().get();
            habitList = conn.getHabitList();
            if(habitList==null)
                createListError();
        }
        catch (InterruptedException | ExecutionException e) {
            createListError();
            e.printStackTrace();
        }
    }

    private void createListError(){
        habitList = new ArrayList<>();
        WarningManager.dbConnectionError(this);
    }

    private void buildRecyclerView(){
        recyclerView = findViewById(R.id.habitsList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new SettingsAdapter(habitList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SettingsAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                editHabit(position);
            }

            @Override
            public void onRemoveClick(int position) {
                removeHabitWarning(position);
            }
        });
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
        IntentManager.changeIntent(this, MainActivity.class,uid);
    }
    public void ClickHabits(View view){
        //GOTO other
        DrawerManager.closeDrawer(drawerLayout);
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