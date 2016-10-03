package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import android.content.Context;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;

/**
 * Created by Admin on 24.06.2016.
 */
public class ContentHelper {

    private int mCategoryId;
    private DatabaseHelper mDatabaseHelper;

    public ContentHelper(Context context, int categoryId) {
        mDatabaseHelper = new DatabaseHelper(context);
        mCategoryId = categoryId;
    }

    public ArrayList<Content> getAllContent(int contentType) {
        ArrayList<Content> allContent = new ArrayList<>();
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                allContent.addAll(mDatabaseHelper.getAllUndoneTasks(mCategoryId));
                allContent.addAll(mDatabaseHelper.getAllDoneTasks(mCategoryId));
                break;
            case MyConstants.CONTENT_EVENT:
                allContent.addAll(mDatabaseHelper.getAllUndoneEvents(mCategoryId));
                allContent.addAll(mDatabaseHelper.getAllDoneEvents(mCategoryId));
                break;
            case MyConstants.CONTENT_NOTE:
                allContent = mDatabaseHelper.getAllCategoryNotes(mCategoryId);
                break;
        }
        return allContent;
    }

    public ArrayList<Content> getAllUndoneContent(int categoryType) {
        ArrayList<Content> allContent = new ArrayList<>();
        switch (categoryType) {
            case MyConstants.CONTENT_TASK:
                ArrayList<Content> tasks = mDatabaseHelper.getAllCategoryTasks(mCategoryId);
                Log.i("all tasks: ", Integer.toString(tasks.size()));
                ArrayList<Task> undoneTasks = new ArrayList<>();
                if (tasks != null) {
                    for (Content content : tasks) {
                        Task task = (Task) content;
                        if (!task.isDone()) {
                            undoneTasks.add(task);
                        }
                    }
                }
                Collections.sort(undoneTasks, new TaskComparator());
                allContent.addAll(undoneTasks);
                Log.i("all content: ", Integer.toString(allContent.size()));
                break;
            case MyConstants.CONTENT_EVENT:
                ArrayList<Content> events = mDatabaseHelper.getAllCategoryEvents(mCategoryId);
                Log.i("JJ: ", Integer.toString(mCategoryId) + " " + events.size());
                ArrayList<Event> undoneEvents = new ArrayList<>();
                if (events != null) {
                    for (Content content : events) {
                        Event event = (Event) content;
                        if (!event.isDone()) {
                            undoneEvents.add(event);
                        }
                    }
                }
                Collections.sort(undoneEvents, new EventComparator());
                allContent.addAll(undoneEvents);
                break;
        }

        return allContent;
    }

    public int getUndoneContentSize(ArrayList<Content> content, int contentType) {
        ArrayList<Content> undoneContent = new ArrayList<>();
        if (contentType == MyConstants.CONTENT_TASK) {
            for (Content c : content) {
                Task task = (Task) c;
                if (!task.isDone()) {
                    undoneContent.add(task);
                }
            }
        } else {
            for (Content c : content) {
                Event event = (Event) c;
                if (!event.isDone()) {
                    undoneContent.add(event);
                }
            }
        }

        return undoneContent.size();
    }

    public int getDoneContentSize(int contentType) {
        int doneContentSize = 0;
        if (contentType == MyConstants.CONTENT_TASK) {
            ArrayList<Content> tasks = mDatabaseHelper.getAllCategoryTasks(mCategoryId);
            ArrayList<Task> doneTasks = new ArrayList<>();
            for (Content c : tasks) {
                Task task = (Task) c;
                if (task.isDone()) {
                    doneTasks.add(task);
                }
            }
            doneContentSize = doneTasks.size();
        } else {
            ArrayList<Content> events = mDatabaseHelper.getAllCategoryEvents(mCategoryId);
            ArrayList<Event> doneEvents = new ArrayList<>();
            for (Content c : events) {
                Event event = (Event) c;
                if (event.isDone()) {
                    doneEvents.add(event);
                }
            }
            doneContentSize = doneEvents.size();
        }
        return doneContentSize;
    }

    public class TaskComparator implements Comparator<Task> {

        @Override
        public int compare(Task lhs, Task rhs) {

            boolean change_one = false;
            boolean change_two = false;
            if (lhs.getDate() == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2090, 1, 1);
                lhs.setDate(calendar);
                change_one = true;
            }
            if (rhs.getDate() == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2091, 1, 1);
                rhs.setDate(calendar);
                change_two = true;
            }
            int i = lhs.getDate().compareTo(rhs.getDate());

            if (change_one) {
                lhs.setDate(null);
            }
            if (change_two) {
                rhs.setDate(null);
            }
            return i;

        }
    }

    public class EventComparator implements Comparator<Event> {

        @Override
        public int compare(Event lhs, Event rhs) {

            boolean change_one = false;
            boolean change_two = false;
            if (lhs.getDate() == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2090, 1, 1);
                lhs.setDate(calendar);
                change_one = true;
            }
            if (rhs.getDate() == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2091, 1, 1);
                rhs.setDate(calendar);
                change_two = true;
            }
            int i = lhs.getDate().compareTo(rhs.getDate());

            if (change_one) {
                lhs.setDate(null);
            }
            if (change_two) {
                rhs.setDate(null);
            }
            return i;

        }
    }


}
