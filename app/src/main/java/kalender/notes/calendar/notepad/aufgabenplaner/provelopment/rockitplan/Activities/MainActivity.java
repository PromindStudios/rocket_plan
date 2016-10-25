package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.CalendarFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.ContentFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.ContentPagerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.ContentTimeFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.DrawerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.TimePagerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimePagerInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.NonSwipeableViewPager;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.CategoryAdapter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.ContentViewPagerAdapter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.TimeViewPagerAdapter;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryAdapterListener, ContentTimeFragment.MainActivityListener {

    // Layout
    private Toolbar mToolbar;
    //private NonSwipeableViewPager mViewPager;
    //private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;
    //private ContentViewPagerAdapter mContentViewPagerAdapter;
    //private TimeViewPagerAdapter mTimeViewPagerAdapter;

    private DrawerFragment mDrawerFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    DatabaseHelper mDatabaseHelper;

    // Fab Buttons
    //FloatingActionButton fabTask;
    //FloatingActionButton fabEvent;
    //FloatingActionButton fabNote;
    //FloatingActionsMenu fabMenu;

    // Listener
    ContentTimePagerInterface mContentTimePagerListener;

    boolean isContent;
    int mCategoryId;
    String mCategoryName;
    int mTimeType;

    // Fragment
    FragmentManager mFragmentManager;



    private CategoryColor mCategoryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDatabaseHelper = new DatabaseHelper(this);
        mCategoryId = 0;

        // Iniatiate layout components
        mToolbar = (Toolbar) findViewById(R.id.toolbarMain);
        //mViewPager = (NonSwipeableViewPager) findViewById(R.id.viewPagerMain);
        //mTabLayout = (TabLayout) findViewById(R.id.tabLayoutMain);
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawerFragment);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Category Color
        mCategoryColor = new CategoryColor(this);

        // Set up toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Category Name");

        // Set up navigation drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerFragment.updateDrawer();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // Set up Default ViewPagerAdapter
        //setUpTimeViewPagerAdapter(MyConstants.TIME_DAY);

        // Iniatate Fab Buttons
        //fabTask = (FloatingActionButton) findViewById(R.id.fabTask);
        //fabEvent = (FloatingActionButton) findViewById(R.id.fabEvent);
        //fabNote = (FloatingActionButton) findViewById(R.id.fabNote);
        //fabMenu = (FloatingActionsMenu) findViewById(R.id.fabMenu);

        // Set up ClickListener for Fab Buttons

        /*
        fabTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContent) {
                    startDetailActivity(mCategoryId, mCategoryName, MyConstants.CONTENT_TASK, MyConstants.CONTENT_TIME_CONTENT, 0);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TabLayout.Tab tab = mTabLayout.getTabAt(MyConstants.CONTENT_TASK);
                            tab.select();
                        }
                    }, 1000);

                } else {
                    letUserSelectCategory(MyConstants.CONTENT_TASK);
                }
                fabMenu.collapse();

            }
        });
        fabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContent) {
                    startDetailActivity(mCategoryId, mCategoryName, MyConstants.CONTENT_EVENT, MyConstants.CONTENT_TIME_CONTENT, 0);
                    Log.i("mCategoryNameee", mCategoryName+Integer.toString(mCategoryId));
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TabLayout.Tab tab = mTabLayout.getTabAt(MyConstants.CONTENT_EVENT);
                            tab.select();
                        }
                    }, 1000);

                } else {
                    letUserSelectCategory(MyConstants.CONTENT_EVENT);
                }
                fabMenu.collapse();
            }
        });
        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContent) {
                    startDetailActivity(mCategoryId, mCategoryName, MyConstants.CONTENT_NOTE, MyConstants.CONTENT_TIME_CONTENT, 0);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TabLayout.Tab tab = mTabLayout.getTabAt(MyConstants.CONTENT_NOTE);
                            tab.select();
                        }
                    }, 1000);
                } else {
                    letUserSelectCategory(MyConstants.CONTENT_NOTE);
                }
                fabMenu.collapse();
            }
        });
        */

        // Add Fragment
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        TimePagerFragment timePagerFragment = new TimePagerFragment();
        mContentTimePagerListener = (ContentTimePagerInterface)timePagerFragment;
        fragmentTransaction.add(R.id.flContainer, timePagerFragment);
        fragmentTransaction.commit();


        // open Drawer in the beginning after a little delay to show the animation
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        }, 500);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDrawer();
    }

    // Implemented methods from Interfaces

    @Override
    public void ItemCategorySelected(int categoryId, String categoryName, int contentType) {
        mCategoryId = categoryId;
        mCategoryName = categoryName;

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        ContentPagerFragment contentPagerFragment = new ContentPagerFragment();
        mContentTimePagerListener = (ContentTimePagerInterface)contentPagerFragment;
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CATEGORY_ID, categoryId);
        bundle.putInt(MyConstants.CONTENT_TYPE, contentType);
        contentPagerFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.flContainer, contentPagerFragment);
        fragmentTransaction.commit();
        closeDrawer();

        // Set Toolbar Title
        //getSupportActionBar().setTitle(mCategoryName);
        // close Navigation Drawer here
        /*
        if (mContentViewPagerAdapter == null) {
            setUpContentViewPagerAdapter(categoryId, categoryName, false, contentType);
        } else {
            setUpContentViewPagerAdapter(categoryId, categoryName, false, contentType);
        }
        */
    }

    /*
    public void setUpContentViewPagerAdapter(int categoryId, String categoryName, boolean isExpanded, int contentType) {

        mContentViewPagerAdapter = null;
        Log.i("change", Integer.toString(categoryId));
        isContent = true;

        // Set up tabs and viewpager to display content
        mTabLayout.removeAllTabs();
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_tabTask)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_tabEvent)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_tabNote)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mContentViewPagerAdapter = new ContentViewPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount(), categoryId, categoryName, isExpanded);
        mViewPager.setAdapter(mContentViewPagerAdapter);
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
        TabLayout.Tab tab = mTabLayout.getTabAt(contentType);
        tab.select();
        mDrawerLayout.closeDrawers();
    }
    */

    @Override
    public void onCreateNewContent(final int categoryId, final String categoryName, final int contentType) {
        mCategoryId = categoryId;
        mCategoryName = categoryName;
        getSupportActionBar().setTitle(mCategoryName);
        startDetailActivity(categoryId, categoryName, contentType);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        ContentPagerFragment contentPagerFragment = new ContentPagerFragment();
        mContentTimePagerListener = (ContentTimePagerInterface)contentPagerFragment;
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CATEGORY_ID, categoryId);
        bundle.putInt(MyConstants.CONTENT_TYPE, contentType);
        contentPagerFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.flContainer, contentPagerFragment);
        fragmentTransaction.commit();
        //setUpContentViewPagerAdapter(categoryId, categoryName, false, contentTyp);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawers();
            }
        }, 500);

    }

    /*
    public void setUpTimeViewPagerAdapter(int timeType) {

        mContentViewPagerAdapter = null;
        // Set Toolbar Title
        getSupportActionBar().setTitle(getString(R.string.overview));
        mTimeType = timeType;
        isContent = false;
        mTabLayout.removeAllTabs();
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_day)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_week)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_month)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTimeViewPagerAdapter = new TimeViewPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mTimeViewPagerAdapter);
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
        TabLayout.Tab tab = mTabLayout.getTabAt(timeType);
        tab.select();
        mDrawerLayout.closeDrawers();
    }
    */


    @Override
    public void onTimeClicked() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        TimePagerFragment timePagerFragment = new TimePagerFragment();
        mContentTimePagerListener = (ContentTimePagerInterface)timePagerFragment;
        fragmentTransaction.replace(R.id.flContainer, timePagerFragment);
        fragmentTransaction.commit();
        closeDrawer();
    }

    @Override
    public void onCalendarClicked() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        CalendarFragment calendarFragment = new CalendarFragment();
        fragmentTransaction.replace(R.id.flContainer, calendarFragment);
        fragmentTransaction.commit();
        closeDrawer();
    }

    private void closeDrawer() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawers();
            }
        }, 100);
    }

    public void startDetailActivity(int categoryId, String categoryName, int contentType) {
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
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CONTENT_TYPE, contentType);
        bundle.putInt(MyConstants.CONTENT_ID, contentId);
        bundle.putInt(MyConstants.DETAIL_TYPE, MyConstants.DETAIL_GENERAL);
        bundle.putInt(MyConstants.CATEGORY_ID, categoryId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void updateDrawer() {
        mDrawerFragment.updateDrawer();
    }

    public void letUserSelectCategory(final int contenType) {
        final ArrayList<Category> categories = mDatabaseHelper.getAllCategories();
        if (categories.size() > 0) {
            List<String> listItems = new ArrayList<>();
            for (Category category : categories) {
                listItems.add(category.getTitle());
            }
            CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    Category category = categories.get(item);
                    //startDetailActivity(category.getId(), category.getTitle(), contenType, MyConstants.CONTENT_TIME_TIME, mTabLayout.getSelectedTabPosition());
                }
            });
            builder.create().show();
        } else {
            // Toast with hint to create Category first + Open Drawer
            Toast.makeText(this, getString(R.string.toast_add_category), Toast.LENGTH_LONG).show();
            try {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void colorHead(int categoryId) {

        if (categoryId == 0) {
            mCategoryColor.setColor(0);
        } else {
            Category category = mDatabaseHelper.getCategory(categoryId);
            mCategoryColor.setColor(category.getColor());
        }
        // Toolbar
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, mCategoryColor.getCategoryColor()));

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // Status Bar
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, mCategoryColor.getCategoryColor()));
        }

        // Fab
        /*
        fabTask.setColorNormal(ContextCompat.getColor(this, mCategoryColor.getCategoryColor()));
        fabEvent.setColorNormal(ContextCompat.getColor(this, mCategoryColor.getCategoryColor()));
        fabNote.setColorNormal(ContextCompat.getColor(this, mCategoryColor.getCategoryColor()));

        // Tabs
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, mCategoryColor.getCategoryColor()));
        */
    }

    public Toolbar getToolbar () {
        return mToolbar;
    }

}
