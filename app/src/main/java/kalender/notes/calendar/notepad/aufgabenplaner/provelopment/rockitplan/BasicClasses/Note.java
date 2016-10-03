package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

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
    }

    public Note () {

    }

}
