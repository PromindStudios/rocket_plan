package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 22.05.2016.
 */
public class DatePickerDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener{

    Calendar mDate;
    DatePickerDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDate = (Calendar)getArguments().getSerializable(MyConstants.TASK_DATE);
        mListener = (DatePickerDialogListener)getTargetFragment();

        final android.app.DatePickerDialog datePickerDialog;
        if (android.os.Build.VERSION.SDK_INT >= 23)  {
            datePickerDialog = new android.app.DatePickerDialog(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert, this, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH), mDate.get(Calendar.DAY_OF_MONTH));
        } else {
            datePickerDialog = new android.app.DatePickerDialog(getActivity(), this, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH), mDate.get(Calendar.DAY_OF_MONTH));
        }
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getActivity().getString(R.string.time), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatePickerDialog.this.getDialog().cancel();
                Calendar calendar = Calendar.getInstance();
                DatePicker datePicker = datePickerDialog.getDatePicker();
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                mListener.onDateSelected(calendar);
                mListener.openTimeDialog();
            }
        });

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mListener.onDateSelected(date);
    }

    public interface DatePickerDialogListener {
        public void onDateSelected(Calendar date);
        public void openTimeDialog();
    }
}
