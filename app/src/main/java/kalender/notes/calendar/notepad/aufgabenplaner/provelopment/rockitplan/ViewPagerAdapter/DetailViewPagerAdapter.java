package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.DetailsFragment;
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
    DetailsFragment mDetailFragment;
    ArrayList<Fragment> mFragmentList;


    public DetailViewPagerAdapter(FragmentManager fm, int tabNumber, int contentType) {
        super(fm);
        mFragmentList = new ArrayList<>();
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
                mFragmentList.add(position, mGeneralFragment);
                return mGeneralFragment;
            case 1:
                if (mContentType == MyConstants.CONTENT_TASK) {
                    mSubtaskFragment = new SubtaskFragment();
                    mSubtaskFragment.setArguments(bundle);
                    mFragmentList.add(position, mSubtaskFragment);
                    return mSubtaskFragment;
                } else {
                    mDetailFragment = new DetailsFragment();
                    mDetailFragment.setArguments(bundle);
                    mFragmentList.add(position, mDetailFragment);
                    return mDetailFragment;
                }
            case 2:
                mFilesFragment = new FilesFragment();
                mFragmentList.add(position, mFilesFragment);
                return mFilesFragment;

            default:
                return null;
        }
    }

    public Fragment getInstance(int position) {
        return mFragmentList.get(position);
    }

    public void handleFocus(int position) {
        if (position == MyConstants.DETAIL_GENERAL) {
            if (mGeneralFragment != null) mGeneralFragment.handleFocus();
        }
        if (position == MyConstants.DETAIL_FILES) {
            if (mFilesFragment != null) mFilesFragment.handleFocus();
        }
        if (position == MyConstants.DETAIL_SUBTASK) {
            if (mSubtaskFragment != null) {
                mSubtaskFragment.closeKeyboard();
            }
            if (mDetailFragment != null) {
                mDetailFragment.closeKeyboard();
            }

        }
    }

    @Override
    public int getCount() {
        return mTabNumber;
    }
}
