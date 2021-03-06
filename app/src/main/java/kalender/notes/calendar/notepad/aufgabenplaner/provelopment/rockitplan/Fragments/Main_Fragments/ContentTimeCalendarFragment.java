package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Main_Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ActionMode.ActionModeCallback;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.TaskEventActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DateTimeTexter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DeleteContentDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ActionModeInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.BodyManagerInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeAdapterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContextualMenuFragmentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ToolbarInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.UpdateInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.ContentTimeCalendarAdapter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.DividerViewHolder;

/**
 * Created by Eric on 17.10.2016.
 */

public class ContentTimeCalendarFragment extends Fragment implements UpdateInterface, ContextualMenuFragmentInterface, ActionModeInterface {

    MainActivity mMainActivity;
    public ContentTimeAdapterInterface mAdapterListener;
    public DatabaseHelper mDatabaseHelper;
    ContentTimeCalendarAdapter mAdapter;

    BodyManagerInterface mBodyManagerInterface;
    ToolbarInterface mToolbarInterface;

    // ActionMode
    ActionMode mActionMode;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
        mBodyManagerInterface = (BodyManagerInterface)context;
        mToolbarInterface = (ToolbarInterface)context;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mActionMode != null) {
                mActionMode.finish();
            }
        }
    }

    public void removeActionMode() {
        if (mActionMode != null)
            mActionMode = null;
        mToolbarInterface.setToolbarOverlay(false);
        mAdapter.update();
    }

    public void deleteRows() {
        mAdapter.deleteContentListDelete();
        mActionMode.finish();
    }

    // Content functions

    private void requestDeleteContent(RecyclerView.ViewHolder viewHolder) {
        final Content content = mAdapterListener.getContent(viewHolder);
        if (content.getContentType() == MyConstants.CONTENT_TASK || content.getContentType() == MyConstants.CONTENT_EVENT) {
            final TaskEvent taskEvent = (TaskEvent) content;
            if (taskEvent.getRepetitionType() != MyConstants.REPETITION_TYPE_NONE) {
                String stringOne;
                String stringTwo;
                if (content.getContentType() == MyConstants.CONTENT_TASK) {
                    stringOne = getActivity().getString(R.string.repetition_delete_task);
                    stringTwo = getActivity().getString(R.string.repetition_delete_all_tasks);
                } else {
                    stringOne = getActivity().getString(R.string.repetition_delete_event);
                    stringTwo = getActivity().getString(R.string.repetition_delete_all_events);
                }
                CharSequence[] items = {stringOne, stringTwo};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                startDetailActivity(taskEvent, mDatabaseHelper.createRepeatTaskEvent(taskEvent));
                                mDatabaseHelper.deleteContent(content.getId(), content.getContentType());
                                updateList();
                                break;
                            case 1:
                                mDatabaseHelper.deleteContent(content.getId(), content.getContentType());
                                updateList();
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.create().show();
            } else {
                DialogFragment dialog = new DeleteContentDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(MyConstants.CONTENT_ID, content.getId());
                bundle.putInt(MyConstants.CONTENT_TYPE, content.getContentType());
                dialog.setArguments(bundle);
                dialog.setTargetFragment(ContentTimeCalendarFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "deleteContentTime");
            }
        } else {
            DialogFragment dialog = new DeleteContentDialog();
            Bundle bundle = new Bundle();
            bundle.putInt(MyConstants.CONTENT_ID, content.getId());
            bundle.putInt(MyConstants.CONTENT_TYPE, content.getContentType());
            dialog.setArguments(bundle);
            dialog.setTargetFragment(ContentTimeCalendarFragment.this, 0);
            dialog.show(getActivity().getSupportFragmentManager(), "deleteContentTime");
        }
    }

    private void checkUncheckContent(RecyclerView.ViewHolder viewHolder) {
        TaskEvent taskEvent = (TaskEvent) mAdapterListener.getContent(viewHolder);
        if (taskEvent.isDone()) {
            taskEvent.setDate(null);
            taskEvent.setTime(null);
            taskEvent.setDone(false);

            startDetailActivity(taskEvent, taskEvent.getId());
        } else {
            if (taskEvent.getRepetitionType() != MyConstants.REPETITION_TYPE_NONE) {
                // Create new Content
                startDetailActivity(taskEvent, mDatabaseHelper.createRepeatTaskEvent(taskEvent));
            }
            Calendar calendar = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            taskEvent.setDate(calendar);
            taskEvent.setTime(calendar2);
            taskEvent.setPriority(0);
            taskEvent.setReminder(0);
            taskEvent.setDone(true);
            taskEvent.setRepetitionType(MyConstants.REPETITION_TYPE_NONE);
            taskEvent.setRepetitionValue(0);
            Log.i("STILLL CRAZY", DateTimeTexter.getNormal(taskEvent));
            Log.i("WIEEE WEER", ""+taskEvent.getTime().get(Calendar.HOUR_OF_DAY));
        }

        // Reminder löschen
        mDatabaseHelper.deleteContentReminder(taskEvent.getId(), taskEvent.getContentType());
        mDatabaseHelper.updateContent(taskEvent);
        mMainActivity.updateDrawer();

        setAdapterUp();
    }

    public Content getContent(int position) {
        return mAdapterListener.getContent(position);
    }

    protected void setAdapterUp() {
    }

    private void startDetailActivity(Content content, int contentId) {
        Intent intent = new Intent(getActivity(), TaskEventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CONTENT_TYPE, content.getContentType());
        bundle.putInt(MyConstants.CONTENT_ID, contentId);
        bundle.putInt(MyConstants.DETAIL_TYPE, MyConstants.DETAIL_GENERAL);
        intent.putExtras(bundle);
        mMainActivity.startActivity(intent);
    }

    public void setSwipeFunction(final int contentType, RecyclerView recyclerView) {
        int swipeDirection = 0;
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                swipeDirection = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                break;
            case MyConstants.CONTENT_EVENT:
                swipeDirection = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                break;
            case MyConstants.CONTENT_NOTE:
                swipeDirection = ItemTouchHelper.LEFT;
                break;
            case MyConstants.CONTENT_TASK_EVENT:
                swipeDirection = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        }

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, swipeDirection) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    requestDeleteContent(viewHolder);
                } else {
                    checkUncheckContent(viewHolder);
                }
                mMainActivity.updateDrawer();
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof DividerViewHolder) return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final float ALPHA_FULL = 1.0f;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    int iconColor = 0; // 0 --> kein Icon und Farbe , 1 --> SetDone , 2 --> SetUndone

                    if (contentType != MyConstants.CONTENT_NOTE) {
                        TaskEvent taskEvent = (TaskEvent) mAdapterListener.getContent(viewHolder.getAdapterPosition());
                        //TaskEvent taskEvent = (TaskEvent) adapterListener.getCorrectContent(viewHolder.getAdapterPosition());
                        if (taskEvent != null && !taskEvent.isDone()) {
                            iconColor = 1;
                        }
                        if (taskEvent != null && taskEvent.isDone()) {
                            iconColor = 2;
                        }
                    }

                    View itemView = viewHolder.itemView;
                    Paint p = new Paint();
                    Bitmap icon;
                    if (dX > 0) {
                        // swip right

                        icon = BitmapFactory.decodeResource(mMainActivity.getApplicationContext().getResources(), R.drawable.ic_camera);
                        p.setColor(ContextCompat.getColor(mMainActivity, R.color.colorTransparent));
                        if (iconColor == 1) {
                            icon = BitmapFactory.decodeResource(mMainActivity.getApplicationContext().getResources(), R.drawable.ic_done);
                            p.setColor(ContextCompat.getColor(mMainActivity, R.color.colorGreen));
                        }
                        if (iconColor == 2) {
                            icon = BitmapFactory.decodeResource(mMainActivity.getApplicationContext().getResources(), R.drawable.ic_refresh);
                            p.setColor(ContextCompat.getColor(mMainActivity, R.color.colorOrange));
                        }
                        // Draw Rect with varying right side, equal to displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), p);

                        // Set the image icon for Right swipe
                        c.drawBitmap(icon,
                                (float) itemView.getLeft() + MyMethods.dpToPx(mMainActivity.getApplicationContext(), 16),
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                                p);
                    } else {
                        icon = BitmapFactory.decodeResource(mMainActivity.getApplicationContext().getResources(), R.drawable.ic_delete_white);
            /* Set your color for negative displacement */
                        p.setColor(ContextCompat.getColor(mMainActivity, R.color.red_delete));
                        // Draw Rect with varying left side, equal to the item's right side
                        // plus negative displacement dX
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), p);
                        //Set the image icon for Left swipe
                        c.drawBitmap(icon, (float) itemView.getRight() - MyMethods.dpToPx(mMainActivity.getApplicationContext(), 16) - icon.getWidth(),
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2, p);
                    }
                    // Fade out the view as it is swiped out of the parent's bounds
                    final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void updateList() {
        setAdapterUp();
        mMainActivity.updateDrawer();
    }

    @Override
    public void startActionMode() {
        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionModeCallback(getActivity(), this));
        mToolbarInterface.setToolbarOverlay(true);
    }

    @Override
    public boolean isActionModeActive() {
        return mActionMode!=null;
    }
}
