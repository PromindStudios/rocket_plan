package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.DetailActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimePagerInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.NonSwipeableViewPager;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.ContentViewPagerAdapter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.TimeViewPagerAdapter;

/**
 * Created by Eric on 25.10.2016.
 */

public class ContentPagerFragment extends Fragment implements ContentTimePagerInterface {

    // Layout
    TabLayout mTabLayout;
    NonSwipeableViewPager mViewPager;
    FloatingActionButton fabTask;
    FloatingActionButton fabEvent;
    FloatingActionButton fabNote;
    FloatingActionsMenu fabMenu;

    // Adpater
    ContentViewPagerAdapter mViewPagerAdapter;

    // Varialbes
    DatabaseHelper mDatabaseHelper;
    CategoryColor mCategoryColor;
    Category mCategory;
    int mContentType;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_content_time_pager, container, false);

        // Handle arguments
        Bundle bundle = getArguments();
        int categoryId = bundle.getInt(MyConstants.CATEGORY_ID);
        mContentType = bundle.getInt(MyConstants.CONTENT_TYPE);

        // Set up layout
        mViewPager = (NonSwipeableViewPager) layout.findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) layout.findViewById(R.id.tabLayout);
        fabTask = (FloatingActionButton) layout.findViewById(R.id.fabTask);
        fabEvent = (FloatingActionButton) layout.findViewById(R.id.fabEvent);
        fabNote = (FloatingActionButton) layout.findViewById(R.id.fabNote);
        fabMenu = (FloatingActionsMenu) layout.findViewById(R.id.fabMenu);

        // Set up variables
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mCategory = mDatabaseHelper.getCategory(categoryId);
        mCategoryColor = new CategoryColor(getActivity(), mCategory.getColor());

        // Set up tablayout
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_tabTask)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_tabEvent)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_tabNote)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Set up toolbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mCategory.getTitle());

        // Handle clickEvents
        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailActivity(mCategory.getId(), mCategory.getTitle(), MyConstants.CONTENT_TASK);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TabLayout.Tab tab = mTabLayout.getTabAt(MyConstants.CONTENT_TASK);
                        tab.select();
                    }
                }, 1000);
                fabMenu.collapse();

            }
        });
        fabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailActivity(mCategory.getId(), mCategory.getTitle(), MyConstants.CONTENT_EVENT);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TabLayout.Tab tab = mTabLayout.getTabAt(MyConstants.CONTENT_EVENT);
                        tab.select();
                    }
                }, 1000);
                fabMenu.collapse();
                fabMenu.collapse();
            }
        });
        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailActivity(mCategory.getId(), mCategory.getTitle(), MyConstants.CONTENT_NOTE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TabLayout.Tab tab = mTabLayout.getTabAt(MyConstants.CONTENT_NOTE);
                        tab.select();
                    }
                }, 1000);
                fabMenu.collapse();
                fabMenu.collapse();
            }
        });

        // Handle Color
        fabTask.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabEvent.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabNote.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        ((MainActivity)getActivity()).getToolbar().setBackgroundColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // Status Bar
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        }

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewPagerAdapter = new ContentViewPagerAdapter(getActivity().getSupportFragmentManager(), mTabLayout.getTabCount(), mCategory.getId(), mCategory.getTitle());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        TabLayout.Tab tab = mTabLayout.getTabAt(mContentType);
        tab.select();
    }

    // Class methods

    private void startDetailActivity(int categoryId, String categoryName, int contentType) {
        int contentId = 0;
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                Task task = new Task(categoryId, categoryName);
                contentId = mDatabaseHelper.createTask(task);
                break;
            case MyConstants.CONTENT_EVENT:
                Event event = new Event(categoryId, categoryName);
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

    @Override
    public void selectTab(int tabNumber) {
        TabLayout.Tab tab = mTabLayout.getTabAt(tabNumber);
        tab.select();
    }
}
