package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.LayoutColorInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 16.01.2017.
 */

public class LayoutColorDialog extends DialogFragment {

    LayoutColorInterface mSettingsActivityListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // DatabaseHelper
        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

        // Layout Color
        final int[] layoutColor = {databaseHelper.getLayoutColorValue()};
        int lc = databaseHelper.getLayoutColorValue();
        final LayoutColor layoutColorClass = new LayoutColor(getActivity(), lc);

        // Layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_layout_color, null);
        final TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        RadioGroup rgLayoutColor = (RadioGroup)dialogView.findViewById(R.id.rgLayoutColor);
        RadioButton rbOne = (RadioButton)dialogView.findViewById(R.id.rbOne);
        RadioButton rbTwo = (RadioButton)dialogView.findViewById(R.id.rbTwo);
        RadioButton rbThree = (RadioButton)dialogView.findViewById(R.id.rbThree);
        RadioButton rbFour = (RadioButton)dialogView.findViewById(R.id.rbFour);
        RadioButton rbFive = (RadioButton)dialogView.findViewById(R.id.rbFive);
        RadioButton rbSix = (RadioButton)dialogView.findViewById(R.id.rbSix);
        ImageButton ibSave = (ImageButton) dialogView.findViewById(R.id.ibSave);
        ImageButton ibExit = (ImageButton) dialogView.findViewById(R.id.ibExit);

        // Title
        tvTitle.setText(getActivity().getString(R.string.layout_color));
        tvTitle.setBackgroundColor(layoutColorClass.getLayoutColor());

        // Color
        if (Build.VERSION.SDK_INT >= 21) {
            rbOne.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.layout_color_one)));
            rbTwo.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.layout_color_two)));
            rbThree.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.layout_color_three)));
            rbFour.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.layout_color_four)));
            rbFive.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.layout_color_five)));
            rbSix.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.layout_color_six)));
        } else {
            int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            rbOne.setButtonDrawable(id);
            rbTwo.setButtonDrawable(id);
            rbThree.setButtonDrawable(id);
            rbFour.setButtonDrawable(id);
            rbFive.setButtonDrawable(id);
            rbSix.setButtonDrawable(id);
        }

        // RadioGroup and Buttons
        rgLayoutColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbOne:
                        layoutColor[0] = 0;
                        layoutColorClass.setColorValue(0);
                        break;
                    case R.id.rbTwo:
                        layoutColor[0] = 1;
                        layoutColorClass.setColorValue(1);
                        break;
                    case R.id.rbThree:
                        layoutColor[0] = 2;
                        layoutColorClass.setColorValue(2);
                        break;
                    case R.id.rbFour:
                        layoutColor[0] = 3;
                        layoutColorClass.setColorValue(3);
                        break;
                    case R.id.rbFive:
                        layoutColor[0] = 4;
                        layoutColorClass.setColorValue(4);
                        break;
                    case R.id.rbSix:
                        layoutColor[0] = 5;
                        layoutColorClass.setColorValue(5);
                        break;
                    default:
                        break;
                }

                tvTitle.setBackgroundColor(layoutColorClass.getLayoutColor());
            }
        });

        switch (lc) {
            case 0:
                rgLayoutColor.check(R.id.rbOne);
                break;
            case 1:
                rgLayoutColor.check(R.id.rbTwo);
                break;
            case 2:
                rgLayoutColor.check(R.id.rbThree);
                break;
            case 3:
                rgLayoutColor.check(R.id.rbFour);
                break;
            case 4:
                rgLayoutColor.check(R.id.rbFive);
                break;
            case 5:
                rgLayoutColor.check(R.id.rbSix);
                break;
            default:
                break;
        }

        // Click Events
        ibExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutColorDialog.this.getDialog().cancel();
            }
        });
        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.setLayoutColorValue(layoutColor[0]);
                mSettingsActivityListener.refreshLayoutColor();
                LayoutColorDialog.this.getDialog().cancel();
            }
        });

        builder.setView(dialogView);
        setCancelable(false);
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSettingsActivityListener = (LayoutColorInterface) context;
    }
}
