package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.ContentFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;

/**
 * Created by eric on 02.05.2016.
 */
public class ContentViewPagerAdapter extends FragmentStatePagerAdapter{

    private int mTabNumber;
    ContentFragment tabTask;
    ContentFragment tabEvent;
    ContentFragment tabNote;
    int mCategoryId;
    String mCategoryName;
    boolean mIsExpanded;

    public ContentViewPagerAdapter(FragmentManager fm, int tabNumber, int categoryId, String categoryName, boolean isExpanded) {
        super(fm);
        mTabNumber = tabNumber;
        mCategoryId = categoryId;
        mCategoryName = categoryName;
        mIsExpanded = isExpanded;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                tabTask = new ContentFragment();
                bundle.putInt(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_TASK);
                bundle.putInt(MyConstants.CATEGORY_ID, mCategoryId);
                bundle.putString(MyConstants.CATEGORY_NAME, mCategoryName);
                bundle.putBoolean(MyConstants.IS_EXPANDED, mIsExpanded);
                tabTask.setArguments(bundle);
                return tabTask;
            case 1:
                tabEvent = new ContentFragment();
                bundle.putInt(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_EVENT);
                bundle.putInt(MyConstants.CATEGORY_ID, mCategoryId);
                bundle.putString(MyConstants.CATEGORY_NAME, mCategoryName);
                tabEvent.setArguments(bundle);

                return tabEvent;
            case 2:
                tabNote = new ContentFragment();
                bundle.putInt(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_NOTE);
                bundle.putInt(MyConstants.CATEGORY_ID, mCategoryId);
                bundle.putString(MyConstants.CATEGORY_NAME, mCategoryName);
                tabNote.setArguments(bundle);

                return tabNote;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabNumber;
    }

    public void categoryChange(int category_id) {
        Log.i("MainActivity: ", "received the click - " + Integer.toString(category_id));
        mCategoryId = category_id;
        notifyDataSetChanged();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
