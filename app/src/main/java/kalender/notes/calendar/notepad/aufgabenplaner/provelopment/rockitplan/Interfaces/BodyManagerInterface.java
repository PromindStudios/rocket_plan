package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces;

import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;

/**
 * Created by Eric on 05.03.2017.
 */

public interface BodyManagerInterface {
    public void addCategory();
    public void addContent(Category category, int contentType, int detailType);
    public void addContent(Category category, int contentType, int detailType, String title, Calendar date, Calendar time);
}
