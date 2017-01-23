package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;

/**
 * Created by Eric on 21.01.2017.
 */

public interface ContentInterface {
    public void createContent(Category category, int contentType, int detailType);
    public void selectCategory(int contentType);
}
