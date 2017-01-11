package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.DetailActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DatePickerDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.InformationDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.MyTimePickerDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimePagerInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyFloatingActionButton.FloatingActionButton;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyFloatingActionButton.FloatingActionsMenu;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.NonSwipeableViewPager;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.ContentViewPagerAdapter;

/**
 * Created by Eric on 25.10.2016.
 */

public class ContentPagerFragment extends Fragment implements ContentTimePagerInterface, DatePickerDialog.DatePickerDialogListener, MyTimePickerDialog.TimePickerDialogListener {

    // Layout
    TabLayout mTabLayout;
    NonSwipeableViewPager mViewPager;
    FloatingActionButton fabTask;
    FloatingActionButton fabEvent;
    FloatingActionButton fabNote;
    android.support.design.widget.FloatingActionButton fabAdd;
    FloatingActionsMenu fabMenu;
    ImageView ivAddDate;
    EditText etAddContent;
    RelativeLayout rlAddContent;
    RelativeLayout rlAddDate;
    TextView tvAddDate;

    // Adpater
    ContentViewPagerAdapter mViewPagerAdapter;

    // Varialbes
    DatabaseHelper mDatabaseHelper;
    CategoryColor mCategoryColor;
    Category mCategory;
    int mContentType;
    Calendar mFastDate;
    Calendar mFastTime;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_content_pager, container, false);

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
        fabAdd = (android.support.design.widget.FloatingActionButton) layout.findViewById(R.id.fabAdd);
        fabMenu = (FloatingActionsMenu) layout.findViewById(R.id.fabMenu);
        ivAddDate = (ImageView) layout.findViewById(R.id.ivDateAdd);
        etAddContent = (EditText) layout.findViewById(R.id.etAddContent);
        rlAddContent = (RelativeLayout)layout.findViewById(R.id.rlContentFab);
        rlAddDate = (RelativeLayout)layout.findViewById(R.id.rlAddDate);
        tvAddDate = (TextView)layout.findViewById(R.id.tvDateAddNumber);

        // Set up variables
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mCategory = mDatabaseHelper.getCategory(categoryId);
        mCategoryColor = new CategoryColor(getActivity(), mCategory.getColor());
        mFastDate = null;
        mFastTime = null;

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
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int contentType = mViewPager.getCurrentItem();
                if (etAddContent.getText().toString().matches("")) {
                    cleanFastAdd();
                    startDetailActivity(mCategory.getId(), mCategory.getTitle(), contentType);
                } else {
                    switch (contentType) {
                        case MyConstants.CONTENT_TASK:
                            Task task = new Task(mCategory.getId(), mCategory.getTitle());
                            task.setTitle(etAddContent.getText().toString());
                            task.setDate(mFastDate);
                            task.setTime(mFastTime);
                            mDatabaseHelper.createTask(task);
                            break;
                        case MyConstants.CONTENT_EVENT:
                            Event event = new Event(mCategory.getId(), mCategory.getTitle());
                            event.setTitle(etAddContent.getText().toString());
                            event.setDate(mFastDate);
                            event.setTime(mFastTime);
                            mDatabaseHelper.createEvent(event);
                            break;
                        case MyConstants.CONTENT_NOTE:
                            Note note = new Note(mCategory.getId(), mCategory.getTitle());
                            note.setTitle(etAddContent.getText().toString());
                            mDatabaseHelper.createNote(note);
                            break;
                    }
                    // update current Tab
                    mViewPagerAdapter.getFragment(contentType).setAdapterUp();
                    cleanFastAdd();
                    closeKeyboard();

                    if (getActivity().getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0).getBoolean(MyConstants.FIRST_CONTENT, true)) {
                        DialogFragment dialog = new InformationDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString(MyConstants.DIALOGE_TITLE, getString(R.string.help_first_content_title));
                        bundle.putString(MyConstants.DIALOGE_CONTENT, getString(R.string.help_first_content));
                        dialog.setArguments(bundle);
                        dialog.show(getActivity().getSupportFragmentManager(), "dialog_about");
                        getActivity().getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0).edit().putBoolean(MyConstants.FIRST_CONTENT, false).commit();
                    }
                }
                mViewPagerAdapter.getFragment(mViewPager.getCurrentItem()).disableHelpText();
                fabMenu.collapse();
            }
        });

        // Set onClickListener for ivAddDate
        ivAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                rlAddContent.requestFocus();
                Calendar date;
                if (mFastDate == null) {
                    date = Calendar.getInstance();
                } else {
                    date = mFastDate;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(MyConstants.TASK_DATE, date);
                DialogFragment dialogFragment = new DatePickerDialog();
                dialogFragment.setTargetFragment(ContentPagerFragment.this, 0);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });


        // Handle Color
        fabTask.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabEvent.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabNote.setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabAdd.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor())));
        fabMenu.getButton().setColorNormal(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        fabMenu.getButton().setColorPressed(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));

        ((MainActivity)getActivity()).getToolbar().setBackgroundColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // Status Bar
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
        }

        // Handle Add Content Component
        handleQuickAddComponent(mContentType);

        setViewPagerUp();
        TabLayout.Tab tab = mTabLayout.getTabAt(mContentType);
        tab.select();

        // Handle EditText Change
        etAddContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etAddContent.getText().toString().matches("")) {
                    // Fab Icon --> Plus
                    fabAdd.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_24dp));
                    mFastDate = null;
                    mFastTime = null;
                    tvAddDate.setVisibility(View.INVISIBLE);
                    ivAddDate.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_event_text_24dp, null));
                } else {
                    // Fab Icon --> Arrow to create fast Content
                    fabAdd.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_quick_add));
                }

            }
        });

        return layout;
    }

    private void handleQuickAddComponent(int contentType) {
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                etAddContent.setHint(getActivity().getString(R.string.add_task));
                rlAddDate.setVisibility(View.VISIBLE);
                etAddContent.setPadding(0, 0, 0, 0);
                break;
            case MyConstants.CONTENT_EVENT:
                etAddContent.setHint(getActivity().getString(R.string.add_event));
                rlAddDate.setVisibility(View.VISIBLE);
                etAddContent.setPadding(0, 0, 0, 0);
                break;
            case MyConstants.CONTENT_NOTE:
                etAddContent.setHint(getActivity().getString(R.string.add_note));
                rlAddDate.setVisibility(View.GONE);
                etAddContent.setPadding(MyMethods.pxToDp(getActivity(), 16), 0, 0, 0);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        closeKeyboard();
        rlAddContent.requestFocus();
        etAddContent.setText("");


    }

    @Override
    public void onPause() {
        super.onPause();
        closeKeyboard();
        rlAddContent.requestFocus();
        etAddContent.setText("");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_fragment_content, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_sort) {
            String notSortedByPriority = "";
            final int currentItem = mViewPager.getCurrentItem();
            boolean sortedByPriority = false;
            switch (mViewPager.getCurrentItem()) {
                case MyConstants.CONTENT_TASK:
                    sortedByPriority = mCategory.isTaskSortedByPriority();
                    mCategory.setTaskSortedByPriority(!sortedByPriority);
                    mDatabaseHelper.updateCategory(mCategory);
                    notSortedByPriority = getString(R.string.sorted_by_due_date);
                    break;
                case MyConstants.CONTENT_EVENT:
                    sortedByPriority = mCategory.isEventSortedByPriority();
                    mCategory.setEventSortedByPriority(!sortedByPriority);
                    mDatabaseHelper.updateCategory(mCategory);
                    notSortedByPriority = getString(R.string.sorted_by_due_date);
                    break;
                case MyConstants.CONTENT_NOTE:
                    sortedByPriority = mCategory.isNoteSortedByPriority();
                    mCategory.setNoteSortedByPriority(!sortedByPriority);
                    mDatabaseHelper.updateCategory(mCategory);
                    notSortedByPriority = getString(R.string.sorted_by_time_of_creation);
                    break;
            }
            mViewPagerAdapter.getFragment(currentItem).setAdapterUp();
            if (!sortedByPriority) {
                Toast.makeText(getActivity(), getString(R.string.sorted_by_priority), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), notSortedByPriority, Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return false;
    }

    private void setViewPagerUp() {
        mViewPagerAdapter = new ContentViewPagerAdapter(getActivity().getSupportFragmentManager(), mTabLayout.getTabCount(), mCategory.getId(), mCategory.getTitle());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                handleQuickAddComponent(tab.getPosition());
                closeKeyboard();
                cleanFastAdd();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

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

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etAddContent.getWindowToken(), 0);
    }

    private void cleanFastAdd() {
        etAddContent.setText("");
        mFastDate = null;
        mFastTime = null;
        tvAddDate.setVisibility(View.INVISIBLE);
        rlAddContent.requestFocus();
        ivAddDate.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_event_text_24dp, null));
    }

    @Override
    public void onDateSelected(Calendar date) {
        mFastDate = date;
        tvAddDate.setVisibility(View.VISIBLE);
        tvAddDate.setText(Integer.toString(mFastDate.get(Calendar.DAY_OF_MONTH)));
        Drawable icEventSolid = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_event_solid, null);
        icEventSolid.mutate().setColorFilter(ResourcesCompat.getColor(getResources(), mCategoryColor.getCategoryColor(), null), PorterDuff.Mode.MULTIPLY);
        ivAddDate.setImageDrawable(icEventSolid);
    }

    @Override
    public void openTimeDialog() {
        Calendar time;
        if (mFastTime == null) {
            time = Calendar.getInstance();
            time.set(Calendar.HOUR_OF_DAY, 12);
            time.set(Calendar.MINUTE, 0);
        } else {
            time = mFastTime;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyConstants.TASK_EVENT_TIME, time);
        DialogFragment dialogFragment = new MyTimePickerDialog();
        dialogFragment.setTargetFragment(ContentPagerFragment.this, 0);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSelected(Calendar time) {
        mFastTime = time;
    }
}
