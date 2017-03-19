package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses;

import android.content.Context;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

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
    int mDrawableId;

    // Files
    String mDescription;
    String mPicturePath;
    String mVideoPath;
    String mAudioPath;

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

    public String getAudioPath() {
        return mAudioPath;
    }

    public void setAudioPath(String mVoicePath) {
        this.mAudioPath = mVoicePath;
    }

    public boolean hasAudio() {
        return mAudioPath !=null;
    }

    public String getVideoPath() {
        return mVideoPath;
    }

    public void setVideoPath(String mVideoPath) {
        this.mVideoPath = mVideoPath;
    }

    public boolean hasVideo() {
        return mVideoPath !=null;
    }

    public int getContentType() {
        return mContentType;
    }

    public String getContentName(Context context) {
        switch (mContentType) {
            case 0:
                return context.getString(R.string.task);
            case 1:
                return context.getString(R.string.event);
            case 2:
                return context.getString(R.string.note);
            default:
                return null;
        }
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

    public int getDrawableId() {
        return mDrawableId;
    }

    public void setDrawableId(int mDrawableId) {
        this.mDrawableId = mDrawableId;
    }
}
