package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import android.content.Context;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;

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
}
