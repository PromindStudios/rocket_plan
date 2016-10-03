package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

/**
 * Created by eric on 05.05.2016.
 */
public class Subtask {

    private int mId;
    private int mContentId;
    private String mTitle;
    private boolean mDone;

    public Subtask (int contentId, String title) {
        mContentId = contentId;
        mTitle = title;
        mDone = false;
    }

    public Subtask () {
    }


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getContentId() {
        return mContentId;
    }

    public void setContentId(int contentId) {
        mContentId = contentId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }
}
