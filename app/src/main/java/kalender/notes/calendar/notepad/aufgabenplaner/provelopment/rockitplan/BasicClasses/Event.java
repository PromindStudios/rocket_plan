package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 05.05.2016.
 */
public class Event extends TaskEvent{

    String mLocation;

    public Event (int categoryId, String categoryName) {
        super(categoryId, categoryName, R.drawable.ic_event_text_16dp);
        mLocation = "";
    }

    public Event() {
        mLocation = "";
        mDrawableId = R.drawable.ic_event_text_16dp;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
