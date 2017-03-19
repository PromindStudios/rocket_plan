package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Main_Fragments.OverviewFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;

/**
 * Created by Admin on 15.07.2016.
 */

public class TimeViewPagerAdapter extends FragmentStatePagerAdapter {

    int mTabNumber;
    Map<Integer, OverviewFragment> mFragmentList;

    public TimeViewPagerAdapter(FragmentManager fm, int tabNumber) {
        super(fm);
        mTabNumber = tabNumber;
        mFragmentList = new HashMap<Integer, OverviewFragment>();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.TIME_TYPE, position);
        switch (position) {
            case MyConstants.TIME_DAY:
                OverviewFragment fragmentOne = new OverviewFragment();
                fragmentOne.setArguments(bundle);
                mFragmentList.put(position, fragmentOne);
                return fragmentOne;
            case MyConstants.TIME_WEEK:
                OverviewFragment fragmentTwo = new OverviewFragment();
                fragmentTwo.setArguments(bundle);
                mFragmentList.put(position, fragmentTwo);
                return fragmentTwo;
            case MyConstants.TIME_MONTH:
                OverviewFragment fragmentThree = new OverviewFragment();
                fragmentThree.setArguments(bundle);
                mFragmentList.put(position, fragmentThree);
                return fragmentThree;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabNumber;
    }

    public void destroyItem (ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragmentList.remove(position);
    }

    public OverviewFragment getFragment(int key) {
        return mFragmentList.get(key);
    }
}
