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
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Subtask;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ActionModeInterface;
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

    public void setUpContentHolder(ContentViewHolder vhContent, Content content, boolean isTime, ActionModeInterface mActionModeInterface, ArrayList<Content> mContentListDelete) {
        Task task = null;
        Event event = null;
        TaskEvent taskEvent = null;
        mContentType = content.getContentType();
        Drawable contentIcon = null;

        boolean detailsVisible;
        // Visibility
        if (isTime) {
            detailsVisible = mContext.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0).getBoolean(MyConstants.VISIBILITY_DETAILS_TIME_PAGER, true);
        } else {
            detailsVisible = mContext.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0).getBoolean(MyConstants.VISIBILITY_DETAILS_CONTENT_PAGER, true);
        }


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
        if (isTime && detailsVisible) {
            vhContent.ivCategory.setVisibility(View.VISIBLE);
            Category category = mDatabaseHelper.getCategory(content.getCategoryId());
            CategoryColor categoryColor = new CategoryColor(mContext, category.getColor());
            vhContent.ivCategory.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), categoryColor.getCategoryColorLight(), null));
        }


        // Layout Color
        LayoutColor layoutColor = new LayoutColor(mContext, mDatabaseHelper.getLayoutColorValue());

        // Priority
        vhContent.ivContent.setVisibility(View.VISIBLE);
        switch (content.getPriority()) {
            case 2:
                contentIcon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), layoutColor.getLayoutColorId(), null), PorterDuff.Mode.MULTIPLY);
                break;
            case MyConstants.PRIORITY_NORMAL:
                contentIcon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorSecondaryText, null), PorterDuff.Mode.MULTIPLY);
                break;
            case MyConstants.PRIORITY_HIGH:
                contentIcon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), layoutColor.getLayoutColorId(), null), PorterDuff.Mode.MULTIPLY);
                break;
            case 3:
                contentIcon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), layoutColor.getLayoutColorId(), null), PorterDuff.Mode.MULTIPLY);
                break;
        }
        vhContent.ivContent.setImageDrawable(contentIcon);

        // Title
        vhContent.tvTitle.setText(content.getTitle());

        // Files
        if ((content.hasVideo() || content.hasAudio() || content.getPicturePath() != null || !content.getDescription().equals("")) && detailsVisible) {
            vhContent.ivFiles.setVisibility(View.VISIBLE);
            Log.i("Zeige File", "doch" + content.getDescription());
        } else {
            vhContent.ivFiles.setVisibility(View.GONE);
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

        // Repetition
        if (mContentType == MyConstants.CONTENT_TASK || mContentType == MyConstants.CONTENT_EVENT) {
            if (taskEvent.getRepetitionType() != MyConstants.REPETITION_TYPE_NONE) {
                vhContent.ivRepeat.setVisibility(View.VISIBLE);
            } else {
                vhContent.ivRepeat.setVisibility(View.GONE);
            }
        }

        // Subtasks
        if (mContentType == MyConstants.CONTENT_TASK && detailsVisible) {
            ArrayList<Subtask> subtasks = new ArrayList<>();
            subtasks = mDatabaseHelper.getAllContentSubtasks(task.getId());

            if (subtasks.size() > 0) {
                vhContent.tvSubtask.setVisibility(View.VISIBLE);
                vhContent.ivSubtaskDetails.setVisibility(View.VISIBLE);
                vhContent.ivSubtaskDetails.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_subtask_16dp, null));
                /*
                if ((content.hasVideo() || content.hasAudio() || content.getPicturePath() != null || !content.getDescription().equals(""))) {
                    vhContent.llSubtaskDetails.setPadding(0,0, MyMethods.dpToPx(mContext, 8), 0);
                } else {
                    vhContent.llSubtaskDetails.setPadding(0,0, MyMethods.dpToPx(mContext, 17), 0);
                }
                */

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
                vhContent.ivSubtaskDetails.setVisibility(View.GONE);
                //vhContent.llSubtaskDetails.setPadding(0,0,0,0);
            }
        } else {
            vhContent.tvSubtask.setVisibility(View.GONE);
            vhContent.ivSubtaskDetails.setVisibility(View.GONE);
            //vhContent.llSubtaskDetails.setPadding(0,0,0,0);
        }

        // Details
        if (mContentType == MyConstants.CONTENT_EVENT && detailsVisible) {
            if ((event.getLocation() != null && !event.getLocation().equals("")) || mDatabaseHelper.getAllContentParticipants(event.getId()).size() > 0) {
                vhContent.ivSubtaskDetails.setVisibility(View.VISIBLE);
                vhContent.ivSubtaskDetails.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_details_16dp, null));
                /*
                if ((content.hasVideo() || content.hasAudio() || content.getPicturePath() != null || !content.getDescription().equals(""))) {
                    vhContent.llSubtaskDetails.setPadding(0,0, MyMethods.dpToPx(mContext, 8), 0);
                } else {
                    vhContent.llSubtaskDetails.setPadding(0,0, MyMethods.dpToPx(mContext, 17), 0);
                }
                */
            } else {
                vhContent.tvSubtask.setVisibility(View.GONE);
                //vhContent.llSubtaskDetails.setPadding(0,0,0,0);
            }

        }

        // ActionMode
        if (mActionModeInterface.isActionModeActive()) {
            vhContent.rbActionMode.setVisibility(View.VISIBLE);
            vhContent.ivContent.setVisibility(View.GONE);
            vhContent.rbActionMode.setChecked(mContentListDelete.contains(content));

        } else {
            vhContent.rbActionMode.setVisibility(View.GONE);
            vhContent.ivContent.setVisibility(View.VISIBLE);
        }


    }
}
