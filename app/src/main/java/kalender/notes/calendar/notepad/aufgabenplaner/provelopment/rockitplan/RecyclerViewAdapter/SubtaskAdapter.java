package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Subtask;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.SubtaskFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 26.05.2016.
 */
public class SubtaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_ADD = 0;
    private final int TYPE_SUBTASK = 1;

    Context mContext;
    DatabaseHelper mDatabaseHelper;
    ArrayList<Subtask> mSubtasks;
    LayoutInflater mLayoutInflater;
    Fragment mFragment;
    int mTaskId;
    FragmentManager mFragmentManager;


    public SubtaskAdapter(Context context, ArrayList<Subtask> subtasks, DatabaseHelper databaseHelper, SubtaskFragment subtaskTaskFragment, int taskId, FragmentManager fragmentManager) {
        mContext = context;
        mDatabaseHelper = databaseHelper;
        mSubtasks = subtasks;
        mLayoutInflater = LayoutInflater.from(mContext);
        mFragment = (Fragment)subtaskTaskFragment;
        mTaskId = taskId;
        mFragmentManager = fragmentManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADD) {
            View view = mLayoutInflater.inflate(R.layout.item_subtask_add, parent, false);
            addSubtaskView holder = new addSubtaskView(view, new addSubtaskView.addSubtaskViewClickListener() {
                @Override
                public void addSubtask(int position) {
                    int categoryId = mDatabaseHelper.getTask(mTaskId).getCategoryId();
                    DialogFragment dialog = new AddEditDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt(MyConstants.DIALOGE_TYPE, MyConstants.DIALOGE_SUBTASK_ADD);
                    bundle.putInt(MyConstants.TASK_ID, mTaskId);
                    bundle.putInt(MyConstants.CATEGORY_ID, categoryId);
                    dialog.setArguments(bundle);
                    dialog.setTargetFragment(mFragment, 0);
                    dialog.show(mFragmentManager, "Add_Subtask");
                }
            });
            return holder;
        }
        if (viewType == TYPE_SUBTASK) {
            View view = mLayoutInflater.inflate(R.layout.item_subtask, parent, false);
            myViewHolder holder = new myViewHolder(view, new myViewHolder.myViewHolderClickListener() {
                @Override
                public void editSubtask(int position) {
                    int categoryId = mDatabaseHelper.getTask(mTaskId).getCategoryId();
                    Subtask subtask = mSubtasks.get(position-1);
                    DialogFragment dialog = new AddEditDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt(MyConstants.DIALOGE_TYPE, MyConstants.DIALOGE_SUBTASK_EDIT);
                    bundle.putInt(MyConstants.SUBTASK_ID, subtask.getId());
                    bundle.putInt(MyConstants.CATEGORY_ID, categoryId);
                    dialog.setArguments(bundle);
                    dialog.setTargetFragment(mFragment, 0);
                    dialog.show(mFragmentManager, "Add_Subtask");
                }

                @Override
                public void removeSubtask(int position) {
                    Subtask subtask = mSubtasks.get(position-1);
                    mDatabaseHelper.deleteSubtask(subtask.getId());
                    mSubtasks.remove(position-1);
                    notifyDataSetChanged();
                }

                @Override
                public void checkSubtask(int position, boolean isChecked) {
                    Subtask subtask = mSubtasks.get(position-1);
                    subtask.setDone(isChecked);
                    mDatabaseHelper.updateSubtask(subtask);
                }
            });
            return holder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            SubtaskAdapter.addSubtaskView myHolder = (SubtaskAdapter.addSubtaskView)holder;

        } else {
            SubtaskAdapter.myViewHolder myHolder = (SubtaskAdapter.myViewHolder)holder;
            Subtask subtask = mSubtasks.get(position-1);
            myHolder.tvTitle.setText(subtask.getTitle());
            myHolder.cbSubtask.setChecked(subtask.isDone());

            if (android.os.Build.VERSION.SDK_INT >= 21) {
                myHolder.cbSubtask.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimary)));
            } else {
                int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                myHolder.cbSubtask.setButtonDrawable(id);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mSubtasks.size()+1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_ADD;
        } else {
            return TYPE_SUBTASK;
        }

    }

    public void updateData() {
        ArrayList<Subtask> subtasks = mDatabaseHelper.getAllContentSubtasks(mTaskId);
        mSubtasks.clear();
        mSubtasks.addAll(subtasks);
        notifyDataSetChanged();
        Log.i("CategoryAdapter: ", "Title clicked");
    }

    static class myViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener{

        TextView tvTitle;
        AppCompatCheckBox cbSubtask;
        AppCompatImageView ivRemoveSubtask;
        myViewHolderClickListener mListener;

        public myViewHolder(View itemView, myViewHolderClickListener listener) {
            super(itemView);
            mListener = listener;
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitleSubtask);
            cbSubtask = (AppCompatCheckBox) itemView.findViewById(R.id.cbSubtask);
            ivRemoveSubtask = (AppCompatImageView)itemView.findViewById(R.id.ivRemoveSubtask);
            tvTitle.setOnLongClickListener(this);
            cbSubtask.setOnCheckedChangeListener(this);
            ivRemoveSubtask.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivRemoveSubtask:
                    mListener.removeSubtask(getAdapterPosition());
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.cbSubtask) {
                mListener.checkSubtask(getAdapterPosition(), isChecked);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.tvTitleSubtask) {
                mListener.editSubtask(getAdapterPosition());
            }
            return false;
        }

        public static interface myViewHolderClickListener {
            public void editSubtask(int position);
            public void removeSubtask (int position);
            public void checkSubtask (int position, boolean isChecked);
        }
    }

    static class addSubtaskView extends RecyclerView.ViewHolder implements View.OnClickListener{

        RelativeLayout rlSubtaskAdd;
        addSubtaskViewClickListener mListener;

        public addSubtaskView(View itemView, addSubtaskViewClickListener listener) {
            super(itemView);
            mListener = listener;
            rlSubtaskAdd = (RelativeLayout)itemView.findViewById(R.id.rlSubtaskAdd);
            rlSubtaskAdd.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mListener.addSubtask(getAdapterPosition());
        }

        public static interface addSubtaskViewClickListener {
            public void addSubtask(int position);
        }
    }


}
