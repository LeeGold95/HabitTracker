package listapp.habittracker.mainscreen.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;

import java.util.Calendar;

import listapp.habittracker.drawer.IntentManager;
import listapp.habittracker.R;

/*
This class implements the calendar screen.
 */

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth); //get chosen date
                int id = IntentManager.getIncomingId(CalendarActivity.this); //send back incoming user id to get correct view in main
                IntentManager.changeIntent(CalendarActivity.this, MainActivity.class, id, cal.getTime().getTime()); //load main screen back with the chosen date
            }
        });
    }
}