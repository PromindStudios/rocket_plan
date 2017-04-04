package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.Introduction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 26.03.2017.
 */

public class ContentListIntroductionDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_introduction_content_list, null);

        ImageView ivAction = (ImageView)dialogView.findViewById(R.id.ibAction);

        boolean isStart = getArguments().getBoolean(MyConstants.DIALOG_INTRODUCTION_IS_START);

        if (isStart) {
            ivAction.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_done_green, null));
            ivAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentListIntroductionDialog.this.getDialog().cancel();
                }
            });
        } else {
            ivAction.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_done_green, null));
            ivAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentListIntroductionDialog.this.getDialog().cancel();
                }
            });
        }

        builder.setView(dialogView);
        Dialog dialog = builder.create();
        return dialog;
    }

}
