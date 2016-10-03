package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.FilesFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.GeneralFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.SubtaskFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;

/**
 * Created by eric on 04.05.2016.
 */
public class DetailViewPagerAdapter extends FragmentStatePagerAdapter{

    private int mTabNumber;
    private int mContentType;

    FilesFragment mFilesFragment;
    GeneralFragment mGeneralFragment;
    SubtaskFragment mSubtaskFragment;


    public DetailViewPagerAdapter(FragmentManager fm, int tabNumber, int contentType) {
        super(fm);
        mTabNumber = tabNumber;
        mContentType = contentType;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CONTENT_TYPE, mContentType);
        switch (position) {
            case 0:
                mGeneralFragment = new GeneralFragment();
                mGeneralFragment.setArguments(bundle);
                return mGeneralFragment;
            case 1:
                mFilesFragment = new FilesFragment();
                mFilesFragment.setArguments(bundle);
                return mFilesFragment;
            case 2:
                mSubtaskFragment = new SubtaskFragment();
                mSubtaskFragment.setArguments(bundle);
                return mSubtaskFragment;
            default:
                return null;
        }
    }

    public void handleFocus(int position) {
        if (position == MyConstants.DETAIL_GENERAL) {
            mGeneralFragment.handleFocus();
        }
        if (position == MyConstants.DETAIL_FILES) {
            mFilesFragment.handleFocus();
        }
        if (position == MyConstants.DETAIL_SUBTASK) {
            if (mSubtaskFragment != null) {
                mSubtaskFragment.closeKeyboard();
            }

        }
    }

    @Override
    public int getCount() {
        return mTabNumber;
    }
}
