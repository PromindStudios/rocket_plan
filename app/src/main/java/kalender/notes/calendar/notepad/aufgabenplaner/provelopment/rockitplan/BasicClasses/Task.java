package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import android.os.Parcelable;

import java.util.Calendar;
import java.lang.Object;

/**
 * Created by eric on 05.05.2016.
 */
public class Task extends TaskEvent {

    public Task (int categoryId, String categoryName) {
        super(categoryId, categoryName);
    }

    public Task() {
    }

}
