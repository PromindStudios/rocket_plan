package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Navigation_Drawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditCategoryDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.PremiumInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.NonSwipeableViewPager;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter.NavigationDrawerViewPagerAdapter;

/**
 * Created by eric on 04.04.2016.
 */
public class DrawerFragment extends Fragment implements AddEditCategoryDialog.AddEditDialogCategoryListener {

    // Layout
    RelativeLayout rlToolbar;
    ImageView ivSearch;
    TabLayout mTabLayout;
    NonSwipeableViewPager mViewPager;
    TextView tvAppTitle;

    // Variables
    PremiumInterface mPremiumInterface;
    LayoutColor mLayoutColor;
    DatabaseHelper mDatabaseHelper;
    NavigationDrawerViewPagerAdapter mNavigationDrawerViewPagerAdapter;
    boolean openSettings = false;

    // Shared Preferences
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        // Initiate layout components
        rlToolbar = (RelativeLayout) layout.findViewById(R.id.rlToolbar);
        ivSearch = (ImageView)layout.findViewById(R.id.ivSearch);
        mTabLayout = (TabLayout)layout.findViewById(R.id.tlNavDrawer);
        mViewPager = (NonSwipeableViewPager) layout.findViewById(R.id.vpNavDrawer);
        tvAppTitle = (TextView)layout.findViewById(R.id.tvAppTitle);

        // Initiate TabLayout
        //mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.home)));
        //mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.settings)));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home_18dp, null)));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_settings_18dp, null)));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_18dp, null)));

        // Put custom font in work for app title
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/custom_font_one.ttf");
        tvAppTitle.setTypeface(custom_font);

        // Set up ViewPager
        mNavigationDrawerViewPagerAdapter = new NavigationDrawerViewPagerAdapter(getActivity().getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mNavigationDrawerViewPagerAdapter);

        // Set up TabLayout
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        if (MyConstants.MAIN_ACTIVITY_ACTION_SETTINGS.equals(getActivity().getIntent().getAction())) {
            //TabLayout.Tab tab = mTabLayout.getTabAt(1);
            //tab.select();
            getActivity().getIntent().setAction(null);
            openSettings = true;
        }

        // Initiate Interface
        mPremiumInterface = (PremiumInterface)getActivity();

        // Initiate SharedPreferences
        mSharedPreferences = getActivity().getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0);
        mEditor = mSharedPreferences.edit();

        // Initiate database helper and get categories
        mDatabaseHelper = new DatabaseHelper(getActivity());

        // Handle Click Events
        /*
        ibColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPremiumInterface.hasPremium()) {
                    LayoutColorDialog dialog = new LayoutColorDialog();
                    dialog.show(getActivity().getSupportFragmentManager(), "layout_color_dialog");
                } else {
                    mPremiumInterface.openDialogPremiumFunction(getString(R.string.premium_function), getString(R.string.premium_silver_colors), getString(R.string.premium_expired));;
                }
            }
        });
        */

        // Color Layout
        mLayoutColor = new LayoutColor(getActivity(), mDatabaseHelper.getLayoutColorValue());

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        colorLayout();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onUpdateData() {
        updateDrawer();
    }

    public void updateDrawer() {
        mNavigationDrawerViewPagerAdapter = new NavigationDrawerViewPagerAdapter(getActivity().getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mNavigationDrawerViewPagerAdapter);
        TabLayout.Tab tab;
        if (openSettings) {
            tab = mTabLayout.getTabAt(1);
            openSettings = false;
        } else {
            tab = mTabLayout.getTabAt(0);
        }
        tab.select();

        /*
        if (categories.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            myFab.setVisibility(View.VISIBLE);
            tvHelp.setVisibility(View.VISIBLE);
            boolean isStart = mSharedPreferences.getBoolean(MyConstants.IS_START_CATEGORY, true);
            if (isStart) {
                tvHelp.setText(getActivity().getString(R.string.help_add_category_and_welcome));
            } else {
                tvHelp.setText(getActivity().getString(R.string.help_add_category));
            }
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            myFab.setVisibility(View.GONE);
            tvHelp.setVisibility(View.GONE);
            mCategoryAdapter = new CategoryAdapter(getActivity(), categories, mDatabaseHelper, DrawerFragment.this, getActivity().getSupportFragmentManager());
            mRecyclerView.setAdapter(mCategoryAdapter);
        }
        */
    }

    public void colorLayout() {
        mLayoutColor.setColorValue(mDatabaseHelper.getLayoutColorValue());
        rlToolbar.setBackgroundColor(mLayoutColor.getLayoutColor());
        mTabLayout.setBackgroundColor(mLayoutColor.getLayoutColor());
    }
}
