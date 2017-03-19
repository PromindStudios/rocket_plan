package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.UpdateInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 08.03.2017.
 */

public class DeleteContentDialog extends DialogFragment {

    UpdateInterface mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mListener = (UpdateInterface)getTargetFragment();
        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Bundle arguments = getArguments();
        final int contentType = arguments.getInt(MyConstants.CONTENT_TYPE);
        final int contentId = arguments.getInt(MyConstants.CONTENT_ID);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_content, null);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        ImageButton ibSave = (ImageButton) dialogView.findViewById(R.id.ibSave);
        ImageButton ibExit = (ImageButton) dialogView.findViewById(R.id.ibExit);

        tvTitle.setText(getActivity().getString(R.string.delete)+"?");

        // handle Color
        DatabaseHelper dh = new DatabaseHelper(getActivity());
        LayoutColor layoutColor = new LayoutColor(getActivity(), databaseHelper.getLayoutColorValue());
        tvTitle.setBackgroundColor(layoutColor.getLayoutColor());

        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteContentDialog.this.getDialog().cancel();
            }
        });
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteContent(contentId, contentType);
                mListener.updateList();
                DeleteContentDialog.this.getDialog().cancel();
            }
        });


        builder.setView(dialogView);
        setCancelable(false);
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;

    }
}
