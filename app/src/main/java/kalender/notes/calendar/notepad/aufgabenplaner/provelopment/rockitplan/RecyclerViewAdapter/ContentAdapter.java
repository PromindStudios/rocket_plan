package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.DetailActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.ContentHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Subtask;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DateTimeTexter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeAdapterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.ContentViewHolder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.DividerViewHolder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.HolderHelper;

/**
 * Created by Admin on 20.06.2016.
 * Set Click Listener for opening editReminder Activity
 */
public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ContentTimeAdapterInterface{

    private final int TYPE_SHOW_HIDE = 0;
    private final int TYPE_CONTENT = 1;

    int mPositionHideShow;
    boolean mExtended;

    HolderHelper mHolderHelper;

    MainActivity mMainActivity;

    Context mContext;
    ArrayList<Content> mContent;
    LayoutInflater mLayoutInflater;
    DatabaseHelper mDataBaseHelper;
    int mCategoryId;
    int mContentType;

    Category mCategory;

    ContentHelper mContentHelper;

    public ContentAdapter(Context context, int categoryId, MainActivity mainActivity, int contentType) {
        Log.i("categoryIdd", Integer.toString(categoryId));
        mContext = context;
        mMainActivity = mainActivity;
        mContentType = contentType;
        mLayoutInflater = LayoutInflater.from(mContext);
        mCategoryId = categoryId;
        mDataBaseHelper = new DatabaseHelper(mContext);
        mCategory = mDataBaseHelper.getCategory(mCategoryId);
        if (contentType == MyConstants.CONTENT_TASK) {
            mExtended = mCategory.isShowTaskDone();
        } else {
            mExtended = mCategory.isShowEventDone();
        }
        if (contentType == MyConstants.CONTENT_TASK) {
            if (mExtended) {
                Log.i("Ja: ", "Extended");
            } else {
                Log.i("Nein: ", "Extended");
            }
            Log.i("Cadegory Id: ", mCategory.getTitle());
        }


        mContentHelper = new ContentHelper(mContext, mCategoryId);
        setContent();
        mHolderHelper = new HolderHelper(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType == TYPE_CONTENT) {
            View view = mLayoutInflater.inflate(R.layout.item_content, parent, false);
            ContentViewHolder holder = new ContentViewHolder(view, new ContentViewHolder.vhTasksClickListener() {
                @Override
                public void openTask(int position, int type) {
                    Content content = getCorrectContent(position);
                    startDetailActivity(content, type);
                }
            });
            return holder;
        } else {
            View view = mLayoutInflater.inflate(R.layout.item_expand_collapse, parent, false);
            DividerViewHolder holder = new DividerViewHolder(view, new DividerViewHolder.vhShowHideClickListener() {
                @Override
                public void onExpandCollapse(int position) {
                    handleExpandCollaps(mExtended);
                    notifyDataSetChanged();
                }
            });
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {

            ContentViewHolder vhContent = (ContentViewHolder) holder;
            Content content;
            content = getCorrectContent(position);

            Log.i("Bei Aufbau: ", Integer.toString(content.getCategoryId()));

            mHolderHelper.setUpContentHolder(vhContent, content, false);

            // Date & Time
            if (mContentType == MyConstants.CONTENT_TASK || mContentType == MyConstants.CONTENT_EVENT) {
                TaskEvent taskEvent = (TaskEvent)content;
                if (taskEvent.getDate() == null && taskEvent.getSubtitle() == null) {
                    vhContent.llSubtitleDate.setVisibility(View.GONE);
                } else {
                    vhContent.llSubtitleDate.setVisibility(View.VISIBLE);
                    vhContent.tvSubtitle.setText(DateTimeTexter.getGeneral(taskEvent));
                }
            }


            // Handle Divider Visibility
            if (position+1 == mPositionHideShow || position == mContent.size()) {
                vhContent.vDivider.setVisibility(View.GONE);
            } else {
                vhContent.vDivider.setVisibility(View.VISIBLE);
            }

        } else {
            DividerViewHolder vhShowHide = (DividerViewHolder) holder;

            String content ="";
            if (mContentType == MyConstants.CONTENT_TASK) {
                content = mContext.getString(R.string.tasks);
            } else {
                content = mContext.getString(R.string.events);
            }

            // Set Text and Icon
            if (mContentHelper.getDoneContentSize(mContentType) == 0) {
                vhShowHide.tvText.setText("");
                vhShowHide.ivExpandCollapse.setVisibility(View.GONE);
            } else {
                vhShowHide.ivExpandCollapse.setVisibility(View.VISIBLE);
                vhShowHide.tvText.setText(mContext.getString(R.string.done)+" "+content);
                if (mExtended) {
                    vhShowHide.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expanded));

                } else {
                    vhShowHide.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_collapsed));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mContentType == MyConstants.CONTENT_NOTE) {
            return mContent.size();
        } else {
            return mContent.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mContentType != MyConstants.CONTENT_NOTE) {
            if (position == mPositionHideShow) {
                return TYPE_SHOW_HIDE;
            } else {
                return TYPE_CONTENT;
            }
        } else {
            return TYPE_CONTENT;
        }
    }

    public Content getCorrectContent(int position) {
        Log.i("Das ist", "die Position: "+Integer.toString(position));
        Log.i("Groesse von mContent ", Integer.toString(mContent.size()));
        if (mContentType == MyConstants.CONTENT_NOTE) {
            return mContent.get(position);
        } else {
            if (position == -1) {
                return null;
            }
            if (position == 0) {
                Log.i("Task Event Eric Id", Integer.toString(mContent.get(position).getCategoryId()));
                //Der Fehler liegt davor iwo..
                return mContent.get(position);

            }
            if (position < mPositionHideShow) {
                return mContent.get(position);
            } else {
                return mContent.get(position - 1);
            }
        }
    }

    private void handleExpandCollaps(boolean isExpanded) {
        if (!isExpanded) {
            // expand
            mContent = mContentHelper.getAllContent(mContentType);
            mPositionHideShow = mContentHelper.getUndoneContentSize(mContent, mContentType);
            mExtended = true;
        } else {
            // collapse
            mContent = mContentHelper.getAllUndoneContent(mContentType);
            mPositionHideShow = mContent.size();
            mExtended = false;
        }
        // Hier klappt alles
        mDataBaseHelper.updateCategoryShow(mCategoryId, mContentType, mExtended);
    }

    private void setContent() {
        if (mContentType == MyConstants.CONTENT_NOTE) {
            mContent = mContentHelper.getAllContent(mContentType);
        } else {
            if (mExtended) {
                mContent = mContentHelper.getAllContent(mContentType);
                Log.i("Länge Content ", Integer.toString(mContent.size())+ Integer.toString(mContentType));
                mPositionHideShow = mContentHelper.getUndoneContentSize(mContent, mContentType);
            } else {
                mContent = mContentHelper.getAllUndoneContent(mContentType);
                mPositionHideShow = mContent.size();
                Log.i("Länge Content ", Integer.toString(mContent.size()));
            }
        }
    }

    public boolean deleteContent(RecyclerView.ViewHolder viewHolder) {
            Content content = getCorrectContent(viewHolder.getAdapterPosition());
            mDataBaseHelper.deleteContent(content.getId(), mContentType);

        return mExtended;
    }

    public void update() {
        setContent();
        notifyDataSetChanged();
    }

    public boolean checkUncheckContent(RecyclerView.ViewHolder viewHolder) {
        Log.i("ContentAdapter", "Gecheckt ");

        Content c = getCorrectContent(viewHolder.getAdapterPosition());
        Log.i("checkUncheckContent", Integer.toString(c.getCategoryId()));
        TaskEvent taskEvent = (TaskEvent)c;
        if (taskEvent.isDone()) {
            taskEvent.setDate(null);
            taskEvent.setTime(null);
            taskEvent.setDone(false);
        } else {
            Calendar calendar = Calendar.getInstance();
            taskEvent.setDate(calendar);
            taskEvent.setTime(calendar);
            taskEvent.setPriority(0);
            taskEvent.setReminder(0);

            taskEvent.setDone(true);

            // Reminder löschen
            mDataBaseHelper.deleteContentReminder(c.getId(), c.getContentType());

        }
        mDataBaseHelper.updateContent(taskEvent);
        if (!taskEvent.isDone()) {
            startDetailActivity(c, MyConstants.DETAIL_GENERAL);
        }
        return taskEvent.isDone();
    }

    public Content getCurrentContent(RecyclerView.ViewHolder viewHolder) {
        return getCorrectContent(viewHolder.getAdapterPosition());
    }

    public boolean isExtended () {
        return mExtended;
    }

    private void startDetailActivity (Content content, int type) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CONTENT_TYPE, mContentType);
        bundle.putInt(MyConstants.CONTENT_ID, content.getId());
        bundle.putInt(MyConstants.DETAIL_TYPE, type);
        intent.putExtras(bundle);
        mMainActivity.startActivity(intent);
    }
}