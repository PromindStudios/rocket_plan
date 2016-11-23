package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.CalendarFragmentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 10.11.2016.
 */

public class CalendarChooseDialog extends DialogFragment{

    int mYearPosition;
    int mMonth;
    ArrayList<TextView> mYearsLayout;
    ArrayList<TextView> mMonthsLayout;
    CalendarFragmentInterface mListener;
    int[] mYearList;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        int year = bundle.getInt(MyConstants.CALENDAR_YEAR);
        mMonth = bundle.getInt(MyConstants.CALENDAR_MONTH);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        mYearList = new int[3];
        for (int i = 0; i<3; i++) {
            mYearList[i] = calendar.get(Calendar.YEAR);
            if (mYearList[i] == year) {
                mYearPosition = i;
            }
            calendar.add(Calendar.YEAR, 1);
        }

        mListener = (CalendarFragmentInterface)getTargetFragment();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_calendar_choose, null);

        ImageButton ibSave = (ImageButton) dialogView.findViewById(R.id.ibSave);
        ImageButton ibExit = (ImageButton) dialogView.findViewById(R.id.ibExit);
        TextView tvYear1 = (TextView)dialogView.findViewById(R.id.tvYear1);
        TextView tvYear2 = (TextView)dialogView.findViewById(R.id.tvYear2);
        TextView tvYear3 = (TextView)dialogView.findViewById(R.id.tvYear3);
        TextView tvMonth1 = (TextView)dialogView.findViewById(R.id.tvMonth1);
        TextView tvMonth2 = (TextView)dialogView.findViewById(R.id.tvMonth2);
        TextView tvMonth3 = (TextView)dialogView.findViewById(R.id.tvMonth3);
        TextView tvMonth4 = (TextView)dialogView.findViewById(R.id.tvMonth4);
        TextView tvMonth5 = (TextView)dialogView.findViewById(R.id.tvMonth5);
        TextView tvMonth6 = (TextView)dialogView.findViewById(R.id.tvMonth6);
        TextView tvMonth7 = (TextView)dialogView.findViewById(R.id.tvMonth7);
        TextView tvMonth8 = (TextView)dialogView.findViewById(R.id.tvMonth8);
        TextView tvMonth9 = (TextView)dialogView.findViewById(R.id.tvMonth9);
        TextView tvMonth10 = (TextView)dialogView.findViewById(R.id.tvMonth10);
        TextView tvMonth11 = (TextView)dialogView.findViewById(R.id.tvMonth11);
        TextView tvMonth12 = (TextView)dialogView.findViewById(R.id.tvMonth12);

        mYearsLayout = new ArrayList<>();
        mYearsLayout.add(tvYear1);
        mYearsLayout.add(tvYear2);
        mYearsLayout.add(tvYear3);
        mMonthsLayout = new ArrayList<>();
        mMonthsLayout.add(tvMonth1);
        mMonthsLayout.add(tvMonth2);
        mMonthsLayout.add(tvMonth3);
        mMonthsLayout.add(tvMonth4);
        mMonthsLayout.add(tvMonth5);
        mMonthsLayout.add(tvMonth6);
        mMonthsLayout.add(tvMonth7);
        mMonthsLayout.add(tvMonth8);
        mMonthsLayout.add(tvMonth9);
        mMonthsLayout.add(tvMonth10);
        mMonthsLayout.add(tvMonth11);
        mMonthsLayout.add(tvMonth12);

        for (int i = 0; i<mMonthsLayout.size(); i++) {
            TextView tvMonth = mMonthsLayout.get(i);
            final int finalI = i;
            tvMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectMonth(finalI);
                }
            });
        }
        for (int i = 0; i<mYearList.length; i++) {
            TextView tvYear = mYearsLayout.get(i);
            final int finalI = i;
            tvYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectYear(finalI);
                }
            });
        }
        selectMonth(mMonth);
        selectYear(mYearPosition);

        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarChooseDialog.this.getDialog().cancel();
            }
        });
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelectPage(mYearList[mYearPosition], mMonth);
                CalendarChooseDialog.this.getDialog().cancel();
            }
        });


        builder.setView(dialogView);
        setCancelable(false);
        Dialog dialog = builder.create();
        return dialog;
    }

    private void selectYear(int newYear) {
        TextView tvYearOld = mYearsLayout.get(mYearPosition);
        TextView tvYearNew = mYearsLayout.get(newYear);
        mYearPosition = newYear;
        tvYearOld.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        tvYearOld.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));
        tvYearNew.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        tvYearNew.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
    }

    private void selectMonth(int newMonth) {
        TextView tvMonthOld = mMonthsLayout.get(mMonth);
        TextView tvMonthNew = mMonthsLayout.get(newMonth);
        mMonth = newMonth;
        tvMonthOld.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        tvMonthOld.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));
        tvMonthNew.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        tvMonthNew.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
    }
}
