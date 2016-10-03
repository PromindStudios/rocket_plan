package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.TimeFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;

/**
 * Created by Admin on 15.07.2016.
 */

public class TimeViewPagerAdapter extends FragmentStatePagerAdapter{

    int mTabNumber;

    public TimeViewPagerAdapter(FragmentManager fm, int tabNumber) {
        super(fm);
        mTabNumber = tabNumber;

    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.TIME_TYPE, position);
        switch (position) {
            case MyConstants.TIME_DAY:
                TimeFragment fragmentOne = new TimeFragment();
                fragmentOne.setArguments(bundle);
                return fragmentOne;
            case MyConstants.TIME_WEEK:
                TimeFragment fragmentTwo = new TimeFragment();
                fragmentTwo.setArguments(bundle);
                return fragmentTwo;
            case MyConstants.TIME_MONTH:
                TimeFragment fragmentThree = new TimeFragment();
                fragmentThree.setArguments(bundle);
                return fragmentThree;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabNumber;
    }
}
