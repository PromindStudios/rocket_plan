package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DeleteContentDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.TimeAdapter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.DividerViewHolder;

/**
 * Created by Admin on 15.07.2016.
 */
public class TimeFragment extends Fragment implements ContentTimeInterface, DeleteContentDialog.DeleteContentDialogListener{

    RecyclerView mRecyclerView;
    int mTimeType;
    MainActivity mMainActivity;
    ContentFragment.MainActivityListener mainActivityListener;

    DatabaseHelper mDatabaseHelper;

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
    public void onResume() {
        super.onResume();
        Log.i("TimeFragment", "onResume ausgefuehrt");
        setAdapterUp();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
        mainActivityListener = (ContentFragment.MainActivityListener)context;
    }


    @Override
    public void delete(RecyclerView.ViewHolder viewHolder) {
        Content content = mTimeAdapter.getCurrentContent(viewHolder);
        DialogFragment dialog = new DeleteContentDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CONTENT_ID, content.getId());
        bundle.putInt(MyConstants.CONTENT_TYPE, content.getContentType());
        bundle.putInt(MyConstants.CATEGORY_ID, content.getCategoryId());
        bundle.putBoolean(MyConstants.IS_EXPANDED, false);
        dialog.setArguments(bundle);
        dialog.setTargetFragment(TimeFragment.this, 0);
        dialog.show(getActivity().getSupportFragmentManager(), "deleteContentTime");
    }

    @Override
    public void checkUncheck(RecyclerView.ViewHolder viewHolder) {
        boolean isDone = mTimeAdapter.checkUncheckContent(viewHolder);
        if (isDone) {
            setAdapterUp();
        }
    }

    @Override
    public Content getContent(int position) {
        return mTimeAdapter.getCorrectContent(position);
    }

    private void setAdapterUp() {
        mTimeAdapter = new TimeAdapter(getActivity(), mTimeType, mMainActivity);
        mRecyclerView.setAdapter(mTimeAdapter);
        MyMethods.setUpSwipeFunction(MyConstants.CONTENT_TASK_EVENT, mRecyclerView, this, mMainActivity, mTimeAdapter);
    }

    @Override
    public void onDeleteContent(int contentId, int contentType, boolean isExtended) {
        mDatabaseHelper.deleteContent(contentId, contentType);
        setAdapterUp();
    }

    @Override
    public void onUpdateContent(boolean isExpanded) {
        setAdapterUp();
    }
}
