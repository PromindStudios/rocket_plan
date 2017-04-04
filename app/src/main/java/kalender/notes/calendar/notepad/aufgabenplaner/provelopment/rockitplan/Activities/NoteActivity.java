package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.GeneralFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.NotesFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 19.02.2017.
 */

public class NoteActivity extends ContentActivity implements GeneralFragment.DetailActivityListener {

    FrameLayout flContainer;
    NotesFragment mNotesFragment;

    ImageView ivContent;
    TextView tvContent;
    TextView tvCategory;

    View vDummy;
    EditText etTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_content_note);
        super.onCreate(savedInstanceState);

        flContainer = (FrameLayout)findViewById(R.id.flNote);

        ivContent = (ImageView)findViewById(R.id.ivContenT);
        tvContent = (TextView)findViewById(R.id.tvContenT);
        tvCategory = (TextView)findViewById(R.id.tvCategory);

        vDummy = findViewById(R.id.vDummy);
        etTitle = (EditText)findViewById(R.id.etTitle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mNotesFragment = new NotesFragment();
        fragmentTransaction.add(R.id.flNote, mNotesFragment);
        fragmentTransaction.commit();

        mContentType = MyConstants.CONTENT_NOTE;
        mContent = mDatabaseHelper.getContent(mContentId, mContentType);
        mCategory = mDatabaseHelper.getCategory(mContent.getCategoryId());

        // Set up Category and Content
        tvCategory.setText(getString(R.string.category)+": "+mContent.getCategory());
        tvContent.setText(getString(R.string.note));
        ivContent.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_note_24dp, null));

        // Title
        if (mContent.getTitle().equals("")) {
            etTitle.requestFocus();
        }

        etTitle.setText(mContent.getTitle());

        etTitle.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {
                mContent.setTitle(editable.toString());
            }
        });


        etTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    closeKeyboard();
                    vDummy.requestFocus();
                }
                return false;
            }
        });

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

    public void closeKeyboard() {
        //((TaskEventActivity)getActivity()).closeKeyboard();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etTitle.getWindowToken(), 0);
    }
}
