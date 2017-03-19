package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan;

import android.content.Context;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;

/**
 * Created by Eric on 15.10.2016.
 */

public class RepeatTexter {

    public static String getStandardText(Context context, TaskEvent taskEvent) {
        String displayText = "zero";
        int repeatValue = taskEvent.getRepetitionValue();
        switch (taskEvent.getRepetitionType()) {
            case MyConstants.REPETITION_TYPE_HOUR:
                if (repeatValue == 1) {
                    displayText = context.getString(R.string.repetition_hourly);
                } else {
                    displayText = context.getString(R.string.every) + " " + repeatValue + " " + context.getString(R.string.hour_plural);
                }
                break;
            case MyConstants.REPETITION_TYPE_DAY:
                if (repeatValue == 1) {
                    displayText = context.getString(R.string.repetition_daily);
                } else {
                    displayText = context.getString(R.string.every) + " " + repeatValue + " " + context.getString(R.string.day_plural);
                }
                break;
            case MyConstants.REPETITION_TYPE_WEEK:
                if (repeatValue == 1) {
                    displayText = context.getString(R.string.repetition_weekly);
                } else {
                    displayText = context.getString(R.string.every) + " " + repeatValue + " " + context.getString(R.string.week_plural);
                }
                break;
            case MyConstants.REPETITION_TYPE_MONTH:
                if (repeatValue == 1) {
                    displayText = context.getString(R.string.repetition_monthly);
                } else {
                    displayText = context.getString(R.string.every) + " " + repeatValue + " " + context.getString(R.string.month_plural);
                }
                break;
        }


        return displayText;
    }

    public static String getStandardText(Context context, int rt, int rv) {
        String displayText = "zero";
        int repeatValue = rv;
        switch (rt) {
            case MyConstants.REPETITION_TYPE_HOUR:
                if (repeatValue == 1) {
                    displayText = context.getString(R.string.repetition_hourly);
                } else {
                    displayText = context.getString(R.string.every) + " " + repeatValue + " " + context.getString(R.string.hour_plural);
                }
                break;
            case MyConstants.REPETITION_TYPE_DAY:
                if (repeatValue == 1) {
                    displayText = context.getString(R.string.repetition_daily);
                } else {
                    displayText = context.getString(R.string.every) + " " + repeatValue + " " + context.getString(R.string.day_plural);
                }
                break;
            case MyConstants.REPETITION_TYPE_WEEK:
                if (repeatValue == 1) {
                    displayText = context.getString(R.string.repetition_weekly);
                } else {
                    displayText = context.getString(R.string.every) + " " + repeatValue + " " + context.getString(R.string.week_plural);
                }
                break;
            case MyConstants.REPETITION_TYPE_MONTH:
                if (repeatValue == 1) {
                    displayText = context.getString(R.string.repetition_monthly);
                } else {
                    displayText = context.getString(R.string.every) + " " + repeatValue + " " + context.getString(R.string.month_plural);
                }
                break;
        }


        return displayText;
    }
}
