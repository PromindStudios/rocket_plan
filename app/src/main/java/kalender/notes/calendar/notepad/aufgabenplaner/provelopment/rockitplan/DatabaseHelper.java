package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Reminder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Subtask;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Reminder.ReminderSetter;

import static android.R.attr.id;
import static kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R.string.events;
import static kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R.string.tasks;

/**
 * Created by eric on 05.05.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION_1 = 1; // first
    private static final int DATABASE_VERSION_2 = 2; // 28.05.16 - updating subtask table
    private static final int DATABASE_VERSION_3 = 3; // 19.10.16 - updating Category Table --> CATEGORY_EXPANDED

    // Database & Table Names
    private static final String DATABASE_NAME = "database_rocketplan";
    private static final String TABLE_CATEGORY = "todos";
    private static final String TABLE_TASK = "table_task";
    private static final String TABLE_EVENT = "table_event";
    private static final String TABLE_NOTE = "table_note";
    private static final String TABLE_SUBTASK = "table_subtask";
    private static final String TABLE_REMINDER = "table_reminder";

    // Common column names
    private static final String ID = "id";
    private static final String ID_CATEGORY = "id_category";
    private static final String ID_TASK = "id_task";
    private static final String ID_CONTENT = "id_content";
    private static final String CATEGORY = "category";
    private static final String TITLE = "title";
    private static final String SUBTITLE = "subtitle";
    private static final String PRIORITY = "priority";
    private static final String DESCRIPTION = "description";
    private static final String FILE_PICTURE = "file_picture";
    private static final String FILE_VIDEO = "file_video";
    private static final String FILE_VOICE = "file_voice";
    private static final String CONTENT_DONE = "content_done";
    private static final String TYPE_CONTENT = "type_content";

    // Category column names
    private static final String CATEGORY_COLOR = "category_color";
    private static final String CATEGORY_EXPANDED = "category_expanded";
    private static final String SHOW_TASK_DONE = "show_task_done";
    private static final String SHOW_EVENT_DONE = "show_event_done";


    // TaskEvent column names
    private static final String TASK_EVENT_DATE = "event_task_date";
    private static final String TASK_EVENT_TIME = "event_task_time";
    private static final String TASK_EVENT_DATE_END = "event_task_date_end";
    private static final String TASK_EVENT_TIME_END = "event_task_time_end";
    private static final String TASK_EVENT_REPETITION_TYPE = "task_event_repetition_type";
    private static final String TASK_EVENT_REPETITION_VALUE = "task_event_repetition_value";
    private static final String TASK_EVENT_LOCATION_LONGITUDE = "task_event_location_longitude";
    private static final String TASK_EVENT_LOCATION_LATITUDE = "task_event_location_latitude";

    // Reminder column names
    private static final String TYPE_REMINDER = "type_reminder";
    private static final String VALUE_REMINDER = "value_reminder";

    Context mContext;


    // Task column names

    // Event column names

    private static final String EVENT_DATETIME_END = "event_datetime_end";
    private static final String EVENT_LOCATION = "event_location";

    // Subtask column names
    private static final String SUBTASK_DONE = "subtask_done";

    // Table Create Statements
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT," + SHOW_TASK_DONE + " INTEGER," + SHOW_EVENT_DONE + " INTEGER," + CATEGORY_EXPANDED + " INTEGER," + CATEGORY_COLOR + " INTEGER" + ")";

    private static final String CREATE_TABLE_TASK = "CREATE TABLE " + TABLE_TASK +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ID_CATEGORY + " INTEGER," + CATEGORY + " TEXT," + TITLE + " TEXT," + SUBTITLE + " TEXT," + TASK_EVENT_DATE + " INTEGER," + TASK_EVENT_TIME + " INTEGER," +
            TASK_EVENT_DATE_END + " INTEGER," + TASK_EVENT_TIME_END + " INTEGER," + TASK_EVENT_REPETITION_TYPE + " INTEGER," + TASK_EVENT_REPETITION_VALUE + " INTEGER," + PRIORITY + " INTEGER," +
            TASK_EVENT_LOCATION_LONGITUDE + " INTEGER," + TASK_EVENT_LOCATION_LATITUDE + " INTEGER," + DESCRIPTION + " TEXT," + FILE_PICTURE + " TEXT," + FILE_VIDEO + " TEXT," + FILE_VOICE + " TEXT," + CONTENT_DONE + " INTEGER" + ")";

    private static final String CREATE_TABLE_EVENT = "CREATE TABLE " + TABLE_EVENT +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ID_CATEGORY + " INTEGER," + CATEGORY + " TEXT," + TITLE + " TEXT," + SUBTITLE + " TEXT," + TASK_EVENT_DATE + " INTEGER," + TASK_EVENT_TIME + " INTEGER," +
            TASK_EVENT_DATE_END + " INTEGER," + TASK_EVENT_TIME_END + " INTEGER," + TASK_EVENT_REPETITION_TYPE + " INTEGER," + TASK_EVENT_REPETITION_VALUE + " INTEGER," + PRIORITY + " INTEGER," +
            TASK_EVENT_LOCATION_LONGITUDE + " INTEGER," + TASK_EVENT_LOCATION_LATITUDE + " INTEGER," + DESCRIPTION + " TEXT," + FILE_PICTURE + " TEXT," + FILE_VIDEO + " TEXT," + FILE_VOICE + " TEXT," + CONTENT_DONE + " INTEGER" + ")";

    private static final String CREATE_TABLE_NOTE = "CREATE TABLE " + TABLE_NOTE +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ID_CATEGORY + " INTEGER," + CATEGORY + " TEXT," + TITLE + " TEXT," + SUBTITLE + " TEXT," +
            PRIORITY + " INTEGER," + DESCRIPTION + " TEXT," + FILE_PICTURE + " TEXT," + FILE_VIDEO + " TEXT," + FILE_VOICE + " TEXT" + ")";

    private static final String CREATE_TABLE_SUBTASK = "CREATE TABLE " + TABLE_SUBTASK +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ID_CONTENT + " INTEGER," + TITLE + " TEXT," + SUBTASK_DONE + " INTEGER" + ")";

    private static final String CREATE_TABLE_REMINDER = "CREATE TABLE " + TABLE_REMINDER +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ID_CONTENT + " INTEGER," + TYPE_CONTENT + " INTEGER," + TYPE_REMINDER + " INTEGER," + VALUE_REMINDER + " INTEGER" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION_3);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) ;
        if (oldVersion < 3) {
            String upgradeQuery = "ALTER TABLE " + TABLE_CATEGORY + " ADD " + CATEGORY_EXPANDED + " INTEGER";
            db.execSQL(upgradeQuery);
        }
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_NOTE);
        db.execSQL(CREATE_TABLE_SUBTASK);
        db.execSQL(CREATE_TABLE_REMINDER);
    }

    private void deleteAllTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBTASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
    }

    // Category methods

    public void createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, category.getTitle());
        values.put(CATEGORY_COLOR, category.getColor());
        values.put(SHOW_TASK_DONE, category.isShowTaskDone() ? 1 : 0);
        values.put(SHOW_EVENT_DONE, category.isShowEventDone() ? 1 : 0);
        values.put(CATEGORY_EXPANDED, category.isExpanded() ? 1 : 0);
        db.insert(TABLE_CATEGORY, null, values);
    }

    public Category getCategory(int category_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + ID + " = " + category_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Category category = new Category();
        category.setId(c.getInt(c.getColumnIndex(ID)));
        category.setTitle(c.getString(c.getColumnIndex(TITLE)));
        category.setColor(c.getInt(c.getColumnIndex(CATEGORY_COLOR)));
        category.setShowTaskDone(c.getInt(c.getColumnIndex(SHOW_TASK_DONE)) == 1);
        category.setShowEventDone(c.getInt(c.getColumnIndex(SHOW_EVENT_DONE)) == 1);
        category.setExpanded(c.getInt(c.getColumnIndex(CATEGORY_EXPANDED)) == 1);

        return category;
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(ID)));
                category.setTitle(c.getString(c.getColumnIndex(TITLE)));
                category.setColor(c.getInt(c.getColumnIndex(CATEGORY_COLOR)));
                category.setShowTaskDone(c.getInt(c.getColumnIndex(SHOW_TASK_DONE)) == 1);
                category.setShowEventDone(c.getInt(c.getColumnIndex(SHOW_EVENT_DONE)) == 1);
                category.setExpanded(c.getInt(c.getColumnIndex(CATEGORY_EXPANDED)) == 1);
                categories.add(category);
            } while (c.moveToNext());
        }
        Log.i("Siize:", Integer.toString(categories.size()));
        return categories;
    }

    public void updateCategory(Category category) {
        Log.i("Es wird: ", "geupdated");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, category.getTitle());
        values.put(CATEGORY_COLOR, category.getColor());
        //values.put(CATEGORY_EXPANDED, category.isExpanded() ? 1 : 0);
        db.update(TABLE_CATEGORY, values, ID + " =?", new String[]{String.valueOf(category.getId())});
    }


    public void updateCategoryExpanded(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_EXPANDED, category.isExpanded() ? 1 : 0);
        db.update(TABLE_CATEGORY, values, ID + " =?", new String[]{String.valueOf(category.getId())});
    }


    public void updateCategoryShow(int categoryId, int contentType, boolean expanded) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("Updadde: ", Integer.toString(categoryId));

        ContentValues values = new ContentValues();

        if (contentType == MyConstants.CONTENT_TASK) {
            values.put(SHOW_TASK_DONE, expanded ? 1 : 0);
        } else {
            values.put(SHOW_EVENT_DONE, expanded ? 1 : 0);
        }
        db.update(TABLE_CATEGORY, values, ID + " =?", new String[]{String.valueOf(categoryId)});
    }

    public void deleteCategory(Category category) {
        SQLiteDatabase db = getWritableDatabase();
        int id = category.getId();
        db.delete(TABLE_CATEGORY, ID + " = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_EVENT, ID_CATEGORY + " = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_NOTE, ID_CATEGORY + " = ?", new String[]{String.valueOf(id)});
        ArrayList<Content> tasks = new ArrayList<>();
        tasks = getAllCategoryTasks(id);
        for (Content task : tasks) {
            deleteContent(task.getId(), MyConstants.CONTENT_TASK);
        }
    }

    public void deleteAllCategories() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CATEGORY, null, null);
    }

    // Task Methods

    public int createTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_CATEGORY, task.getCategoryId());
        values.put(CATEGORY, task.getCategory());
        values.put(TITLE, task.getTitle());
        values.put(SUBTITLE, task.getSubtitle());
        if (task.getDate() == null) {
            Log.i(MyConstants.DATABASE_HELPER, "create, Task ist null");
            values.put(TASK_EVENT_DATE, 0);
        } else {
            Log.i(MyConstants.DATABASE_HELPER, "create, Task ist nicht null");
            values.put(TASK_EVENT_DATE, task.getDate().getTimeInMillis());
        }
        if (task.getTime() == null) {
            values.put(TASK_EVENT_TIME, 0);
        } else {
            values.put(TASK_EVENT_TIME, task.getTime().getTimeInMillis());
        }
        values.put(PRIORITY, task.getPriority());
        values.put(DESCRIPTION, task.getDescription());
        values.put(FILE_PICTURE, task.getPicturePath());
        values.put(CONTENT_DONE, task.isDone() ? 1 : 0);
        values.put(TASK_EVENT_REPETITION_TYPE, task.getRepetitionType());
        values.put(TASK_EVENT_REPETITION_VALUE, task.getRepetitionValue());

        long task_id = db.insert(TABLE_TASK, null, values);

        return (int) task_id;
    }

    public Content getTask(int task_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TASK + " WHERE " + ID + " = " + task_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Task task = new Task();
        task.setId(c.getInt(c.getColumnIndex(ID)));
        task.setCategoryId(c.getInt(c.getColumnIndex(ID_CATEGORY)));
        task.setCategory(c.getString(c.getColumnIndex(CATEGORY)));
        task.setTitle(c.getString(c.getColumnIndex(TITLE)));
        task.setSubtitle(c.getString(c.getColumnIndex(SUBTITLE)));
        if (c.getLong(c.getColumnIndex(TASK_EVENT_DATE)) == 0) {
            task.setDate(null);
        } else {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(c.getLong(c.getColumnIndex(TASK_EVENT_DATE)));
            task.setDate(date);
        }
        if (c.getLong(c.getColumnIndex(TASK_EVENT_TIME)) == 0) {
            task.setTime(null);
        } else {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(c.getLong(c.getColumnIndex(TASK_EVENT_TIME)));
            task.setTime(date);
        }
        task.setPriority(c.getInt(c.getColumnIndex(PRIORITY)));
        task.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
        task.setPicturePath(c.getString(c.getColumnIndex(FILE_PICTURE)));
        task.setDone(c.getInt(c.getColumnIndex(CONTENT_DONE)) == 1);
        task.setRepetitionType(c.getInt(c.getColumnIndex(TASK_EVENT_REPETITION_TYPE)));
        task.setRepetitionValue(c.getInt(c.getColumnIndex(TASK_EVENT_REPETITION_VALUE)));
        task.setContentType(MyConstants.CONTENT_TASK);
        task.setTable(TABLE_TASK);

        return task;
    }

    public ArrayList<Content> getAllTasks() {
        ArrayList<Content> tasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return cursorTasks(tasks, c);
    }

    public ArrayList<Content> getAllCategoryTasks(int category_id) {
        ArrayList<Content> tasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASK + " WHERE " + ID_CATEGORY + " = " + category_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return cursorTasks(tasks, c);
    }

    public ArrayList<Content> getAllUndoneTasks(int category_id) {
        ArrayList<Content> tasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASK + " WHERE " + ID_CATEGORY + " = " + category_id + " AND " + CONTENT_DONE + " = " + 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return cursorTasks(tasks, c);
    }

    public ArrayList<Content> getAllDoneTasks(int category_id) {
        ArrayList<Content> tasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASK + " WHERE " + ID_CATEGORY + " = " + category_id + " AND " + CONTENT_DONE + " = " + 1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return cursorTasks(tasks, c);
    }


    private ArrayList<Content> cursorTasks(ArrayList<Content> tasks, Cursor c) {
        if (c.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(c.getInt(c.getColumnIndex(ID)));
                task.setCategoryId(c.getInt(c.getColumnIndex(ID_CATEGORY)));
                task.setCategory(c.getString(c.getColumnIndex(CATEGORY)));
                task.setTitle(c.getString(c.getColumnIndex(TITLE)));
                task.setSubtitle(c.getString(c.getColumnIndex(SUBTITLE)));
                if (c.getLong(c.getColumnIndex(TASK_EVENT_DATE)) == 0) {
                    task.setDate(null);
                } else {
                    Calendar date = Calendar.getInstance();
                    date.setTimeInMillis(c.getLong(c.getColumnIndex(TASK_EVENT_DATE)));
                    Log.i("Datum nach DB", Long.toString(c.getLong(c.getColumnIndex(TASK_EVENT_DATE))));
                    task.setDate(date);
                }
                if (c.getLong(c.getColumnIndex(TASK_EVENT_TIME)) == 0) {
                    task.setTime(null);
                } else {
                    Calendar date = Calendar.getInstance();
                    date.setTimeInMillis(c.getLong(c.getColumnIndex(TASK_EVENT_TIME)));
                    task.setTime(date);
                }
                task.setPriority(c.getInt(c.getColumnIndex(PRIORITY)));
                task.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                task.setPicturePath(c.getString(c.getColumnIndex(FILE_PICTURE)));
                task.setDone(c.getInt(c.getColumnIndex(CONTENT_DONE)) == 1);
                task.setRepetitionType(c.getInt(c.getColumnIndex(TASK_EVENT_REPETITION_TYPE)));
                task.setRepetitionValue(c.getInt(c.getColumnIndex(TASK_EVENT_REPETITION_VALUE)));
                task.setContentType(MyConstants.CONTENT_TASK);
                task.setTable(TABLE_TASK);
                tasks.add(task);
            } while (c.moveToNext());
        }
        Collections.sort(tasks, new TaskEventComparator());
        return tasks;
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUBTITLE, task.getSubtitle());
        values.put(TITLE, task.getTitle());
        values.put(CATEGORY, task.getCategory());
        if (task.getDate() == null) {
            values.put(TASK_EVENT_DATE, 0);
        } else {
            values.put(TASK_EVENT_DATE, task.getDate().getTimeInMillis());
        }
        if (task.getTime() == null) {
            values.put(TASK_EVENT_TIME, 0);
        } else {
            values.put(TASK_EVENT_TIME, task.getTime().getTimeInMillis());
        }
        values.put(PRIORITY, task.getPriority());
        values.put(DESCRIPTION, task.getDescription());
        values.put(FILE_PICTURE, task.getPicturePath());
        values.put(CONTENT_DONE, task.isDone() ? 1 : 0);
        values.put(TASK_EVENT_REPETITION_TYPE, task.getRepetitionType());
        values.put(TASK_EVENT_REPETITION_VALUE, task.getRepetitionValue());

        db.update(TABLE_TASK, values, ID + " =?", new String[]{String.valueOf(task.getId())});
    }

    public void updateTaskCategoryId(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_CATEGORY, task.getCategoryId());

        db.update(TABLE_TASK, values, ID + " =?", new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TASK, ID + " = ?", new String[]{String.valueOf(id)});

        // delete all connected reminders
        deleteContentReminder(id, MyConstants.CONTENT_EVENT);
    }

    public void deleteAllTasks() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TASK, null, null);
    }

    // Event Methods

    public int createEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_CATEGORY, event.getCategoryId());
        Log.i("II : ", Integer.toString(event.getCategoryId()));
        values.put(CATEGORY, event.getCategory());
        values.put(TITLE, event.getTitle());
        values.put(SUBTITLE, event.getSubtitle());
        if (event.getDate() == null) {
            values.put(TASK_EVENT_DATE, 0);
        } else {
            values.put(TASK_EVENT_DATE, event.getDate().getTimeInMillis());
        }
        if (event.getTime() == null) {
            values.put(TASK_EVENT_TIME, 0);
        } else {
            values.put(TASK_EVENT_TIME, event.getTime().getTimeInMillis());
        }
        values.put(PRIORITY, event.getPriority());
        values.put(DESCRIPTION, event.getDescription());
        values.put(FILE_PICTURE, event.getPicturePath());
        values.put(CONTENT_DONE, event.isDone() ? 1 : 0);
        values.put(TASK_EVENT_REPETITION_TYPE, event.getRepetitionType());
        values.put(TASK_EVENT_REPETITION_VALUE, event.getRepetitionValue());

        long event_id = db.insert(TABLE_EVENT, null, values);

        return (int) event_id;
    }

    public Content getEvent(int event_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EVENT + " WHERE " + ID + " = " + event_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Event event = new Event();
        event.setId(c.getInt(c.getColumnIndex(ID)));
        event.setCategoryId(c.getInt(c.getColumnIndex(ID_CATEGORY)));
        event.setCategory(c.getString(c.getColumnIndex(CATEGORY)));
        event.setTitle(c.getString(c.getColumnIndex(TITLE)));
        event.setSubtitle(c.getString(c.getColumnIndex(SUBTITLE)));
        if (c.getLong(c.getColumnIndex(TASK_EVENT_DATE)) == 0) {
            event.setDate(null);
        } else {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(c.getLong(c.getColumnIndex(TASK_EVENT_DATE)));
            event.setDate(date);
        }
        if (c.getLong(c.getColumnIndex(TASK_EVENT_TIME)) == 0) {
            event.setTime(null);
        } else {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(c.getLong(c.getColumnIndex(TASK_EVENT_TIME)));
            event.setTime(date);
        }
        event.setPriority(c.getInt(c.getColumnIndex(PRIORITY)));
        event.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
        event.setPicturePath(c.getString(c.getColumnIndex(FILE_PICTURE)));
        event.setDone(c.getInt(c.getColumnIndex(CONTENT_DONE)) == 1);
        event.setRepetitionType(c.getInt(c.getColumnIndex(TASK_EVENT_REPETITION_TYPE)));
        event.setRepetitionValue(c.getInt(c.getColumnIndex(TASK_EVENT_REPETITION_VALUE)));
        event.setContentType(MyConstants.CONTENT_EVENT);
        event.setTable(TABLE_EVENT);

        return event;
    }

    public ArrayList<Content> getAllEvents() {
        ArrayList<Content> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return cursorEvents(events, c);
    }

    public ArrayList<Content> getAllCategoryEvents(int category_id) {
        Log.i("Heeeee", Integer.toString(category_id));

        ArrayList<Content> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENT + " WHERE " + ID_CATEGORY + " = " + category_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return cursorEvents(events, c);
    }

    public ArrayList<Content> getAllUndoneEvents(int category_id) {
        ArrayList<Content> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENT + " WHERE " + ID_CATEGORY + " = " + category_id + " AND " + CONTENT_DONE + " = " + 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return cursorEvents(events, c);
    }

    public ArrayList<Content> getAllDoneEvents(int category_id) {
        ArrayList<Content> events = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENT + " WHERE " + ID_CATEGORY + " = " + category_id + " AND " + CONTENT_DONE + " = " + 1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        return cursorEvents(events, c);
    }


    private ArrayList<Content> cursorEvents(ArrayList<Content> events, Cursor c) {
        if (c.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(c.getInt(c.getColumnIndex(ID)));
                event.setCategoryId(c.getInt(c.getColumnIndex(ID_CATEGORY)));
                event.setCategory(c.getString(c.getColumnIndex(CATEGORY)));
                event.setTitle(c.getString(c.getColumnIndex(TITLE)));
                event.setSubtitle(c.getString(c.getColumnIndex(SUBTITLE)));
                if (c.getLong(c.getColumnIndex(TASK_EVENT_DATE)) == 0) {
                    event.setDate(null);
                } else {
                    Calendar date = Calendar.getInstance();
                    date.setTimeInMillis(c.getLong(c.getColumnIndex(TASK_EVENT_DATE)));
                    event.setDate(date);
                }
                if (c.getLong(c.getColumnIndex(TASK_EVENT_TIME)) == 0) {
                    event.setTime(null);
                } else {
                    Calendar date = Calendar.getInstance();
                    date.setTimeInMillis(c.getLong(c.getColumnIndex(TASK_EVENT_TIME)));
                    event.setTime(date);
                }
                event.setPriority(c.getInt(c.getColumnIndex(PRIORITY)));
                event.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                event.setPicturePath(c.getString(c.getColumnIndex(FILE_PICTURE)));
                event.setDone(c.getInt(c.getColumnIndex(CONTENT_DONE)) == 1);
                event.setRepetitionType(c.getInt(c.getColumnIndex(TASK_EVENT_REPETITION_TYPE)));
                event.setRepetitionValue(c.getInt(c.getColumnIndex(TASK_EVENT_REPETITION_VALUE)));
                event.setContentType(MyConstants.CONTENT_EVENT);
                event.setTable(TABLE_EVENT);
                events.add(event);
            } while (c.moveToNext());
        }
        Collections.sort(events, new TaskEventComparator());
        return events;
    }

    public void updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Log.i("HH Id: ", Integer.toString(event.getCategoryId()));
        values.put(ID_CATEGORY, event.getCategoryId());
        values.put(CATEGORY, event.getCategory());
        values.put(TITLE, event.getTitle());
        values.put(SUBTITLE, event.getSubtitle());
        if (event.getDate() == null) {
            Log.i(MyConstants.DATABASE_HELPER, "create, Task ist null");
            values.put(TASK_EVENT_DATE, 0);
        } else {
            Log.i(MyConstants.DATABASE_HELPER, "create, Task ist nicht null");
            values.put(TASK_EVENT_DATE, event.getDate().getTimeInMillis());
        }
        if (event.getTime() == null) {
            values.put(TASK_EVENT_TIME, 0);
        } else {
            values.put(TASK_EVENT_TIME, event.getTime().getTimeInMillis());
        }
        values.put(PRIORITY, event.getPriority());
        values.put(DESCRIPTION, event.getDescription());
        values.put(FILE_PICTURE, event.getPicturePath());
        values.put(CONTENT_DONE, event.isDone() ? 1 : 0);
        values.put(TASK_EVENT_REPETITION_TYPE, event.getRepetitionType());
        values.put(TASK_EVENT_REPETITION_VALUE, event.getRepetitionValue());

        db.update(TABLE_EVENT, values, ID + " =?", new String[]{String.valueOf(event.getId())});
    }

    public void updateEventCategoryId(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_CATEGORY, event.getCategoryId());

        db.update(TABLE_EVENT, values, ID + " =?", new String[]{String.valueOf(event.getId())});
    }

    public void deleteEvent(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EVENT, ID + " = ?", new String[]{String.valueOf(id)});

        // delete all connected reminders
        deleteContentReminder(id, MyConstants.CONTENT_EVENT);
    }

    public void deleteAllEvents() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EVENT, null, null);
    }

    // Note Methods

    public int createNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_CATEGORY, note.getCategoryId());
        values.put(CATEGORY, note.getCategory());
        values.put(TITLE, note.getTitle());
        values.put(SUBTITLE, note.getSubtitle());
        values.put(PRIORITY, note.getPriority());
        values.put(DESCRIPTION, note.getDescription());
        values.put(FILE_PICTURE, note.getPicturePath());
        long note_id = db.insert(TABLE_NOTE, null, values);
        return (int) note_id;
    }

    public Content getNote(int note_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NOTE + " WHERE " + ID + " = " + note_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Note note = new Note();
        note.setId(c.getInt(c.getColumnIndex(ID)));
        note.setCategoryId(c.getInt(c.getColumnIndex(ID_CATEGORY)));
        note.setCategory(c.getString(c.getColumnIndex(CATEGORY)));
        note.setTitle(c.getString(c.getColumnIndex(TITLE)));
        note.setSubtitle(c.getString(c.getColumnIndex(SUBTITLE)));
        note.setSubtitle(c.getString(c.getColumnIndex(SUBTITLE)));
        note.setPriority(c.getInt(c.getColumnIndex(PRIORITY)));
        note.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
        note.setPicturePath(c.getString(c.getColumnIndex(FILE_PICTURE)));
        note.setContentType(MyConstants.CONTENT_NOTE);
        note.setTable(TABLE_NOTE);

        return note;
    }

    public ArrayList<Content> getAllNotes() {
        ArrayList<Content> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(c.getInt(c.getColumnIndex(ID)));
                note.setCategoryId(c.getInt(c.getColumnIndex(ID_CATEGORY)));
                note.setCategory(c.getString(c.getColumnIndex(CATEGORY)));
                note.setTitle(c.getString(c.getColumnIndex(TITLE)));
                note.setSubtitle(c.getString(c.getColumnIndex(SUBTITLE)));
                note.setSubtitle(c.getString(c.getColumnIndex(SUBTITLE)));
                note.setPriority(c.getInt(c.getColumnIndex(PRIORITY)));
                note.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                note.setPicturePath(c.getString(c.getColumnIndex(FILE_PICTURE)));
                note.setContentType(MyConstants.CONTENT_NOTE);
                note.setTable(TABLE_NOTE);
                notes.add(note);
            } while (c.moveToNext());
        }
        return notes;
    }

    public ArrayList<Content> getAllCategoryNotes(int category_id) {

        ArrayList<Content> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTE + " WHERE " + ID_CATEGORY + " = " + category_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(c.getInt(c.getColumnIndex(ID)));
                note.setCategoryId(c.getInt(c.getColumnIndex(ID_CATEGORY)));
                note.setCategory(c.getString(c.getColumnIndex(CATEGORY)));
                note.setTitle(c.getString(c.getColumnIndex(TITLE)));
                note.setSubtitle(c.getString(c.getColumnIndex(SUBTITLE)));
                note.setSubtitle(c.getString(c.getColumnIndex(SUBTITLE)));
                note.setPriority(c.getInt(c.getColumnIndex(PRIORITY)));
                note.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                note.setPicturePath(c.getString(c.getColumnIndex(FILE_PICTURE)));
                note.setContentType(MyConstants.CONTENT_NOTE);
                note.setTable(TABLE_NOTE);
                notes.add(note);
            } while (c.moveToNext());
        }
        return notes;
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_CATEGORY, note.getCategoryId());
        values.put(CATEGORY, note.getCategory());
        values.put(TITLE, note.getTitle());
        values.put(SUBTITLE, note.getSubtitle());
        values.put(PRIORITY, note.getPriority());
        values.put(DESCRIPTION, note.getDescription());
        values.put(FILE_PICTURE, note.getPicturePath());
        db.update(TABLE_NOTE, values, ID + " =?", new String[]{String.valueOf(note.getId())});
    }

    public void updateNoteCategoryId(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_CATEGORY, note.getCategoryId());

        db.update(TABLE_NOTE, values, ID + " =?", new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NOTE, ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteAllNotes() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NOTE, null, null);
    }

    // Content Methods

    public int createContent(Content content) {
        int contentId = -2;
        switch (content.getContentType()) {
            case MyConstants.CONTENT_TASK:
                contentId = createTask((Task) content);
                break;
            case MyConstants.CONTENT_EVENT:
                contentId = createTask((Task) content);
                break;
            case MyConstants.CONTENT_NOTE:
                contentId = createTask((Task) content);
                break;
        }
        return contentId;
    }

    public Content getContent(int id, int contentType) {
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                return getTask(id);
            case MyConstants.CONTENT_EVENT:
                return getEvent(id);
            case MyConstants.CONTENT_NOTE:
                return getNote(id);
            default:
                return null;
        }
    }

    public void deleteContent(int id, int contentType) {
        SQLiteDatabase db = getWritableDatabase();
        deletePictures(id, contentType);
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                db.delete(TABLE_TASK, ID + " = ?", new String[]{String.valueOf(id)});
                deleteContentReminder(id, MyConstants.CONTENT_TASK);
                deleteContentSubtask(id);
                break;
            case MyConstants.CONTENT_EVENT:
                db.delete(TABLE_EVENT, ID + " = ?", new String[]{String.valueOf(id)});
                deleteContentReminder(id, MyConstants.CONTENT_EVENT);
                break;
            case MyConstants.CONTENT_NOTE:
                db.delete(TABLE_NOTE, ID + " = ?", new String[]{String.valueOf(id)});
        }

    }

    public void updateContent(Content content) {
        SQLiteDatabase db = this.getWritableDatabase();
        int contentType = content.getContentType();
        ContentValues values = new ContentValues();
        values.put(ID_CATEGORY, content.getCategoryId());
        values.put(CATEGORY, content.getCategory());
        values.put(TITLE, content.getTitle());
        values.put(SUBTITLE, content.getSubtitle());
        values.put(PRIORITY, content.getPriority());
        values.put(DESCRIPTION, content.getDescription());
        values.put(FILE_PICTURE, content.getPicturePath());
        if (contentType == MyConstants.CONTENT_EVENT || contentType == MyConstants.CONTENT_TASK) {
            TaskEvent taskEvent = (TaskEvent) content;
            if (taskEvent.getDate() == null) {
                Log.i(MyConstants.DATABASE_HELPER, "create, Task ist null");
                values.put(TASK_EVENT_DATE, 0);
            } else {
                Log.i(MyConstants.DATABASE_HELPER, "create, Task ist nicht null");
                values.put(TASK_EVENT_DATE, taskEvent.getDate().getTimeInMillis());
            }
            if (taskEvent.getTime() == null) {
                values.put(TASK_EVENT_TIME, 0);
            } else {
                values.put(TASK_EVENT_TIME, taskEvent.getTime().getTimeInMillis());
            }
            values.put(CONTENT_DONE, taskEvent.isDone() ? 1 : 0);
            values.put(TASK_EVENT_REPETITION_TYPE, taskEvent.getRepetitionType());
            values.put(TASK_EVENT_REPETITION_VALUE, taskEvent.getRepetitionValue());
        }
        db.update(content.getTable(), values, ID + " =?", new String[]{String.valueOf(content.getId())});
    }

    // TaskEvent Methods

    public int createRepeatTaskEvent(TaskEvent oldTaskEvent) {
        int id;
        switch (oldTaskEvent.getRepetitionType()) {
            case MyConstants.REPETITION_TYPE_HOUR:
                // geht nur wenn Time auch ausgewählt ist
                if (oldTaskEvent.getTime() != null) {
                    Calendar oldDate = Calendar.getInstance();
                    oldDate.set(Calendar.YEAR, oldTaskEvent.getDate().get(Calendar.YEAR));
                    oldDate.set(Calendar.MONTH, oldTaskEvent.getDate().get(Calendar.MONTH));
                    oldDate.set(Calendar.DAY_OF_MONTH, oldTaskEvent.getDate().get(Calendar.DAY_OF_MONTH));
                    oldDate.set(Calendar.HOUR_OF_DAY, oldTaskEvent.getTime().get(Calendar.HOUR_OF_DAY));
                    oldDate.set(Calendar.MINUTE, oldTaskEvent.getTime().get(Calendar.MINUTE));
                    oldDate.add(Calendar.HOUR_OF_DAY, oldTaskEvent.getRepetitionValue());
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(Calendar.YEAR, oldDate.get(Calendar.YEAR));
                    newDate.set(Calendar.MONTH, oldDate.get(Calendar.MONTH));
                    newDate.set(Calendar.DAY_OF_MONTH, oldDate.get(Calendar.DAY_OF_MONTH));
                    Calendar newTime = Calendar.getInstance();
                    newTime.set(Calendar.HOUR_OF_DAY, oldDate.get(Calendar.HOUR_OF_DAY));
                    newTime.set(Calendar.MINUTE, oldDate.get(Calendar.MINUTE));
                    oldTaskEvent.setDate(newDate);
                    oldTaskEvent.setTime(newTime);
                }
                break;
            case MyConstants.REPETITION_TYPE_DAY:
                Calendar oldDate1 = oldTaskEvent.getDate();
                oldDate1.add(Calendar.DAY_OF_MONTH, oldTaskEvent.getRepetitionValue());
                oldTaskEvent.setDate(oldDate1);
                break;
            case MyConstants.REPETITION_TYPE_WEEK:
                Calendar oldDate2 = oldTaskEvent.getDate();
                oldDate2.add(Calendar.WEEK_OF_YEAR, oldTaskEvent.getRepetitionValue());
                oldTaskEvent.setDate(oldDate2);
                break;
            case MyConstants.REPETITION_TYPE_MONTH:
                Calendar oldDate3 = oldTaskEvent.getDate();
                oldDate3.add(Calendar.MONTH, oldTaskEvent.getRepetitionValue());
                oldTaskEvent.setDate(oldDate3);
                break;
        }
        id = createContent(oldTaskEvent);
        return id;
    }

    public ArrayList<Content> getAllTaskEvents() {
        ArrayList<Content> tasks = new ArrayList<>();
        ArrayList<Content> events = new ArrayList<>();
        ArrayList<Content> taskEvents = new ArrayList<>();
        tasks = getAllTasks();
        events = getAllEvents();
        taskEvents.addAll(tasks);
        taskEvents.addAll(events);

        Collections.sort(taskEvents, new TaskEventComparator());

        return taskEvents;
    }

    // Subtask Methods

    public int createSubtask(Subtask subtask) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_CONTENT, subtask.getContentId());
        values.put(TITLE, subtask.getTitle());
        values.put(SUBTASK_DONE, subtask.isDone() ? 1 : 0);

        long subtask_id = db.insert(TABLE_SUBTASK, null, values);

        return (int) subtask_id;
    }

    public Subtask getSubtask(int subtask_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_SUBTASK + " WHERE " + ID + " = " + subtask_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Subtask subtask = new Subtask();
        subtask.setId(c.getInt(c.getColumnIndex(ID)));
        subtask.setContentId(c.getColumnIndex(ID_CONTENT));
        subtask.setTitle(c.getString(c.getColumnIndex(TITLE)));
        subtask.setDone(c.getInt(c.getColumnIndex(SUBTASK_DONE)) == 1);

        return subtask;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SUBTASK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Subtask subtask = new Subtask();
                subtask.setId(c.getInt(c.getColumnIndex(ID)));
                subtask.setContentId(c.getColumnIndex(ID_CONTENT));
                subtask.setTitle(c.getString(c.getColumnIndex(TITLE)));
                subtask.setDone(c.getInt(c.getColumnIndex(SUBTASK_DONE)) == 1);
                subtasks.add(subtask);
            } while (c.moveToNext());
        }
        return subtasks;
    }

    public ArrayList<Subtask> getAllContentSubtasks(int content_id) {

        ArrayList<Subtask> subtasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SUBTASK + " WHERE " + ID_CONTENT + " = " + content_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Subtask subtask = new Subtask();
                subtask.setId(c.getInt(c.getColumnIndex(ID)));
                subtask.setContentId(c.getColumnIndex(ID_CONTENT));
                subtask.setTitle(c.getString(c.getColumnIndex(TITLE)));
                subtask.setDone(c.getInt(c.getColumnIndex(SUBTASK_DONE)) == 1);
                subtasks.add(subtask);
            } while (c.moveToNext());
        }
        return subtasks;

    }

    public void updateSubtask(Subtask subtask) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, subtask.getTitle());
        values.put(SUBTASK_DONE, subtask.isDone() ? 1 : 0);

        db.update(TABLE_SUBTASK, values, ID + " =?", new String[]{String.valueOf(subtask.getId())});
    }

    public void deleteSubtask(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SUBTASK, ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteContentSubtask(int content_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SUBTASK, ID_CONTENT + " = ?", new String[]{String.valueOf(content_id)});
    }

    public void deleteAllSubtasks() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SUBTASK, null, null);
    }

    // Reminder Methods

    public int createReminder(Reminder reminder, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_CONTENT, reminder.getContentId());
        values.put(TYPE_CONTENT, reminder.getContentType());
        values.put(TYPE_REMINDER, reminder.getReminderType());
        values.put(VALUE_REMINDER, reminder.getReminderValue());

        long reminder_id = db.insert(TABLE_REMINDER, null, values);

        return (int) reminder_id;
    }

    public Reminder getReminder(int reminder_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_REMINDER + " WHERE " + ID + " = " + reminder_id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Reminder reminder = new Reminder();
        reminder.setId(c.getInt(c.getColumnIndex(ID)));
        reminder.setContentId(c.getInt(c.getColumnIndex(ID_CONTENT)));
        reminder.setContentType(c.getInt(c.getColumnIndex(TYPE_CONTENT)));
        reminder.setReminderType(c.getInt(c.getColumnIndex(TYPE_REMINDER)));
        reminder.setReminderValue(c.getInt(c.getColumnIndex(VALUE_REMINDER)));

        return reminder;
    }

    public ArrayList<Reminder> getAllReminders() {
        ArrayList<Reminder> reminders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_REMINDER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setId(c.getInt(c.getColumnIndex(ID)));
                reminder.setContentId(c.getInt(c.getColumnIndex(ID_CONTENT)));
                reminder.setContentType(c.getInt(c.getColumnIndex(TYPE_CONTENT)));
                reminder.setReminderType(c.getInt(c.getColumnIndex(TYPE_REMINDER)));
                reminder.setReminderValue(c.getInt(c.getColumnIndex(VALUE_REMINDER)));
                reminders.add(reminder);
            } while (c.moveToNext());
        }
        return reminders;
    }

    public ArrayList<Reminder> getAllContentReminders(int content_id, int content_type) {
        ArrayList<Reminder> reminders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_REMINDER + " WHERE " + ID_CONTENT + " = " + content_id + " AND " + TYPE_CONTENT + " = " + content_type;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setId(c.getInt(c.getColumnIndex(ID)));
                reminder.setContentId(c.getInt(c.getColumnIndex(ID_CONTENT)));
                reminder.setContentType(c.getInt(c.getColumnIndex(TYPE_CONTENT)));
                reminder.setReminderType(c.getInt(c.getColumnIndex(TYPE_REMINDER)));
                reminder.setReminderValue(c.getInt(c.getColumnIndex(VALUE_REMINDER)));
                reminders.add(reminder);
            } while (c.moveToNext());
        }
        return reminders;
    }

    public void updateReminder(Reminder reminder, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TYPE_REMINDER, reminder.getReminderType());
        values.put(VALUE_REMINDER, reminder.getReminderValue());

        int n = db.update(TABLE_REMINDER, values, ID + " =?", new String[]{String.valueOf(reminder.getId())});
        Log.i("MyRowsAffected", "" + n);

    }

    public void deleteReminder(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_REMINDER, ID + " = ?", new String[]{String.valueOf(id)});
        ReminderSetter.cancelAlarm(mContext, id);
    }

    public void deleteContentReminder(int contentId, int contentType) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Reminder> reminders = getAllContentReminders(contentId, contentType);
        for (int i = 0; i < reminders.size(); i++) {
            ReminderSetter.cancelAlarm(mContext, reminders.get(i).getId());
        }
        db.delete(TABLE_REMINDER, ID_CONTENT + " = ? AND " + TYPE_CONTENT + " = ?", new String[]{String.valueOf(contentId), String.valueOf(contentType)});
    }

    public void deleteAllReminders() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_REMINDER, null, null);
    }

    public boolean existsReminder(int reminderId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_REMINDER + " WHERE " + ID + " = " + reminderId;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0) {
            Log.i("Reminderr: ", "Exists");
            return true;
        } else {
            Log.i("Reminderr: ", "Doesnt Exists");
            return false;
        }
    }

    // Others

    public void deletePictures(int id, int contentType) {
        Content content = null;
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                content = getTask(id);
                break;
            case MyConstants.CONTENT_EVENT:
                content = getEvent(id);
                break;
            case MyConstants.CONTENT_NOTE:
                content = getNote(id);
                break;
        }
        if (content.getPicturePath() != null) {
            File file = new File(content.getPicturePath());
            file.delete();
        }

    }

    private class TaskEventComparator implements Comparator<Content> {

        @Override
        public int compare(Content lhs, Content rhs) {

            TaskEvent lhsC = (TaskEvent) lhs;
            TaskEvent rhsC = (TaskEvent) rhs;

            Calendar dateOne;
            Calendar dateTwo;

            boolean change_one = false;
            boolean change_two = false;
            if (lhsC.getDate() == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2090, 1, 1);
                lhsC.setDate(calendar);
                change_one = true;
            }
            if (rhsC.getDate() == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2091, 1, 1);
                rhsC.setDate(calendar);
                change_two = true;
            }
            dateOne = lhsC.getDate();
            dateTwo = rhsC.getDate();
            if (lhsC.getTime() != null) {
                Calendar time = lhsC.getTime();
                dateOne.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
                dateOne.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
            }
            if (rhsC.getTime() != null) {
                Calendar time = rhsC.getTime();
                dateTwo.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
                dateTwo.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
            }
            int i = dateOne.compareTo(dateTwo);

            if (change_one) {
                lhsC.setDate(null);
            }
            if (change_two) {
                rhsC.setDate(null);
            }
            return i;
        }
    }


}
