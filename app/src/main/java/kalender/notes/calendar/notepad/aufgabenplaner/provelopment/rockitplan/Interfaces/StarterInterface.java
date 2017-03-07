package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;

/**
 * Created by Eric on 05.03.2017.
 */

public interface StarterInterface {
    public void startOverviewPagerFragment();
    public void startCalendarFragment();
    public void startContentPagerFragment(Category category, int contentType);
    public void startDetailActivity(Category category, int contentType, int contentId, int detailType);
}
