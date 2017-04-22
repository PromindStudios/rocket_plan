package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.StatusBar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;

/**
 * Created by Eric on 19.04.2017.
 */

public class StatusBar_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (MyConstants.STATUS_BAR_ACTION_VISIBILITY.equals(intent.getAction())) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(MyConstants.STATUS_BAR_AGENDA_VISIBLE, !sharedPreferences.getBoolean(MyConstants.STATUS_BAR_AGENDA_VISIBLE, true)).commit();
            StatusBarManager statusBarManager = new StatusBarManager(context, new DatabaseHelper(context));
            statusBarManager.update();
        }
        if (MyConstants.STATUS_BAR_DAILY_UPDATE.equals(intent.getAction())) {
            StatusBarManager statusBarManager = new StatusBarManager(context, new DatabaseHelper(context));
            statusBarManager.update();
            Log.i("Repeating", "works");
        }
    }
}
