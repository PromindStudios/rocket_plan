package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

/**
 * Created by Admin on 03.07.2016.
 */
public class Content {

    // Id & Category
    int id;
    int mCategoryId;
    String mCategory;

    // General
    String mTitle;
    String mSubtitle;
    int mPriority;
    int mContentType;
    String mTable;

    // Files
    String mDescription;
    String mPicturePath;
    String mVideoPath;
    String mVoicePath;

    public Content() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getPicturePath() {
        return mPicturePath;
    }

    public void setPicturePath(String mPicturePath) {
        this.mPicturePath = mPicturePath;
    }

    public String getVoicePath() {
        return mVoicePath;
    }

    public void setVoicePath(String mVoicePath) {
        this.mVoicePath = mVoicePath;
    }

    public String getVideoPath() {
        return mVideoPath;
    }

    public void setVideoPath(String mVideoPath) {
        this.mVideoPath = mVideoPath;
    }

    public int getContentType() {
        return mContentType;
    }

    public void setContentType(int contentType) {
        mContentType = contentType;
    }

    public String getTable() {
        return mTable;
    }

    public void setTable(String table) {
        mTable = table;
    }
}
