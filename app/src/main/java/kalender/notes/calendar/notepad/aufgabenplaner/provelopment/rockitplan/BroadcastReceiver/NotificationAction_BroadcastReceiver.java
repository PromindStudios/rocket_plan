package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BroadcastReceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 14.04.2017.
 */

public class NotificationAction_BroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check content
        if (MyConstants.NOTIFICATION_ACTION_CHECK_CONTENT.equals(intent.getAction())) {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            TaskEvent taskEvent = (TaskEvent)databaseHelper.getContent(intent.getIntExtra(MyConstants.CONTENT_ID, 0), intent.getIntExtra(MyConstants.CONTENT_TYPE, 0));
            if (taskEvent.isDone() == false) {
                databaseHelper.checkUncheckTaskEvent(taskEvent);
            } else {
                Toast.makeText(context, context.getString(R.string.reminder_is_already_checked), Toast.LENGTH_LONG).show();
            }
            notificationManager.cancel(intent.getIntExtra(MyConstants.REMINDER_ID, 0));
        }

        // Dismiss notification and delete it
        if (intent.getAction().equals(MyConstants.NOTIFICATION_ACTION_DISMISS)) {
            notificationManager.cancel(intent.getIntExtra(MyConstants.REMINDER_ID, 0));
        }

    }
}
