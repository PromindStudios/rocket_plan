package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import android.content.Context;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 15.08.2016.
 */
public class Reminder {

    int mId;
    int mContentId;
    int mContentType;
    int mReminderType;
    int mReminderValue;

    public Reminder() {

    }

    public Reminder(int contentId, int contentType, int reminderType, int reminderValue) {
        mContentId = contentId;
        mContentType = contentType;
        mReminderType = reminderType;
        mReminderValue = reminderValue;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getContentId() {
        return mContentId;
    }

    public void setContentId(int mContentId) {
        this.mContentId = mContentId;
    }

    public int getContentType() {
        return mContentType;
    }

    public void setContentType(int mContentType) {
        this.mContentType = mContentType;
    }

    public int getReminderType() {
        return mReminderType;
    }

    public void setReminderType(int mReminderType) {
        this.mReminderType = mReminderType;
    }

    public int getReminderValue() {
        return mReminderValue;
    }

    public void setReminderValue(int mReminderValue) {
        this.mReminderValue = mReminderValue;
    }

    public String getReminderTypeString(Context context) {
        if (mReminderValue < 2) {
            switch (mReminderType) {
                case MyConstants.REMINDER_MINUTE:
                    return context.getResources().getString(R.string.reminder_minute);
                case MyConstants.REMINDER_HOUR:
                    return context.getResources().getString(R.string.reminder_hour);
                case MyConstants.REMINDER_DAY:
                    return context.getResources().getString(R.string.reminder_day);
                case MyConstants.REMINDER_WEEK:
                    return context.getResources().getString(R.string.reminder_week);
                default:
                    return "";
            }
        } else {
            switch (mReminderType) {
                case MyConstants.REMINDER_MINUTE:
                    return context.getResources().getString(R.string.minute_plural);
                case MyConstants.REMINDER_HOUR:
                    return context.getResources().getString(R.string.hour_plural);
                case MyConstants.REMINDER_DAY:
                    return context.getResources().getString(R.string.day_plural);
                case MyConstants.REMINDER_WEEK:
                    return context.getResources().getString(R.string.week_plural);
                default:
                    return "";
            }
        }
    }
}
