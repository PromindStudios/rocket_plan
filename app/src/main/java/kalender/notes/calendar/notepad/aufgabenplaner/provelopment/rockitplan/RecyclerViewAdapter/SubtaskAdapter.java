package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Subtask;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditSubtaskDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.SubtaskFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 26.05.2016.
 */
public class SubtaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SubtaskFragment.ItemTouchHelperAdapter{

    private final int TYPE_ADD = 0;
    private final int TYPE_SUBTASK = 1;

    Context mContext;
    DatabaseHelper mDatabaseHelper;
    ArrayList<Subtask> mSubtasks;
    LayoutInflater mLayoutInflater;
    Fragment mFragment;
    int mTaskId;
    CategoryColor mCategoryColor;
    FragmentManager mFragmentManager;
    Category mCategory;


    public SubtaskAdapter(Context context, ArrayList<Subtask> subtasks, DatabaseHelper databaseHelper, SubtaskFragment subtaskTaskFragment, int taskId, FragmentManager fragmentManager, Category category) {
        mContext = context;
        mDatabaseHelper = databaseHelper;
        mCategory = category;
        mCategoryColor = new CategoryColor(context, category.getColor());
        mSubtasks = subtasks;
        mLayoutInflater = LayoutInflater.from(mContext);
        mFragment = (Fragment)subtaskTaskFragment;
        mTaskId = taskId;
        mFragmentManager = fragmentManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SUBTASK) {
            View view = mLayoutInflater.inflate(R.layout.item_subtask, parent, false);
            myViewHolder holder = new myViewHolder(view, new myViewHolder.myViewHolderClickListener() {
                @Override
                public void editSubtask(int position) {
                    int categoryId = mDatabaseHelper.getTask(mTaskId).getCategoryId();
                    Subtask subtask = mSubtasks.get(position);
                    DialogFragment dialog = new AddEditSubtaskDialog();
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
                    Subtask subtask = mSubtasks.get(position);
                    mDatabaseHelper.deleteSubtask(subtask.getId());
                    mSubtasks.remove(position);
                    notifyDataSetChanged();
                }

                @Override
                public void checkSubtask(int position, boolean isChecked) {
                    Subtask subtask = mSubtasks.get(position);
                    subtask.setDone(isChecked);
                    mDatabaseHelper.updateSubtask(subtask);
                    try {
                        notifyItemChanged(position);
                    } catch (IllegalStateException e) {
                        // no update at this position
                    }

                }
            });
            return holder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            SubtaskAdapter.myViewHolder myHolder = (SubtaskAdapter.myViewHolder)holder;
            Subtask subtask = mSubtasks.get(position);
            myHolder.tvTitle.setText(subtask.getTitle());
            myHolder.cbSubtask.setChecked(subtask.isDone());

            if (subtask.isDone()) {
                myHolder.tvTitle.setPaintFlags(myHolder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                myHolder.tvTitle.setPaintFlags(myHolder.tvTitle.getPaintFlags() & ( ~ Paint.STRIKE_THRU_TEXT_FLAG));
            }

            if (android.os.Build.VERSION.SDK_INT >= 21) {
                myHolder.cbSubtask.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, mCategoryColor.getCategoryColor())));
            } else {
                int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                myHolder.cbSubtask.setButtonDrawable(id);

        }
    }

    @Override
    public int getItemCount() {
        return mSubtasks.size();
    }

    @Override
    public int getItemViewType(int position) {
            return TYPE_SUBTASK;
    }

    public void updateData() {
        ArrayList<Subtask> subtasks = mDatabaseHelper.getAllContentSubtasks(mTaskId);
        mSubtasks.clear();
        mSubtasks.addAll(subtasks);
        notifyDataSetChanged();
        Log.i("CategoryAdapter: ", "Title clicked");
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        /*
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mSubtasks, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mSubtasks, i, i - 1);
            }
        }
        */

        Log.i("EEY"+fromPosition, ""+toPosition);
        Collections.swap(mSubtasks, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        for (Subtask subtask : mSubtasks) {
            subtask.setPosition(mSubtasks.indexOf(subtask));
            mDatabaseHelper.updateSubtask(subtask);
        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    static class myViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, CompoundButton.OnCheckedChangeListener{

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
            tvTitle.setOnClickListener(this);
            cbSubtask.setOnCheckedChangeListener(this);
            ivRemoveSubtask.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivRemoveSubtask:
                    mListener.removeSubtask(getAdapterPosition());
                    break;
                case R.id.tvTitleSubtask:
                    mListener.editSubtask(getAdapterPosition());
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


        public static interface myViewHolderClickListener {
            public void editSubtask(int position);
            public void removeSubtask (int position);
            public void checkSubtask (int position, boolean isChecked);
        }
    }
    }



