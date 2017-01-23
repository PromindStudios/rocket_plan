package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.AppWidget.AppWidgetProvider;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.CalendarFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.ContentPagerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.DrawerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.TimePagerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.LayoutColorInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.CategoryAdapter;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryAdapterListener, LayoutColorInterface, ContentInterface {

    // Layout
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    // Variables
    DatabaseHelper mDatabaseHelper;
    private ActionBarDrawerToggle mDrawerToggle;

    // Fragment
    FragmentManager mFragmentManager;
    private DrawerFragment mDrawerFragment;

    // Color Issues
    private CategoryColor mCategoryColor;
    private LayoutColor mLayoutColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up layout
        setContentView(R.layout.activity_main);

        // Set up Database Helper
        mDatabaseHelper = new DatabaseHelper(this);

        // create user and personal information
        mDatabaseHelper.createUser();

        // Iniatiate layout components
        mToolbar = (Toolbar) findViewById(R.id.toolbarMain);
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawerFragment);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Set up toolbar
        setSupportActionBar(mToolbar);

        // Initiate color objects
        mCategoryColor = new CategoryColor(this);
        colorLayout();

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

        // Add Fragment
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        TimePagerFragment timePagerFragment = new TimePagerFragment();
        fragmentTransaction.replace(R.id.flContainer, timePagerFragment);
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
    protected void onPostResume() {
        try {super.onPostResume();} catch (Exception e) {e.printStackTrace();}
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDrawer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Update App Widget
        Intent intentAppWidget = new Intent(this, AppWidgetProvider.class);
        intentAppWidget.setAction(MyConstants.UPDATE);
        sendBroadcast(intentAppWidget);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyConstants.REQUEST_ACTIVITY_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                colorLayout();
                mDrawerFragment.colorLayout();
            }
        }
    }

    // Implemented methods from Interfaces

    @Override
    public void ItemCategorySelected(int categoryId, String categoryName, int contentType) {
        ContentPagerFragment contentPagerFragment = new ContentPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CATEGORY_ID, categoryId);
        bundle.putInt(MyConstants.CONTENT_TYPE, contentType);
        contentPagerFragment.setArguments(bundle);
        changeFragment(contentPagerFragment);
    }

    @Override
    public void onCreateNewContent(final int categoryId, final String categoryName, final int contentType) {
        //getSupportActionBar().setTitle(categoryName);
        mDatabaseHelper.incrementContentCounter();
        startDetailActivity(categoryId, categoryName, contentType);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        ContentPagerFragment contentPagerFragment = new ContentPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CATEGORY_ID, categoryId);
        bundle.putInt(MyConstants.CONTENT_TYPE, contentType);
        contentPagerFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.flContainer, contentPagerFragment);
        fragmentTransaction.commit();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawers();
            }
        }, 500);

    }

    @Override
    public void onTimeClicked() {
        TimePagerFragment timePagerFragment = new TimePagerFragment();
        changeFragment(timePagerFragment);
    }

    @Override
    public void onCalendarClicked() {
        CalendarFragment calendarFragment = new CalendarFragment();
        changeFragment(calendarFragment);
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragment);
        fragmentTransaction.commit();
        closeDrawer();
    }

    public void updateCalendarFragmentList(Calendar calendar, int calendarPage) {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.flContainer);
        if (fragment instanceof CalendarFragment) {
            ((CalendarFragment)fragment).updateContentList(calendar, calendarPage);
        }
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

    private void colorLayout() {
        mLayoutColor = new LayoutColor(this, mDatabaseHelper.getLayoutColorValue());
        mToolbar.setBackgroundColor(mLayoutColor.getLayoutColor());
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mLayoutColor.getLayoutColor());
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void refreshActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
     }

    @Override
    public void createContent(Category category, int contentType, int detailType) {
        mDatabaseHelper.incrementContentCounter();

        int contentId = 0;
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                Task task = new Task(category.getId(), category.getTitle());
                contentId = mDatabaseHelper.createTask(task);
                break;
            case MyConstants.CONTENT_EVENT:
                Event event = new Event(category.getId(), category.getTitle());
                contentId = mDatabaseHelper.createEvent(event);
                break;
            case MyConstants.CONTENT_NOTE:
                Note note = new Note(category.getId(), category.getTitle());
                contentId = mDatabaseHelper.createNote(note);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CONTENT_TYPE, contentType);
        bundle.putInt(MyConstants.CONTENT_ID, contentId);
        bundle.putInt(MyConstants.DETAIL_TYPE, detailType);
        bundle.putInt(MyConstants.CATEGORY_ID, category.getId());
        startActivity(new Intent(this, DetailActivity.class).putExtras(bundle));
    }

    @Override
    public void selectCategory(final int contentType) {
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
                    createContent(category, contentType, MyConstants.DETAIL_GENERAL);
                }
            });
            builder.create().show();
        } else {
            Toast.makeText(this, getString(R.string.toast_add_category), Toast.LENGTH_LONG).show();
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
}
