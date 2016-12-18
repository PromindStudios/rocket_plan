package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan;

import android.content.Context;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;

/**
 * Created by Admin on 23.08.2016.
 */
public class DateTimeTexter {

    public static String getGeneral(TaskEvent taskEvent, Context context) {
        String dateTime = "";
        int daysBetween = taskEvent.getDaysTillDueDate();
        String textTime = "";
        if (taskEvent.getTime() != null) {
            textTime = "  |  " + MyMethods.formatTime(taskEvent.getTime());
        }

        boolean isDone = taskEvent.isDone();
        if (daysBetween < 0 && !isDone) {
            dateTime = context.getString(R.string.overdue);
        }
        if (daysBetween == 0) {
            dateTime = context.getString(R.string.today) + textTime;
        }
        if (daysBetween == 1 && !isDone) {
            dateTime = context.getString(R.string.tomorrow) + textTime;
        }
        if (daysBetween > 1 || isDone) {
            dateTime = MyMethods.formatDate(taskEvent.getDate()) + textTime;
        }
        return dateTime;
    }

    public static String getTimeDay(TaskEvent taskEvent, Context context) {
        String dateTime = "";
        int daysBetween = taskEvent.getDaysTillDueDate();
        if (taskEvent.getTime() != null) {
            dateTime = MyMethods.formatTime(taskEvent.getTime());
        }
        if (daysBetween < 0) {
            if (taskEvent.isDone()) {
                dateTime = MyMethods.formatTime(taskEvent.getTime());
            } else {
                dateTime = context.getString(R.string.overdue);
            }

        }

        return dateTime;
    }

    public static String getTimeWeek(TaskEvent taskEvent, Context context) {
        int daysBetween = taskEvent.getDaysTillDueDate();
        String dateTime = "";
        String textTime = "";
        if (taskEvent.getTime() != null) {
            textTime = "  |  " + MyMethods.formatTime(taskEvent.getTime());
        }

        boolean isDone = taskEvent.isDone();
        if (daysBetween < 0 && !isDone) {
            dateTime = context.getString(R.string.overdue);
        }
        if (daysBetween == 0) {
            dateTime = context.getString(R.string.today) + textTime;
        }
        if (daysBetween == 1 && !isDone) {
            dateTime = context.getString(R.string.tomorrow) + textTime;
        }
        if (daysBetween > 1 || isDone) {
            dateTime = MyMethods.formatWeekDay(taskEvent.getDate()) + textTime;
        }
        return dateTime;
    }

    public static String getTimeMonth(TaskEvent taskEvent, Context context) {
        return getGeneral(taskEvent, context);
    }

    public static String getNormal(TaskEvent taskEvent) {
        String dateTime = "";
        String textTime = "";
        if (taskEvent.getTime() != null) {
            textTime = "  |  " + MyMethods.formatTime(taskEvent.getTime());
        }
        dateTime = MyMethods.formatDate(taskEvent.getDate()) + textTime;

        return dateTime;
    }


}
