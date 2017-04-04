package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Main_Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeAdapterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.TimeAdapter;

/**
 * Created by Admin on 15.07.2016.
 */
public class OverviewFragment extends ContentTimeCalendarFragment {

    RecyclerView mRecyclerView;
    int mTimeType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        mTimeType = bundle.getInt(MyConstants.TIME_TYPE);
        mDatabaseHelper = new DatabaseHelper(getActivity());
        View layout = inflater.inflate(R.layout.fragment_content_time, container, false);
        mRecyclerView = (RecyclerView)layout.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapterUp();

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
         setAdapterUp();
    }


    protected void setAdapterUp() {
        mAdapter = new TimeAdapter(getActivity(), mTimeType, mMainActivity, this);
        mAdapterListener = (ContentTimeAdapterInterface)mAdapter;
        mRecyclerView.setAdapter(mAdapter);
        setSwipeFunction(MyConstants.CONTENT_TASK_EVENT, mRecyclerView);

    }
}
