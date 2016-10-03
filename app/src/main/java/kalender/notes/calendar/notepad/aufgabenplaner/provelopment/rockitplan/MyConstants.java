package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan;

/**
 * Created by eric on 16.05.2016.
 */
public class MyConstants {

    // Classes
    public static final String DATABASE_HELPER = "Database Helper: ";

    // Activities
    public static final String MAIN_ACTIVITY = "Main Activity: ";
    public static final String DETAIL_ACTIVITY = "Detail Activity: ";
    public static final int REQUEST_CODE_UPDATE = 4;

    // Fragments
    public static final String GERNERAL_TASK_FRAGMENT = "General task fragment: ";

    // Dialog
    public static final String DIALOGE_TYPE = "dialoge_type";
    public static final int DIALOGE_CATEGORY_ADD = 0;
    public static final int DIALOGE_CATEGORY_EDIT = 1;
    public static final int DIALOGE_SUBTASK_ADD = 2;
    public static final int DIALOGE_SUBTASK_EDIT = 3;

    // Category
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";

    // Task
    public static final String TASK_ID = "task_id";
    public static final String TASK = "task";
    public static final String TASK_DATE = "task_date";
    public static final String TASK_TIME = "task_time";
    public static final int TASK_UNDONE = 0;
    public static final int TASK_DONE = 1;

    // TaskEvent
    public static final String TASK_EVENT_TIME = "task_event_time";

    // Subtask
    public static final String SUBTASK_ID = "subtask_id";

    // Content
    public static final String CONTENT_ID = "content_id";
    public static final String CONTENT_TYPE = "content_type";
    public static final int CONTENT_TASK = 0;
    public static final int CONTENT_EVENT = 1;
    public static final int CONTENT_NOTE = 2;
    public static final int CONTENT_TASK_EVENT = 3;
    public static final int REQUEST_CODE_IMAGE = 8;
    public static final String ALARM_REMINDER = "alarm_reminder";
    public static final String INTENT_REMINDER = "intent_reminder";
    public static final String IS_EXPANDED = "is_expanded";

    // Detail
    public static final int DETAIL_GENERAL = 0;
    public static final int DETAIL_FILES = 1;
    public static final int DETAIL_SUBTASK = 2;
    public static final String DETAIL_TYPE = "detail_type";

    // Time
    public static final String TIME_TYPE = "time_type";
    public static final int TIME_DAY = 0;
    public static final int TIME_WEEK = 1;
    public static final int TIME_MONTH = 2;

    // ContentTime
    public static final String CONTENT_TIME_TYPE = "content_time_type";
    public static final int CONTENT_TIME_CONTENT = 0;
    public static final int CONTENT_TIME_TIME = 1;

    // Alarm
    public static final String IS_ALARM_SET = "is_alarm_set";

    // Reminder
    public static final int REMINDER_AT_DUE_TIME = -1;
    public static final int REMINDER_MINUTE = 0;
    public static final int REMINDER_HOUR = 1;
    public static final int REMINDER_DAY = 2;
    public static final int REMINDER_WEEK = 3;
    public static final String REMINDER_TYPE = "reminder_type";
    public static final String REMINDER_VALUE = "reminder_value";
    public static final String REMINDER_ID = "reminder_id";
    public static final String REMINDER_NOTIFICATION = "reminder_notification";
    public static final String REMINDER_FROM = "reminder_from";


    // Shared Preferences
    public static final String SHARED_PREFERENCES = "shared_preferences_rocketplan";
    public static final String IS_EX_TIME_DAY_THIS = "is_ex_time_day_this";
    public static final String IS_EX_TIME_DAY_NEXT = "is_ex_time_day_next";
    public static final String IS_EX_TIME_DAY_DONE = "is_ex_time_day_done";
    public static final String IS_EX_TIME_WEEK_THIS = "is_ex_time_week_this";
    public static final String IS_EX_TIME_WEEK_NEXT = "is_ex_time_week_next";
    public static final String IS_EX_TIME_WEEK_DONE = "is_ex_time_week_done";
    public static final String IS_EX_TIME_MONTH_THIS = "is_ex_time_month_this";
    public static final String IS_EX_TIME_MONTH_NEXT = "is_ex_time_month_next";
    public static final String IS_EX_TIME_MONTH_DONE = "is_ex_time_month_done";


}