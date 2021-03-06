package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Subtask;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditSubtaskDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.AnalyticsInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.PremiumInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.Functions;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.SubtaskAdapter;

/**
 * Created by eric on 04.05.2016.
 */
public class SubtasksFragment extends Fragment implements AddEditSubtaskDialog.AddEditDialogListener {

    RecyclerView mRecyclerView;
    RelativeLayout rlSubtaskAdd;

    GeneralFragment.DetailActivityListener mListener;

    Task mTask;
    DatabaseHelper mDatabaseHelper;
    ArrayList<Subtask> mSubtasks;
    SubtaskAdapter mAdapter;

    // Interface
    PremiumInterface mPremiumInterface;
    AnalyticsInterface mAnalyticsInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.tab_subtasks, container, false);

        mTask = (Task)mListener.getContent();

        rlSubtaskAdd = (RelativeLayout)layout.findViewById(R.id.rlSubtaskAdd);
        rlSubtaskAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnalyticsInterface.track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_SUBTASK);
                if (!Functions.PREMIUM_FUNCTION_SUBTASKS ||mPremiumInterface.hasPremium()) {
                    openSubtaskDialog();
                } else {
                    mPremiumInterface.openDialogPremiumFunction(getString(R.string.premium_function), getString(R.string.premium_silver_subtasks), getString(R.string.premium_expired));
                }

            }
        });

        mDatabaseHelper = new DatabaseHelper(getActivity());
        mSubtasks = mDatabaseHelper.getAllContentSubtasks(mTask.getId());
        Log.i("SubtaskTasFragment", Integer.toString(mTask.getId()));
        mAdapter = new SubtaskAdapter(getActivity(), mSubtasks, mDatabaseHelper, SubtasksFragment.this, mTask.getId(), getActivity().getSupportFragmentManager(), mListener.getCategory());
        mRecyclerView = (RecyclerView)layout.findViewById(R.id.rvSubtasks);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        return layout;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (GeneralFragment.DetailActivityListener)context;
        mPremiumInterface = (PremiumInterface)context;
        mAnalyticsInterface = (AnalyticsInterface)context;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onUpdateData() {
        mAdapter.updateData();
    }

    @Override
    public void openSubtaskDialog() {
        int categoryId = mDatabaseHelper.getTask(mTask.getId()).getCategoryId();
        DialogFragment dialog = new AddEditSubtaskDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.DIALOGE_TYPE, MyConstants.DIALOGE_SUBTASK_ADD);
        bundle.putInt(MyConstants.TASK_ID, mTask.getId());
        bundle.putInt(MyConstants.CATEGORY_ID, categoryId);
        dialog.setArguments(bundle);
        dialog.setTargetFragment(SubtasksFragment.this, 0);
        dialog.show(getActivity().getSupportFragmentManager(), "Add_Subtask");
    }

    public void closeKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mRecyclerView.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final SubtasksFragment.ItemTouchHelperAdapter itemTouchHelperAdapter;

        public SimpleItemTouchHelperCallback(SubtasksFragment.ItemTouchHelperAdapter itemTouchHelperAdapte) {
            itemTouchHelperAdapter = itemTouchHelperAdapte;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }
    }

    public interface ItemTouchHelperAdapter {

        boolean onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }

}
