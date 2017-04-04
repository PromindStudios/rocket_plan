package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ActionMode;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContextualMenuFragmentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 27.03.2017.
 */

public class ActionModeCallback implements android.support.v7.view.ActionMode.Callback {

    private Context context;
    private ContextualMenuFragmentInterface mFragmentInterface;

    public ActionModeCallback(Context context, ContextualMenuFragmentInterface fragmentInterface) {
        this.context = context;
        mFragmentInterface = fragmentInterface;
    }

    @Override
    public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.contextual_menu_content_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
        menu.findItem(R.id.menu_item_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_delete:
                mFragmentInterface.deleteRows();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(android.support.v7.view.ActionMode actionMode) {
        if (mFragmentInterface != null) {
            mFragmentInterface.removeActionMode();
        }
    }
}
