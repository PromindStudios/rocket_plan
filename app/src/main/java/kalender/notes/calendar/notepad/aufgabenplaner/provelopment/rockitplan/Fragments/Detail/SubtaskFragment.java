package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Subtask;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.SubtaskAdapter;

/**
 * Created by eric on 04.05.2016.
 */
public class SubtaskFragment extends Fragment implements AddEditDialog.AddEditDialogListener {

    RecyclerView mRecyclerView;

    GeneralFragment.DetailActivityListener mListener;

    Task mTask;
    DatabaseHelper mDatabaseHelper;
    ArrayList<Subtask> mSubtasks;
    SubtaskAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.tab_subtasks, container, false);

        mTask = mListener.getTask();
        mDatabaseHelper = new DatabaseHelper(getActivity());
        mSubtasks = mDatabaseHelper.getAllContentSubtasks(mTask.getId());
        Log.i("SubtaskTasFragment", Integer.toString(mTask.getId()));
        mAdapter = new SubtaskAdapter(getActivity(), mSubtasks, mDatabaseHelper, SubtaskFragment.this, mTask.getId(), getActivity().getSupportFragmentManager(), mListener.getCategory());
        mRecyclerView = (RecyclerView)layout.findViewById(R.id.rvSubtasks);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (GeneralFragment.DetailActivityListener)context;
    }

    @Override
    public void onUpdateData() {
        mAdapter.updateData();
    }

    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mRecyclerView.getWindowToken(), 0);
    }
}
