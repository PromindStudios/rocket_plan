package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Reminder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;

/**
 * Created by Admin on 30.06.2016.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        ArrayList<Reminder> reminders = new ArrayList<>();
        reminders = mDatabaseHelper.getAllReminders();
        for (Reminder reminder : reminders) {
            TaskEvent taskEvent = (TaskEvent)mDatabaseHelper.getContent(reminder.getContentId(), reminder.getContentType());
            ReminderSetter.setAlarm(context, reminder, taskEvent);
        }
    }
}
