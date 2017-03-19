package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Navigation_Drawer.HomeFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Navigation_Drawer.SettingsFragment;

/**
 * Created by Eric on 05.03.2017.
 */

public class NavigationDrawerViewPagerAdapter extends FragmentStatePagerAdapter {

    int mTabNumber;

    public NavigationDrawerViewPagerAdapter(FragmentManager fm, int tabNumber) {
        super(fm);
        mTabNumber = tabNumber;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                Fragment settingsFragment = new SettingsFragment();
                return settingsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mTabNumber;
    }
}
