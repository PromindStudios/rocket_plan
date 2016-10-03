package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AboutDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditCategoryDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.CategoryAdapter;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 04.04.2016.
 */
public class DrawerFragment extends Fragment implements AddEditCategoryDialog.AddEditDialogCategoryListener {

    private RecyclerView mRecyclerView;
    private CategoryAdapter mCategoryAdapter;
    DatabaseHelper mDatabaseHelper;
    Context mContext;
    TextView tvInfo;
    ImageButton ibSettings;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_drawer, container, false);

        // Initiate database helper and get categories
        mDatabaseHelper = new DatabaseHelper(getActivity());
        ArrayList<Category> categories = mDatabaseHelper.getAllCategories();

        // Set up recycler view
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rvCategory);
        mCategoryAdapter = new CategoryAdapter(getActivity(), categories, mDatabaseHelper, DrawerFragment.this, getActivity().getSupportFragmentManager());
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*
        FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.fabCategory);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new AddEditCategoryDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(MyConstants.DIALOGE_TYPE, MyConstants.DIALOGE_CATEGORY_ADD);
                dialog.setArguments(bundle);
                dialog.setTargetFragment(DrawerFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "Add_Category");
            }
        });
        */

        com.getbase.floatingactionbutton.FloatingActionButton myFab = (com.getbase.floatingactionbutton.FloatingActionButton) layout.findViewById(R.id.myFab);
        myFab.setColorNormal(ResourcesCompat.getColor(getResources(), R.color.colorWhite, null));
        myFab.setColorPressed(ResourcesCompat.getColor(getResources(), R.color.white_pressed, null));
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new AddEditCategoryDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(MyConstants.DIALOGE_TYPE, MyConstants.DIALOGE_CATEGORY_ADD);
                dialog.setArguments(bundle);
                dialog.setTargetFragment(DrawerFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "Add_Category");
            }
        });

        // Button settings
        ibSettings = (ImageButton)layout.findViewById(R.id.ibSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new AboutDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "dialog_about");
            }
        });

        // Set current date
        tvInfo = (TextView) layout.findViewById(R.id.tvInfo);
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("cccc', 'dd'.' MMM yyyy");
        tvInfo.setText(sdf.format(today.getTime()));
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set current date
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("cccc', 'dd'.' MMMM yyyy");
        tvInfo.setText(sdf.format(today.getTime()));
    }

    @Override
    public void onUpdateData() {
        mCategoryAdapter.updateData();
    }

    public void updateRecyclerView() {
        ArrayList<Category> categories = mDatabaseHelper.getAllCategories();
        mCategoryAdapter = new CategoryAdapter(getActivity(), categories, mDatabaseHelper, DrawerFragment.this, getActivity().getSupportFragmentManager());
        mRecyclerView.setAdapter(mCategoryAdapter);
    }

}
