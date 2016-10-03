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

import org.w3c.dom.Text;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 25.08.2016.
 */
public class AboutDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_information, null);
        ImageButton ibSave = (ImageButton)dialogView.findViewById(R.id.ibSave);
        TextView tvInfo = (TextView)dialogView.findViewById(R.id.tvInformation);
        TextView tvTitle = (TextView)dialogView.findViewById(R.id.tvTitle);
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutDialog.this.getDialog().cancel();
            }
        });
        tvInfo.setText(getActivity().getString(R.string.dialog_about_text));
        tvTitle.setText(getActivity().getString(R.string.title_about));
        builder.setView(dialogView);
        return builder.create();
    }
}
