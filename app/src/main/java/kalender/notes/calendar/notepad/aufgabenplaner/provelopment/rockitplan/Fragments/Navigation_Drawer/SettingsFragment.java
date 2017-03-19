package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Navigation_Drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.SettingsAdapter;

/**
 * Created by Eric on 05.03.2017.
 */

public class SettingsFragment extends Fragment {

    RecyclerView rvSettings;
    Toolbar mToolbar;
    LayoutColor mLayoutColor;
    DatabaseHelper mDatabaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);

        rvSettings = (RecyclerView)layout.findViewById(R.id.rvSettings);

        // RecyclerView
        SettingsAdapter rvAdapter = new SettingsAdapter(getActivity(), getActivity().getSupportFragmentManager());
        rvSettings.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSettings.setAdapter(rvAdapter);


        return layout;
    }
}
