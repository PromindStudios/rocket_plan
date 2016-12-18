package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.DetailActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyFloatingActionButton.FloatingActionButton;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyFloatingActionButton.FloatingActionsMenu;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimePagerInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
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
        mCategoryColor = new CategoryColor(getActivity(), 0);

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
        fabTask.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabEvent.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabNote.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabMenu.getButton().setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabMenu.getButton().setColorPressed(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        int selectedTab = mViewPager.getCurrentItem();

        // Set up adapter
        mViewPagerAdapter = new TimeViewPagerAdapter(getActivity().getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        mViewPager.setCurrentItem(selectedTab);
    }

    // Class Methods
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
