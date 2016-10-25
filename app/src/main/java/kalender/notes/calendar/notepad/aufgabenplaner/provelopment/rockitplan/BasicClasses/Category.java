package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

/**
 * Created by eric on 05.05.2016.
 */
public class Category {

    private String mTitle;
    private int mId;
    private int mColor;
    private boolean mShowTaskDone;
    private boolean mShowEventDone;
    private boolean mExpanded;

    public Category() {
        mTitle = null;
        mExpanded = true;
    }

    public Category(String title, int color) {
        mTitle = title;
        mColor = color;
        mShowEventDone = false;
        mShowTaskDone = true;
        mExpanded = true;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public boolean isShowTaskDone() {
        return mShowTaskDone;
    }

    public void setShowTaskDone(boolean showTaskDone) {
        mShowTaskDone = showTaskDone;
    }

    public boolean isShowEventDone() {
        return mShowEventDone;
    }

    public void setShowEventDone(boolean mShowEventDone) {
        this.mShowEventDone = mShowEventDone;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }
}
