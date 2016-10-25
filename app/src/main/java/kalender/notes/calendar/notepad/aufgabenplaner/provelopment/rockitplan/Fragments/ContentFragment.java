package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DeleteContentDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeAdapterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.ContentAdapter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.DividerViewHolder;

/**
 * Created by eric on 02.05.2016.
 */
public class ContentFragment extends ContentTimeFragment{

    private RecyclerView mRecyclerView;
    ContentAdapter mContentAdapter;
    int mContentType;
    int mCategoryId;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    TextView tvHelp;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_content_time, container, false);

        Bundle bundle = getArguments();
        mContentType = bundle.getInt(MyConstants.CONTENT_TYPE);
        mCategoryId = bundle.getInt(MyConstants.CATEGORY_ID);
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mSharedPreferences = getActivity().getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0);
        tvHelp = (TextView)layout.findViewById(R.id.tvHelpContent);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mainActivityListener.colorHead(mCategoryId);

        return layout;
    }



    @Override
    public void onResume() {
        super.onResume();
        boolean isStart = mSharedPreferences.getBoolean(MyConstants.IS_START_CONTENT, true);
        if (isStart) {
            tvHelp.setVisibility(View.VISIBLE);
            mEditor = mSharedPreferences.edit();
            mEditor.putBoolean(MyConstants.IS_START_CONTENT, false).commit();
        } else {
            tvHelp.setVisibility(View.GONE);
            setAdapterUp();
        }
    }

    protected void setAdapterUp() {
        mContentAdapter = new ContentAdapter(getActivity(), mCategoryId, mMainActivity, mContentType);
        mAdapterListener = (ContentTimeAdapterInterface)mContentAdapter;
        mRecyclerView.setAdapter(mContentAdapter);
        setSwipeFunction(mContentType, mRecyclerView);

    }

}
