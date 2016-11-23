package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeAdapterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.TimeAdapter;

/**
 * Created by Admin on 15.07.2016.
 */
public class TimeFragment extends ContentTimeCalendarFragment {

    RecyclerView mRecyclerView;
    int mTimeType;
    TimeAdapter mTimeAdapter;

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
        mainActivityListener.colorHead(0);

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
        mainActivityListener = (ContentTimeCalendarFragment.MainActivityListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapterUp();
    }

    protected void setAdapterUp() {
        mTimeAdapter = new TimeAdapter(getActivity(), mTimeType, mMainActivity);
        mAdapterListener = (ContentTimeAdapterInterface)mTimeAdapter;
        mRecyclerView.setAdapter(mTimeAdapter);
        setSwipeFunction(MyConstants.CONTENT_TASK_EVENT, mRecyclerView);
    }
}
