package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 05.05.2016.
 */
public class Note extends Content{

    public Note (int categoryId, String categoryName) {
        mCategoryId = categoryId;
        mCategory = categoryName;
        mTitle = "";
        mSubtitle = null;
        mPriority = 0;
        mDescription = "";
        mPicturePath = null;
        mVideoPath = null;
        mAudioPath = null;
        mDrawableId = R.drawable.ic_note;
    }

    public Note () {
        mDrawableId = R.drawable.ic_note;
    }

}
