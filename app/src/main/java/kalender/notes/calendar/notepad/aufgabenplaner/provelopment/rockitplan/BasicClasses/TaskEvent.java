package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;

/**
 * Created by Admin on 11.07.2016.
 */
public class TaskEvent extends Content {

    private Calendar mDate;
    private Calendar mTime;
    private int mRepetitionType;
    private int mRepetitionValue;
    private int mReminder;
    private boolean mDone;

    public TaskEvent(int categoryId, String categoryName, int drawableId) {
        mCategoryId = categoryId;
        mCategory = categoryName;
        mTitle = "";
        mSubtitle = null;
        mDate = null;
        mTime = null;
        mRepetitionType = MyConstants.REPETITION_TYPE_NONE;
        mRepetitionValue = 0;
        mReminder = 0;
        mPriority = 0;
        mDescription = "";
        mPicturePath = null;
        mVideoPath = null;
        mAudioPath = null;
        mDone = false;
        mDrawableId = drawableId;
    }

    public TaskEvent() {
    }

    public Calendar getDate() {
        return mDate;
    }

    public void setDate(Calendar date) {
        mDate = date;
    }

    public Calendar getTime() {
        return mTime;
    }

    public void setTime(Calendar time) {
        mTime = time;
    }

    public int getRepetitionType() {
        return mRepetitionType;
    }

    public void setRepetitionType(int repetitionType) {
        mRepetitionType = repetitionType;
    }

    public int getRepetitionValue() {
        return mRepetitionValue;
    }

    public void setRepetitionValue(int repetitionValue) {
        mRepetitionValue = repetitionValue;
    }

    public int getReminder() {
        return mReminder;
    }

    public void setReminder(int reminder) {
        mReminder = reminder;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        this.mDone = done;
    }

    public int getDaysTillDueDate() {
        Calendar t = Calendar.getInstance();
        DateTime today = new DateTime(t.get(Calendar.YEAR), t.get(Calendar.MONTH)+1, t.get(Calendar.DAY_OF_MONTH), 0, 0);
        DateTime due = new DateTime(mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH)+1, mDate.get(Calendar.DAY_OF_MONTH), 0, 0);
        Log.i("Daays", ""+ Days.daysBetween(today, due).getDays());
        return Days.daysBetween(today, due).getDays();
    }
}
