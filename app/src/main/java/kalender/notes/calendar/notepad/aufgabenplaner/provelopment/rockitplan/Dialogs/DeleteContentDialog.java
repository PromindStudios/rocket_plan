package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 17.09.2016.
 */
public class DeleteContentDialog extends DialogFragment {

    DeleteContentDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mListener = (DeleteContentDialogListener)getTargetFragment();
        Bundle arguments = getArguments();
        final int contentId = arguments.getInt(MyConstants.CONTENT_ID);
        final int contentType = arguments.getInt(MyConstants.CONTENT_TYPE);
        int categoryId = arguments.getInt(MyConstants.CATEGORY_ID);
        final boolean isExtended = arguments.getBoolean(MyConstants.IS_EXPANDED);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_content, null);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        ImageButton ibSave = (ImageButton) dialogView.findViewById(R.id.ibSave);
        ImageButton ibExit = (ImageButton) dialogView.findViewById(R.id.ibExit);

        tvTitle.setText(getActivity().getString(R.string.delete)+"?");

        // handle Color
        DatabaseHelper dh = new DatabaseHelper(getActivity());
        Category category = dh.getCategory(categoryId);
        final CategoryColor categoryColor = new CategoryColor(getActivity(), category.getColor());
        tvTitle.setBackgroundColor(ContextCompat.getColor(getActivity(), categoryColor.getCategoryColor()));

        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUpdateContent(isExtended);
                DeleteContentDialog.this.getDialog().cancel();
            }
        });
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteContent(contentId, contentType, isExtended);
                DeleteContentDialog.this.getDialog().cancel();
            }
        });


        builder.setView(dialogView);
        setCancelable(false);
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;

    }



    public interface DeleteContentDialogListener {
        public void onDeleteContent(int contentId, int contentType, boolean isExtended);
        public void onUpdateContent(boolean isExtended);
    }
}
