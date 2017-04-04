package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.Introduction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 25.03.2017.
 */

public class WelcomeDialog extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_welcome, null);

        ImageView ivNext = (ImageView)dialogView.findViewById(R.id.ivNext);
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WelcomeDialog.this.getDialog().cancel();
                CategoryIntroductionDialog dialog = new CategoryIntroductionDialog();
                Bundle arg = new Bundle();
                arg.putBoolean(MyConstants.DIALOG_INTRODUCTION_IS_START, true);
                dialog.setArguments(arg);
                dialog.show(getActivity().getSupportFragmentManager(), "intro");
            }
        });


        builder.setView(dialogView);
        setCancelable(false);
        Dialog dialog = builder.create();
        return dialog;
    }
}
