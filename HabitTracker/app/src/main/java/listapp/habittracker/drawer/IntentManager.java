package listapp.habittracker.drawer;

import android.app.Activity;
import android.content.Intent;

/*
This class handles all intent changes in app (activity changes)
 */

public class IntentManager {

    //change activity without extra data
    public static void changeIntent(Activity activity, Class moveClass){
        Intent intent = new Intent(activity, moveClass);
        activity.startActivity(intent);
    }
    //change activity and send id data to next activity
    public static void changeIntent(Activity activity, Class moveClass, int uid){
        Intent intent = new Intent(activity, moveClass);
        intent.putExtra("id", uid);
        activity.startActivity(intent);
    }
    //change activity and send id and date data to next activity
    public static void changeIntent(Activity activity, Class moveClass, int uid, long date){
        Intent intent = new Intent(activity, moveClass);
        intent.putExtra("id", uid);
        intent.putExtra("date", date);
        activity.startActivity(intent);
    }

    //pull id data from previous Activity (if any exists)
    public static int getIncomingId(Activity activity){
        return  activity.getIntent().getIntExtra("id", -1);
    }
    //pull date data from previous Activity (if any exists)
    public static long getIncomingDate(Activity activity){
        return  activity.getIntent().getLongExtra("date", -1);
    }

}
