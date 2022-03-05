package listapp.habittracker.drawer;

import android.app.Activity;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import listapp.habittracker.R;
import listapp.habittracker.users.activities.LoginActivity;

/*
This class handles drawer functions that are common to all activities with drawer.
 */

public class DrawerManager {

    public static void openDrawer(DrawerLayout drawerLayout){
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        //close drawer layout
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void LogOut(Activity activity){
        IntentManager.changeIntent(activity, LoginActivity.class, -1);
    }

    //make sure the title on the menu bar matches the current activity
    public static void changeMenuTitle(DrawerLayout drawerLayout, String title){
        TextView menuBar = drawerLayout.findViewById(R.id.menuTitle);
        menuBar.setText(title);
    }


}
