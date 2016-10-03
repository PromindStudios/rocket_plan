package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.FilesFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.GeneralFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.DetailViewPagerAdapter;

/**
 * Created by eric on 04.05.2016.
 */
public class DetailActivity extends AppCompatActivity implements GeneralFragment.DetailActivityListener {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DetailViewPagerAdapter mAdapterViewPager;
    FloatingActionButton fabDone;

    int mContentId;
    int mContentType;
    int mDetailType;

    Task mTask;
    Event mEvent;
    Note mNote;
    Content mContent;

    int mCategoryId;
    String mCategoryName;

    String mPicturePath;

    DatabaseHelper mDatabaseHelper;

    FilesFragment mFilesFragment;

    boolean tabSelectedFirst = false;
    boolean mFromReminder = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        mDatabaseHelper = new DatabaseHelper(this);

        fabDone = (FloatingActionButton)findViewById(R.id.fabDone);

        // Get Extras
        Bundle extras = getIntent().getExtras();
        mContentId = extras.getInt(MyConstants.CONTENT_ID);
        mContentType = extras.getInt(MyConstants.CONTENT_TYPE);
        mDetailType = extras.getInt(MyConstants.DETAIL_TYPE);
        mFromReminder = extras.getBoolean(MyConstants.REMINDER_FROM);


        Log.i("Detail Type", Integer.toString(mDetailType));

        // Initiate layout components
        mToolbar = (Toolbar)findViewById(R.id.toolbar_detail);
        mViewPager = (ViewPager)findViewById(R.id.view_pager_detail);
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout_detail);



        mTabLayout.addTab(mTabLayout.newTab().setIcon(ContextCompat.getDrawable(this, R.drawable.ic_general)));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(ContextCompat.getDrawable(this, R.drawable.ic_files)));
        if (mContentType == MyConstants.CONTENT_TASK) {
            mTask = (Task)mDatabaseHelper.getTask(mContentId);
            mContent = mTask;
            mTabLayout.addTab(mTabLayout.newTab().setIcon(ContextCompat.getDrawable(this, R.drawable.ic_subtask_18dp)));
        }
        if (mContentType == MyConstants.CONTENT_EVENT) {
            mEvent = (Event)mDatabaseHelper.getEvent(mContentId);
            mContent = mEvent;
            Log.i("Sau: ", Integer.toString(mEvent.getCategoryId()));
        }
        if (mContentType == MyConstants.CONTENT_NOTE) {
            mNote = (Note)mDatabaseHelper.getNote(mContentId);
            mContent = mNote;
        }

        mCategoryName = mContent.getCategory();

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mAdapterViewPager = new DetailViewPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount(), mContentType);
        mViewPager.setAdapter(mAdapterViewPager);

        // Set up toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mContent.getTitle());

        // Set up Fab Button
        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endActivity(false);
            }
        });

        // Default Tab Settings
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("Tab selected: ", Integer.toString(position));
                mAdapterViewPager.handleFocus(position);
                TabLayout.Tab tab = mTabLayout.getTabAt(position);
                tab.select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

        // Handle Color

        Category category = mDatabaseHelper.getCategory(mContent.getCategoryId());
        CategoryColor categoryColor = new CategoryColor(this, category.getColor());
        Log.i("CaaaategoryId", " "+mContent.getCategoryId());

        // Toolbar
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, categoryColor.getCategoryColor()));

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // Status Bar
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, categoryColor.getCategoryColor()));
        }

        // Fab
        fabDone.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, categoryColor.getCategoryColor())));

        // Tabs
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, categoryColor.getCategoryColor()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        switch (mContentType) {
            case 0:
                mDatabaseHelper.updateTask(mTask);
                break;
            case 1:
                mDatabaseHelper.updateEvent(mEvent);
                break;

            case 2:
                mDatabaseHelper.updateNote(mNote);
                break;

            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        endActivity(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyConstants.REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            mFilesFragment.onSetPicture(mPicturePath);
        }
    }

    @Override
    public Task getTask() {
        return mTask;
    }

    @Override
    public Event getEvent() {
        return mEvent;
    }

    @Override
    public Note getNote() {
        return mNote;
    }

    @Override
    public void takePicture(FilesFragment filesFragment) {
        mFilesFragment = filesFragment;
        String path = "RockitPlan_"+ Long.toString(System.nanoTime());
        File directory = new File(Environment.getExternalStorageDirectory(), "RockitPlan");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File pictureFile = new File(directory, path + ".jpg");
        mPicturePath = pictureFile.getAbsolutePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
        startActivityForResult(intent, MyConstants.REQUEST_CODE_IMAGE);
    }

    @Override
    public void selectTab() {
        if (!tabSelectedFirst) {
            mTabLayout.getTabAt(mDetailType).select();
            tabSelectedFirst = true;
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void endActivity(boolean delete) {
        if (mContent.getTitle().equals("") || delete) {
            mDatabaseHelper.deleteContent(mContent.getId(), mContentType);
        }
        if (mFromReminder) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        finish();
    }
}
