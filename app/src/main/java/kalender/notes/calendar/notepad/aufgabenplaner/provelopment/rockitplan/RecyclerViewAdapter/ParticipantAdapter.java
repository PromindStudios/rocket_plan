package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Participant;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Reminder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.ReminderDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 24.02.2017.
 */

public class ParticipantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Participant> mParticipants;
    DatabaseHelper mDatabaseHelper;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public ParticipantAdapter(Context context, int contentId) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatabaseHelper = new DatabaseHelper(context);
        mParticipants = mDatabaseHelper.getAllContentParticipants(contentId);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_participant, parent, false);
        ReminderAdapter.reminderHolder holder = new ReminderAdapter.reminderHolder(view, new ReminderAdapter.reminderHolder.reminderHolderClickListener() {
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
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        Participant participant = mParticipants.get(position);
        participantHolder holder = (participantHolder)h;
        holder.tvParticipant.setText(participant.getName());

    }

    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    static class participantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvParticipant;
        ImageView ivRemoveParticipant;
        ImageView ivParticipant;

        ParticipantAdapter.participantHolder.participantHolderClickListener mListener;

        public participantHolder(View itemView, ParticipantAdapter.participantHolder.participantHolderClickListener listener) {
            super(itemView);
            tvParticipant = (TextView) itemView.findViewById(R.id.tvParticipant);
            ivRemoveParticipant = (ImageView) itemView.findViewById(R.id.ivParticipantRemove);
            ivParticipant = (ImageView) itemView.findViewById(R.id.ivParticipantIcon);
            mListener = listener;
            tvParticipant.setOnClickListener(this);
            ivRemoveParticipant.setOnClickListener(this);
            ivParticipant.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvParticipant || v.getId() == R.id.ivParticipantIcon) {
                mListener.onEdit(getAdapterPosition());
            }
            if (v.getId() == R.id.ivParticipantRemove) {
                mListener.onRemove(getAdapterPosition());
            }
        }

        public interface participantHolderClickListener {
            public void onEdit(int position);
            public void onRemove(int position);
        }
    }

}
