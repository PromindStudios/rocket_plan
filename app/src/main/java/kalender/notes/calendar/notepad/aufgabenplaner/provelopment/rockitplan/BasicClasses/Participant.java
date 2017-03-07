package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

/**
 * Created by Eric on 24.02.2017.
 */

public class Participant {

    int mId;
    int mContentId;
    String mName;
    String mContactId;
    String mInformation;

    public Participant () {
        mName = "";
        mContactId = "";
        mInformation = "";
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

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getContactId() {
        return mContactId;
    }

    public void setContactId(String contactId) {
        this.mContactId = contactId;
    }

    public String getInformation() {
        return mInformation;
    }

    public void setInformation(String mInformation) {
        this.mInformation = mInformation;
    }
}
