package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ActionModeInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeAdapterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContextualMenuAdapterInterface;

/**
 * Created by Eric on 17.10.2016.
 */
public class ContentTimeCalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ContextualMenuAdapterInterface, ContentTimeAdapterInterface {

    ActionModeInterface mActionModeInterface;
    ArrayList<Content> mContentListDelete;
    ArrayList<Content> mContent;
    DatabaseHelper mDataBaseHelper;

    public ContentTimeCalendarAdapter(ActionModeInterface mActionModeInterface) {
        this.mActionModeInterface = mActionModeInterface;
        mContentListDelete = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void toggleSelection(int position) {
        Content content = getContent(position);
        selectView(position, !mContentListDelete.contains(content), content);
    }

    public void selectView(int position, boolean value, Content content) {
        if (value)
            mContentListDelete.add(content);
        else
            mContentListDelete.remove(content);
        notifyDataSetChanged();
    }

    public void update() {
    }

    public void deleteContentListDelete() {
        for (Content content : mContentListDelete) {
            mDataBaseHelper.deleteContent(content.getId(), content.getContentType());
        }
        resetContentListDelete();
    }

    @Override
    public void resetContentListDelete() {
        mContentListDelete = new ArrayList<>();
    }

    public ArrayList<Content> getContentList() {
        return mContent;
    }

    @Override
    public Content getContent(int position) {
        Log.i("NNOOOO", "not called please");
        return null;
    }

    @Override
    public Content getContent(RecyclerView.ViewHolder viewHolder) {
        Log.i("NNOOOO", "not called please");
        return null;

    }
}
