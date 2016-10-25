package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.CalendarViewPagerAdapter;

/**
 * Created by Eric on 25.10.2016.
 */

public class CalendarFragment extends Fragment {

    ViewPager mViewPager;
    CalendarViewPagerAdapter mViePagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Set up layout
        mViewPager = (ViewPager)layout.findViewById(R.id.viewPager);
        mViePagerAdapter = new CalendarViewPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mViePagerAdapter);

        return layout;
    }
}
