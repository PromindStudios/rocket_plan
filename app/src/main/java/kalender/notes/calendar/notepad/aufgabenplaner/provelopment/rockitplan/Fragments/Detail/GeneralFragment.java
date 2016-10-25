package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.DetailActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Reminder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DatePickerDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.MyTimePickerDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.ReminderDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.RepeatDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.ReminderAdapter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Reminder.ReminderSetter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RepeatTexter;

/**
 * Created by eric on 04.05.2016.
 */
public class GeneralFragment extends Fragment implements DatePickerDialog.DatePickerDialogListener, MyTimePickerDialog.TimePickerDialogListener, ReminderDialog.ReminderDialogListener, RepeatDialog.RepeatDialogListener {

    EditText etTitle;
    AppCompatImageView ivAddDate;
    AppCompatImageView ivAddTime;
    AppCompatImageView ivAddRepeat;
    AppCompatImageView ivRemoveDate;
    AppCompatImageView ivRemoveTime;
    AppCompatImageView ivRemoveRepeat;
    TextView tvTitleDeadline;
    TextView tvDate;
    TextView tvTime;
    TextView tvRepeat;
    TextView tvReminderTitle;
    CheckBox cbPriority;
    View vDividerReminder;

    LinearLayout llDateReminder;
    RelativeLayout rlTime;
    RelativeLayout rlRepeat;
    RelativeLayout rlDate;

    RecyclerView rvReminder;

    Task mTask;
    Event mEvent;
    Note mNote;
    Content mContent;
    TaskEvent mTaskEvent;
    View vDummy;
    View vDividerTime;

    int mContentType;

    DatabaseHelper mDatabaseHelper;

    DetailActivityListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.tab_general, container, false);

        // Initiate layout components
        etTitle = (EditText) layout.findViewById(R.id.etTitle);
        ivAddDate = (AppCompatImageView) layout.findViewById(R.id.ivAddDate);
        ivAddTime = (AppCompatImageView) layout.findViewById(R.id.ivAddTime);
        ivAddRepeat = (AppCompatImageView) layout.findViewById(R.id.ivAddRepeat);
        ivRemoveDate = (AppCompatImageView) layout.findViewById(R.id.ivRemoveDate);
        ivRemoveTime = (AppCompatImageView) layout.findViewById(R.id.ivRemoveTime);
        ivRemoveRepeat = (AppCompatImageView) layout.findViewById(R.id.ivRemoveRepeat);
        tvTitleDeadline = (TextView) layout.findViewById(R.id.tvTitleDeadline);
        tvDate = (TextView) layout.findViewById(R.id.tvDate);
        tvTime = (TextView) layout.findViewById(R.id.tvTime);
        tvRepeat = (TextView) layout.findViewById(R.id.tvRepeat);
        tvReminderTitle = (TextView) layout.findViewById(R.id.tvTitleReminder);
        cbPriority = (AppCompatCheckBox) layout.findViewById(R.id.cbPriority);
        vDividerReminder = layout.findViewById(R.id.dividerReminder);
        llDateReminder = (LinearLayout) layout.findViewById(R.id.llDateReminder);
        vDummy = layout.findViewById(R.id.vDummyGeneral);
        vDividerTime = layout.findViewById(R.id.vDividerTime);
        rvReminder = (RecyclerView) layout.findViewById(R.id.rvReminder);
        rlTime = (RelativeLayout) layout.findViewById(R.id.rlTime);
        rlRepeat = (RelativeLayout) layout.findViewById(R.id.rlRepeat);
        rlDate = (RelativeLayout) layout.findViewById(R.id.rlDate);

        mDatabaseHelper = new DatabaseHelper(getActivity());

        mContentType = getArguments().getInt(MyConstants.CONTENT_TYPE);

        switch (mContentType) {
            case MyConstants.CONTENT_TASK:
                mContent = mListener.getTask();
                mTask = mListener.getTask();
                mTaskEvent = mListener.getTask();
                break;
            case MyConstants.CONTENT_EVENT:
                mContent = mListener.getEvent();
                mEvent = mListener.getEvent();
                mTaskEvent = mListener.getEvent();
                break;
            case MyConstants.CONTENT_NOTE:
                mContent = mListener.getNote();
                mNote = mListener.getNote();
        }

        // Title
        etTitle.setText(mContent.getTitle());
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mListener.setToolbarTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                mContent.setTitle(s.toString());
            }
        });

        if (mContent.getTitle().equals("")) {
            etTitle.requestFocus();
            Log.i("Title", "ist leer");
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etTitle, InputMethodManager.SHOW_IMPLICIT);
        }


        if (mContentType == MyConstants.CONTENT_TASK || mContentType == MyConstants.CONTENT_EVENT) {

            // Check if taskEvent isDone
            if (mTaskEvent.isDone()) {
                setDateComponentsVisible(false);
            } else {
                // Reminder

                // Get Reminders
                ArrayList<Reminder> reminders = new ArrayList<>();
                Log.i("Infoo Content: ", mContent.getTitle() + " Id: " + mContent.getId());
                reminders = mDatabaseHelper.getAllContentReminders(mContent.getId(), mContentType);

                // Set up RecyclerView
                rvReminder.setLayoutManager(new LinearLayoutManager(getActivity()));
                ReminderAdapter adapter = new ReminderAdapter(getActivity(), reminders, mContent.getId(), mContentType, getFragmentManager(), this, mContent.getCategoryId());
                rvReminder.setAdapter(adapter);

                // Date
                Calendar date = mTaskEvent.getDate();
                if (date != null) {
                    tvDate.setText(MyMethods.formatDate(date));
                    tvDate.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorSecondaryText, null));
                    ivAddDate.setVisibility(View.GONE);
                    ivRemoveDate.setVisibility(View.VISIBLE);
                    setRepeatVisible(true);
                    setReminderVisible(true);
                    setTimeVisible(true);
                } else {
                    tvDate.setText(getActivity().getString(R.string.detail_subtext_date));
                    ivAddDate.setVisibility(View.VISIBLE);
                    ivRemoveDate.setVisibility(View.GONE);
                    setRepeatVisible(false);
                    setReminderVisible(false);
                    setTimeVisible(false);
                }
                View.OnClickListener dateOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar date = mTaskEvent.getDate();
                        if (date == null) {
                            date = Calendar.getInstance();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(MyConstants.TASK_DATE, date);
                        DialogFragment dialogFragment = new DatePickerDialog();
                        dialogFragment.setTargetFragment(GeneralFragment.this, 0);
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                    }
                };
                tvDate.setOnClickListener(dateOnClickListener);
                ivAddDate.setOnClickListener(dateOnClickListener);
                ivRemoveDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTaskEvent.setDate(null);
                        tvDate.setText(getActivity().getString(R.string.detail_subtext_date));
                        tvDate.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorDivider, null));
                        ivAddDate.setVisibility(View.VISIBLE);
                        ivRemoveDate.setVisibility(View.GONE);
                        setRepeatVisible(false);
                        setReminderVisible(false);
                        setTimeVisible(false);

                        // also remove Time
                        mTaskEvent.setTime(null);
                        tvTime.setText(getActivity().getString(R.string.detail_subtext_time));
                        tvTime.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorDivider, null));
                        ivAddTime.setVisibility(View.VISIBLE);
                        ivRemoveTime.setVisibility(View.GONE);
                    }
                });

                // Time
                // Work right here and solve time date issue
                Calendar time = mTaskEvent.getTime();
                if (time != null) {
                    tvTime.setText(MyMethods.formatTime(time));
                    tvTime.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorSecondaryText, null));
                    ivAddTime.setVisibility(View.GONE);
                    ivRemoveTime.setVisibility(View.VISIBLE);
                } else {
                    tvTime.setText(getActivity().getString(R.string.detail_subtext_time));
                    ivAddTime.setVisibility(View.VISIBLE);
                    ivRemoveTime.setVisibility(View.GONE);
                }
                View.OnClickListener timeOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar time = mTaskEvent.getTime();
                        if (time == null) {
                            time = Calendar.getInstance();
                            time.set(Calendar.HOUR_OF_DAY, 12);
                            time.set(Calendar.MINUTE, 0);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(MyConstants.TASK_EVENT_TIME, time);
                        DialogFragment dialogFragment = new MyTimePickerDialog();
                        dialogFragment.setTargetFragment(GeneralFragment.this, 0);
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
                    }
                };
                tvTime.setOnClickListener(timeOnClickListener);
                ivAddTime.setOnClickListener(timeOnClickListener);
                ivRemoveTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTaskEvent.setTime(null);
                        tvTime.setText(getActivity().getString(R.string.detail_subtext_time));
                        tvTime.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorDivider, null));
                        ivAddTime.setVisibility(View.VISIBLE);
                        ivRemoveTime.setVisibility(View.GONE);
                    }
                });

                // Repetition

                updateRepeat();

                View.OnClickListener repeatOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mTaskEvent.getRepetitionType() == MyConstants.REPETITION_TYPE_NONE) {
                            CharSequence[] items = {getActivity().getString(R.string.repetition_daily), getActivity().getString(R.string.repetition_weekly), getActivity().getString(R.string.repetition_monthly), getActivity().getString(R.string.custom)};
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    switch (item) {
                                        case 0:
                                            mTaskEvent.setRepetitionType(MyConstants.REPETITION_TYPE_DAY);
                                            mTaskEvent.setRepetitionValue(1);
                                            updateRepeat();
                                            break;
                                        case 1:
                                            mTaskEvent.setRepetitionType(MyConstants.REPETITION_TYPE_WEEK);
                                            mTaskEvent.setRepetitionValue(1);
                                            updateRepeat();
                                            break;
                                        case 2:
                                            mTaskEvent.setRepetitionType(MyConstants.REPETITION_TYPE_MONTH);
                                            mTaskEvent.setRepetitionValue(1);
                                            updateRepeat();
                                            break;
                                        case 3:
                                            DialogFragment dialog_repeat = new RepeatDialog();
                                            Bundle bundle = new Bundle();
                                            dialog_repeat.setTargetFragment(GeneralFragment.this, 0);
                                            dialog_repeat.show(getActivity().getSupportFragmentManager(), "Reminder_Dialog");
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                            builder.create().show();
                        } else {
                            DialogFragment dialog_repeat = new RepeatDialog();
                            Bundle bundle = new Bundle();
                            dialog_repeat.setTargetFragment(GeneralFragment.this, 0);
                            dialog_repeat.show(getActivity().getSupportFragmentManager(), "Reminder_Dialog");
                        }
                    }
                };
                ivAddRepeat.setOnClickListener(repeatOnClickListener);
                tvRepeat.setOnClickListener(repeatOnClickListener);
                ivRemoveRepeat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleRepeatComponents(false);
                        mTaskEvent.setRepetitionType(MyConstants.REPETITION_TYPE_NONE);
                        mTaskEvent.setRepetitionValue(0);
                    }
                });
                vDividerTime.setVisibility(View.VISIBLE);
            }

        } else {
            vDividerTime.setVisibility(View.GONE);
            vDividerReminder.setVisibility(View.GONE);
            llDateReminder.setVisibility(View.GONE);
        }


        // Category Color
        CategoryColor categoryColor = new CategoryColor(getActivity(), mListener.getCategory().getColor());

        // Priority

        int priority = mContent.getPriority();
        if (priority > 0) {
            cbPriority.setChecked(true);
        } else {
            cbPriority.setChecked(false);
        }
        cbPriority.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mContent.setPriority(1);
                } else {
                    mContent.setPriority(0);
                }

            }
        });

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            cbPriority.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), categoryColor.getCategoryColor())));
        } else {
            int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            cbPriority.setButtonDrawable(id);
        }


        return layout;
    }


    private void setReminderVisible(boolean visible) {
        if (visible) {
            tvReminderTitle.setVisibility(View.VISIBLE);
            vDividerReminder.setVisibility(View.VISIBLE);
            rvReminder.setVisibility(View.VISIBLE);
        } else {
            tvReminderTitle.setVisibility(View.GONE);
            vDividerReminder.setVisibility(View.GONE);
            rvReminder.setVisibility(View.GONE);
        }
    }

    private void setTimeVisible(boolean visible) {
        if (visible) {
            rlTime.setVisibility(View.VISIBLE);
        } else {
            rlTime.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.selectTab();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (DetailActivity) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(MyConstants.GERNERAL_TASK_FRAGMENT, "onPause()");
        updateReminders();
    }

    @Override
    public void onDateSelected(Calendar date) {
        mTaskEvent.setDate(date);
        /*
        if (mContentType == MyConstants.CONTENT_TASK) {
            mTask.setDate(date);
        }
        if (mContentType == MyConstants.CONTENT_EVENT) {
            mEvent.setDate(date);
        }
        */
        tvDate.setText(MyMethods.formatDate(date));
        ivAddDate.setVisibility(View.GONE);
        ivRemoveDate.setVisibility(View.VISIBLE);
        tvDate.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorSecondaryText, null));
        setRepeatVisible(true);
        setReminderVisible(true);
        setTimeVisible(true);
        updateReminders();
    }

    @Override
    public void onTimeSelected(Calendar time) {
        mTaskEvent.setTime(time);
        /*
        if (mContentType == MyConstants.CONTENT_TASK) {
            mTask.setTime(time);
        }
        if (mContentType == MyConstants.CONTENT_EVENT) {
            mEvent.setTime(time);
        }
        */
        tvTime.setText(MyMethods.formatTime(time));
        ivAddTime.setVisibility(View.GONE);
        ivRemoveTime.setVisibility(View.VISIBLE);
        tvTime.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorSecondaryText, null));
    }

    @Override
    public void saveReminder(int reminderType, int reminderValue) {

        createReminder(reminderType, reminderValue);

        // Get Reminders
        ArrayList<Reminder> reminders = new ArrayList<>();
        reminders = mDatabaseHelper.getAllContentReminders(mContent.getId(), mContentType);

        // update RecyclerView
        ReminderAdapter adapter = new ReminderAdapter(getActivity(), reminders, mContent.getId(), mContentType, getFragmentManager(), this, mContent.getCategoryId());
        rvReminder.setAdapter(adapter);
    }

    @Override
    public void editReminder(int reminderId, int reminderType, int reminderValue) {
        Reminder reminder = mDatabaseHelper.getReminder(reminderId);
        reminder.setReminderType(reminderType);
        reminder.setReminderValue(reminderValue);
        mDatabaseHelper.updateReminder(reminder, getActivity());
        setAlarmForReminder(reminder);

        // Get all reminders
        // Get Reminders
        ArrayList<Reminder> reminders = new ArrayList<>();
        reminders = mDatabaseHelper.getAllContentReminders(mContent.getId(), mContentType);
        Log.i("MyReminderSize", "" + reminders.size());

        // update RecyclerView
        ReminderAdapter adapter = new ReminderAdapter(getActivity(), reminders, mContent.getId(), mContentType, getFragmentManager(), this, mContent.getCategoryId());
        rvReminder.setAdapter(adapter);
    }

    public interface DetailActivityListener {
        public Task getTask();

        public Event getEvent();

        public Note getNote();

        public Category getCategory();

        public void takePicture(FilesFragment filesFragment);

        public void selectTab();

        public void setToolbarTitle(String title);
    }

    public TaskEvent getTaskEvent() {
        return mTaskEvent;
    }

    @Override
    public void updateRepeat() {
        if (mTaskEvent.getRepetitionType() == MyConstants.REPETITION_TYPE_NONE) {
            handleRepeatComponents(false);
            tvRepeat.setText(getActivity().getString(R.string.add_repeat));
        } else {
            handleRepeatComponents(true);
            tvRepeat.setText(RepeatTexter.getStandardText(getActivity(), mTaskEvent));
        }
    }

    public void handleFocus() {
        if (mContent.getTitle() == null || mContent.getTitle().equals("")) {
            etTitle.requestFocus();
            Log.i("Title ist", "ist leer");
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etTitle, InputMethodManager.SHOW_IMPLICIT);
        } else {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etTitle.getWindowToken(), 0);
        }
    }

    public void createReminder(int reminderType, int reminderValue) {
        Reminder reminder = new Reminder(mTaskEvent.getId(), mContentType, reminderType, reminderValue);
        int reminderId = mDatabaseHelper.createReminder(reminder, getActivity());
        reminder.setId(reminderId);
        setAlarmForReminder(reminder);
    }

    public void setAlarmForReminder(Reminder reminder) {
        ReminderSetter.setAlarm(getActivity(), reminder, mTaskEvent);
    }

    public void updateReminders() {
        ArrayList<Reminder> reminders = new ArrayList<>();
        reminders = mDatabaseHelper.getAllContentReminders(mContent.getId(), mContentType);
        for (int i = 0; i < reminders.size(); i++) {
            Reminder reminder = reminders.get(i);
            setAlarmForReminder(reminder);
        }
    }

    private void handleRepeatComponents(boolean hasValue) {
        if (hasValue) {
            ivAddRepeat.setVisibility(View.GONE);
            tvRepeat.setText(RepeatTexter.getStandardText(getActivity(), mTaskEvent));
            tvRepeat.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorSecondaryText));
            ivRemoveRepeat.setVisibility(View.VISIBLE);
        } else {
            ivAddRepeat.setVisibility(View.VISIBLE);
            tvRepeat.setText(getActivity().getString(R.string.add_repeat));
            tvRepeat.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorDivider));
            ivRemoveRepeat.setVisibility(View.GONE);
        }
    }

    private void setDateVisible(boolean visible) {
        if (visible) {
            rlDate.setVisibility(View.VISIBLE);
            vDividerTime.setVisibility(View.VISIBLE);
        } else {
            rlDate.setVisibility(View.GONE);
            tvTitleDeadline.setVisibility(View.GONE);
            vDividerTime.setVisibility(View.GONE);
        }
    }

    private void setRepeatVisible(boolean visible) {
        if (visible) {
            rlRepeat.setVisibility(View.VISIBLE);
        } else {
            rlRepeat.setVisibility(View.GONE);
        }
    }


    private void setDateComponentsVisible(boolean visible) {
        setDateVisible(visible);
        setTimeVisible(visible);
        setRepeatVisible(visible);
        setReminderVisible(visible);
    }


}
