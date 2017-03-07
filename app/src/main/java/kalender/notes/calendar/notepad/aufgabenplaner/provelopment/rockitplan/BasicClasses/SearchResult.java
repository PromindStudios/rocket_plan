package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

/**
 * Created by Eric on 26.02.2017.
 */

public class SearchResult {

    String mName;
    String mId;

    public SearchResult() {
        mName = "";
        mId = "";
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getContactId() {
        return mId;
    }

    public void setContactId(String contactId) {
        this.mId = contactId;
    }
}
