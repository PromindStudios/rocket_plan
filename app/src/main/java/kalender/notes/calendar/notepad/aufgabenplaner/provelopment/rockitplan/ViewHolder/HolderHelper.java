package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Subtask;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 18.07.2016.
 */
public class HolderHelper {


    Context mContext;
    DatabaseHelper mDatabaseHelper;
    int mContentType;

    public HolderHelper(Context context) {
        mContext = context;
        mDatabaseHelper = new DatabaseHelper(mContext);
    }

    public void setUpContentHolder(ContentViewHolder vhContent, Content content, boolean isTime) {
        Task task = null;
        Event event = null;
        TaskEvent taskEvent = null;
        mContentType = content.getContentType();
        Drawable contentIcon = null;

        if (mContentType == MyConstants.CONTENT_TASK) {
            task = (Task) content;
            taskEvent = (TaskEvent) content;
            contentIcon = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_task, null);
        }
        if (mContentType == MyConstants.CONTENT_EVENT) {
            event = (Event) content;
            taskEvent = (TaskEvent) content;
            contentIcon = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_event_text_16dp, null);
        }
        if (mContentType == MyConstants.CONTENT_NOTE) {
            contentIcon = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_note, null);
        }

        // Category
        vhContent.ivCategory.setVisibility(View.VISIBLE);
        Category category = mDatabaseHelper.getCategory(content.getCategoryId());
        CategoryColor categoryColor = new CategoryColor(mContext, category.getColor());
        vhContent.ivCategory.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), categoryColor.getCategoryColorLight(), null));

        // Priority
        vhContent.ivContent.setVisibility(View.VISIBLE);
        switch (content.getPriority()) {
            case MyConstants.PRIORITY_NONE:
                contentIcon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorSecondaryText, null), PorterDuff.Mode.MULTIPLY);
                break;
            case MyConstants.PRIORITY_NORMAL:
                contentIcon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorSecondaryText, null), PorterDuff.Mode.MULTIPLY);
                break;
            case MyConstants.PRIORITY_HIGH:
                contentIcon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.layout_color_light_five, null), PorterDuff.Mode.MULTIPLY);
                break;
            case MyConstants.PRIORITY_VERY_HIGH:
                contentIcon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.layout_color_light_one, null), PorterDuff.Mode.MULTIPLY);
                break;
        }
        vhContent.ivContent.setImageDrawable(contentIcon);

        // Title
        vhContent.tvTitle.setText(content.getTitle());


        // Files
        if (content.hasVideo() || content.hasAudio() || content.getPicturePath() != null || !content.getDescription().equals("")) {
            vhContent.llFiles.setVisibility(View.VISIBLE);
            Log.i("Zeige File", "doch" + content.getDescription());
        } else {
            vhContent.llFiles.setVisibility(View.GONE);
            Log.i("Zeige File", "nicht");
        }

        // Subtitle
        if (content.getSubtitle() != null) {
            vhContent.tvSubtitle.setText(content.getSubtitle());

            vhContent.llSubtitleDate.setVisibility(View.VISIBLE);
        } else {
            vhContent.llSubtitleDate.setVisibility(View.GONE);
        }

        // Date & Time

        /*
        if (mContentType == MyConstants.CONTENT_TASK || mContentType == MyConstants.CONTENT_EVENT) {
            if (taskEvent.getDate() == null && taskEvent.getSubtitle() == null) {
                vhContent.llSubtitleDate.setVisibility(View.GONE);
            } else {
                Calendar date = taskEvent.getDate();
                Calendar time = taskEvent.getTime();
                vhContent.llSubtitleDate.setVisibility(View.VISIBLE);
                if (taskEvent.getSubtitle() != null) {
                    vhContent.tvSubtitle.setVisibility(View.VISIBLE);
                } else {
                    vhContent.tvSubtitle.setVisibility(View.GONE);
                }

                if (date != null) {
                    vhContent.tvDate.setVisibility(View.VISIBLE);
                    if (taskEvent.isDone()) {
                        vhContent.tvDate.setText(mContext.getString(R.string.date_done_at) + " " + MyMethods.formatDate(date) + "  |  " + MyMethods.formatTime(time));
                    } else {
                        if (time != null) {
                            vhContent.tvDate.setText(MyMethods.formatDate(date) + "  |  " + MyMethods.formatTime(time));
                        } else {
                            vhContent.tvDate.setText(MyMethods.formatDate(date));
                        }

                    }
                } else {
                    vhContent.tvDate.setVisibility(View.GONE);
                }
                if (taskEvent.getDate() != null && taskEvent.getSubtitle() != null) {
                    vhContent.vSubtitleDateDivider.setVisibility(View.VISIBLE);
                } else {
                    vhContent.vSubtitleDateDivider.setVisibility(View.GONE);
                }
            }
        }
        */

        // Repetition
        if (mContentType == MyConstants.CONTENT_TASK || mContentType == MyConstants.CONTENT_EVENT) {
            if (taskEvent.getRepetitionType() != MyConstants.REPETITION_TYPE_NONE) {
                vhContent.ivRepeat.setVisibility(View.VISIBLE);
            } else {
                vhContent.ivRepeat.setVisibility(View.GONE);
            }
        }

        // Subtasks
        if (mContentType == MyConstants.CONTENT_TASK) {
            ArrayList<Subtask> subtasks = new ArrayList<>();
            subtasks = mDatabaseHelper.getAllContentSubtasks(task.getId());

            if (subtasks.size() > 0) {
                vhContent.tvSubtask.setVisibility(View.VISIBLE);
                vhContent.ivSubtask.setVisibility(View.VISIBLE);
                vhContent.llSubtask.setPadding(0,0, MyMethods.dpToPx(mContext, 16), 0);
                ArrayList<Subtask> doneSubtasks = new ArrayList<>();
                for (Subtask subtask : subtasks) {
                    if (subtask.isDone()) {
                        doneSubtasks.add(subtask);
                    }
                }
                String dSubtasks = Integer.toString(doneSubtasks.size());
                String aSubtasks = Integer.toString(subtasks.size());
                vhContent.tvSubtask.setText(dSubtasks + " / " + aSubtasks);
            } else {
                vhContent.tvSubtask.setVisibility(View.GONE);
                vhContent.ivSubtask.setVisibility(View.GONE);
                vhContent.llSubtask.setPadding(0,0,0,0);
            }
        } else {
            vhContent.tvSubtask.setVisibility(View.GONE);
            vhContent.ivSubtask.setVisibility(View.GONE);
            vhContent.llSubtask.setPadding(0,0,0,0);
        }
    }
}
