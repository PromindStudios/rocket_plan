package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.NotificationActivities.ShiftContentActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.TaskEventActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Reminder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BroadcastReceiver.NotificationAction_BroadcastReceiver;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 18.08.2016.
 */
public class ReminderSetter {

    public static void setAlarm(Context context, Reminder r, TaskEvent taskEvent) {

        // Get Content in order to find out when the alarm shall go off
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Reminder reminder = r;
        String subtext;

        // Create Notification
        if (taskEvent.getTime() != null) {
            subtext = MyMethods.formatDate(taskEvent.getDate())+"   |   "+MyMethods.formatTime(taskEvent.getTime());
        } else {
            subtext = MyMethods.formatDate(taskEvent.getDate());
        }
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(subtext);
        inboxStyle.setSummaryText(taskEvent.getCategory());
        Intent resultIntent = new Intent(context, TaskEventActivity.class);
        resultIntent.putExtra(MyConstants.INTENT_REMINDER, true);
        resultIntent.putExtra(MyConstants.CONTENT_ID, taskEvent.getId());
        resultIntent.putExtra(MyConstants.CONTENT_TYPE, r.getContentType());
        resultIntent.putExtra(MyConstants.DETAIL_TYPE, MyConstants.DETAIL_GENERAL);
        resultIntent.putExtra(MyConstants.REMINDER_FROM, true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Custom Layout for Reminder
        RemoteViews customLayout = new RemoteViews(context.getPackageName(), R.layout.item_content_app_widget);

        // Notification icon
        int notificationIcon;
        if (taskEvent.getContentType() == MyConstants.CONTENT_TASK) {
            notificationIcon = R.drawable.ic_task_24dp;
        } else {
            notificationIcon = R.drawable.ic_event_24dp;
        }

        // NotificationColor
        LayoutColor layoutColor = new LayoutColor(context, new DatabaseHelper(context).getLayoutColorValue());
        int notificationColor;
        if (taskEvent.getPriority() == MyConstants.PRIORITY_NORMAL) {
            notificationColor = layoutColor.getLayoutColor();
        } else {
            notificationColor = layoutColor.getLayoutColor();
        }

        // Shift Intent
        Intent shiftIntent = Intent.makeRestartActivityTask(new ComponentName(context, ShiftContentActivity.class));
        shiftIntent.putExtra(MyConstants.CONTENT_ID, taskEvent.getId());
        shiftIntent.putExtra(MyConstants.CONTENT_TYPE, taskEvent.getContentType());
        shiftIntent.putExtra(MyConstants.REMINDER_ID, reminder.getId());
        shiftIntent.putExtra(MyConstants.REMINDER_TYPE, reminder.getReminderType());
        shiftIntent.putExtra(MyConstants.REMINDER_VALUE, reminder.getReminderValue());
        PendingIntent piShift = PendingIntent.getActivity(context, 0, shiftIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Variable Action Intent
        int actionIntentDrawable;
        String actionIntentString;
        PendingIntent actionPendingIntent;
        if (r.getReminderType() == MyConstants.REMINDER_AT_DUE_TIME || (r.getReminderType() == MyConstants.REMINDER_HOUR && r.getReminderValue() <= 24)) {
            // Check
            actionIntentDrawable = R.drawable.ic_check;
            actionIntentString = context.getString(R.string.done);
            Intent actionIntent = new Intent(context, NotificationAction_BroadcastReceiver.class);
            actionIntent.setAction(MyConstants.NOTIFICATION_ACTION_CHECK_CONTENT);
            actionIntent.putExtra(MyConstants.REMINDER_ID, reminder.getId());
            actionIntent.putExtra(MyConstants.CONTENT_TYPE, taskEvent.getContentType());
            actionIntent.putExtra(MyConstants.CONTENT_ID, taskEvent.getId());
            actionPendingIntent = PendingIntent.getBroadcast(context, 0, actionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        } else {
            // Dismiss
            actionIntentDrawable = R.drawable.ic_thumb_18dp_2dp_padding;
            actionIntentString = context.getString(R.string.ok);
            Intent actionIntent = new Intent(context, NotificationAction_BroadcastReceiver.class);
            actionIntent.setAction(MyConstants.NOTIFICATION_ACTION_DISMISS);
            actionIntent.putExtra(MyConstants.REMINDER_ID, reminder.getId());
            actionPendingIntent = PendingIntent.getBroadcast(context, 0, actionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        // Create Actions
        NotificationCompat.Action actionOne = new NotificationCompat.Action.Builder(R.drawable.ic_date_shift_18dp_2dp_padding, context.getString(R.string.reminder_shift), piShift).build();
        NotificationCompat.Action actionTwo = new NotificationCompat.Action.Builder(actionIntentDrawable, actionIntentString, actionPendingIntent).build();


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Log.i("Text", taskEvent.getCategory());
        long[] vibratePattern = {500,500,500};
        Notification notification = mBuilder
                .setSmallIcon(notificationIcon)
                .setColor(notificationColor)
                .setAutoCancel(true)
                .setContentTitle(taskEvent.getTitle())
                .setContentText(subtext)
                .setStyle(inboxStyle)
                .setShowWhen(false)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(resultPendingIntent)
                //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_6))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(vibratePattern)
                .addAction(actionOne)
                .addAction(actionTwo)
                .build();

        // Set Intent and PendingIntent, which will be called the alarm goes off
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction(MyConstants.ALARM_REMINDER);
        intent.putExtra(MyConstants.REMINDER_ID, reminder.getId());
        intent.putExtra(MyConstants.REMINDER_NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.i("II Set", "the alarm: "+reminder.getId());

        // Set Alarm
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, MyMethods.getAlarmTimeInMillis(taskEvent, reminder), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, MyMethods.getAlarmTimeInMillis(taskEvent, reminder), pendingIntent);
        }
    }

    public static void cancelAlarm (Context context, int reminderId) {

        // Set the Pending Intent which will be deleted
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction(MyConstants.ALARM_REMINDER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i("MyAlarm canceled", "");
        // Cancel Alarm
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
