package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.MonthFragment;

/**
 * Created by Eric on 25.10.2016.
 */

public class CalendarViewPagerAdapter extends FragmentStatePagerAdapter {

    int mTabNumber;

    public CalendarViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mTabNumber = 14;
    }

    @Override
    public Fragment getItem(int position) {
        MonthFragment monthFragment = new MonthFragment();
        return monthFragment;
    }

    @Override
    public int getCount() {
        return mTabNumber;
    }
}
