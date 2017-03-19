package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Reminder;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.TaskEvent;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeAdapterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.DividerViewHolder;

/**
 * Created by eric on 22.05.2016.
 */
public class MyMethods {

    public static String formatDate(Calendar calendar) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd. MMMM");
        fmt.setCalendar(calendar);
        Log.i("Datum", calendar.toString());
        String dateFormated = fmt.format(calendar.getTime());
        return dateFormated;
    }

    public static String formatWeekDay(Calendar calendar) {
        SimpleDateFormat fmt = new SimpleDateFormat("EEEE");
        fmt.setCalendar(calendar);
        String formated = fmt.format(calendar.getTime());
        return formated;
    }

    public static String formatTime(Calendar calendar) {
        SimpleDateFormat fmt = new SimpleDateFormat("k : mm");
        fmt.setCalendar(calendar);
        String dateFormated = fmt.format(calendar.getTime());
        return dateFormated;
    }

    public static String formatDateForCalendarTitle(Calendar calendar) {
        SimpleDateFormat fmt = new SimpleDateFormat("MMMM - yyyy");
        fmt.setCalendar(calendar);
        String dateFormated = fmt.format(calendar.getTime());
        return dateFormated;
    }

    public static Bitmap rotatePicture(String filename) {
        File pictureFile = new File(filename);
        Bitmap bitmap = null;
        Bitmap smallBitmap = null;
        try {
            int rotationangle = 0;
            FileInputStream in = new FileInputStream(pictureFile);
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeStream(in, null, options);
            smallBitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth()/4), (bitmap.getHeight()/4), false);
            ExifInterface exifInterface = new ExifInterface(pictureFile.getAbsolutePath());
            String orientationString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientationString != null ? Integer.parseInt(orientationString) : ExifInterface.ORIENTATION_NORMAL;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationangle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationangle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationangle = 270;
            Matrix matrix = new Matrix();
            matrix.setRotate(rotationangle, (float) smallBitmap.getWidth() / 2, (float) smallBitmap.getHeight() / 2);
            smallBitmap = Bitmap.createBitmap(smallBitmap, 0, 0, smallBitmap.getWidth(), smallBitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return smallBitmap;
    }


    public static Bitmap pictureFillScreen(Bitmap bitmap, int screenWidth) {
        double height = bitmap.getHeight();
        double width = bitmap.getWidth();
        double scaleFactor;
        Bitmap scaledBitmap;

        Log.i("screenWidth", Integer.toString(screenWidth));
        Log.i("pictureWidth", Double.toString(width));

        scaleFactor = width / screenWidth;
        Log.i("scaledFactor", Double.toString(scaleFactor));
        Double sh = height / scaleFactor;
        int scaledHeight = sh.intValue();
        scaledBitmap = bitmap.createScaledBitmap(bitmap, screenWidth, scaledHeight, true);

        return  scaledBitmap;

    }

    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pixelWidth = displayMetrics.widthPixels;
        return pixelWidth;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(px * density);
    }

    public static int getDaysTillDueDate(Calendar d) {
        Calendar t = Calendar.getInstance();
        DateTime today = new DateTime(t.get(Calendar.YEAR), t.get(Calendar.MONTH), t.get(Calendar.DAY_OF_MONTH), t.get(Calendar.HOUR_OF_DAY), t.get(Calendar.MINUTE));
        DateTime due = new DateTime(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH), d.get(Calendar.HOUR_OF_DAY), d.get(Calendar.MINUTE));
        Log.i("Daays", ""+Days.daysBetween(today, due).getDays());
        return Days.daysBetween(today, due).getDays();
    }

    public static void setUpSwipeFunction (final int contentType, RecyclerView recyclerView, ContentTimeInterface fragment, final MainActivity mainActivity, RecyclerView.Adapter adapter) {
        int swipeDirection = 0;
        final ContentTimeInterface targetFragment = (ContentTimeInterface)fragment;
        final ContentTimeAdapterInterface adapterListener = (ContentTimeAdapterInterface)adapter;
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
                    targetFragment.delete(viewHolder);
                } else {
                    targetFragment.checkUncheck(viewHolder);
                }
                mainActivity.updateDrawer();
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
                        TaskEvent taskEvent = (TaskEvent) targetFragment.getContent(viewHolder.getAdapterPosition());
                        //TaskEvent taskEvent = (TaskEvent) adapterListener.getCorrectContent(viewHolder.getAdapterPosition());
                        if (taskEvent != null && !taskEvent.isDone()) {
                            iconColor = 1;
                            Log.i("Naaame", taskEvent.getTitle()+" Position"+Integer.toString(viewHolder.getAdapterPosition()));
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

                        icon = BitmapFactory.decodeResource(mainActivity.getApplicationContext().getResources(), R.drawable.ic_camera);
                        p.setColor(ContextCompat.getColor(mainActivity, R.color.colorTransparent));
                        if (iconColor == 1) {
                            icon = BitmapFactory.decodeResource(mainActivity.getApplicationContext().getResources(), R.drawable.ic_done);
                            p.setColor(ContextCompat.getColor(mainActivity, R.color.colorGreen));
                        }
                        if (iconColor == 2) {
                            icon = BitmapFactory.decodeResource(mainActivity.getApplicationContext().getResources(), R.drawable.ic_refresh);
                            p.setColor(ContextCompat.getColor(mainActivity, R.color.colorOrange));
                        }
                        // Draw Rect with varying right side, equal to displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), p);

                        // Set the image icon for Right swipe
                        c.drawBitmap(icon,
                                (float) itemView.getLeft() + MyMethods.dpToPx(mainActivity.getApplicationContext(), 16),
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                                p);
                    } else {
                        icon = BitmapFactory.decodeResource(mainActivity.getApplicationContext().getResources(), R.drawable.ic_delete_white);
            /* Set your color for negative displacement */
                        p.setColor(ContextCompat.getColor(mainActivity, R.color.red_delete));
                        // Draw Rect with varying left side, equal to the item's right side
                        // plus negative displacement dX
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), p);
                        //Set the image icon for Left swipe
                        c.drawBitmap(icon, (float) itemView.getRight() - MyMethods.dpToPx(mainActivity.getApplicationContext(), 16) - icon.getWidth(),
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

    public static long getAlarmTimeInMillis(TaskEvent taskEvent, Reminder reminder) {
        Calendar alarmTime = Calendar.getInstance();
        Calendar dueTime = Calendar.getInstance();
        Log.i("Heute", dueTime.get(Calendar.YEAR)+" "+dueTime.get(Calendar.MONTH)+" "+dueTime.get(Calendar.DAY_OF_MONTH));
        Log.i("Heute2", dueTime.get(Calendar.HOUR_OF_DAY)+" "+dueTime.get(Calendar.MINUTE));
        Calendar date = taskEvent.getDate();
        Calendar time = taskEvent.getTime();
        dueTime.set(Calendar.YEAR, date.get(Calendar.YEAR));
        dueTime.set(Calendar.MONTH, date.get(Calendar.MONTH));
        dueTime.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
        Log.i("Duuuuu", date.get(Calendar.YEAR)+" "+date.get(Calendar.MONTH)+" "+date.get(Calendar.DAY_OF_MONTH));
        if (time != null) {
            Log.i("Icccch", time.get(Calendar.HOUR_OF_DAY)+" "+time.get(Calendar.MINUTE));
            dueTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
            dueTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
        }
        Log.i("Due", dueTime.get(Calendar.YEAR)+" "+dueTime.get(Calendar.MONTH)+" "+dueTime.get(Calendar.DAY_OF_MONTH));
        Log.i("Due2", dueTime.get(Calendar.HOUR_OF_DAY)+" "+dueTime.get(Calendar.MINUTE));

        // Subtract Time
        switch (reminder.getReminderType()) {
            case MyConstants.REMINDER_AT_DUE_TIME:
                alarmTime = dueTime;
                break;
            case MyConstants.REMINDER_MINUTE:
                dueTime.add(Calendar.MINUTE, -reminder.getReminderValue());
                alarmTime = dueTime;
                break;
            case MyConstants.REMINDER_HOUR:
                dueTime.add(Calendar.HOUR_OF_DAY, -reminder.getReminderValue());
                alarmTime = dueTime;
                break;
            case MyConstants.REMINDER_DAY:
                dueTime.add(Calendar.DAY_OF_MONTH, -reminder.getReminderValue());
                alarmTime = dueTime;
                break;
            case MyConstants.REMINDER_WEEK:
                dueTime.add(Calendar.WEEK_OF_YEAR, -reminder.getReminderValue());
                alarmTime = dueTime;
                break;
        }
        Log.i("Alarm", alarmTime.get(Calendar.YEAR)+" "+alarmTime.get(Calendar.MONTH)+" "+alarmTime.get(Calendar.DAY_OF_MONTH));
        Log.i("Alarm2", alarmTime.get(Calendar.HOUR_OF_DAY)+" "+alarmTime.get(Calendar.MINUTE));

        return alarmTime.getTimeInMillis();
    }

    public static String getContentName (Context context, int contentType) {
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                return context.getString(R.string.task);
            case MyConstants.CONTENT_EVENT:
                return context.getString(R.string.event);
            case MyConstants.CONTENT_NOTE:
                return context.getString(R.string.note);
            default:
                return "Call Eric for help";
        }
    }

}
