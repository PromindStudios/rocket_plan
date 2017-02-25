package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces;

import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;

/**
 * Created by Eric on 21.01.2017.
 */

public interface ContentInterface {
    public void createContent(Category category, int contentType, int detailType, String title, Calendar date, Calendar time, boolean startDetailActivity);
    public void selectCategory(int contentType);
}
