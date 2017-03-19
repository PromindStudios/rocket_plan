package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.FrameLayout;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.NotesFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.GeneralFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 19.02.2017.
 */

public class NoteActivity extends ContentActivity implements GeneralFragment.DetailActivityListener {

    FrameLayout flContainer;
    NotesFragment mNotesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_content_note);
        super.onCreate(savedInstanceState);

        flContainer = (FrameLayout)findViewById(R.id.flNote);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mNotesFragment = new NotesFragment();
        fragmentTransaction.add(R.id.flNote, mNotesFragment);
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
    public NotesFragment getFilesFragment() {
        return mNotesFragment;
    }
}
