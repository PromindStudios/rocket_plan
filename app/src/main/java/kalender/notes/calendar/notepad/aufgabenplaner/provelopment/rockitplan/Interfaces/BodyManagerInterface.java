package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces;

import android.support.v4.app.Fragment;

import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;

/**
 * Created by Eric on 05.03.2017.
 */

public interface BodyManagerInterface {
    public void addCategory(Fragment targetFragment);
    public void deleteCategory(Category category, Fragment targetFragment);
    public void addContent(Category category, int contentType, int detailType);
    public void addContent(Category category, int contentType, int detailType, String title, Calendar date, Calendar time);
}
