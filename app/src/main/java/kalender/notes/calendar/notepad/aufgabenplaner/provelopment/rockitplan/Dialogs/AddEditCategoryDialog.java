package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.DrawerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 06.08.2016.
 */
public class AddEditCategoryDialog extends DialogFragment {

    AddEditDialogCategoryListener mListener;
    int category_id;
    int color = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

        Bundle arguments = getArguments();
        int dialog_type = arguments.getInt(MyConstants.DIALOGE_TYPE);

        final CategoryColor categoryColor = new CategoryColor(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_category, null);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        final TextView tvColor = (TextView) dialogView.findViewById(R.id.tvColor);
        ImageButton ibSave = (ImageButton) dialogView.findViewById(R.id.ibSave);
        ImageButton ibExit = (ImageButton) dialogView.findViewById(R.id.ibExit);
        RelativeLayout rlColor = (RelativeLayout)dialogView.findViewById(R.id.rlColor);
        final View vColor = dialogView.findViewById(R.id.vColor);
        final EditText etEingabe = (EditText) dialogView.findViewById(R.id.edit_text);
        etEingabe.requestFocus();

        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CategoryAddDialog: ", "Close");
                AddEditCategoryDialog.this.getDialog().cancel();
            }
        });
        rlColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {getActivity().getString(R.string.color_one), getActivity().getString(R.string.color_two), getActivity().getString(R.string.color_three), getActivity().getString(R.string.color_four), getActivity().getString(R.string.color_five), getActivity().getString(R.string.color_six)};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                color = 0;
                                break;
                            case 1:
                                color = 1;
                                break;
                            case 2:
                                color = 2;
                                break;
                            case 3:
                                color = 3;
                                break;
                            case 4:
                                color = 4;
                                break;
                            case 5:
                                color = 5;
                                break;
                            default:
                                break;
                        }
                        categoryColor.setColor(color);
                        tvColor.setText(categoryColor.getCategoryColorName());
                        vColor.setBackgroundColor(ResourcesCompat.getColor(getActivity().getResources(), categoryColor.getCategoryColor(), null));
                    }

                });
                builder.create().show();
            }
        });


        switch (dialog_type) {
            case MyConstants.DIALOGE_CATEGORY_ADD:
                mListener = (DrawerFragment) getTargetFragment();
                tvTitle.setText(getActivity().getString(R.string.title_add_category));
                etEingabe.setHint(getActivity().getString(R.string.hint_dialog_add));
                tvColor.setText(categoryColor.getCategoryColorName());
                vColor.setBackgroundColor(ResourcesCompat.getColor(getActivity().getResources(), categoryColor.getCategoryColor(), null));

                ibSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String eingabe = etEingabe.getText().toString();
                        if (!eingabe.matches("")) {
                            DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                            Category category = new Category(eingabe, color);
                            databaseHelper.createCategory(category);
                            Log.i("CategoryAddDialog: ", "Save");
                            mListener.onUpdateData();
                            AddEditCategoryDialog.this.getDialog().cancel();
                        } else {
                            Log.i("CategoryAddDialog: ", "EditText empty");
                            AddEditCategoryDialog.this.getDialog().cancel();
                        }
                    }
                });

                break;

            case MyConstants.DIALOGE_CATEGORY_EDIT:
                mListener = (DrawerFragment) getTargetFragment();
                tvTitle.setText(getActivity().getString(R.string.title_edit_category));
                category_id = arguments.getInt(MyConstants.CATEGORY_ID);
                final Category category = databaseHelper.getCategory(category_id);
                etEingabe.setText(category.getTitle());

                categoryColor.setColor(category.getColor());
                color = category.getColor();
                tvColor.setText(categoryColor.getCategoryColorName());
                vColor.setBackgroundColor(ResourcesCompat.getColor(getActivity().getResources(), categoryColor.getCategoryColor(), null));

                ibSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String eingabe = etEingabe.getText().toString();
                        if (!eingabe.matches("")) {
                            category.setTitle(eingabe);
                            category.setColor(color);
                            databaseHelper.updateCategory(category);
                            Log.i("CategoryAddDialog: ", "Save");
                            mListener.onUpdateData();
                            AddEditCategoryDialog.this.getDialog().cancel();
                        } else {
                            Log.i("CategoryAddDialog: ", "EditText empty");
                            AddEditCategoryDialog.this.getDialog().cancel();
                        }
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

    public interface AddEditDialogCategoryListener {
        public void onUpdateData();
    }
}
