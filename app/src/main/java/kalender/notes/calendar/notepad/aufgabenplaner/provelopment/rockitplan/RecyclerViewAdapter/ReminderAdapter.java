package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Reminder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditCategoryDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.ReminderDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.GeneralFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 16.08.2016.
 */
public class ReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // ViewHolder Types
    private final int TYPE_REMINDER = 0;
    private final int TYPE_REMINDER_ADD = 1;

    Context mContext;
    ArrayList<Reminder> mReminders;
    LayoutInflater mLayoutInflater;
    DatabaseHelper mDatabaseHelper;
    FragmentManager mFragmentManager;
    GeneralFragment mGeneralFragment;

    int mContentId;
    int mContentType;
    int mCategoryId;

    public ReminderAdapter(Context context, ArrayList<Reminder> reminders, int contentId, int contentType, FragmentManager fragmentManager, GeneralFragment fragment, int categoryId) {
        mContext = context;
        mReminders = reminders;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatabaseHelper = new DatabaseHelper(mContext);
        mContentId = contentId;
        mContentType = contentType;
        mFragmentManager = fragmentManager;
        mGeneralFragment = fragment;
        mCategoryId = categoryId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_REMINDER) {
            View view = mLayoutInflater.inflate(R.layout.item_reminder, parent, false);
            reminderHolder holder = new reminderHolder(view, new reminderHolder.reminderHolderClickListener() {
                @Override
                public void onEdit(int position) {
                    Log.i("Es geht los", "Jaaa");
                    Reminder reminder = mReminders.get(position);
                    if (mDatabaseHelper.existsReminder(reminder.getId())) {
                        DialogFragment dialog_reminder = new ReminderDialog();
                        Bundle bundle = new Bundle();
                        bundle.putInt(MyConstants.REMINDER_TYPE, reminder.getReminderType());
                        bundle.putInt(MyConstants.REMINDER_VALUE, reminder.getReminderValue());
                        bundle.putInt(MyConstants.CATEGORY_ID, mCategoryId);
                        bundle.putInt(MyConstants.REMINDER_ID, reminder.getId());
                        dialog_reminder.setArguments(bundle);
                        dialog_reminder.setTargetFragment(mGeneralFragment, 0);
                        dialog_reminder.show(mFragmentManager, "Reminder_Dialog");
                    } else {
                        addReminder();
                    }

                }

                @Override
                public void onRemove(int position) {
                    mDatabaseHelper.deleteReminder(mReminders.get(position).getId());
                    mReminders = mDatabaseHelper.getAllContentReminders(mContentId, mContentType);
                    notifyDataSetChanged();
                    Log.i("Haaaste", "Remove");
                }
            });
            return holder;
        } else {
            View view = mLayoutInflater.inflate(R.layout.item_reminder_add, parent, false);
            addReminderHolder holder = new addReminderHolder(view, new addReminderHolder.addReminderHolderClickListener() {
                @Override
                public void onAdd(int position) {
                    addReminder();
                }
            });
            return holder;
        }

    }

    public void addReminder() {
        CharSequence[] items = {mContext.getString(R.string.at_due_time), mContext.getString(R.string.reminder_default_one), mContext.getString(R.string.reminder_default_two), mContext.getString(R.string.custom)};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        mGeneralFragment.createReminder(MyConstants.REMINDER_AT_DUE_TIME, 0);
                        mReminders = mDatabaseHelper.getAllContentReminders(mContentId, mContentType);
                        notifyDataSetChanged();
                        break;
                    case 1:
                        mGeneralFragment.createReminder(MyConstants.REMINDER_HOUR, 1);
                        mReminders = mDatabaseHelper.getAllContentReminders(mContentId, mContentType);
                        notifyDataSetChanged();
                        break;
                    case 2:
                        mGeneralFragment.createReminder(MyConstants.REMINDER_DAY, 1);
                        mReminders = mDatabaseHelper.getAllContentReminders(mContentId, mContentType);
                        notifyDataSetChanged();
                        break;
                    case 3:
                        DialogFragment dialog_reminder = new ReminderDialog();
                        Bundle bundle = new Bundle();
                        bundle.putInt(MyConstants.REMINDER_TYPE, 1);
                        bundle.putInt(MyConstants.REMINDER_VALUE, 2);
                        bundle.putInt(MyConstants.CATEGORY_ID, mCategoryId);
                        bundle.putInt(MyConstants.REMINDER_ID, 0);
                        dialog_reminder.setArguments(bundle);
                        dialog_reminder.setTargetFragment(mGeneralFragment, 0);
                        dialog_reminder.show(mFragmentManager, "Reminder_Dialog");
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if (h instanceof addReminderHolder) {

        } else {
            Reminder reminder = mReminders.get(position);
            int reminderType = reminder.getReminderType();
            reminderHolder holder = (reminderHolder) h;
            if (reminderType == MyConstants.REMINDER_AT_DUE_TIME) {
                holder.tvReminder.setText(mContext.getString(R.string.at_due_time));
            } else {
                holder.tvReminder.setText(reminder.getReminderValue() + " " + reminder.getReminderTypeString(mContext) + " " + mContext.getString(R.string.before));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mReminders.size() == 3) {
            return 3;
        } else {
            return mReminders.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mReminders.size() == 3) {
            return TYPE_REMINDER;
        } else {
            if (position == mReminders.size()) {
                return TYPE_REMINDER_ADD;
            } else {
                return TYPE_REMINDER;
            }
        }
    }

    static class reminderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvReminder;
        ImageView ivRemoveReminder;

        reminderHolderClickListener mListener;

        public reminderHolder(View itemView, reminderHolderClickListener listener) {
            super(itemView);
            tvReminder = (TextView) itemView.findViewById(R.id.tvReminder);
            ivRemoveReminder = (ImageView) itemView.findViewById(R.id.ivRemoveReminder);
            mListener = listener;
            tvReminder.setOnClickListener(this);
            ivRemoveReminder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvReminder) {
                mListener.onEdit(getAdapterPosition());
            }
            if (v.getId() == R.id.ivRemoveReminder) {
                mListener.onRemove(getAdapterPosition());
            }
        }

        public interface reminderHolderClickListener {
            public void onEdit(int position);

            public void onRemove(int position);
        }
    }

    static class addReminderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout rlAddReminder;

        addReminderHolderClickListener mListener;

        public addReminderHolder(View itemView, addReminderHolderClickListener listener) {
            super(itemView);
            rlAddReminder = (RelativeLayout) itemView.findViewById(R.id.rlAddReminder);
            mListener = listener;
            rlAddReminder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.rlAddReminder) {
                mListener.onAdd(getAdapterPosition());
            }
        }

        public interface addReminderHolderClickListener {
            public void onAdd(int position);
        }
    }

}