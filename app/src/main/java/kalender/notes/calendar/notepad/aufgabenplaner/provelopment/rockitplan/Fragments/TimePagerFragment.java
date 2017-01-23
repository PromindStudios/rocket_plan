package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimePagerInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyFloatingActionButton.FloatingActionButton;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyFloatingActionButton.FloatingActionsMenu;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.NonSwipeableViewPager;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.TimeViewPagerAdapter;

/**
 * Created by Eric on 25.10.2016.
 */

public class TimePagerFragment extends Fragment implements ContentTimePagerInterface {

    // Layout
    TabLayout mTabLayout;
    NonSwipeableViewPager mViewPager;
    FloatingActionButton fabTask;
    FloatingActionButton fabEvent;
    FloatingActionButton fabNote;
    FloatingActionsMenu fabMenu;

    // Adpater
    TimeViewPagerAdapter mViewPagerAdapter;

    // Varialbes
    DatabaseHelper mDatabaseHelper;
    CategoryColor mCategoryColor;
    LayoutColor mLayoutColor;

    // Inferface
    ContentInterface mContentInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_time_pager, container, false);

        // Set up layout
        mViewPager = (NonSwipeableViewPager) layout.findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) layout.findViewById(R.id.tabLayout);
        fabTask = (FloatingActionButton) layout.findViewById(R.id.fabTask);
        fabEvent = (FloatingActionButton) layout.findViewById(R.id.fabEvent);
        fabNote = (FloatingActionButton) layout.findViewById(R.id.fabNote);
        fabMenu = (FloatingActionsMenu) layout.findViewById(R.id.fabMenu);

        // Set up variables
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mLayoutColor = new LayoutColor(getActivity(), mDatabaseHelper.getLayoutColorValue());

        // Set up toolbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.overview));

        // Set up tablayout
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_day)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_week)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_month)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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

        // Set up ViewPager
        setUpViewPager();

        // Handle Color
        colorLayout();

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        colorLayout();
        try {
            mViewPagerAdapter.getFragment(mViewPager.getCurrentItem()).setAdapterUp();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContentInterface = (ContentInterface)context;
    }

    // Class Methods

    private void setUpViewPager() {
        mViewPagerAdapter = null;
        mViewPagerAdapter = new TimeViewPagerAdapter(this.getFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                mViewPagerAdapter.getFragment(tab.getPosition()).setAdapterUp();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void colorLayout() {
        mLayoutColor = new LayoutColor(getActivity(), mDatabaseHelper.getLayoutColorValue());
        mTabLayout.setBackgroundColor(mLayoutColor.getLayoutColor());
        fabTask.setColorNormal(mLayoutColor.getLayoutColor());
        fabEvent.setColorNormal(mLayoutColor.getLayoutColor());
        fabNote.setColorNormal(mLayoutColor.getLayoutColor());
        fabMenu.getButton().setColorNormal(mLayoutColor.getLayoutColor());
        fabMenu.getButton().setColorPressed(mLayoutColor.getLayoutColor());
    }

    @Override
    public void selectTab(int tabNumber) {
        TabLayout.Tab tab = mTabLayout.getTabAt(tabNumber);
        tab.select();
    }
}
