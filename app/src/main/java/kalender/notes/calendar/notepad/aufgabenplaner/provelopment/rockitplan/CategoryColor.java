package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

/**
 * Created by Admin on 06.08.2016.
 */
public class CategoryColor {


    Context mContext;
    public int mColor;
    int[] mCategoryColor;
    int[] mCategoryColorLight;
    int[] mCategoryColorDark;
    int[][] mStates;

    ColorStateList mCategoryColorStateList;

    public CategoryColor (Context context, int color) {
        mContext = context;
        mColor = color;

        // set States
        mStates = new int[][] {new int[] { android.R.attr.state_enabled},};

        mCategoryColor = new int[5];
        mCategoryColor[0] = R.color.category_one;
        mCategoryColor[1] = R.color.category_two;
        mCategoryColor[2] = R.color.category_three;
        mCategoryColor[3] = R.color.category_four;
        mCategoryColor[4] = R.color.category_five;

        mCategoryColorLight = new int[5];
        mCategoryColorLight[0] = R.color.category_light_one;
        mCategoryColorLight[1] = R.color.category_light_two;
        mCategoryColorLight[2] = R.color.category_light_three;
        mCategoryColorLight[3] = R.color.category_light_four;
        mCategoryColorLight[4] = R.color.category_light_five;

        mCategoryColorDark = new int[5];
        mCategoryColorLight[0] = R.color.category_dark_one;
        mCategoryColorLight[1] = R.color.category_dark_two;
        mCategoryColorLight[2] = R.color.category_dark_three;
        mCategoryColorLight[3] = R.color.category_dark_four;
        mCategoryColorLight[4] = R.color.category_dark_five;
    }

    public CategoryColor (Context context) {
        mContext = context;
        mColor = 0;

        // set States
        mStates = new int[][] {new int[] { android.R.attr.state_enabled},};

        mCategoryColor = new int[5];
        mCategoryColor[0] = R.color.category_one;
        mCategoryColor[1] = R.color.category_two;
        mCategoryColor[2] = R.color.category_three;
        mCategoryColor[3] = R.color.category_four;
        mCategoryColor[4] = R.color.category_five;

        mCategoryColorLight = new int[5];
        mCategoryColorLight[0] = R.color.category_light_one;
        mCategoryColorLight[1] = R.color.category_light_two;
        mCategoryColorLight[2] = R.color.category_light_three;
        mCategoryColorLight[3] = R.color.category_light_four;
        mCategoryColorLight[4] = R.color.category_light_five;

        mCategoryColorDark = new int[5];
        mCategoryColorLight[0] = R.color.category_dark_one;
        mCategoryColorLight[1] = R.color.category_dark_two;
        mCategoryColorLight[2] = R.color.category_dark_three;
        mCategoryColorLight[3] = R.color.category_dark_four;
        mCategoryColorLight[4] = R.color.category_dark_five;
    }

    public int getCategoryColor() {

        return mCategoryColor[mColor];
    }

    public ColorStateList getCategoryColorStateList() {

        int[] categoryColor = new int[] {
                mCategoryColor[mColor]
        };

        mCategoryColorStateList = new ColorStateList(mStates, categoryColor);
        return mCategoryColorStateList;
    }

    public int getCategoryColorLight() {

        return mCategoryColorLight[mColor];
    }

    public int getCategoryColorDark() {

        return mCategoryColorDark[mColor];
    }

    public void setColor (int color) {
        mColor = color;
    }

    public Drawable colorIcon(Drawable icon) {
        icon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), mCategoryColor[mColor], null), PorterDuff.Mode.MULTIPLY);
        return icon;
    }

    public Drawable colorIconLight(Drawable icon) {
        icon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), mCategoryColorLight[mColor], null), PorterDuff.Mode.MULTIPLY);
        return icon;
    }

}
