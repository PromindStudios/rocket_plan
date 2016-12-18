package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.DetailActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.CalendarChooseDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyFloatingActionButton.FloatingActionButton;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyFloatingActionButton.FloatingActionsMenu;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.CalendarFragmentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
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

    // Adapter
    CalendarViewPagerAdapter mViePagerAdapter;

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

        // Set up arrayList with calendar objects
        mCalendarList = new ArrayList<>();
        Calendar calendarToday = Calendar.getInstance();
        Calendar calendarReference = Calendar.getInstance();
        calendarReference.set(Calendar.DAY_OF_MONTH, 1);
        calendarReference.add(Calendar.YEAR, -1);
        for (int i1 = 0; i1 < 12; i1++) {
            //calendarReference.set(Calendar.MONTH, i1);
            Calendar calendarObject = Calendar.getInstance();
            calendarObject.set(Calendar.DAY_OF_MONTH, 1);
            calendarObject.set(Calendar.YEAR, calendarReference.get(Calendar.YEAR));
            calendarObject.set(Calendar.MONTH, i1);
            //calendarObject.set(Calendar.HOUR_OF_DAY, 0);
            //calendarObject.set(Calendar.MINUTE, 0);
            //calendarObject.set(Calendar.SECOND, 0);
            //calendarObject.set(Calendar.MILLISECOND, 0);
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
                //calendarObject.set(Calendar.HOUR_OF_DAY, 0);
                //calendarObject.set(Calendar.MINUTE, 0);
                //calendarObject.set(Calendar.SECOND, 0);
                //calendarObject.set(Calendar.MILLISECOND, 0);
                if (i == 0 && calendarObject.get(Calendar.MONTH) == calendarToday.get(Calendar.MONTH))
                    positionToday = mCalendarList.size();
                mCalendarList.add(calendarObject);
            }
        }

        // Handle clickEvents
        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letUserSelectCategory(MyConstants.CONTENT_TASK);
                fabMenu.collapse();
            }
        });
        fabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letUserSelectCategory(MyConstants.CONTENT_EVENT);
                fabMenu.collapse();
            }
        });
        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letUserSelectCategory(MyConstants.CONTENT_NOTE);
                fabMenu.collapse();
            }
        });


        // Handle Color
        CategoryColor mCategoryColor = new CategoryColor(getActivity(), 0);
        fabTask.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabEvent.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabNote.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabMenu.getButton().setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabMenu.getButton().setColorPressed(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));

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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(MyMethods.formatDateForCalendarTitle(calendarToday));

        // Set up color
        mainActivityListener.colorHead(0);

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
        //((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViewPager();
        if (mCurrentCalendar != null) {
            setAdapterUp();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
        mainActivityListener = (ContentTimeCalendarFragment.MainActivityListener) context;
    }

    protected void setAdapterUp() {
        updateContentList(mCurrentCalendar);
        updateViewPager();
    }

    private void letUserSelectCategory(final int contenType) {
        final ArrayList<Category> categories = mDatabaseHelper.getAllCategories();
        if (categories.size() > 0) {
            List<String> listItems = new ArrayList<>();
            for (Category category : categories) {
                listItems.add(category.getTitle());
            }
            CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    Category category = categories.get(item);
                    startDetailActivity(category.getId(), category.getTitle(), contenType);
                }
            });
            builder.create().show();
        } else {
            // Toast with hint to create Category first + Open Drawer
            Toast.makeText(getActivity(), getString(R.string.toast_add_category), Toast.LENGTH_LONG).show();
            try {
                // open Drawer here
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void startDetailActivity(int categoryId, String categoryName, int contentType) {
        int contentId = 0;
        mCurrentCalendar = getSelectedDate(mViewPager.getCurrentItem());
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                Task task = new Task(categoryId, categoryName);
                task.setDate(mCurrentCalendar);
                contentId = mDatabaseHelper.createTask(task);
                break;
            case MyConstants.CONTENT_EVENT:
                Event event = new Event(categoryId, categoryName);
                event.setDate(mCurrentCalendar);
                contentId = mDatabaseHelper.createEvent(event);
                break;
            case MyConstants.CONTENT_NOTE:
                Note note = new Note(categoryId, categoryName);
                contentId = mDatabaseHelper.createNote(note);
        }
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CONTENT_TYPE, contentType);
        bundle.putInt(MyConstants.CONTENT_ID, contentId);
        bundle.putInt(MyConstants.DETAIL_TYPE, MyConstants.DETAIL_GENERAL);
        bundle.putInt(MyConstants.CATEGORY_ID, categoryId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void updateContentList(Calendar calendar, int calendarPage) {
        mCurrentCalendar = calendar;
        if (calendarPage == mViewPager.getCurrentItem()) {
            Log.i("WADDD", "LOOS");
            mRecyclerView.setVisibility(View.VISIBLE);
            CalendarAdapter adapter = new CalendarAdapter(getActivity(), calendar);
            mAdapterListener = adapter;
            mRecyclerView.setAdapter(adapter);
        }
    }

    public void updateContentList(Calendar calendar) {
        mCurrentCalendar = calendar;
        Log.i("WADDD HIER", "LOOS");
        mRecyclerView.setVisibility(View.VISIBLE);
        CalendarAdapter adapter = new CalendarAdapter(getActivity(), calendar);
        mAdapterListener = adapter;
        mRecyclerView.setAdapter(adapter);
    }

    private Calendar getSelectedDate(int position) {
        MonthFragment monthFragment = mViePagerAdapter.getFragment(position);
        if (monthFragment != null) {
            return monthFragment.getInformationForCalendar();
        } else {
            return null;
        }
    }

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
            /*
            final int selectedDay = monthFragment.getSelectedPosition();
            mViePagerAdapter = new CalendarViewPagerAdapter(getActivity().getSupportFragmentManager(), mCalendarList, CalendarFragment.this);
            mViewPager.setAdapter(mViePagerAdapter);
            mViewPager.setCurrentItem(pagerPosition);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final MonthFragment monthFragment2;
                    monthFragment2 = mViePagerAdapter.getFragment(pagerPosition);
                    if (monthFragment2 != null) {
                        Log.i("HAALLOO", "Eric");
                        monthFragment2.getInformationForCalendar();
                        monthFragment2.selectDay(selectedDay);
                    }
                }
            }, 1);
            */


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
}
