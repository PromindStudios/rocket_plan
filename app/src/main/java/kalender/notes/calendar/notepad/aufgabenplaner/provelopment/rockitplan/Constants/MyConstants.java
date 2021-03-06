package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants;

/**
 * Created by eric on 16.05.2016.
 */
public class MyConstants {

    // General
    public static final String UPDATE = "Update__";
    public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";
    public static final String TEXT = "text";

    // Main Activity
    public static final String MAIN_ACTIVITY_ACTION_OVERVIEW = "MAIN_ACTIVITY_ACTION_OVERVIEW";
    public static final String MAIN_ACTIVITY_ACTION_SETTINGS = "MAIN_ACTIVITY_ACTION_SETTINGS";

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
    public static final String FIRST_CONTENT = "first_content";

    // Detail
    public static final int DETAIL_GENERAL = 0;
    public static final int DETAIL_FILES = 2;
    public static final int DETAIL_SUBTASK = 1;
    public static final int DETAIL_DETAILS = 1;
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

    // Notification Action
    public static final String NOTIFICATION_ACTION_CHECK_CONTENT = "NOTIFICATION_ACTION_CHECK_CONTENT";
    public static final String NOTIFICATION_ACTION_DISMISS = "NOTIFICATION_ACTION_DISMISS";

    // Status Bar
    public static final int STATUS_BAR_ID = 30000;
    public static final String STATUS_BAR_ACTIVATED = "NOTIFICATION_FIXED_ACTIVATED";
    public static final String STATUS_BAR_INITIATION = "NOTIFICATION_FIXED_INITIATION";
    public static final String STATUS_BAR_ACTION_ADD = "STATUS_BAR_ACTION_ADD";
    public static final String STATUS_BAR_ACTION_VISIBILITY = "STATUS_BAR_ACTION_VISIBILITY";
    public static final String STATUS_BAR_ACTION_SETTINGS = "STATUS_BAR_ACTION_SETTINGS";
    public static final String STATUS_BAR_ACTION_OVERVIEW = "STATUS_BAR_ACTION_OVERVIEW";
    public static final String STATUS_BAR_AGENDA_VISIBLE = "STATUS_BAR_AGENDA_VISIBLE";
    public static final String STATUS_BAR_DAILY_UPDATE = "STATUS_BAR_DAILY_UPDATE";



    // Repetition
    public static final int REPETITION_TYPE_NONE = 0;
    public static final int REPETITION_TYPE_HOUR = 1;
    public static final int REPETITION_TYPE_DAY = 2;
    public static final int REPETITION_TYPE_WEEK = 3;
    public static final int REPETITION_TYPE_MONTH = 4;


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
    public static final String IS_START_CATEGORY = "is_start_category";
    public static final String IS_START_CONTENT = "is_start_content";
    public static final String APP_WIDGET_TAB_SELECTED = "app_widget_tab_selected";
    public static final int APP_WIDGET_TAB_TODAY = 1;
    public static final int APP_WIDGET_TAB_DONE = 2;
    public static final int APP_WIDGET_TAB_TOMORROW = 3;

    // Calendar
    public static final String CALENDAR_OBJECT = "calendar_object";
    public static final String CALENDAR_PAGE = "calendar_page";
    public static final String CALENDAR_YEAR = "calendar_year";
    public static final String CALENDAR_MONTH = "calendar_month";

    // App Widget
    public static final String APP_WIDGET_ITEM_CLICK = "app_widget_item_click";
    public static final String APP_WIDGET_ITEM_CHECK = "app_widget_item_check";
    public static final String APP_WIDGET_LIST_VIEW_EVENT = "app_widget_list_view_event";
    public static final String APP_WIDGET_ITEM_EVENT = "app_widget_item_event";

    // Priority
    public static final int PRIORITY_NORMAL = 0;
    public static final int PRIORITY_HIGH = 1;

    // Dialog
    public static final String DIALOGE_TITLE = "dialoge_title";
    public static final String DIALOGE_CONTENT = "dialoge_content";

    // Requests
    public static final int REQUEST_ACTIVITY_SETTINGS = 1;
    public static final int REQUEST_PURCHASE = 1001;
    public static final int REQUEST_DONATION = 2001;
    public static final int REQUEST_ACTIVITY_DETAIL = 2;
    public static final int REQUEST_VIDEO_RECORD = 3;

    // PREMIUM
    public static final String PREMIUM_SILVER = "rocket_plan_premium_silber";
    public static final String PREMIUM_GOLD = "rocket_plan_premium_gold";
    public static final boolean DEVELOPER_PREMIUM_MODE = false;
    public static final int RESULT_PREMIUM_UPDATE = 8;

    // Image
    public static final String IMAGE_PATH = "image_path";

    // Video
    public static final String VIDEO_PATH = "video_path";

    // Participant
    public static final String PARTICIPANT_ID = "participant_id";

    // Visibility
    public static final String VISIBILITY_DETAILS_CONTENT_PAGER = "visibility_details_content_pager";
    public static final String VISIBILITY_DETAILS_TIME_PAGER = "visibility_details_time_pager";

    // Dialog Introduction
    public static final String DIALOG_INTRODUCTION_IS_START = "dialog_introduction_is_start";

    // Donation Type
    public static final int DONATION_TYPE_ONE = 1;
    public static final int DONATION_TYPE_TWO = 2;
    public static final int DONATION_TYPE_THREE = 3;
}
