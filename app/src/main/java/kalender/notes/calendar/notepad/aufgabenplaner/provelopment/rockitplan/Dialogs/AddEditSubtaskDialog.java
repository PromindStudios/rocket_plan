package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;


import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Subtask;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.SubtaskFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 16.05.2016.
 */
public class AddEditSubtaskDialog extends DialogFragment {

    AddEditDialogListener mListener;
    int category_id;
    int task_id;
    int subtask_id;
    EditText etEingabe;
    Bundle arguments;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

        arguments = getArguments();
        final int dialog_type = arguments.getInt(MyConstants.DIALOGE_TYPE);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_subtask, null);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        ImageButton ibSave = (ImageButton) dialogView.findViewById(R.id.ibSave);
        ImageButton ibExit = (ImageButton) dialogView.findViewById(R.id.ibExit);
        ImageButton ibAdd = (ImageButton) dialogView.findViewById(R.id.ibAdd);
        etEingabe = (EditText) dialogView.findViewById(R.id.edit_text);
        etEingabe.requestFocus();

        // handle Color
        int categoryId = getArguments().getInt(MyConstants.CATEGORY_ID);
        DatabaseHelper dh = new DatabaseHelper(getActivity());
        Category category = dh.getCategory(categoryId);

        CategoryColor mCategoryColor = new CategoryColor(getActivity(), category.getColor());
        tvTitle.setBackgroundColor(ContextCompat.getColor(getActivity(), mCategoryColor.getCategoryColor()));

        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CategoryAddDialog: ", "Close");
                AddEditSubtaskDialog.this.getDialog().cancel();
            }
        });

        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubtask(dialog_type, etEingabe.getText().toString());
                mListener.openSubtaskDialog();

            }
        });

        mListener = (SubtaskFragment) getTargetFragment();

        switch (dialog_type) {

            case MyConstants.DIALOGE_SUBTASK_ADD:
                tvTitle.setText(getActivity().getString(R.string.title_add_subtask));
                etEingabe.setHint(getActivity().getString(R.string.hint_dialog_add));

                ibSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String eingabe = etEingabe.getText().toString();
                        addSubtask(dialog_type, eingabe);
                    }
                });
                break;
            case MyConstants.DIALOGE_SUBTASK_EDIT:
                tvTitle.setText(getActivity().getString(R.string.title_edit_category));
                subtask_id = arguments.getInt(MyConstants.SUBTASK_ID);
                final Subtask subtask = databaseHelper.getSubtask(subtask_id);
                etEingabe.setText(subtask.getTitle());
                ibSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String eingabe = etEingabe.getText().toString();
                        addSubtask(dialog_type, eingabe);
                    }
                });
                break;

            default:
                break;
        }


        builder.setView(dialogView);
        setCancelable(false);
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    public interface AddEditDialogListener {
        public void onUpdateData();
        public void openSubtaskDialog();
    }

    private void addSubtask(int dialogType, String eingabe) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        if (dialogType == MyConstants.DIALOGE_SUBTASK_ADD) {
            task_id = getArguments().getInt(MyConstants.TASK_ID);
            if (!eingabe.matches("")) {
                Subtask subtask2 = new Subtask(task_id, eingabe);
                databaseHelper.createSubtask(subtask2);
                Log.i("CategoryAddDialog: ", "Save");
                mListener.onUpdateData();
                AddEditSubtaskDialog.this.getDialog().cancel();
            } else {
                Log.i("CategoryAddDialog: ", "EditText empty");
                AddEditSubtaskDialog.this.getDialog().cancel();
            }
        } else {
            subtask_id = arguments.getInt(MyConstants.SUBTASK_ID);
            final Subtask subtask = databaseHelper.getSubtask(subtask_id);
            if (!eingabe.matches("")) {
                subtask.setTitle(eingabe);
                databaseHelper.updateSubtask(subtask);
                Log.i("CategoryAddDialog: ", "Save");
                mListener.onUpdateData();
                AddEditSubtaskDialog.this.getDialog().cancel();
            } else {
                Log.i("CategoryAddDialog: ", "EditText empty");
                AddEditSubtaskDialog.this.getDialog().cancel();
            }
        }
    }
}
