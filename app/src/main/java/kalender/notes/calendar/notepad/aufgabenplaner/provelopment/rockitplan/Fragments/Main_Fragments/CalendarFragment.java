package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Main_Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.CalendarChooseDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.CalendarFragmentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyFloatingActionButton.FloatingActionButton;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyFloatingActionButton.FloatingActionsMenu;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.CalendarAdapter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.CalendarViewPagerAdapter;

/**
 * Created by Eric on 25.10.2016.
 */

public class CalendarFragment extends ContentTimeCalendarFragment implements CalendarFragmentInterface {

    // Layout
    ViewPager mViewPager;
    RecyclerView mRecyclerView;
    FloatingActionButton fabTask;
    FloatingActionButton fabEvent;
    FloatingActionButton fabNote;
    FloatingActionsMenu fabMenu;

    // Variables
    ArrayList<Calendar> mCalendarList;
    int positionToday;
    Calendar mCurrentCalendar;
    LayoutColor mLayoutColor;

    // Adapter
    CalendarViewPagerAdapter mViePagerAdapter;

    // Inferface
    ContentInterface mContentInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Set up layout
        mViewPager = (ViewPager) layout.findViewById(R.id.viewPager);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rvCalendar);
        fabTask = (FloatingActionButton) layout.findViewById(R.id.fabTask);
        fabEvent = (FloatingActionButton) layout.findViewById(R.id.fabEvent);
        fabNote = (FloatingActionButton) layout.findViewById(R.id.fabNote);
        fabMenu = (FloatingActionsMenu) layout.findViewById(R.id.fabMenu);

        // Set up RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setSwipeFunction(MyConstants.CONTENT_TASK_EVENT, mRecyclerView);

        // Set up variables
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mLayoutColor = new LayoutColor(getActivity(), mDatabaseHelper.getLayoutColorValue());

        // Set up arrayList with calendar objects
        mCalendarList = new ArrayList<>();
        Calendar calendarToday = Calendar.getInstance();
        Calendar calendarReference = Calendar.getInstance();
        calendarReference.set(Calendar.DAY_OF_MONTH, 1);
        calendarReference.add(Calendar.YEAR, -1);
        for (int i1 = 0; i1 < 12; i1++) {
            Calendar calendarObject = Calendar.getInstance();
            calendarObject.set(Calendar.DAY_OF_MONTH, 1);
            calendarObject.set(Calendar.YEAR, calendarReference.get(Calendar.YEAR));
            calendarObject.set(Calendar.MONTH, i1);
            mCalendarList.add(calendarObject);
        }
        for (int i = 0; i < 2; i++) {
            calendarReference.add(Calendar.YEAR, +1);
            for (int i2 = 0; i2 < 12; i2++) {
                calendarReference.set(Calendar.MONTH, i2);
                Calendar calendarObject = Calendar.getInstance();
                calendarObject.set(Calendar.DAY_OF_MONTH, 1);
                calendarObject.set(Calendar.YEAR, calendarReference.get(Calendar.YEAR));
                calendarObject.set(Calendar.MONTH, calendarReference.get(Calendar.MONTH));
                if (i == 0 && calendarObject.get(Calendar.MONTH) == calendarToday.get(Calendar.MONTH))
                    positionToday = mCalendarList.size();
                mCalendarList.add(calendarObject);
            }
        }

        // Handle clickEvents
        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentInterface.selectCategory(MyConstants.CONTENT_TASK);
                fabMenu.collapse();
            }
        });
        fabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentInterface.selectCategory(MyConstants.CONTENT_EVENT);
                fabMenu.collapse();
            }
        });
        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentInterface.selectCategory(MyConstants.CONTENT_NOTE);
                fabMenu.collapse();
            }
        });

        // Handle Color
        colorLayout();

        // Set up Adapter
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                // Set toolbar title
                Calendar calendar = mCalendarList.get(position);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(MyMethods.formatDateForCalendarTitle(calendar));
                MonthFragment monthFragment = mViePagerAdapter.getFragment(position);
                if (monthFragment != null) {
                    updateContentList(monthFragment.getInformationForCalendar());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViePagerAdapter = new CalendarViewPagerAdapter(getActivity().getSupportFragmentManager(), mCalendarList, CalendarFragment.this);
        mViewPager.setAdapter(mViePagerAdapter);
        mViewPager.setCurrentItem(positionToday);

        // Toolbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(MyMethods.formatDateForCalendarTitle(calendarToday));

        return layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViewPager();
        if (mCurrentCalendar != null) {
            setAdapterUp();
        }
        colorLayout();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContentInterface = (ContentInterface)context;
    }

    protected void setAdapterUp() {
        updateContentList(mCurrentCalendar);
        updateViewPager();
    }

    public void updateContentList(Calendar calendar, int calendarPage) {
        mCurrentCalendar = calendar;
        if (calendarPage == mViewPager.getCurrentItem()) {
            Log.i("WADDD", "LOOS");
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new CalendarAdapter(getActivity(), calendar, this);
            mAdapterListener = mAdapter;
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void updateContentList(Calendar calendar) {
        mCurrentCalendar = calendar;
        Log.i("WADDD HIER", "LOOS");
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter = new CalendarAdapter(getActivity(), calendar, this);
        mAdapterListener = mAdapter;
        mRecyclerView.setAdapter(mAdapter);
    }

    /*
    private Calendar getSelectedDate(int position) {
        MonthFragment monthFragment = mViePagerAdapter.getFragment(position);
        if (monthFragment != null) {
            return monthFragment.getInformationForCalendar();
        } else {
            return null;
        }
    }
    */

    private void updateViewPager() {
        final int pagerPosition = mViewPager.getCurrentItem();
        if (mViePagerAdapter != null) {
            MonthFragment monthFragment = mViePagerAdapter.getFragment(pagerPosition);
            MonthFragment monthFragment2 = mViePagerAdapter.getFragment(pagerPosition - 1);
            MonthFragment monthFragment3 = mViePagerAdapter.getFragment(pagerPosition + 2);
            if (monthFragment != null) {
                int selectedDay = monthFragment.getSelectedPosition();
                monthFragment.setUpCalendarContent();
                monthFragment.selectDay(selectedDay);
            }
            if (monthFragment2 != null) {
                monthFragment2.setUpCalendarContent();
            }
            if (monthFragment3 != null) {
                monthFragment3.setUpCalendarContent();
            }
        }
    }

    @Override
    public void onSelectPage(int year, int month) {
        for (int i = 0; i < mCalendarList.size(); i++) {
            Calendar calendar = mCalendarList.get(i);
            if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month) {
                mViewPager.setCurrentItem(i);
            }
        }
    }

    public void openChooseCalendarDialog() {
        DialogFragment dialog = new CalendarChooseDialog();
        Calendar calendar = mCalendarList.get(mViewPager.getCurrentItem());
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CALENDAR_YEAR, calendar.get(Calendar.YEAR));
        bundle.putInt(MyConstants.CALENDAR_MONTH, calendar.get(Calendar.MONTH));
        dialog.setArguments(bundle);
        dialog.setTargetFragment(this, 0);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog_choose");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_activity_calendar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.navigation_down) {
            openChooseCalendarDialog();
            return true;
        }
        return false;
    }

    public void colorLayout() {
        mLayoutColor.setColorValue(mDatabaseHelper.getLayoutColorValue());
        fabTask.setColorNormal(mLayoutColor.getLayoutColor());
        fabEvent.setColorNormal(mLayoutColor.getLayoutColor());
        fabNote.setColorNormal(mLayoutColor.getLayoutColor());
        fabMenu.getButton().setColorNormal(mLayoutColor.getLayoutColor());
        fabMenu.getButton().setColorPressed(mLayoutColor.getLayoutColor());
    }
}
