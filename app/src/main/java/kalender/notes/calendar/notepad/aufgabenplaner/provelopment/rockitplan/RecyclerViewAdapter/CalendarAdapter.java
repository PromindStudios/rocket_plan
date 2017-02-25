package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
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
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.ContentViewHolder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.DividerViewHolder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.HolderHelper;

/**
 * Created by Eric on 01.11.2016.
 */

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ContentTimeAdapterInterface {

    private static final int TYPE_DIVIDER = 0;
    private static final int TYPE_TASKEVENT = 1;

    private static final int THIS = 0;
    private static final int DONE = 1;


    int mPositionDividerTwo;

    boolean mTimeOneExtended = true;
    boolean mTimeTwoExtended = false;

    MainActivity mMainActivity;
    DatabaseHelper mDataBaseHelper;
    HolderHelper mHolderHelper;

    Context mContext;
    int mTimeType;
    int mTimeNextPosition;
    ArrayList<Content> mTaskEvents;
    LayoutInflater mLayoutInflater;

    Calendar mSelectedDay;

    int mThisCount;
    int mDoneCount;

    public CalendarAdapter(Context context, Calendar calendar) {
        mMainActivity = (MainActivity)context;
        mSelectedDay = calendar;
        mContext = context;
        mDataBaseHelper = new DatabaseHelper(mContext);
        mLayoutInflater = LayoutInflater.from(mContext);
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
            Content content = getContent(position);
            mHolderHelper.setUpContentHolder(h, content, true);

            // Date & Time

            TaskEvent taskEvent = (TaskEvent) content;
            Calendar date = taskEvent.getDate();
            Calendar time = taskEvent.getTime();

            h.llSubtitleDate.setVisibility(View.VISIBLE);
            if (taskEvent.getDaysTillDueDate() < 0 || time != null) {
                h.tvDate.setText(DateTimeTexter.getTimeDay(taskEvent, mContext));
            } else {
                h.llSubtitleDate.setVisibility(View.GONE);
            }

            // Handle Divider Visibility
            if (position + 1 == mPositionDividerTwo || position == mTaskEvents.size() + 1 || position == mTaskEvents.size() + 2) {
                h.vDivider.setVisibility(View.GONE);
            } else {
                h.vDivider.setVisibility(View.GONE);
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

            dueThis = mContext.getString(R.string.time_due_today);
            doneThis = mContext.getString(R.string.done);
            dueThisNone = mContext.getString(R.string.time_due_today_none);
            doneThisNone = mContext.getString(R.string.time_done_today_none);


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
                    vhDivider.ivExpandCollapse.setVisibility(View.GONE);
                    vhDivider.tvText.setVisibility(View.GONE);

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
        }
    }

    @Override
    public int getItemCount() {
        return mTaskEvents.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mPositionDividerTwo) {
            return TYPE_DIVIDER;
        } else {
            return TYPE_TASKEVENT;
        }
    }

    public ArrayList<Content> getTaskEvents() {
        ArrayList<Content> taskEvents = new ArrayList<>();
        taskEvents = mDataBaseHelper.getAllTaskEventsAtDate(mSelectedDay);
        ArrayList<TaskEvent> taskEventsToday = new ArrayList<>();
        ArrayList<TaskEvent> taskEventsTodayDone = new ArrayList<>();
        for (Content content : taskEvents) {
            TaskEvent taskEvent = (TaskEvent) content;
            if (!taskEvent.isDone()) {

                taskEventsToday.add(taskEvent);

            } else {
                taskEventsTodayDone.add(taskEvent);
            }
        }
        mThisCount = taskEventsToday.size();
        mDoneCount = taskEventsTodayDone.size();
        taskEvents.clear();
        mPositionDividerTwo = 1;
        if (mTimeOneExtended) {
            Log.i("Added", "Yes");
            taskEvents.addAll(taskEventsToday);
            mPositionDividerTwo = taskEvents.size() + 1;
        }
        if (mTimeTwoExtended) {
            taskEvents.addAll(taskEventsTodayDone);
        }
        return taskEvents;

    }

    public Content getContent(int position) {
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


    public Content getContent(RecyclerView.ViewHolder viewHolder) {
        return getContent(viewHolder.getAdapterPosition());
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
        Log.i("Naame:", content.getTitle()+ " "+content.getContentType());
        intent.putExtras(bundle);
        mMainActivity.startActivity(intent);
    }
}
