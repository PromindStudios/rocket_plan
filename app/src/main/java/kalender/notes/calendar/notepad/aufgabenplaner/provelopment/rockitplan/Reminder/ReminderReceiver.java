package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Reminder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.PhoneWaker;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 18.08.2016.
 */
public class ReminderReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        if (MyConstants.ALARM_REMINDER.equals(intent.getAction())) {

            PhoneWaker.acquire(context);

            int reminderId = intent.getIntExtra(MyConstants.REMINDER_ID, 0);

            // Delete Reminder
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.deleteReminder(reminderId);
            Log.i("MyAlarm deleted", "");

            Notification notification = intent.getParcelableExtra(MyConstants.REMINDER_NOTIFICATION);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(reminderId, notification);
        }

    }
}
