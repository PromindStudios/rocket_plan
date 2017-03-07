package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DatePickerDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.InformationDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.MyTimePickerDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimePagerInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.PremiumInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
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
    android.support.design.widget.FloatingActionButton fabAdd;
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
    LayoutColor mLayoutColor;
    Category mCategory;
    int mContentType;
    Calendar mFastDate = null;
    Calendar mFastTime = null;

    // Inferface
    ContentInterface mContentInterface;
    PremiumInterface mPremiumInterface;

    // SharedPreferences
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_content_pager, container, false);

        // Handle arguments
        Bundle bundle = getArguments();
        int categoryId = bundle.getInt(MyConstants.CATEGORY_ID);
        mContentType = bundle.getInt(MyConstants.CONTENT_TYPE);

        // Set up SharedPreferences
        mSharedPreferences = getActivity().getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0);
        mEditor = mSharedPreferences.edit();

        // Set up layout
        mViewPager = (NonSwipeableViewPager) layout.findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) layout.findViewById(R.id.tabLayout);
        fabAdd = (android.support.design.widget.FloatingActionButton) layout.findViewById(R.id.fabAdd);
        ivAddDate = (ImageView) layout.findViewById(R.id.ivDateAdd);
        etAddContent = (EditText) layout.findViewById(R.id.etAddContent);
        rlAddContent = (RelativeLayout)layout.findViewById(R.id.rlContentFab);
        rlAddDate = (RelativeLayout)layout.findViewById(R.id.rlAddDate);
        tvAddDate = (TextView)layout.findViewById(R.id.tvDateAddNumber);

        // Set up variables
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mCategory = mDatabaseHelper.getCategory(categoryId);
        //mFastDate = null;
        //mFastTime = null;

        // Set up tablayout
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_tabTask)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_tabEvent)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.title_tabNote)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Set up toolbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mCategory.getTitle());

        // Handle clickEvents
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentContent = mViewPager.getCurrentItem();
                if (etAddContent.getText().toString().matches("")) {
                    mContentInterface.createContent(mCategory, currentContent, MyConstants.DETAIL_GENERAL, "", null, null, true);
                } else {
                    mContentInterface.createContent(mCategory, currentContent, MyConstants.DETAIL_GENERAL, etAddContent.getText().toString(), mFastDate, mFastTime, false);
                    mViewPagerAdapter.getFragment(currentContent).setAdapterUp();
                }
                resetFastAdd();

                if (mSharedPreferences.getBoolean(MyConstants.FIRST_CONTENT, true)) {
                    DialogFragment dialog = new InformationDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString(MyConstants.DIALOGE_TITLE, getString(R.string.help_first_content_title));
                    bundle.putString(MyConstants.DIALOGE_CONTENT, getString(R.string.help_first_content));
                    dialog.setArguments(bundle);
                    dialog.show(getActivity().getSupportFragmentManager(), "dialog_about");
                    mEditor.putBoolean(MyConstants.FIRST_CONTENT, false).commit();
                }
                mViewPagerAdapter.getFragment(mViewPager.getCurrentItem()).disableHelpText();
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
        colorLayout();

        // Handle Add Content Component
        handleQuickAddComponent(mContentType);

        // Set up ViewPager
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
                    Drawable fabIcon = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.ic_add_24dp, null);
                    fabIcon.mutate().setColorFilter(ResourcesCompat.getColor(getResources(), mLayoutColor.getLayoutColorId(), null), PorterDuff.Mode.MULTIPLY);
                    fabAdd.setImageDrawable(fabIcon);
                    mFastDate = null;
                    mFastTime = null;
                    tvAddDate.setVisibility(View.INVISIBLE);
                    ivAddDate.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_date_none_24dp, null));
                    rlAddDate.setVisibility(View.GONE);
                    etAddContent.setPadding(MyMethods.pxToDp(getActivity(), 16), 0, 0, 0);
                } else {
                    // Fab Icon --> Arrow to create fast Content
                    Drawable quickAddIcon = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.ic_quick_add, null);
                    quickAddIcon.mutate().setColorFilter(ResourcesCompat.getColor(getResources(), mLayoutColor.getLayoutColorId(), null), PorterDuff.Mode.MULTIPLY);
                    fabAdd.setImageDrawable(quickAddIcon);
                    if (mContentType!= MyConstants.CONTENT_NOTE) {
                        rlAddDate.setVisibility(View.VISIBLE);
                        etAddContent.setPadding(0, 0, 0, 0);
                    }

                }
            }
        });

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContentInterface = (ContentInterface)context;
        mPremiumInterface = (PremiumInterface) context;
    }

    private void handleQuickAddComponent(int contentType) {
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                etAddContent.setHint(getActivity().getString(R.string.add_task));
                etAddContent.setPadding(MyMethods.pxToDp(getActivity(), 16), 0, 0, 0);
                break;
            case MyConstants.CONTENT_EVENT:
                etAddContent.setHint(getActivity().getString(R.string.add_event));
                etAddContent.setPadding(MyMethods.pxToDp(getActivity(), 16), 0, 0, 0);
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
        resetFastAdd();
        colorLayout();
    }

    @Override
    public void onPause() {
        super.onPause();
        resetFastAdd();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_fragment_content, menu);
        boolean visible = mSharedPreferences.getBoolean(MyConstants.VISIBILITY_DETAILS_CONTENT_PAGER, true);
        if (visible) {
            menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_24dp, null));
        } else {
            menu.getItem(0).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_gone_24dp, null));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int currentItem = mViewPager.getCurrentItem();
        if (item.getItemId() == R.id.menu_item_sort) {
            if (mPremiumInterface.isPremium()) {
                String notSortedByPriority = "";
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
            } else {
                mPremiumInterface.openDialogPremiumFunction(getString(R.string.premium_function), getString(R.string.premium_silver_sort), getString(R.string.premium_expired));;
            }

            return true;
        }
        if (item.getItemId() == R.id.menu_item_visibility) {
            boolean visible = mSharedPreferences.getBoolean(MyConstants.VISIBILITY_DETAILS_CONTENT_PAGER, true);
            mEditor.putBoolean(MyConstants.VISIBILITY_DETAILS_CONTENT_PAGER, !visible);
            if (!visible) {
                item.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_24dp, null));
            } else {
                item.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_visibility_gone_24dp, null));
            }
            mEditor.commit();

            mViewPagerAdapter.getFragment(currentItem).setAdapterUp();
        }
        return false;
    }

    private void setViewPagerUp() {
        mViewPagerAdapter = new ContentViewPagerAdapter(getActivity().getSupportFragmentManager(), mTabLayout.getTabCount(), mCategory.getId(), mCategory.getTitle());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                handleQuickAddComponent(tab.getPosition());
                resetFastAdd();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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
        ivAddDate.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_date_none_24dp, null));
        rlAddDate.setVisibility(View.GONE);
        etAddContent.setPadding(MyMethods.pxToDp(getActivity(), 16), 0, 0, 0);
    }

    private void resetFastAdd() {
        closeKeyboard();
        cleanFastAdd();
    }

    @Override
    public void onDateSelected(Calendar date) {
        mFastDate = date;
        tvAddDate.setVisibility(View.VISIBLE);
        tvAddDate.setText(Integer.toString(mFastDate.get(Calendar.DAY_OF_MONTH)));
        Drawable icEventSolid = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_event_solid, null);
        icEventSolid.mutate().setColorFilter(ResourcesCompat.getColor(getResources(), R.color.white, null), PorterDuff.Mode.MULTIPLY);
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

    public void colorLayout() {
        mLayoutColor = new LayoutColor(getActivity(), mDatabaseHelper.getLayoutColorValue());
        //fabAdd.setBackgroundTintList(ColorStateList.valueOf(mLayoutColor.getLayoutColor()));
        fabAdd.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.colorWhite, null)));
        tvAddDate.setTextColor(mLayoutColor.getLayoutColor());

        mTabLayout.setBackgroundColor(mLayoutColor.getLayoutColor());
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mLayoutColor.getLayoutColor());
        }
    }
}