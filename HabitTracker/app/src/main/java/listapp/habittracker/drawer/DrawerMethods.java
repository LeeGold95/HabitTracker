package listapp.habittracker.drawer;

import android.view.View;

/*
This interface is implemented by every activity that contains a menu drawer.
It is meant to make sure all necessary drawer functions were implemented in class.

If a new item is added to navigation drawer --> add it's function in this interface.
 */

public interface DrawerMethods {

    public void ClickMenu(View view);
    public void ClickLogo(View view);
    public void ClickHome(View view);
    public void ClickHabits(View view);
    public void ClickLogOut(View view);
    public void ClickProfile(View view);

}
