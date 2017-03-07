package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

/**
 * Created by eric on 05.05.2016.
 */
public class Event extends TaskEvent{

    String mLocation;

    public Event (int categoryId, String categoryName) {
        super(categoryId, categoryName);
        mLocation = "";
    }

    public Event() {
        mLocation = "";
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }
}
