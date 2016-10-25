package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 16.08.2016.
 */
public class ReminderDialog extends DialogFragment {

    TextView tvTitle;
    EditText etReminder;

    RelativeLayout rlMinute;
    TextView tvMinute;
    ImageView ivCheckMinute;

    RelativeLayout rlHour;
    TextView tvHour;
    ImageView ivCheckHour;

    RelativeLayout rlDay;
    TextView tvDay;
    ImageView ivCheckDay;

    RelativeLayout rlWeek;
    TextView tvWeek;
    ImageView ivCheckWeek;

    ImageButton ibRemove;
    ImageButton ibSave;

    int mReminderType;
    int mReminderValue;
    boolean isSingular;

    int mReminderId;

    CategoryColor mCategoryColor;

    ReminderDialogListener mListener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mListener = (ReminderDialogListener)getTargetFragment();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reminder_repeat_custom, null);

        tvTitle = (TextView)dialogView.findViewById(R.id.tvTitle);
        etReminder = (EditText)dialogView.findViewById(R.id.etCustom);

        rlMinute = (RelativeLayout)dialogView.findViewById(R.id.rlOne);
        tvMinute = (TextView)dialogView.findViewById(R.id.tvOne);
        ivCheckMinute = (ImageView)dialogView.findViewById(R.id.ivCheckOne);

        rlHour = (RelativeLayout)dialogView.findViewById(R.id.rlTwo);
        tvHour = (TextView)dialogView.findViewById(R.id.tvTwo);
        ivCheckHour = (ImageView)dialogView.findViewById(R.id.ivCheckTwo);

        rlDay = (RelativeLayout)dialogView.findViewById(R.id.rlThree);
        tvDay = (TextView)dialogView.findViewById(R.id.tvThree);
        ivCheckDay = (ImageView)dialogView.findViewById(R.id.ivCheckThree);

        rlWeek = (RelativeLayout)dialogView.findViewById(R.id.rlFour);
        tvWeek = (TextView)dialogView.findViewById(R.id.tvFour);
        ivCheckWeek = (ImageView)dialogView.findViewById(R.id.ivCheckFour);

        ibRemove = (ImageButton)dialogView.findViewById(R.id.ibRemove);
        ibSave = (ImageButton)dialogView.findViewById(R.id.ibSave);

        // handle Color
        int categoryId = bundle.getInt(MyConstants.CATEGORY_ID);
        DatabaseHelper dh = new DatabaseHelper(getActivity());
        Category category = dh.getCategory(categoryId);

        // Get ReminderId
        mReminderId = bundle.getInt(MyConstants.REMINDER_ID);

        mCategoryColor = new CategoryColor(getActivity(), category.getColor());
        tvTitle.setBackgroundColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));


        mReminderValue = bundle.getInt(MyConstants.REMINDER_VALUE);
        etReminder.setText(Integer.toString(mReminderValue));
        if (mReminderValue < 2) {
            isSingular = true;
        } else {
            isSingular = false;
        }
        mReminderType = bundle.getInt(MyConstants.REMINDER_TYPE);
        check(mReminderType);

        etReminder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    int i = Integer.parseInt(s.toString());
                    mReminderValue = i;
                    if (i > 1 || i==0) {
                        if (isSingular) {
                            isSingular = false;
                            check(mReminderType);
                        }
                    } else {
                        if (!isSingular) {
                            isSingular = true;
                            check(mReminderType);
                        }
                    }
                }
            }
        });

        rlMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReminderType = MyConstants.REMINDER_MINUTE;
                check(mReminderType);
            }
        });
        rlHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReminderType = MyConstants.REMINDER_HOUR;
                check(mReminderType);
            }
        });
        rlDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReminderType = MyConstants.REMINDER_DAY;
                check(mReminderType);
            }
        });
        rlWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReminderType = MyConstants.REMINDER_WEEK;
                check(mReminderType);
            }
        });

        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReminderValue == 0) {
                    mReminderType = MyConstants.REMINDER_AT_DUE_TIME;
                }
                if (mReminderId == 0) {
                    mListener.saveReminder(mReminderType, mReminderValue);
                } else {
                    mListener.editReminder(mReminderId, mReminderType, mReminderValue);
                }
                ReminderDialog.this.getDialog().cancel();
            }
        });
        ibRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderDialog.this.getDialog().cancel();
            }
        });

        builder.setView(dialogView);
        setCancelable(false);
        Dialog dialog = builder.create();

        return dialog;
    }

    private void check(int reminderType) {
        clean();
        switch (reminderType) {
            case MyConstants.REMINDER_MINUTE:
                tvMinute.setText(isSingular ? getActivity().getString(R.string.reminder_minute) + " " + getActivity().getString(R.string.before) : getActivity().getString(R.string.minute_plural) + " " + getActivity().getString(R.string.before));
                ivCheckMinute.setVisibility(View.VISIBLE);
                tvMinute.setTextColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
                ivCheckMinute.setImageDrawable(mCategoryColor.colorIcon(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.ic_check, null)));
                break;
            case MyConstants.REMINDER_HOUR:
                tvHour.setText(isSingular ? getActivity().getString(R.string.reminder_hour) + " " + getActivity().getString(R.string.before) : getActivity().getString(R.string.hour_plural) + " " + getActivity().getString(R.string.before));
                ivCheckHour.setVisibility(View.VISIBLE);
                tvHour.setTextColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
                ivCheckHour.setImageDrawable(mCategoryColor.colorIcon(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.ic_check, null)));
                break;
            case MyConstants.REMINDER_DAY:
                tvDay.setText(isSingular ? getActivity().getString(R.string.reminder_day) + " " + getActivity().getString(R.string.before) : getActivity().getString(R.string.day_plural) + " " + getActivity().getString(R.string.before));
                ivCheckDay.setVisibility(View.VISIBLE);
                tvDay.setTextColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
                ivCheckDay.setImageDrawable(mCategoryColor.colorIcon(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.ic_check, null)));
                break;
            case MyConstants.REMINDER_WEEK:
                tvWeek.setText(isSingular ? getActivity().getString(R.string.reminder_week) + " " + getActivity().getString(R.string.before) : getActivity().getString(R.string.week_plural) + " " + getActivity().getString(R.string.before));
                ivCheckWeek.setVisibility(View.VISIBLE);
                tvWeek.setTextColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
                ivCheckWeek.setImageDrawable(mCategoryColor.colorIcon(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.ic_check, null)));
                break;
        }
    }

    private void clean() {
        tvMinute.setText(isSingular ? getActivity().getString(R.string.reminder_minute) : getActivity().getString(R.string.minute_plural));
        tvHour.setText(isSingular ? getActivity().getString(R.string.reminder_hour) : getActivity().getString(R.string.hour_plural));
        tvDay.setText(isSingular ? getActivity().getString(R.string.reminder_day) : getActivity().getString(R.string.day_plural));
        tvWeek.setText(isSingular ? getActivity().getString(R.string.reminder_week) : getActivity().getString(R.string.week_plural));
        tvMinute.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));
        tvHour.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));
        tvDay.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));
        tvWeek.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));

        ivCheckMinute.setVisibility(View.INVISIBLE);
        ivCheckHour.setVisibility(View.INVISIBLE);
        ivCheckDay.setVisibility(View.INVISIBLE);
        ivCheckWeek.setVisibility(View.INVISIBLE);
    }


    public interface ReminderDialogListener {
        public void saveReminder(int reminderType, int reminderValue);
        public void editReminder(int reminderId, int reminderType, int reminderValue);
    }

}
