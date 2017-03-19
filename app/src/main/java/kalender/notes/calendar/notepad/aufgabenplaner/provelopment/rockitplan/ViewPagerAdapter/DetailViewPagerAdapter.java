package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.DetailsFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.NotesFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.GeneralFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.SubtasksFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;

/**
 * Created by eric on 04.05.2016.
 */
public class DetailViewPagerAdapter extends FragmentStatePagerAdapter{

    private int mTabNumber;
    private int mContentType;

    NotesFragment mNotesFragment;
    GeneralFragment mGeneralFragment;
    SubtasksFragment mSubtasksFragment;
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
                    mSubtasksFragment = new SubtasksFragment();
                    mSubtasksFragment.setArguments(bundle);
                    mFragmentList.add(position, mSubtasksFragment);
                    return mSubtasksFragment;
                } else {
                    mDetailFragment = new DetailsFragment();
                    mDetailFragment.setArguments(bundle);
                    mFragmentList.add(position, mDetailFragment);
                    return mDetailFragment;
                }
            case 2:
                mNotesFragment = new NotesFragment();
                mFragmentList.add(position, mNotesFragment);
                return mNotesFragment;

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
            if (mNotesFragment != null) mNotesFragment.handleFocus();
        }
        if (position == MyConstants.DETAIL_SUBTASK) {
            if (mSubtasksFragment != null) {
                mSubtasksFragment.closeKeyboard();
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
