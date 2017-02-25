package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.FrameLayout;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.FilesFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.GeneralFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 19.02.2017.
 */

public class NoteActivity extends DetailActivity implements GeneralFragment.DetailActivityListener {

    FrameLayout flContainer;
    FilesFragment mFilesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_content_note);
        super.onCreate(savedInstanceState);

        flContainer = (FrameLayout)findViewById(R.id.flNote);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mFilesFragment = new FilesFragment();
        fragmentTransaction.add(R.id.flNote, mFilesFragment);
        fragmentTransaction.commit();

        ivContent.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_note_24dp, null));
        tvContent.setText(getString(R.string.note));


        mContentType = MyConstants.CONTENT_NOTE;
        mContent = mDatabaseHelper.getContent(mContentId, mContentType);
        mCategory = mDatabaseHelper.getCategory(mContent.getCategoryId());

        cbTitle.setVisibility(View.GONE);
    }

    public void endActivity(boolean delete) {
        if (mContent.getTitle().equals("") || delete) {
            mDatabaseHelper.deleteContent(mContent.getId(), mContentType);
        }
        finish();
    }

    @Override
    public void selectTab() {

    }

    @Override
    public void selectTabTwo() {

    }

    @Override
    public void setToolbarTitle(String title) {

    }

    @Override
    public int getCurrentTab() {
        return 0;
    }

    @Override
    public FilesFragment getFilesFragment() {
        return mFilesFragment;
    }
}
