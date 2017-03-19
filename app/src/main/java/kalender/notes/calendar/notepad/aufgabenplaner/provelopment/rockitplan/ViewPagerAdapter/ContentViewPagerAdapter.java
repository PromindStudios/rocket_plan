package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Main_Fragments.ContentListFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;

/**
 * Created by eric on 02.05.2016.
 */
public class ContentViewPagerAdapter extends FragmentStatePagerAdapter{

    private int mTabNumber;
    ContentListFragment tabTask;
    ContentListFragment tabEvent;
    ContentListFragment tabNote;
    int mCategoryId;
    String mCategoryName;
    Map<Integer, ContentListFragment> mFragmentList;

    public ContentViewPagerAdapter(FragmentManager fm, int tabNumber, int categoryId, String categoryName) {
        super(fm);
        mTabNumber = tabNumber;
        mCategoryId = categoryId;
        mCategoryName = categoryName;
        mFragmentList = new HashMap<Integer, ContentListFragment>();
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                tabTask = new ContentListFragment();
                bundle.putInt(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_TASK);
                bundle.putInt(MyConstants.CATEGORY_ID, mCategoryId);
                bundle.putString(MyConstants.CATEGORY_NAME, mCategoryName);
                tabTask.setArguments(bundle);
                mFragmentList.put(position, tabTask);
                return tabTask;
            case 1:
                tabEvent = new ContentListFragment();
                bundle.putInt(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_EVENT);
                bundle.putInt(MyConstants.CATEGORY_ID, mCategoryId);
                bundle.putString(MyConstants.CATEGORY_NAME, mCategoryName);
                tabEvent.setArguments(bundle);
                mFragmentList.put(position, tabEvent);
                return tabEvent;
            case 2:
                tabNote = new ContentListFragment();
                bundle.putInt(MyConstants.CONTENT_TYPE, MyConstants.CONTENT_NOTE);
                bundle.putInt(MyConstants.CATEGORY_ID, mCategoryId);
                bundle.putString(MyConstants.CATEGORY_NAME, mCategoryName);
                tabNote.setArguments(bundle);
                mFragmentList.put(position, tabNote);
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

    public void destroyItem (ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragmentList.remove(position);
    }

    public ContentListFragment getFragment(int key) {
        return mFragmentList.get(key);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
