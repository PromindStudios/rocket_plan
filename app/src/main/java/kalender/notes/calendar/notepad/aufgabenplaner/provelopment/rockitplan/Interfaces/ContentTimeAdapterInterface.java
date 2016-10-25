package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces;

import android.support.v7.widget.RecyclerView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;

/**
 * Created by Admin on 22.07.2016.
 */
public interface ContentTimeAdapterInterface {
    public Content getContent(int position);
    public Content getContent(RecyclerView.ViewHolder viewHolder);
}
