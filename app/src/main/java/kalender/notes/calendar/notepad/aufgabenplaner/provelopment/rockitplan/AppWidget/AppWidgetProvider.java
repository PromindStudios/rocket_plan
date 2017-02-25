package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.AppWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.ChooseCategoryActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.TaskEventActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 13.11.2016.
 */

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    // Constants
    private static final String HOME_CLICKED = "home_clicked";
    private static final String TODAY_CLICKED = "today_clicked";
    private static final String TOMORROW_CLICKED = "tomorrow_clicked";
    private static final String DONE_CLICKED = "done_clicked";
    private static final String ADD_TASK_CLICKED = "add_task_clicked";
    private static final String ADD_EVENT_CLICKED = "add_event_clicked";
    private static final String ADD_NOTE_CLICKED = "add_note_clicked";

    // Variables
    DatabaseHelper mDatabaseHelper;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateWidget(appWidgetManager, context, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Initiate
        mDatabaseHelper = new DatabaseHelper(context);
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        // Handle events
        switch (intent.getAction()) {
            case HOME_CLICKED:
                Intent intentHome = new Intent(context, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intentHome);
                break;
            case TODAY_CLICKED:
                context.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0).edit().putInt(MyConstants.APP_WIDGET_TAB_SELECTED, MyConstants.APP_WIDGET_TAB_TODAY).commit();
                onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetProvider.class)));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvContent);
                break;
            case TOMORROW_CLICKED:
                context.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0).edit().putInt(MyConstants.APP_WIDGET_TAB_SELECTED, MyConstants.APP_WIDGET_TAB_TOMORROW).commit();
                onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetProvider.class)));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvContent);
                break;
            case DONE_CLICKED:
                context.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0).edit().putInt(MyConstants.APP_WIDGET_TAB_SELECTED, MyConstants.APP_WIDGET_TAB_DONE).commit();
                onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetProvider.class)));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvContent);
                break;
            case ADD_TASK_CLICKED:
                addContent(context, MyConstants.CONTENT_TASK);
                break;
            case ADD_EVENT_CLICKED:
                addContent(context, MyConstants.CONTENT_EVENT);
                break;
            case ADD_NOTE_CLICKED:
                addContent(context, MyConstants.CONTENT_NOTE);
                break;
            case MyConstants.APP_WIDGET_LIST_VIEW_EVENT:
                TaskEvent taskEvent = (TaskEvent) mDatabaseHelper.getContent(intent.getIntExtra(MyConstants.CONTENT_ID, 0), intent.getIntExtra(MyConstants.CONTENT_TYPE, 0));
                if (intent.getStringExtra(MyConstants.APP_WIDGET_ITEM_EVENT).equals(MyConstants.APP_WIDGET_ITEM_CHECK)) {
                    // set taskEvent done
                    if (!taskEvent.isDone()) {
                        mDatabaseHelper.checkUncheckTaskEvent(taskEvent);
                        onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetProvider.class)));
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvContent);
                    }
                } else {
                    // open taskEvent
                    Intent intentDetail = new Intent(context, TaskEventActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(MyConstants.CONTENT_TYPE, taskEvent.getContentType());
                    bundle.putInt(MyConstants.CONTENT_ID, taskEvent.getId());
                    bundle.putInt(MyConstants.DETAIL_TYPE, intent.getIntExtra(MyConstants.DETAIL_TYPE, MyConstants.DETAIL_GENERAL));
                    intentDetail.putExtras(bundle);
                    intentDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intentDetail);
                }
                break;
            case MyConstants.UPDATE:
                onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetProvider.class)));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvContent);
        }

        super.onReceive(context, intent);
    }

    private void addContent(Context context, int contentType) {
        Intent intentAddContent = new Intent(context, ChooseCategoryActivity.class);
        intentAddContent.putExtra(MyConstants.CONTENT_TYPE, contentType);
        intentAddContent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intentAddContent);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void updateWidget(AppWidgetManager appWidgetManager, Context context, int[] appWidgetIds) {
        final int n = appWidgetIds.length;
        for (int i = 0; i < n; i++) {
            Intent intentService = new Intent(context, AppWidgetService.class);
            intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intentService.setData(Uri.parse(intentService.toUri(Intent.URI_INTENT_SCHEME)));

            // Initiate RemoteViews
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.appwidget_list_add);

            rv.setEmptyView(R.id.lvContent, R.id.tvNoContent);
            // Set TabIndicator Visibility
            switch (context.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0).getInt(MyConstants.APP_WIDGET_TAB_SELECTED, MyConstants.APP_WIDGET_TAB_TODAY)) {
                case MyConstants.APP_WIDGET_TAB_TODAY:
                    rv.setViewVisibility(R.id.vIndicatorToday, View.VISIBLE);
                    rv.setViewVisibility(R.id.vIndicatorTomorrow, View.INVISIBLE);
                    rv.setViewVisibility(R.id.vIndicatorDone, View.INVISIBLE);
                    rv.setTextViewText(R.id.tvNoContent, context.getString(R.string.tab_one_empty));
                    break;
                case MyConstants.APP_WIDGET_TAB_DONE:
                    rv.setViewVisibility(R.id.vIndicatorToday, View.INVISIBLE);
                    rv.setViewVisibility(R.id.vIndicatorTomorrow, View.INVISIBLE);
                    rv.setViewVisibility(R.id.vIndicatorDone, View.VISIBLE);
                    rv.setTextViewText(R.id.tvNoContent, context.getString(R.string.tab_two_empty));
                    break;
                case MyConstants.APP_WIDGET_TAB_TOMORROW:
                    rv.setViewVisibility(R.id.vIndicatorToday, View.INVISIBLE);
                    rv.setViewVisibility(R.id.vIndicatorTomorrow, View.VISIBLE);
                    rv.setViewVisibility(R.id.vIndicatorDone, View.INVISIBLE);
                    rv.setTextViewText(R.id.tvNoContent, context.getString(R.string.tab_three_empty));
                    break;
            }

            // Layout color
            LayoutColor layoutColor = new LayoutColor(context, mDatabaseHelper.getLayoutColorValue());
            if (layoutColor.getLayoutColorValue() > 0) {
                Log.i("ColorValue: ", ""+layoutColor.getLayoutColorValue());
                rv.setInt(R.id.rlAppWidget, "setBackgroundResource", layoutColor.getLayoutColorId());
                rv.setInt(R.id.llIndicator, "setBackgroundResource", layoutColor.getLayoutColorId());
            }

            // Show ListView
            rv.setRemoteAdapter(R.id.lvContent, intentService);

            // Handle simple Clicks
            rv.setOnClickPendingIntent(R.id.ibHome, getPendingSelfIntent(context, HOME_CLICKED));
            rv.setOnClickPendingIntent(R.id.tvTabToday, getPendingSelfIntent(context, TODAY_CLICKED));
            rv.setOnClickPendingIntent(R.id.tvTabTomorrow, getPendingSelfIntent(context, TOMORROW_CLICKED));
            rv.setOnClickPendingIntent(R.id.tvTabDone, getPendingSelfIntent(context, DONE_CLICKED));
            rv.setOnClickPendingIntent(R.id.rlAddTask, getPendingSelfIntent(context, ADD_TASK_CLICKED));
            rv.setOnClickPendingIntent(R.id.rlAddEvent, getPendingSelfIntent(context, ADD_EVENT_CLICKED));
            rv.setOnClickPendingIntent(R.id.rlAddNote, getPendingSelfIntent(context, ADD_NOTE_CLICKED));

            // Set pending intent for any events in listview
            Intent lvIntent = new Intent(context, AppWidgetProvider.class);
            lvIntent.setAction(MyConstants.APP_WIDGET_LIST_VIEW_EVENT);
            lvIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            lvIntent.setData(Uri.parse(intentService.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent checkPendingIntent = PendingIntent.getBroadcast(context, 0, lvIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.lvContent, checkPendingIntent);

            // Update
            //appWidgetManager.updateAppWidget(appWidgetIds[i], null);
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.lvContent);
        }
    }
}
