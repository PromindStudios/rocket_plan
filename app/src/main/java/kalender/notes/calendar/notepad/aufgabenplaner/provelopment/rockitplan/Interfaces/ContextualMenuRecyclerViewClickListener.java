package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces;

import android.view.View;

/**
 * Created by Eric on 28.03.2017.
 */

public interface ContextualMenuRecyclerViewClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
