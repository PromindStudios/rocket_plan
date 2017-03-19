package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 05.05.2016.
 */
public class Task extends TaskEvent {

    public Task (int categoryId, String categoryName) {
        super(categoryId, categoryName, R.drawable.ic_task);
    }

    public Task() {
        mDrawableId = R.drawable.ic_task;
    }

}
