package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 15.11.2016.
 */

public class ChooseCategoryActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.tab_general);
        mDatabaseHelper = new DatabaseHelper(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));


        final int contentType = getIntent().getIntExtra(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_TASK);

        // Let user select category
        final ArrayList<Category> categories = mDatabaseHelper.getAllCategories();
        if (categories.size() > 0) {
            List<String> listItems = new ArrayList<>();
            for (Category category : categories) {
                listItems.add(category.getTitle());
            }
            CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.alert_dialog_category_title)+" "+ MyMethods.getContentName(ChooseCategoryActivity.this, contentType));
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    finish();
                }
            });

            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    Category category = categories.get(item);
                    startDetailActivity(category.getId(), category.getTitle(), contentType);
                }
            });
            builder.create().show();
        } else {
            // Toast with hint to create Category first + Open Drawer
            Toast.makeText(this, getString(R.string.toast_add_category), Toast.LENGTH_LONG).show();
            try {
                // open Drawer here
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void startDetailActivity(int categoryId, String categoryName, int contentType) {
        int contentId = 0;
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                Task task = new Task(categoryId, categoryName);
                contentId = mDatabaseHelper.createTask(task);
                break;
            case MyConstants.CONTENT_EVENT:
                Event event = new Event(categoryId, categoryName);
                contentId = mDatabaseHelper.createEvent(event);
                break;
            case MyConstants.CONTENT_NOTE:
                Note note = new Note(categoryId, categoryName);
                contentId = mDatabaseHelper.createNote(note);
        }
        Intent intent = new Intent(this, TaskEventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CONTENT_TYPE, contentType);
        bundle.putInt(MyConstants.CONTENT_ID, contentId);
        bundle.putInt(MyConstants.DETAIL_TYPE, MyConstants.DETAIL_GENERAL);
        bundle.putInt(MyConstants.CATEGORY_ID, categoryId);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
