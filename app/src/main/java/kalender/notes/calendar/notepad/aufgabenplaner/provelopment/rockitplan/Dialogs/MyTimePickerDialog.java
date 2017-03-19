package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;

/**
 * Created by Admin on 14.08.2016.
 */
public class MyTimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    TimePickerDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar time = (Calendar)getArguments().getSerializable(MyConstants.TASK_EVENT_TIME);
        mListener = (TimePickerDialogListener)getTargetFragment();
        Log.i("Timee", "is selected");

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE),DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, hourOfDay);
        time.set(Calendar.MINUTE, minute);
        mListener.onTimeSelected(time);
    }

    public interface TimePickerDialogListener {
        public void onTimeSelected(Calendar time);
    }
}
