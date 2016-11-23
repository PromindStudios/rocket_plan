package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;

/**
 * Created by Admin on 23.08.2016.
 */
public class DateTimeTexter {

    public static String getGeneral(TaskEvent taskEvent) {
        String dateTime = "";
        int daysBetween = taskEvent.getDaysTillDueDate();
        String textTime = "";
        if (taskEvent.getTime() != null) {
            textTime = "  -  " + MyMethods.formatTime(taskEvent.getTime());
        }

        boolean isDone = taskEvent.isDone();
        if (daysBetween < 0 && !isDone) {
            dateTime = "OVERDUE";
        }
        if (daysBetween == 0) {
            dateTime = "Today" + textTime;
        }
        if (daysBetween == 1 && !isDone) {
            dateTime = "Tomorrow" + textTime;
        }
        if (daysBetween > 1 || isDone) {
            dateTime = MyMethods.formatDate(taskEvent.getDate()) + textTime;
        }
        return dateTime;
    }

    public static String getTimeDay(TaskEvent taskEvent) {
        String dateTime = "";
        int daysBetween = taskEvent.getDaysTillDueDate();
        if (taskEvent.getTime() != null) {
            dateTime = MyMethods.formatTime(taskEvent.getTime());
        }
        if (daysBetween < 0) {
            dateTime = "OVERDUE";
        }

        return dateTime;
    }

    public static String getTimeWeek(TaskEvent taskEvent) {
        int daysBetween = taskEvent.getDaysTillDueDate();
        String dateTime = "";
        String textTime = "";
        if (taskEvent.getTime() != null) {
            textTime = "  -  " + MyMethods.formatTime(taskEvent.getTime());
        }

        boolean isDone = taskEvent.isDone();
        if (daysBetween < 0 && !isDone) {
            dateTime = "OVERDUE";
        }
        if (daysBetween == 0) {
            dateTime = "Today" + textTime;
        }
        if (daysBetween == 1 && !isDone) {
            dateTime = "Tomorrow" + textTime;
        }
        if (daysBetween > 1 || isDone) {
            dateTime = MyMethods.formatWeekDay(taskEvent.getDate()) + textTime;
        }
        return dateTime;
    }

    public static String getTimeMonth(TaskEvent taskEvent) {
        return getGeneral(taskEvent);
    }

    public static String getNormal(TaskEvent taskEvent) {
        String dateTime = "";
        String textTime = "";
        if (taskEvent.getTime() != null) {
            textTime = "  -  " + MyMethods.formatTime(taskEvent.getTime());
        }
        dateTime = MyMethods.formatDate(taskEvent.getDate()) + textTime;

        return dateTime;
    }


}
