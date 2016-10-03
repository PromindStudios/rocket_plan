package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.DetailActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Reminder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
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
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(taskEvent.getCategory());
        if (taskEvent.getTime() != null) {
            subtext = MyMethods.formatDate(taskEvent.getDate())+" "+context.getString(R.string.at)+" "+MyMethods.formatTime(taskEvent.getTime());
        } else {
            subtext = MyMethods.formatDate(taskEvent.getDate());
        }
        inboxStyle.setSummaryText(subtext);
        Intent resultIntent = new Intent(context, DetailActivity.class);
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

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Log.i("Text", taskEvent.getCategory());
        long[] vibratePattern = {500,500,500};
        Notification notification = mBuilder
                .setSmallIcon(R.drawable.ic_rocket)
                .setAutoCancel(true)
                .setContentTitle(taskEvent.getTitle())
                .setContentText(subtext)
                .setStyle(inboxStyle)
                .setContentIntent(resultPendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_6))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(vibratePattern)
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
