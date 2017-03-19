package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 25.08.2016.
 */
public class InformationDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_information, null);
        ImageButton ibSave = (ImageButton)dialogView.findViewById(R.id.ibSave);
        TextView tvInfo = (TextView)dialogView.findViewById(R.id.tvInformation);
        TextView tvTitle = (TextView)dialogView.findViewById(R.id.tvTitle);
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationDialog.this.getDialog().cancel();
            }
        });
        tvInfo.setText(bundle.getString(MyConstants.DIALOGE_CONTENT));
        tvTitle.setText(bundle.getString(MyConstants.DIALOGE_TITLE));
        builder.setView(dialogView);
        return builder.create();
    }
}
