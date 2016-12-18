package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
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
        ArrayList<Content> undoneContent = new ArrayList<>();
        ArrayList<Content> doneContent = new ArrayList<>();
        ArrayList<Content> allContent = new ArrayList<>();
        Category category = mDatabaseHelper.getCategory(mCategoryId);
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                allContent.addAll(mDatabaseHelper.getAllUndoneTasks(mCategoryId, category.isTaskSortedByPriority()));
                allContent.addAll(mDatabaseHelper.getAllDoneTasks(mCategoryId, category.isTaskSortedByPriority()));
                break;
            case MyConstants.CONTENT_EVENT:
                allContent.addAll(mDatabaseHelper.getAllUndoneEvents(mCategoryId, category.isEventSortedByPriority()));
                allContent.addAll(mDatabaseHelper.getAllDoneEvents(mCategoryId, category.isEventSortedByPriority()));
                break;
            case MyConstants.CONTENT_NOTE:
                allContent = mDatabaseHelper.getAllCategoryNotes(mCategoryId, category.isNoteSortedByPriority());
                break;
        }
        return allContent;
    }

    public ArrayList<Content> getAllUndoneContent(int categoryId, int categoryType) {
        Category category = mDatabaseHelper.getCategory(categoryId);
        ArrayList<Content> allContent = new ArrayList<>();
        switch (categoryType) {
            case MyConstants.CONTENT_TASK:
                allContent = mDatabaseHelper.getAllUndoneTasks(categoryId, category.isTaskSortedByPriority());
                break;
            case MyConstants.CONTENT_EVENT:
                allContent = mDatabaseHelper.getAllUndoneEvents(categoryId, category.isEventSortedByPriority());;
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
