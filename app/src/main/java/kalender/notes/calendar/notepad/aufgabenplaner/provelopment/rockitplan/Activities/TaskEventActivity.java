package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.GeneralFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.NotesFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.DetailViewPagerAdapter;

/**
 * Created by eric on 04.05.2016.
 */
public class TaskEventActivity extends ContentActivity implements GeneralFragment.DetailActivityListener {

    // Layout
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DetailViewPagerAdapter mAdapterViewPager;

    ImageView ivContent;
    TextView tvContent;
    TextView tvCategory;

    // Content
    int mDetailType;
    TaskEvent mTaskEvent;

    // Other variables
    boolean tabSelectedFirst = false;
    boolean tabTwoSelectedFirst = false;
    boolean mFromReminder = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_task_event);
        super.onCreate(savedInstanceState);

        // Get Extras
        Bundle extras = getIntent().getExtras();
        mDetailType = extras.getInt(MyConstants.DETAIL_TYPE);
        mFromReminder = extras.getBoolean(MyConstants.REMINDER_FROM);

        // Set up content & category
        mTaskEvent = (TaskEvent)mContent;



        // Initialize layout
        mViewPager = (ViewPager)findViewById(R.id.view_pager_detail);
        mTabLayout = (TabLayout)findViewById(R.id.tab_layout_detail);

        ivContent = (ImageView)findViewById(R.id.ivContenT);
        tvContent = (TextView)findViewById(R.id.tvContenT);
        tvCategory = (TextView)findViewById(R.id.tvCategory);

        // Content Status (Done / Undone)

        /*
        if (android.os.Build.VERSION.SDK_INT >= 21) {
        } else {
            int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            cbTitle.setButtonDrawable(id);
        }

        cbTitle.setChecked(mTaskEvent.isDone());
        cbTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                closeKeyboard();
                vDummy.requestFocus();
                mTaskEvent.setDone(b);
                ((GeneralFragment)mAdapterViewPager.getInstance(0)).setDateComponentsVisible(!b);
            }
        });
        */

        // Set up Category
        tvCategory.setText(getString(R.string.category)+": "+mContent.getCategory());

        // TabLayout & ViewPager

        // Add Tabs
        //mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_key_data)));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_general_16dp, null)));

        if (mContentType == MyConstants.CONTENT_TASK) {
            ivContent.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_task_24dp, null));
            tvContent.setText(getString(R.string.task));
            //mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.premium_silver_subtasks)));
            mTabLayout.addTab(mTabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subtask, null)));
        }
        if (mContentType == MyConstants.CONTENT_EVENT) {
            ivContent.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_event_text_24dp, null));
            tvContent.setText(getString(R.string.event));
            //mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_details)));
            mTabLayout.addTab(mTabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_details, null)));
        }

        mTabLayout.addTab(mTabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_files, null)));
        //mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_notes)));

        // Set up ViewPager and TabLayout
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mAdapterViewPager = new DetailViewPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount(), mContentType);
        mViewPager.setAdapter(mAdapterViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                handleTabIconText(position);
                Log.i("Tab selected: ", Integer.toString(position));
                mAdapterViewPager.handleFocus(position);
                TabLayout.Tab tab = mTabLayout.getTabAt(position);
                tab.select();

            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //mAdapterViewPager.handleFocus(tab.getPosition());
                mViewPager.setCurrentItem(tab.getPosition());
                //vDummy.requestFocus();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Handle Color
        LayoutColor layoutColor = new LayoutColor(this, mDatabaseHelper.getLayoutColorValue());
        /*
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // Status Bar
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, categoryColor.getCategoryColorDark()));
        }
        */
        mTabLayout.setBackgroundColor(layoutColor.getLayoutColor());
    }

    @Override
    public void selectTab() {
        if (!tabSelectedFirst) {
            mTabLayout.getTabAt(mDetailType).select();
            tabSelectedFirst = true;
        }
    }

    @Override
    public void selectTabTwo() {
        /*
        if (!tabTwoSelectedFirst) {
            mTabLayout.getTabAt(mDetailType).select();
            mAdapterViewPager.handleFocus(mDetailType);
            tabTwoSelectedFirst = true;
        }
        */
    }

    @Override
    public void setToolbarTitle(String title) {

    }

    @Override
    public int getCurrentTab() {
        return mViewPager.getCurrentItem();
    }


    public void endActivity(boolean delete) {
        if (mContent.getTitle().equals("") || delete) {
            mDatabaseHelper.deleteContent(mContent.getId(), mContentType);
        }
        if (mFromReminder) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        finish();
    }

    private void handleTabIconText(int selectedTab) {
        /*
        if (mContentType == MyConstants.CONTENT_TASK) {
            mTabLayout.getTabAt(1).setText(getString(R.string.premium_silver_subtasks));
        } else {
            mTabLayout.getTabAt(1).setText(getString(R.string.tab_details));
        }
        mTabLayout.getTabAt(1).setIcon(null);
        mTabLayout.getTabAt(2).setText(getString(R.string.tab_notes));
        mTabLayout.getTabAt(2).setIcon(null);
        TabLayout.Tab tab = mTabLayout.getTabAt(selectedTab);
        switch (selectedTab) {
            case 1:
                if (mContentType == MyConstants.CONTENT_TASK) {
                    tab.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subtask, null));
                } else {
                    tab.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_details, null));
                }
                tab.setText("");
                break;
            case 2:
                tab.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_files, null));
                tab.setText("");
                break;
        }
        */
    }

    @Override
    public NotesFragment getFilesFragment() {
        return (NotesFragment)mAdapterViewPager.getInstance(MyConstants.DETAIL_FILES);
    }

}