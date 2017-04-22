package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.StatusBar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.ChooseCategoryActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 15.04.2017.
 */

public class StatusBarManager {

    // Variables
    int tasksToday = 0;
    int eventsToday = 0;
    int contentDoneToday = 0;

    Context mContext;
    DatabaseHelper mDatabaseHelper;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    public StatusBarManager(Context context, DatabaseHelper databaseHelper) {
        mContext = context;
        mDatabaseHelper = databaseHelper;
        mSharedPreferences = mContext.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0);
        mEditor = mSharedPreferences.edit();
    }

    public void update() {

         if (mContext.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0).getBoolean(MyConstants.STATUS_BAR_ACTIVATED, true)) {
             Thread thread = new Thread(new Runnable() {
                 @Override
                 public void run() {
                     tasksToday = 0;
                     eventsToday = 0;
                     contentDoneToday = 0;
                     ArrayList<Content> taskEvents = new ArrayList<>();
                     taskEvents = mDatabaseHelper.getAllTaskEvents();
                     for (Content content : taskEvents) {
                         TaskEvent taskEvent = (TaskEvent)content;
                         if (taskEvent.getDate() != null) {
                             int daysTillDueDate = taskEvent.getDaysTillDueDate();
                             if (!taskEvent.isDone()) {
                                 if (daysTillDueDate <= 0) {
                                     if (taskEvent instanceof Task) {
                                         tasksToday = tasksToday+1;
                                     } else {
                                         eventsToday = eventsToday+1;
                                     }
                                 }
                             } else {
                                 if (daysTillDueDate == 0) {
                                     contentDoneToday = contentDoneToday+1;
                                 }
                             }
                         }
                     }
                     showStatusBar();
                 }
             });
             thread.start();
        }
    }

    public void activate() {
        // Set repeating update to the beginning of every day
        Intent updateIntent = new Intent(mContext, StatusBar_Receiver.class);
        updateIntent.setAction(MyConstants.STATUS_BAR_DAILY_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, updateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE, 1);
        AlarmManager alarm = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        mEditor.putBoolean(MyConstants.STATUS_BAR_ACTIVATED, true);
        mEditor.commit();
        update();
    }

    public void deactivate() {
        //  cancel repeating update
        Intent deleteIntent = new Intent(mContext, StatusBar_Receiver.class);
        deleteIntent.setAction(MyConstants.STATUS_BAR_DAILY_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,0, deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);

        mEditor.putBoolean(MyConstants.STATUS_BAR_ACTIVATED, false);
        mEditor.commit();
        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(MyConstants.STATUS_BAR_ID);
    }

    private void showStatusBar() {
        RemoteViews customLayout = new RemoteViews(mContext.getPackageName(), R.layout.clap_out_menu_agenda);

        // Visibility
        Bitmap iconAdd;
        if (mSharedPreferences.getBoolean(MyConstants.STATUS_BAR_AGENDA_VISIBLE, true)) {
            iconAdd = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_add_24dp);
            customLayout.setImageViewBitmap(R.id.ivAdd, iconAdd);
            customLayout.setViewVisibility(R.id.llAddContent, View.GONE);
            customLayout.setViewVisibility(R.id.ivSettings, View.VISIBLE);
            customLayout.setViewVisibility(R.id.llStatus, View.VISIBLE);
        } else {
            iconAdd = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_remove_18dp);
            customLayout.setImageViewBitmap(R.id.ivAdd, iconAdd);
            customLayout.setViewVisibility(R.id.llAddContent, View.VISIBLE);
            customLayout.setViewVisibility(R.id.ivSettings, View.GONE);
            customLayout.setViewVisibility(R.id.llStatus, View.GONE);
        }

        // Text
        String topString = "Rocket-Plan";
        String taskString = (tasksToday == 1) ? mContext.getString(R.string.task) : mContext.getString(R.string.tasks);
        String eventString = (eventsToday == 1) ? mContext.getString(R.string.event) : mContext.getString(R.string.events);
        if (tasksToday > 0 && eventsToday > 0) {
            topString = Integer.toString(tasksToday)+" "+taskString+" & "+Integer.toString(eventsToday)+" "+eventString+ " "+mContext.getString(R.string.due);
        }
        if (tasksToday > 0 && eventsToday == 0) {
            topString = Integer.toString(tasksToday)+" "+taskString+" "+mContext.getString(R.string.due);
        }
        if (tasksToday == 0 && eventsToday > 0) {
            topString = Integer.toString(eventsToday)+" "+eventString+" "+mContext.getString(R.string.due);
        }
        if (tasksToday == 0 && eventsToday == 0) {
            topString = Integer.toString(eventsToday)+" "+eventString+" "+mContext.getString(R.string.due);
            if (contentDoneToday > 0) {
                topString = mContext.getString(R.string.status_bar_nothing_more_due_today);
            } else {
                topString = mContext.getString(R.string.status_bar_nothing_due_today);
            }
        }
        customLayout.setTextViewText(R.id.tvStatusBarTop, topString);

        if (contentDoneToday > 0) {
            customLayout.setViewVisibility(R.id.tvStatusBarBottom, View.VISIBLE);
            customLayout.setTextViewText(R.id.tvStatusBarBottom, Integer.toString(contentDoneToday)+" "+mContext.getString(R.string.status_bar_already_done));
        } else {
            customLayout.setViewVisibility(R.id.tvStatusBarBottom, View.GONE);
        }

        // Handle clicks

        // Settings
        Intent settingsIntent = Intent.makeRestartActivityTask(new ComponentName(mContext, MainActivity.class));
        settingsIntent.setAction(MyConstants.MAIN_ACTIVITY_ACTION_SETTINGS);
        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        customLayout.setOnClickPendingIntent(R.id.ivSettings, PendingIntent.getActivity(mContext, 4, settingsIntent, PendingIntent.FLAG_ONE_SHOT));

        // Overview - Click on StatusBar
        Intent overviewIntent = Intent.makeRestartActivityTask(new ComponentName(mContext, MainActivity.class));
        overviewIntent.setAction(MyConstants.MAIN_ACTIVITY_ACTION_OVERVIEW);
        overviewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        customLayout.setOnClickPendingIntent(R.id.llStatus, PendingIntent.getActivity(mContext, 5, overviewIntent, PendingIntent.FLAG_ONE_SHOT));

        // Visibility
        Intent visibilityIntent = new Intent(mContext, StatusBar_Receiver.class);
        visibilityIntent.setAction(MyConstants.STATUS_BAR_ACTION_VISIBILITY);
        customLayout.setOnClickPendingIntent(R.id.rlAdd, PendingIntent.getBroadcast(mContext, 6, visibilityIntent, PendingIntent.FLAG_CANCEL_CURRENT));

         // Add Task
        Intent intentAddTask = Intent.makeRestartActivityTask(new ComponentName(mContext, ChooseCategoryActivity.class));
        intentAddTask.putExtra(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_TASK);
        intentAddTask.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentAddTask = PendingIntent.getActivity(mContext, 1, intentAddTask, PendingIntent.FLAG_UPDATE_CURRENT);
        customLayout.setOnClickPendingIntent(R.id.rlAddTask, pendingIntentAddTask);

        // Add Event
        Intent intentAddEvent = Intent.makeRestartActivityTask(new ComponentName(mContext, ChooseCategoryActivity.class));
        intentAddEvent.putExtra(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_EVENT);
        intentAddEvent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentAddEvent = PendingIntent.getActivity(mContext, 2, intentAddEvent, PendingIntent.FLAG_UPDATE_CURRENT);
        customLayout.setOnClickPendingIntent(R.id.rlAddEvent, pendingIntentAddEvent);


        // Add Note
        Intent intentAddNote = Intent.makeRestartActivityTask(new ComponentName(mContext, ChooseCategoryActivity.class));
        intentAddNote.putExtra(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_NOTE);
        intentAddNote.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        customLayout.setOnClickPendingIntent(R.id.rlAddNote, PendingIntent.getActivity(mContext, 3, intentAddNote, PendingIntent.FLAG_UPDATE_CURRENT));


        // Build
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        Notification notification = mBuilder
                .setSmallIcon(R.drawable.ic_rocket)
                .setCustomContentView(customLayout)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MyConstants.STATUS_BAR_ID, notification);
    }
}
