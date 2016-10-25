package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RepeatTexter;

/**
 * Created by Eric on 16.10.2016.
 */

public class RepeatDialog extends android.support.v4.app.DialogFragment{

    TextView tvTitle;
    EditText etRepeat;

    RelativeLayout rlHour;
    TextView tvHour;
    ImageView ivCheckHour;

    RelativeLayout rlDay;
    TextView tvDay;
    ImageView ivCheckDay;

    RelativeLayout rlWeek;
    TextView tvWeek;
    ImageView ivCheckWeek;

    RelativeLayout rlMonth;
    TextView tvMonth;
    ImageView ivCheckMonth;

    ImageButton ibRemove;
    ImageButton ibSave;

    int mRepeatType;
    int mRepeatValue;

    CategoryColor mCategoryColor;

    RepeatDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mListener = (RepeatDialogListener) getTargetFragment();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reminder_repeat_custom, null);

        tvTitle = (TextView)dialogView.findViewById(R.id.tvTitle);
        etRepeat = (EditText)dialogView.findViewById(R.id.etCustom);

        rlMonth = (RelativeLayout)dialogView.findViewById(R.id.rlFour);
        tvMonth = (TextView)dialogView.findViewById(R.id.tvFour);
        ivCheckMonth = (ImageView)dialogView.findViewById(R.id.ivCheckFour);

        rlHour = (RelativeLayout)dialogView.findViewById(R.id.rlOne);
        tvHour = (TextView)dialogView.findViewById(R.id.tvOne);
        ivCheckHour = (ImageView)dialogView.findViewById(R.id.ivCheckOne);

        rlDay = (RelativeLayout)dialogView.findViewById(R.id.rlTwo);
        tvDay = (TextView)dialogView.findViewById(R.id.tvTwo);
        ivCheckDay = (ImageView)dialogView.findViewById(R.id.ivCheckTwo);

        rlWeek = (RelativeLayout)dialogView.findViewById(R.id.rlThree);
        tvWeek = (TextView)dialogView.findViewById(R.id.tvThree);
        ivCheckWeek = (ImageView)dialogView.findViewById(R.id.ivCheckThree);

        ibRemove = (ImageButton)dialogView.findViewById(R.id.ibRemove);
        ibSave = (ImageButton)dialogView.findViewById(R.id.ibSave);

        final TaskEvent taskEvent = mListener.getTaskEvent();

        // handle Color
        int categoryId = taskEvent.getCategoryId();
        DatabaseHelper dh = new DatabaseHelper(getActivity());
        Category category = dh.getCategory(categoryId);
        mCategoryColor = new CategoryColor(getActivity(), category.getColor());
        tvTitle.setBackgroundColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));

        mRepeatType = taskEvent.getRepetitionType();
        mRepeatValue = taskEvent.getRepetitionValue();

        // Set Title
        tvTitle.setText(getActivity().getString(R.string.repetition));

        // Set Edit Text title
        if (mRepeatValue == 0) mRepeatValue = 1;
        etRepeat.setText(Integer.toString(mRepeatValue));

        // Set Content
        if (mRepeatType == 0) mRepeatType = 3;
        check(mRepeatType);

        etRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    int i = Integer.parseInt(s.toString());
                    mRepeatValue = i;
                    check(mRepeatType);
                }
            }
        });

        rlMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepeatType = MyConstants.REPETITION_TYPE_MONTH;
                check(mRepeatType);
            }
        });
        rlHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepeatType = MyConstants.REPETITION_TYPE_HOUR;
                check(mRepeatType);
            }
        });
        rlDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepeatType = MyConstants.REPETITION_TYPE_DAY;
                check(mRepeatType);
            }
        });
        rlWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepeatType = MyConstants.REPETITION_TYPE_WEEK;
                check(mRepeatType);
            }
        });

        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskEvent.setRepetitionType(mRepeatType);
                taskEvent.setRepetitionValue(mRepeatValue);
                mListener.updateRepeat();
                RepeatDialog.this.getDialog().cancel();
            }
        });
        ibRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepeatDialog.this.getDialog().cancel();
            }
        });

        builder.setView(dialogView);
        setCancelable(false);
        Dialog dialog = builder.create();

        return dialog;

    }

    private void check(int repeatType) {
        clean();
        switch (repeatType) {
            case MyConstants.REPETITION_TYPE_MONTH:
                tvMonth.setText(RepeatTexter.getStandardText(getActivity(), repeatType, mRepeatValue));
                ivCheckMonth.setVisibility(View.VISIBLE);
                tvMonth.setTextColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
                ivCheckMonth.setImageDrawable(mCategoryColor.colorIcon(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.ic_check, null)));
                break;
            case MyConstants.REPETITION_TYPE_HOUR:
                tvHour.setText(RepeatTexter.getStandardText(getActivity(), repeatType, mRepeatValue));
                ivCheckHour.setVisibility(View.VISIBLE);
                tvHour.setTextColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
                ivCheckHour.setImageDrawable(mCategoryColor.colorIcon(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.ic_check, null)));
                break;
            case MyConstants.REPETITION_TYPE_DAY:
                tvDay.setText(RepeatTexter.getStandardText(getActivity(), repeatType, mRepeatValue));
                ivCheckDay.setVisibility(View.VISIBLE);
                tvDay.setTextColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
                ivCheckDay.setImageDrawable(mCategoryColor.colorIcon(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.ic_check, null)));
                break;
            case MyConstants.REPETITION_TYPE_WEEK:
                tvWeek.setText(RepeatTexter.getStandardText(getActivity(), repeatType, mRepeatValue));
                ivCheckWeek.setVisibility(View.VISIBLE);
                tvWeek.setTextColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));
                ivCheckWeek.setImageDrawable(mCategoryColor.colorIcon(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.ic_check, null)));
                break;
        }
    }

    private void clean() {

        tvHour.setText((mRepeatValue==1) ? getActivity().getString(R.string.repetition_hourly) : getActivity().getString(R.string.hour_plural));
        tvDay.setText((mRepeatValue==1) ? getActivity().getString(R.string.repetition_daily) : getActivity().getString(R.string.day_plural));
        tvWeek.setText((mRepeatValue==1) ? getActivity().getString(R.string.repetition_weekly) : getActivity().getString(R.string.week_plural));
        tvMonth.setText((mRepeatValue==1) ? getActivity().getString(R.string.repetition_monthly) : getActivity().getString(R.string.month_plural));

        tvHour.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));
        tvDay.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));
        tvWeek.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));
        tvMonth.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));

        ivCheckHour.setVisibility(View.INVISIBLE);
        ivCheckDay.setVisibility(View.INVISIBLE);
        ivCheckWeek.setVisibility(View.INVISIBLE);
        ivCheckMonth.setVisibility(View.INVISIBLE);
    }

    public interface RepeatDialogListener {
        public TaskEvent getTaskEvent();
        public void updateRepeat();
    }
}
