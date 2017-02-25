package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.SettingsActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditCategoryDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DeleteContentDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.LayoutColorDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.PremiumInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.CategoryAdapter;

/**
 * Created by eric on 04.04.2016.
 */
public class DrawerFragment extends Fragment implements AddEditCategoryDialog.AddEditDialogCategoryListener, DeleteContentDialog.DeleteContentDialogListener {

    private RecyclerView mRecyclerView;
    private CategoryAdapter mCategoryAdapter;
    DatabaseHelper mDatabaseHelper;
    TextView tvHelp;
    ImageButton ibSettings;
    ImageButton ibColor;
    ImageView ivHead;
    LayoutColor mLayoutColor;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    com.getbase.floatingactionbutton.FloatingActionButton myFab;

    PremiumInterface mPremiumInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_drawer, container, false);

        // Initiate layout components
        tvHelp = (TextView)layout.findViewById(R.id.tvHelpCategory);
        ivHead = (ImageView) layout.findViewById(R.id.ivHeader);
        myFab = (com.getbase.floatingactionbutton.FloatingActionButton) layout.findViewById(R.id.myFab);
        ibColor = (ImageButton)layout.findViewById(R.id.ibColor);
        ibSettings = (ImageButton)layout.findViewById(R.id.ibSettings);

        // Initiate Interface
        mPremiumInterface = (PremiumInterface)getActivity();

        // Initiate SharedPreferences
        mSharedPreferences = getActivity().getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0);
        mEditor = mSharedPreferences.edit();

        // Initiate database helper and get categories
        mDatabaseHelper = new DatabaseHelper(getActivity());

        // Set up recycler view
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rvCategory);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Handle Click Events

        myFab.setColorNormal(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        myFab.setColorPressed(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putBoolean(MyConstants.IS_START_CATEGORY, false).commit();
                DialogFragment dialog = new AddEditCategoryDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(MyConstants.DIALOGE_TYPE, MyConstants.DIALOGE_CATEGORY_ADD);
                dialog.setArguments(bundle);
                dialog.setTargetFragment(DrawerFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "Add_Category");
            }
        });
        updateDrawer();

        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(), SettingsActivity.class), MyConstants.REQUEST_ACTIVITY_SETTINGS);
            }
        });

        ibColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPremiumInterface.isPremium()) {
                    LayoutColorDialog dialog = new LayoutColorDialog();
                    dialog.show(getActivity().getSupportFragmentManager(), "layout_color_dialog");
                } else {
                    mPremiumInterface.openDialogPremiumFunction(getString(R.string.premium_function), getString(R.string.premium_silver_colors), getString(R.string.premium_expired));;
                }
            }
        });

        // Color Layout
        mLayoutColor = new LayoutColor(getActivity(), mDatabaseHelper.getLayoutColorValue());

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        colorLayout();
    }

    public void addCategory() {
        mEditor.putBoolean(MyConstants.IS_START_CATEGORY, false).commit();
        DialogFragment dialog = new AddEditCategoryDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.DIALOGE_TYPE, MyConstants.DIALOGE_CATEGORY_ADD);
        dialog.setArguments(bundle);
        dialog.setTargetFragment(DrawerFragment.this, 0);
        dialog.show(getActivity().getSupportFragmentManager(), "Add_Category");
    }

    @Override
    public void onUpdateData() {
        updateDrawer();
    }

    public void updateDrawer() {
        ArrayList<Category> categories = new ArrayList<>();
        categories = mDatabaseHelper.getAllCategories();

        if (categories.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            myFab.setVisibility(View.VISIBLE);
            tvHelp.setVisibility(View.VISIBLE);
            boolean isStart = mSharedPreferences.getBoolean(MyConstants.IS_START_CATEGORY, true);
            if (isStart) {
                tvHelp.setText(getActivity().getString(R.string.help_add_category_and_welcome));
            } else {
                tvHelp.setText(getActivity().getString(R.string.help_add_category));
            }
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            myFab.setVisibility(View.GONE);
            tvHelp.setVisibility(View.GONE);
            mCategoryAdapter = new CategoryAdapter(getActivity(), categories, mDatabaseHelper, DrawerFragment.this, getActivity().getSupportFragmentManager());
            mRecyclerView.setAdapter(mCategoryAdapter);
        }
    }

    @Override
    public void onDeleteContent(int contentId, int contentType) {
        mDatabaseHelper.deleteCategory(mDatabaseHelper.getCategory(contentId));
        updateDrawer();
    }

    @Override
    public void onUpdateContent() {
    }

    public void colorLayout() {
        mLayoutColor.setColorValue(mDatabaseHelper.getLayoutColorValue());
        ivHead.setBackgroundColor(mLayoutColor.getLayoutColor());
        ArrayList<Category> categories = new ArrayList<>();
        categories = mDatabaseHelper.getAllCategories();
        mCategoryAdapter = new CategoryAdapter(getActivity(), categories, mDatabaseHelper, DrawerFragment.this, getActivity().getSupportFragmentManager());
        mRecyclerView.setAdapter(mCategoryAdapter);
    }
}
