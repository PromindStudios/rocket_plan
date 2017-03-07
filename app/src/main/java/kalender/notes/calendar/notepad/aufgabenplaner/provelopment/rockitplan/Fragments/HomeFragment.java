package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.StarterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.UpdateInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.CategoryAdapter;

/**
 * Created by Eric on 05.03.2017.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, UpdateInterface{

    RelativeLayout rlOverview;
    RelativeLayout rlCalendar;
    RecyclerView rvCategory;
    ImageView ivOverview;
    ImageView ivCalendar;

    LayoutColor mLayoutColor;
    DatabaseHelper mDatabaseHelper;
    CategoryAdapter mCategoryAdapter;

    // Interface
    StarterInterface mStarterInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_home, container, false);

        // Initiate Layout
        rlOverview = (RelativeLayout)layout.findViewById(R.id.rlOverview);
        rlCalendar = (RelativeLayout)layout.findViewById(R.id.rlCalendar);
        rvCategory = (RecyclerView)layout.findViewById(R.id.rvCategory);
        ivOverview = (ImageView)layout.findViewById(R.id.ivOverview);
        ivCalendar = (ImageView)layout.findViewById(R.id.ivCalendar);

        // Set ClickListener
        rlOverview.setOnClickListener(HomeFragment.this);
        rlCalendar.setOnClickListener(HomeFragment.this);

        // Initiate Variables
        mDatabaseHelper = new DatabaseHelper(getActivity());

        // Layout Color
        mLayoutColor = new LayoutColor(getActivity(), mDatabaseHelper.getLayoutColorValue());
        ivOverview.setColorFilter(mLayoutColor.getLayoutColor());
        ivCalendar.setColorFilter(mLayoutColor.getLayoutColor());

        // Set up RecyclerView
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCategoryAdapter = new CategoryAdapter(getActivity(), mDatabaseHelper.getAllCategories(), mDatabaseHelper, HomeFragment.this, getActivity().getSupportFragmentManager());
        rvCategory.setAdapter(mCategoryAdapter);

        return layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlOverview:
                mStarterInterface.startOverviewPagerFragment();
                break;
            case R.id.ivCalendar:
                mStarterInterface.startCalendarFragment();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStarterInterface = (StarterInterface)context;
    }

    @Override
    public void updateCompleteList() {
        mCategoryAdapter = new CategoryAdapter(getActivity(), mDatabaseHelper.getAllCategories(), mDatabaseHelper, HomeFragment.this, getActivity().getSupportFragmentManager());
        rvCategory.setAdapter(mCategoryAdapter);
    }
}