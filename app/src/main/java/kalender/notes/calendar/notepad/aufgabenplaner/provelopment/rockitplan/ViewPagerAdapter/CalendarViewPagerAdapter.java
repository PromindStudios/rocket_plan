package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.CalendarFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.MonthFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;

/**
 * Created by Eric on 25.10.2016.
 */

public class CalendarViewPagerAdapter extends FragmentStatePagerAdapter {

    // Variables
    int mTabNumber;
    ArrayList<Calendar> mCalendarList;
    CalendarFragment mCalendarFragment;
    Map<Integer, MonthFragment> mFragmentList;

    public CalendarViewPagerAdapter(FragmentManager fm, ArrayList<Calendar> calendarList, CalendarFragment calendarFragment) {
        super(fm);
        mCalendarFragment = calendarFragment;
        mTabNumber = 36;
        mCalendarList = calendarList;
        mFragmentList = new HashMap<Integer, MonthFragment>();
    }

    @Override
    public Fragment getItem(int position) {
        MonthFragment monthFragment = new MonthFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyConstants.CALENDAR_OBJECT, mCalendarList.get(position));
        bundle.putInt(MyConstants.CALENDAR_PAGE, position);
        monthFragment.setArguments(bundle);
        mFragmentList.put(position, monthFragment);
        return monthFragment;
    }

    public void destroyItem (ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragmentList.remove(position);
    }

    public MonthFragment getFragment(int key) {
        return mFragmentList.get(key);
    }

    @Override
    public int getCount() {
        return mTabNumber;
    }


}
