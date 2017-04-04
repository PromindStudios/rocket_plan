package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Main_Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeAdapterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.ContentAdapter;

/**
 * Created by eric on 02.05.2016.
 */
public class ContentListFragment extends ContentTimeCalendarFragment  {

    private RecyclerView mRecyclerView;
    int mContentType;
    int mCategoryId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_content_time, container, false);

        Bundle bundle = getArguments();
        mContentType = bundle.getInt(MyConstants.CONTENT_TYPE);
        mCategoryId = bundle.getInt(MyConstants.CATEGORY_ID);
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapterUp();
    }

    protected void setAdapterUp() {
        mAdapter = new ContentAdapter(getActivity(), mCategoryId, mMainActivity, mContentType, this);
        mAdapterListener = (ContentTimeAdapterInterface) mAdapter;
        mRecyclerView.setAdapter(mAdapter);
        setSwipeFunction(mContentType, mRecyclerView);
    }
}
