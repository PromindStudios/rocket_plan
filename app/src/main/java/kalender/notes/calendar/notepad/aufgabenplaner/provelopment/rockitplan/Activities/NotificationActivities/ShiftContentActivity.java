package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.NotificationActivities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import java.util.ArrayList;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Reminder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DatePickerDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.MyTimePickerDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Reminder.ReminderSetter;

/**
 * Created by Eric on 10.04.2017.
 */

public class ShiftContentActivity extends AppCompatActivity implements DatePickerDialog.DatePickerDialogListener, MyTimePickerDialog.TimePickerDialogListener {

    // Variables
    TaskEvent mTaskEvent;
    DatabaseHelper mDatabaseHelper;
    int reminderValue;
    int reminderType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShiftContentActivity", "onCreate");

        // prepare Window
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // dismiss notification
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(getIntent().getIntExtra(MyConstants.REMINDER_ID, 0));

        // Initiate Variables
        mDatabaseHelper = new DatabaseHelper(this);
        mTaskEvent = (TaskEvent)mDatabaseHelper.getContent(getIntent().getIntExtra(MyConstants.CONTENT_ID, 0), getIntent().getIntExtra(MyConstants.CONTENT_TYPE, 0));
        reminderType = getIntent().getIntExtra(MyConstants.REMINDER_TYPE, 0);
        reminderValue = getIntent().getIntExtra(MyConstants.REMINDER_VALUE, 0);
        Log.i("Reminder, Second", ""+getIntent().getIntExtra(MyConstants.REMINDER_TYPE, 0)+" "+getIntent().getIntExtra(MyConstants.REMINDER_VALUE, 0));

        // Show Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.reminder_shift_due_date));

        builder.setItems(new String[]{getString(R.string.reminder_shift_due_date_day), getString(R.string.reminder_shift_due_date_week), getString(R.string.custom)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        Log.i("YesSS", mTaskEvent.getTitle());
                        mTaskEvent.getDate().add(Calendar.DAY_OF_MONTH, 1);
                        mDatabaseHelper.updateContent(mTaskEvent);
                        finish();
                        break;
                    case 1:
                        mTaskEvent.getDate().add(Calendar.WEEK_OF_MONTH, 1);
                        mDatabaseHelper.updateContent(mTaskEvent);
                        finish();
                        break;
                    case 2:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(MyConstants.TASK_DATE, mTaskEvent.getDate());
                        DialogFragment dialogFragment = new DatePickerDialog();
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getSupportFragmentManager(), "datePicker");
                        break;
                }
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Update Content
        mDatabaseHelper.updateContent(mTaskEvent);

        // Update Reminders
        ArrayList<Reminder> reminders = new ArrayList<>();
        reminders = mDatabaseHelper.getAllContentReminders(mTaskEvent.getId(), mTaskEvent.getContentType());
        for (int i = 0; i < reminders.size(); i++) {
            Reminder reminder = reminders.get(i);
            ReminderSetter.setAlarm(this, reminder, mTaskEvent);
        }

        // Add initial reminder (was deleted in ReminderReceiver)
        Reminder reminder = new Reminder(mTaskEvent.getId(), mTaskEvent.getContentType(), reminderType, reminderValue);
        int reminderId = mDatabaseHelper.createReminder(reminder);
        reminder.setId(reminderId);
        ReminderSetter.setAlarm(this, reminder, mTaskEvent);
    }

    @Override
    public void onDateSelected(Calendar date, boolean timeSelected) {
        mTaskEvent.setDate(date);
        if (!timeSelected) finish();
    }

    @Override
    public void openTimeDialog() {
        Calendar time = mTaskEvent.getTime();
        if (time == null) {
            time = Calendar.getInstance();
            time.set(Calendar.HOUR_OF_DAY, 12);
            time.set(Calendar.MINUTE, 0);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyConstants.TASK_EVENT_TIME, time);
        DialogFragment dialogFragment = new MyTimePickerDialog();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSelected(Calendar time) {
        mTaskEvent.setTime(time);
        finish();
    }
}
