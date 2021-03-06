package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.AppWidget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Subtask;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DateTimeTexter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

import static kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R.string.task;

/**
 * Created by Eric on 13.11.2016.
 */

public class AppWidgetService extends RemoteViewsService{


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class AppWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private ArrayList<Content> mTaskEvents;
        SharedPreferences mSharedPreferences;
        DatabaseHelper mDatabaseHelper;
        CategoryColor mCategoryColor;

        public AppWidgetRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mSharedPreferences = context.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0);
            mDatabaseHelper = new DatabaseHelper(context);
            mCategoryColor = new CategoryColor(mContext);
        }

        @Override
        public void onCreate() {
            mTaskEvents = getContent();
        }

        @Override
        public void onDataSetChanged() {
            mTaskEvents = getContent();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mTaskEvents.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_content_app_widget);
            TaskEvent taskEvent = (TaskEvent)mTaskEvents.get(i);

            // Title
            rv.setTextViewText(R.id.tvTitle, taskEvent.getTitle());

            // Content
            rv.setViewVisibility(R.id.ivContent, View.GONE);

            // Type of Content
            rv.setTextViewText(R.id.tvSubtitle, (taskEvent instanceof Task) ? mContext.getString(task) : mContext.getString(R.string.event));
            rv.setViewVisibility(R.id.tvSubtitle, View.VISIBLE);

            // Repetition
            if (taskEvent.getRepetitionType() != MyConstants.REPETITION_TYPE_NONE) {
                rv.setViewVisibility(R.id.ivRepeat, View.VISIBLE);
            } else {
                rv.setViewVisibility(R.id.ivRepeat, View.GONE);
            }

            // Time
            if (taskEvent.getTime() != null ) {
                rv.setTextViewText(R.id.tvDate, DateTimeTexter.getTimeDay(taskEvent, mContext));
                rv.setViewVisibility(R.id.vSubtitleDateDivider, View.VISIBLE);
            } else {
                rv.setViewVisibility(R.id.vSubtitleDateDivider, View.GONE);
                rv.setViewVisibility(R.id.tvDate, View.GONE);
            }

            // Checkbox
            rv.setViewVisibility(R.id.ivCheckbox, View.VISIBLE);
            Category category = mDatabaseHelper.getCategory(taskEvent.getCategoryId());
            mCategoryColor.setColor(category.getColor());
            Bitmap source;
            if (taskEvent.isDone()) {
                source = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_checkbox_checked_24dp);
            } else {
                source = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_checkbox_24dp);
            }
            int[] priorityColors = new int[]{R.color.colorSecondaryText, R.color.colorSecondaryText, R.color.layout_color_light_five, R.color.layout_color_light_one};
            Bitmap result = changeBitmapColor(source, ResourcesCompat.getColor(mContext.getResources(), priorityColors[taskEvent.getPriority()], null));
            rv.setBitmap(R.id.ivCheckbox, "setImageBitmap", result);

            // Category Color
            rv.setInt(R.id.ivCategory, "setBackgroundColor", ResourcesCompat.getColor(mContext.getResources(), mCategoryColor.getCategoryColorLight(), null));


            // Subtask
            if (taskEvent instanceof Task) {
                ArrayList<Subtask> subtasks = new ArrayList<>();
                subtasks = mDatabaseHelper.getAllContentSubtasks(taskEvent.getId());

                if (subtasks.size() > 0) {
                    rv.setViewVisibility(R.id.ivSubtaskDetails, View.VISIBLE);
                    rv.setViewVisibility(R.id.tvSubtask, View.VISIBLE);
                    ArrayList<Subtask> doneSubtasks = new ArrayList<>();
                    for (Subtask subtask : subtasks) {
                        if (subtask.isDone()) {
                            doneSubtasks.add(subtask);
                        }
                    }
                    String dSubtasks = Integer.toString(doneSubtasks.size());
                    String aSubtasks = Integer.toString(subtasks.size());
                    rv.setTextViewText(R.id.tvSubtask,dSubtasks + " / " + aSubtasks);
                } else {
                    rv.setViewVisibility(R.id.ivSubtaskDetails, View.GONE);
                    rv.setViewVisibility(R.id.tvSubtask, View.GONE);
                }
            } else {
                rv.setViewVisibility(R.id.ivSubtaskDetails, View.GONE);
                rv.setViewVisibility(R.id.tvSubtask, View.GONE);
            }

            // Files
            if (taskEvent.getPicturePath() != null || !taskEvent.getDescription().equals("")) {
                rv.setViewVisibility(R.id.ivFiles, View.VISIBLE);
            } else {
                rv.setViewVisibility(R.id.ivFiles, View.GONE);
            }

            // Attach information to pending intent - Item Click - General

            Bundle bundleClick = new Bundle();
            bundleClick.putString(MyConstants.APP_WIDGET_ITEM_EVENT, MyConstants.APP_WIDGET_ITEM_CLICK);
            bundleClick.putInt(MyConstants.CONTENT_ID, taskEvent.getId());
            bundleClick.putInt(MyConstants.CONTENT_TYPE, taskEvent.getContentType());
            bundleClick.putInt(MyConstants.DETAIL_TYPE, MyConstants.DETAIL_GENERAL);
            Intent intentClick = new Intent();
            intentClick.putExtras(bundleClick);
            rv.setOnClickFillInIntent(R.id.llContent, intentClick);

            // Attach information to pending intent - Item Click - Files

            Bundle bundleClickFiles = new Bundle();
            bundleClickFiles.putString(MyConstants.APP_WIDGET_ITEM_EVENT, MyConstants.APP_WIDGET_ITEM_CLICK);
            bundleClickFiles.putInt(MyConstants.CONTENT_ID, taskEvent.getId());
            bundleClickFiles.putInt(MyConstants.CONTENT_TYPE, taskEvent.getContentType());
            bundleClickFiles.putInt(MyConstants.DETAIL_TYPE, MyConstants.DETAIL_FILES);
            Intent intentClickFiles = new Intent();
            intentClickFiles.putExtras(bundleClickFiles);
            rv.setOnClickFillInIntent(R.id.ivFiles, intentClickFiles);

            // Attach information to pending intent - Item Click - Subtask

            Bundle bundleClickSubtask = new Bundle();
            bundleClickSubtask.putString(MyConstants.APP_WIDGET_ITEM_EVENT, MyConstants.APP_WIDGET_ITEM_CLICK);
            bundleClickSubtask.putInt(MyConstants.CONTENT_ID, taskEvent.getId());
            bundleClickSubtask.putInt(MyConstants.CONTENT_TYPE, taskEvent.getContentType());
            bundleClickSubtask.putInt(MyConstants.DETAIL_TYPE, MyConstants.DETAIL_SUBTASK);
            Intent intentClickSubtask = new Intent();
            intentClickSubtask.putExtras(bundleClickSubtask);
            rv.setOnClickFillInIntent(R.id.llSubtaskDetails, intentClickSubtask);

            // Attach information to pending intent - Item Check

            Bundle bundleCheck = new Bundle();
            bundleCheck.putString(MyConstants.APP_WIDGET_ITEM_EVENT, MyConstants.APP_WIDGET_ITEM_CHECK);
            bundleCheck.putInt(MyConstants.CONTENT_ID, taskEvent.getId());
            bundleCheck.putInt(MyConstants.CONTENT_TYPE, taskEvent.getContentType());
            Intent intentCheck = new Intent();
            intentCheck.putExtras(bundleCheck);
            rv.setOnClickFillInIntent(R.id.ivCheckbox, intentCheck);

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        private ArrayList<Content> getContent() {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            ArrayList<Content> taskEvents = new ArrayList<>();
            taskEvents = mDatabaseHelper.getAllTaskEvents();
            ArrayList<Content> taskEventsToday = new ArrayList<>();

            switch (mSharedPreferences.getInt(MyConstants.APP_WIDGET_TAB_SELECTED, MyConstants.APP_WIDGET_TAB_TODAY)) {
                case MyConstants.APP_WIDGET_TAB_TODAY:
                    for (Content content : taskEvents) {
                        TaskEvent taskEvent = (TaskEvent)content;
                        if (taskEvent.getDate() != null) {
                            int daysTillDueDate = taskEvent.getDaysTillDueDate();
                            if (!taskEvent.isDone()) {
                                if (daysTillDueDate <= 0) {
                                    taskEventsToday.add(taskEvent);
                                }
                            }
                        }
                    }
                    //return mDatabaseHelper.getAllTaskEventsAtDateAndDoneCheck(today, false);
                    Log.i("Show me!", ""+mSharedPreferences.getInt(MyConstants.APP_WIDGET_TAB_SELECTED, MyConstants.APP_WIDGET_TAB_TODAY)+"         "+taskEventsToday.size());
                    return taskEventsToday;
                case MyConstants.APP_WIDGET_TAB_DONE:
                    return mDatabaseHelper.getAllTaskEventsAtDateAndDoneCheck(today, true);
                case MyConstants.APP_WIDGET_TAB_TOMORROW:
                    Calendar tomorrow = Calendar.getInstance();
                    tomorrow.add(Calendar.DAY_OF_MONTH, 1);
                    tomorrow.set(Calendar.HOUR_OF_DAY, 0);
                    tomorrow.set(Calendar.MINUTE, 0);
                    tomorrow.set(Calendar.SECOND, 0);
                    tomorrow.set(Calendar.MILLISECOND, 0);
                    return mDatabaseHelper.getAllTaskEventsAtDateAndDoneCheck(tomorrow, false);
                default:
                    return null;
            }
        }

        private Bitmap changeBitmapColor(Bitmap sourceBitmap, int color) {
            Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                    sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
            Paint p = new Paint();
            ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
            p.setColorFilter(filter);
            Canvas canvas = new Canvas(resultBitmap);
            canvas.drawBitmap(resultBitmap, 0, 0, p);
            return resultBitmap;
        }
    }
}
