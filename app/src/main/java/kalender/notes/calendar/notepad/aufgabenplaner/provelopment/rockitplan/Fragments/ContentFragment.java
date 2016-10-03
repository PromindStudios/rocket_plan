package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DeleteContentDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentTimeInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.ContentAdapter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder.DividerViewHolder;

/**
 * Created by eric on 02.05.2016.
 */
public class ContentFragment extends Fragment implements ContentTimeInterface, DeleteContentDialog.DeleteContentDialogListener{

    private RecyclerView mRecyclerView;
    ContentAdapter mContentAdapter;
    int mContentType;
    int mCategoryId;
    String mCategoryName;
    MainActivity mMainActivity;
    List<Task> mTasks;
    DatabaseHelper mDatabaseHelper;
    boolean mIsExpanded;
    MainActivityListener mainActivityListener;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_content_time, container, false);

        Bundle bundle = getArguments();
        mContentType = bundle.getInt(MyConstants.CONTENT_TYPE);
        mCategoryId = bundle.getInt(MyConstants.CATEGORY_ID);
        mIsExpanded = bundle.getBoolean(MyConstants.IS_EXPANDED);
        mCategoryName = bundle.getString(MyConstants.CATEGORY_NAME);

        mDatabaseHelper = new DatabaseHelper(getActivity());

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mainActivityListener.colorHead(mCategoryId);

        setAdapterUp();
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
        mainActivityListener = (MainActivityListener)context;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("ContentFragment", "onResume ausgefuehrt");
        setAdapterUp();
    }

    public void delete(RecyclerView.ViewHolder viewHolder) {
        Content content = mContentAdapter.getCurrentContent(viewHolder);
        DialogFragment dialog = new DeleteContentDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CONTENT_ID, content.getId());
        bundle.putInt(MyConstants.CONTENT_TYPE, content.getContentType());
        bundle.putInt(MyConstants.CATEGORY_ID, content.getCategoryId());
        bundle.putBoolean(MyConstants.IS_EXPANDED, mContentAdapter.isExtended());
        dialog.setArguments(bundle);
        dialog.setTargetFragment(ContentFragment.this, 0);
        dialog.show(getActivity().getSupportFragmentManager(), "deleteContent");
    }

    @Override
    public void checkUncheck(RecyclerView.ViewHolder viewHolder) {
        boolean isDone = mContentAdapter.checkUncheckContent(viewHolder);
        if (isDone) {
            setAdapterUp();
        }


    }

    private void setAdapterUp() {

        mContentAdapter = new ContentAdapter(getActivity(), mCategoryId, mMainActivity, mContentType);
        mRecyclerView.setAdapter(mContentAdapter);
        MyMethods.setUpSwipeFunction(mContentType, mRecyclerView, this, mMainActivity, mContentAdapter);

    }

    @Override
    public void onDeleteContent(int contentId, int contentType, boolean isExtended) {
        mDatabaseHelper.deleteContent(contentId, contentType);
        setAdapterUp();
    }

    @Override
    public void onUpdateContent(boolean isExpanded) {
        setAdapterUp();
    }

    public interface MainActivityListener {
        public void colorHead(int categoryId);
    }

    public Content getContent(int position) {
        return mContentAdapter.getCorrectContent(position);
    }

}
