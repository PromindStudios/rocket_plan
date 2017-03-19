package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.TaskEventActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DateTimeTexter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeAdapterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.ContentViewHolder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.DividerViewHolder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.HolderHelper;

/**
 * Created by Admin on 15.07.2016.
 */
public class TimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ContentTimeAdapterInterface {

    private static final int TYPE_DIVIDER = 0;
    private static final int TYPE_TASKEVENT = 1;

    private static final int THIS = 0;
    private static final int NEXT = 1;
    private static final int DONE = 2;


    int mPositionDividerTwo;
    int mPositionDividerThree;

    boolean mTimeOneExtended;
    boolean mTimeTwoExtended;
    boolean mTimeThreeExtended;

    MainActivity mMainActivity;
    DatabaseHelper mDataBaseHelper;
    HolderHelper mHolderHelper;

    Context mContext;
    int mTimeType;
    int mTimeNextPosition;
    ArrayList<Content> mTaskEvents;
    LayoutInflater mLayoutInflater;

    int mThisCount;
    int mDoneCount;
    int mNextCount;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    public TimeAdapter(Context context, int timeType, MainActivity mainActivity) {
        mSharedPreferences = context.getSharedPreferences(MyConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mMainActivity = mainActivity;
        mContext = context;
        mDataBaseHelper = new DatabaseHelper(mContext);
        mTimeType = timeType;
        mLayoutInflater = LayoutInflater.from(mContext);
        setExtended();
        mTaskEvents = getTaskEvents();
        mHolderHelper = new HolderHelper(mContext);


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TASKEVENT) {
            View view = mLayoutInflater.inflate(R.layout.item_content, parent, false);
            ContentViewHolder holder = new ContentViewHolder(view, new ContentViewHolder.vhTasksClickListener() {
                @Override
                public void openContent(int position, int type) {
                    Content content = getContent(position);
                    startDetailActivity(content, type);
                }
            });
            return holder;
        } else {
            View view = mLayoutInflater.inflate(R.layout.item_expand_collapse, parent, false);
            DividerViewHolder holder = new DividerViewHolder(view, new DividerViewHolder.vhShowHideClickListener() {
                @Override
                public void onExpandCollapse(int position) {
                    if (position == 0) {
                        mTimeOneExtended = !mTimeOneExtended;
                    }
                    if (position == mPositionDividerTwo) {
                        mTimeTwoExtended = !mTimeTwoExtended;
                    }
                    if (position == mPositionDividerThree) {
                        mTimeThreeExtended = !mTimeThreeExtended;
                    }
                    saveExtended(position);
                    mTaskEvents = getTaskEvents();
                    notifyDataSetChanged();
                }
            });
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            ContentViewHolder h = (ContentViewHolder) holder;
            Log.i("TaskEvent Positionn", ""+position);
            Content content = getContent(position);
            Log.i("TaskEvent Name", content.getTitle());
            mHolderHelper.setUpContentHolder(h, content, true);

            // Date & Time

            TaskEvent taskEvent = (TaskEvent)content;
            Calendar date = taskEvent.getDate();
            Calendar time = taskEvent.getTime();

            h.llSubtitleDate.setVisibility(View.VISIBLE);
            switch (mTimeType) {
                case MyConstants.TIME_DAY:
                    if (taskEvent.getDaysTillDueDate() < 0 || time != null) {
                        h.tvDate.setText(DateTimeTexter.getTimeDay(taskEvent, mContext));
                    } else {
                        h.llSubtitleDate.setVisibility(View.GONE);
                    }
                    break;
                case MyConstants.TIME_WEEK:
                    h.tvDate.setText(DateTimeTexter.getTimeWeek(taskEvent, mContext));
                    break;
                case MyConstants.TIME_MONTH:
                    h.tvDate.setText(DateTimeTexter.getTimeMonth(taskEvent, mContext));
                    break;

            }

            // Handle Divider Visibility
            if (position + 1 == mPositionDividerTwo || position + 1 == mPositionDividerThree || position == mTaskEvents.size() + 2) {
                h.vDivider.setVisibility(View.GONE);
            } else {
                h.vDivider.setVisibility(View.VISIBLE);
                Log.i("WWWWochendende", "lkjklsdj2!");
            }
        } else {
            DividerViewHolder vhDivider = (DividerViewHolder) holder;

            vhDivider.tvText.setVisibility(View.VISIBLE);
            vhDivider.ivExpandCollapse.setVisibility(View.VISIBLE);

            String dueThis = "";
            String dueThisNone = "";
            String doneThis = "";
            String doneThisNone = "";
            String dueNext = "";
            String dueNextNone = "";

            switch (mTimeType) {
                case MyConstants.TIME_DAY:
                    dueThis = mContext.getString(R.string.time_due_today);
                    doneThis = mContext.getString(R.string.time_done_today);
                    dueNext = mContext.getString(R.string.time_due_tomorrow);
                    dueThisNone = mContext.getString(R.string.time_due_today_none);
                    doneThisNone = mContext.getString(R.string.time_done_today_none);
                    dueNextNone = mContext.getString(R.string.time_due_tomorrow_none);
                    break;
                case MyConstants.TIME_WEEK:
                    dueThis = mContext.getString(R.string.time_due_this_week);
                    doneThis = mContext.getString(R.string.time_done_this_week);
                    dueNext = mContext.getString(R.string.time_due_next_week);
                    dueThisNone = mContext.getString(R.string.time_due_this_week_none);
                    doneThisNone = mContext.getString(R.string.time_done_this_week_none);
                    dueNextNone = mContext.getString(R.string.time_due_next_week_none);
                    break;
                case MyConstants.TIME_MONTH:
                    dueThis = mContext.getString(R.string.time_due_this_month);
                    doneThis = mContext.getString(R.string.time_done_this_month);
                    dueNext = mContext.getString(R.string.time_due_next_month);
                    dueThisNone = mContext.getString(R.string.time_due_this_month_none);
                    doneThisNone = mContext.getString(R.string.time_done_this_month_none);
                    dueNextNone = mContext.getString(R.string.time_due_next_month_none);
                    break;
            }

            vhDivider.tvText.setVisibility(View.VISIBLE);
            vhDivider.ivExpandCollapse.setVisibility(View.VISIBLE);
            if (position == 0) {
                if (mThisCount > 0) {
                    if (mTimeOneExtended) {
                        vhDivider.tvText.setText(dueThis);
                        vhDivider.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expanded));
                    } else {
                        vhDivider.tvText.setText(dueThis);
                        vhDivider.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_collapsed));
                    }

                } else {
                    vhDivider.ivExpandCollapse.setVisibility(View.VISIBLE);
                    vhDivider.tvText.setText(dueThisNone);
                    Drawable icon_right = ContextCompat.getDrawable(mContext, R.drawable.ic_right_18dp);
                    icon_right.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorDivider, null), PorterDuff.Mode.MULTIPLY);
                    vhDivider.ivExpandCollapse.setImageDrawable(icon_right);
                    vhDivider.tvText.setVisibility(View.GONE);
                    vhDivider.ivExpandCollapse.setVisibility(View.GONE);
                }
            }

            if (position == mPositionDividerTwo) {
                if (mDoneCount > 0) {
                    if (mTimeTwoExtended) {
                        vhDivider.tvText.setText(doneThis);
                        vhDivider.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expanded));
                    } else {
                        Log.i("Teeeest", Integer.toString(mDoneCount));
                        vhDivider.tvText.setText(doneThis);
                        vhDivider.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_collapsed));
                    }

                } else {
                    vhDivider.tvText.setText(doneThisNone);
                    vhDivider.ivExpandCollapse.setVisibility(View.VISIBLE);
                    Drawable icon_right = ContextCompat.getDrawable(mContext, R.drawable.ic_right_18dp);
                    icon_right.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorDivider, null), PorterDuff.Mode.MULTIPLY);
                    vhDivider.ivExpandCollapse.setImageDrawable(icon_right);
                    vhDivider.tvText.setVisibility(View.GONE);
                    vhDivider.ivExpandCollapse.setVisibility(View.GONE);
                }
            }
            if (position == mPositionDividerThree) {
                if (mNextCount > 0) {
                    if (mTimeThreeExtended) {
                        vhDivider.tvText.setText(dueNext);
                        vhDivider.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expanded));
                    } else {
                        vhDivider.tvText.setText(dueNext);
                        vhDivider.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_collapsed));
                    }

                } else {
                    vhDivider.tvText.setText(dueNextNone);
                    vhDivider.ivExpandCollapse.setVisibility(View.VISIBLE);
                    Drawable icon_right = ContextCompat.getDrawable(mContext, R.drawable.ic_right_18dp);
                    icon_right.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorDivider, null), PorterDuff.Mode.MULTIPLY);
                    vhDivider.ivExpandCollapse.setImageDrawable(icon_right);
                    vhDivider.tvText.setVisibility(View.GONE);
                    vhDivider.ivExpandCollapse.setVisibility(View.GONE);
                }
            }

            /*

            if (position == mPositionDividerTwo) {
                if (mDoneCount > 0) {
                    if (mTimeTwoExtended) {
                        vhDivider.tvText.setText(doneThis);
                        vhDivider.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expanded));
                    } else {
                        vhDivider.tvText.setText(doneThis + " " + "(" + Integer.toString(mNextCount) + ")");
                        vhDivider.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_collapsed));
                    }

                } else {
                    vhDivider.tvText.setText(doneThisNone);
                    vhDivider.ivExpandCollapse.setVisibility(View.VISIBLE);
                    Drawable icon_right = ContextCompat.getDrawable(mContext, R.drawable.ic_right_18dp);
                    icon_right.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorDivider, null), PorterDuff.Mode.MULTIPLY);
                    vhDivider.ivExpandCollapse.setImageDrawable(icon_right);
                }
            }
            */
        }


    }

    @Override
    public int getItemCount() {
        return mTaskEvents.size() + 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mPositionDividerTwo || position == mPositionDividerThree) {
            return TYPE_DIVIDER;
        } else {
            return TYPE_TASKEVENT;
        }
    }

    public ArrayList<Content> getTaskEvents() {
        ArrayList<Content> taskEvents = new ArrayList<>();
        taskEvents = mDataBaseHelper.getAllTaskEvents();
        Log.i("TaskEvents Size Beg.", Integer.toString(taskEvents.size()));
        switch (mTimeType) {
            case MyConstants.TIME_DAY:
                Log.i("TIME_TYPE", "TIME_DAY");
                ArrayList<TaskEvent> taskEventsToday = new ArrayList<>();
                ArrayList<TaskEvent> taskEventsTodayDone = new ArrayList<>();
                ArrayList<TaskEvent> taskEventsTomorrow = new ArrayList<>();
                for (Content content : taskEvents) {
                    TaskEvent taskEvent = (TaskEvent) content;
                    if (taskEvent.getDate() != null)  {
                        int daysTillDueDate = taskEvent.getDaysTillDueDate();
                        if (!taskEvent.isDone()) {
                            Log.i("Days till due date", Integer.toString(daysTillDueDate));
                            if (daysTillDueDate <= 0) {
                                taskEventsToday.add(taskEvent);
                            }
                            if (daysTillDueDate == 1) {
                                taskEventsTomorrow.add(taskEvent);
                            }
                        } else {
                            if (daysTillDueDate == 0) {
                                taskEventsTodayDone.add(taskEvent);
                            }
                        }
                    }
                }
                mThisCount = taskEventsToday.size();
                mDoneCount = taskEventsTodayDone.size();
                mNextCount = taskEventsTomorrow.size();
                taskEvents.clear();
                mPositionDividerTwo = 1;
                mPositionDividerThree = 2;
                if (mTimeOneExtended) {
                    Log.i("Added", "Yes");
                    taskEvents.addAll(taskEventsToday);
                    mPositionDividerTwo = taskEvents.size() + 1;
                    mPositionDividerThree = taskEvents.size() + 2;
                }
                if (mTimeTwoExtended) {
                    taskEvents.addAll(taskEventsTodayDone);
                    mPositionDividerThree = taskEvents.size() + 2;
                }
                if (mTimeThreeExtended) {
                    taskEvents.addAll(taskEventsTomorrow);
                }
                Log.i("TaskEvents Size End.", Integer.toString(taskEvents.size()));
                return taskEvents;
            case MyConstants.TIME_WEEK:
                int thisWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
                ArrayList<TaskEvent> taskEventsThisWeek = new ArrayList<>();
                ArrayList<TaskEvent> taskEventsDoneThisWeek = new ArrayList<>();
                ArrayList<TaskEvent> taskEventsNextWeek = new ArrayList<>();
                for (Content content : taskEvents) {
                    TaskEvent taskEvent = (TaskEvent) content;
                    if (taskEvent.getDate() != null) {
                        int dueWeek = taskEvent.getDate().get(Calendar.WEEK_OF_YEAR);
                        if (!taskEvent.isDone()) {
                            if (dueWeek == thisWeek) {
                                taskEventsThisWeek.add(taskEvent);
                            }
                            if (dueWeek == thisWeek + 1) {
                                taskEventsNextWeek.add(taskEvent);
                            }
                        } else {
                            if (dueWeek == thisWeek) {
                                taskEventsDoneThisWeek.add(taskEvent);
                            }

                        }

                    }
                }
                taskEvents.clear();
                mThisCount = taskEventsThisWeek.size();
                mDoneCount = taskEventsDoneThisWeek.size();
                mNextCount = taskEventsNextWeek.size();

                mPositionDividerTwo = 1;
                mPositionDividerThree = 2;
                if (mTimeOneExtended) {
                    Log.i("Added", "Yes");
                    taskEvents.addAll(taskEventsThisWeek);
                    mPositionDividerTwo = taskEvents.size() + 1;
                    mPositionDividerThree = taskEvents.size() + 2;
                }
                if (mTimeTwoExtended) {
                    taskEvents.addAll(taskEventsDoneThisWeek);
                    mPositionDividerThree = taskEvents.size() + 2;
                }
                if (mTimeThreeExtended) {
                    taskEvents.addAll(taskEventsNextWeek);
                }

                return taskEvents;
            case MyConstants.TIME_MONTH:
                int thisMonth = Calendar.getInstance().get(Calendar.MONTH);
                ArrayList<TaskEvent> taskEventsThisMonth = new ArrayList<>();
                ArrayList<TaskEvent> taskEventsDoneThisMonth = new ArrayList<>();
                ArrayList<TaskEvent> taskEventsNextMonth = new ArrayList<>();
                for (Content content : taskEvents) {
                    TaskEvent taskEvent = (TaskEvent) content;
                    if (taskEvent.getDate() != null) {
                        int dueMonth = taskEvent.getDate().get(Calendar.MONTH);
                        if (!taskEvent.isDone()) {
                            if (dueMonth == thisMonth) {
                                taskEventsThisMonth.add(taskEvent);
                            }
                            if (dueMonth == thisMonth + 1) {
                                taskEventsNextMonth.add(taskEvent);
                            }
                        } else {
                            if (dueMonth == thisMonth) {
                                taskEventsDoneThisMonth.add(taskEvent);
                            }
                        }

                    }
                }
                taskEvents.clear();
                mThisCount = taskEventsThisMonth.size();
                mDoneCount = taskEventsDoneThisMonth.size();
                mNextCount = taskEventsNextMonth.size();
                mPositionDividerTwo = 1;
                mPositionDividerThree = 2;
                if (mTimeOneExtended) {
                    Log.i("Added", "Yes");
                    taskEvents.addAll(taskEventsThisMonth);
                    mPositionDividerTwo = taskEvents.size() + 1;
                    mPositionDividerThree = taskEvents.size() + 2;
                }
                if (mTimeTwoExtended) {
                    taskEvents.addAll(taskEventsDoneThisMonth);
                    mPositionDividerThree = taskEvents.size() + 2;
                }
                if (mTimeThreeExtended) {
                    taskEvents.addAll(taskEventsNextMonth);
                }
                return taskEvents;
            default:
                return null;
        }
    }

    public Content getContent(int position) {
        Log.i("Poositin", Integer.toString(position));
        if (mTimeThreeExtended && position > mPositionDividerThree) {
            return mTaskEvents.get(position - 3);
        } else {
            if (mTimeOneExtended && position < mPositionDividerTwo) {
                return mTaskEvents.get(position - 1);
            } else {
                if (position > mPositionDividerTwo) {
                    return mTaskEvents.get(position - 2);
                } else {
                    return null;
                }
            }
        }
    }


    public Content getContent(RecyclerView.ViewHolder viewHolder) {
        return getContent(viewHolder.getAdapterPosition());
    }

    private void saveExtended(int position) {
        switch (mTimeType) {
            case MyConstants.TIME_DAY:
                if (position == 0) {
                    mEditor.putBoolean(MyConstants.IS_EX_TIME_DAY_THIS, mTimeOneExtended);
                }
                if (position == mPositionDividerTwo) {
                    mEditor.putBoolean(MyConstants.IS_EX_TIME_DAY_DONE, mTimeTwoExtended);
                }
                if (position == mPositionDividerThree) {
                    mEditor.putBoolean(MyConstants.IS_EX_TIME_DAY_NEXT, mTimeThreeExtended);
                }
                mEditor.commit();
                break;
            case MyConstants.TIME_WEEK:
                if (position == 0) {
                    mEditor.putBoolean(MyConstants.IS_EX_TIME_WEEK_THIS, mTimeOneExtended);
                }
                if (position == mPositionDividerTwo) {
                    mEditor.putBoolean(MyConstants.IS_EX_TIME_WEEK_DONE, mTimeTwoExtended);
                }
                if (position == mPositionDividerThree) {
                    mEditor.putBoolean(MyConstants.IS_EX_TIME_WEEK_NEXT, mTimeThreeExtended);
                }
                mEditor.commit();
                break;
            case MyConstants.TIME_MONTH:
                if (position == 0) {
                    mEditor.putBoolean(MyConstants.IS_EX_TIME_MONTH_THIS, mTimeOneExtended);
                }
                if (position == mPositionDividerTwo) {
                    mEditor.putBoolean(MyConstants.IS_EX_TIME_MONTH_DONE, mTimeTwoExtended);
                }
                if (position == mPositionDividerThree) {
                    mEditor.putBoolean(MyConstants.IS_EX_TIME_MONTH_NEXT, mTimeThreeExtended);
                }
                mEditor.commit();
                break;
        }

    }

    private void setExtended() {
        switch (mTimeType) {
            case MyConstants.TIME_DAY:
                mTimeOneExtended = mSharedPreferences.getBoolean(MyConstants.IS_EX_TIME_DAY_THIS, true);
                mTimeTwoExtended = mSharedPreferences.getBoolean(MyConstants.IS_EX_TIME_DAY_DONE, true);
                mTimeThreeExtended = mSharedPreferences.getBoolean(MyConstants.IS_EX_TIME_DAY_NEXT, true);
                break;
            case MyConstants.TIME_WEEK:
                mTimeOneExtended = mSharedPreferences.getBoolean(MyConstants.IS_EX_TIME_WEEK_THIS, true);
                mTimeTwoExtended = mSharedPreferences.getBoolean(MyConstants.IS_EX_TIME_WEEK_DONE, true);
                mTimeThreeExtended = mSharedPreferences.getBoolean(MyConstants.IS_EX_TIME_WEEK_NEXT, true);
                break;
            case MyConstants.TIME_MONTH:
                mTimeOneExtended = mSharedPreferences.getBoolean(MyConstants.IS_EX_TIME_MONTH_THIS, true);
                mTimeTwoExtended = mSharedPreferences.getBoolean(MyConstants.IS_EX_TIME_MONTH_DONE, true);
                mTimeThreeExtended = mSharedPreferences.getBoolean(MyConstants.IS_EX_TIME_MONTH_NEXT, true);
                break;
        }
    }

    private void startDetailActivity(Content content, int type) {
        Intent intent = new Intent(mContext, TaskEventActivity.class);
        Bundle bundle = new Bundle();
        if (content instanceof Task) {
            bundle.putInt(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_TASK);
        } else {
            bundle.putInt(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_EVENT);
        }
        bundle.putInt(MyConstants.CONTENT_ID, content.getId());
        bundle.putInt(MyConstants.DETAIL_TYPE, type);
        intent.putExtras(bundle);
        mMainActivity.startActivity(intent);
    }
}

