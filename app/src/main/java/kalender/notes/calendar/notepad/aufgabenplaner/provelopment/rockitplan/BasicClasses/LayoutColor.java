package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 15.01.2017.
 */

public class LayoutColor {

    Context mContext;
    int mColor;
    int[] mLayoutColors;
    int[] mLayoutColorsDark;
    int[] mLayoutColorsLight;
    String[] mLayoutColorNames;

    public LayoutColor(Context context, int color) {
        mContext = context;
        mColor = color;

        mLayoutColors = new int[6];
        mLayoutColors[0] = R.color.layout_color_one;
        mLayoutColors[1] = R.color.layout_color_two;
        mLayoutColors[2] = R.color.layout_color_three;
        mLayoutColors[3] = R.color.layout_color_four;
        mLayoutColors[4] = R.color.layout_color_five;
        mLayoutColors[5] = R.color.layout_color_six;

        mLayoutColorsLight = new int[6];
        mLayoutColorsLight[0] = R.color.layout_color_light_one;
        mLayoutColorsLight[1] = R.color.layout_color_light_two;
        mLayoutColorsLight[2] = R.color.layout_color_light_three;
        mLayoutColorsLight[3] = R.color.layout_color_light_four;
        mLayoutColorsLight[4] = R.color.layout_color_light_five;
        mLayoutColorsLight[5] = R.color.layout_color_light_six;

        mLayoutColorsDark = new int[6];
        mLayoutColorsDark[0] = R.color.layout_color_dark_one;
        mLayoutColorsDark[1] = R.color.layout_color_dark_two;
        mLayoutColorsDark[2] = R.color.layout_color_dark_three;
        mLayoutColorsDark[3] = R.color.layout_color_dark_four;
        mLayoutColorsDark[4] = R.color.layout_color_dark_five;
        mLayoutColorsDark[5] = R.color.layout_color_dark_six;

        mLayoutColorNames = new String[6];
        mLayoutColorNames[0] = mContext.getString(R.string.color_layout_one);
        mLayoutColorNames[1] = mContext.getString(R.string.color_layout_two);
        mLayoutColorNames[2] = mContext.getString(R.string.color_layout_three);
        mLayoutColorNames[3] = mContext.getString(R.string.color_layout_four);
        mLayoutColorNames[4] = mContext.getString(R.string.color_layout_five);
        mLayoutColorNames[5] = mContext.getString(R.string.color_layout_six);
    }

    public void setColorValue(int color) {
        mColor = color;
    }

    public int getLayoutColor() {
        return ContextCompat.getColor(mContext, mLayoutColors[mColor]);
    }

    public int getLayoutColorDark() {
        return ContextCompat.getColor(mContext, mLayoutColorsDark[mColor]);
    }

    public int getLayoutColorLight() {
        return ContextCompat.getColor(mContext, mLayoutColorsLight[mColor]);
    }

    public int getLayoutColorId() {
        return mLayoutColors[mColor];
    }

    public String getLayoutColorName() {
        return mLayoutColorNames[mColor];
    }

    public int getLayoutColorValue() {
        return mColor;
    }
}
