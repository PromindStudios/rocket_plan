package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.LayoutColorInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.SettingsAdapter;

/**
 * Created by Eric on 12.01.2017.
 */

public class SettingsActivity extends AppCompatActivity implements LayoutColorInterface {

    RecyclerView rvSettings;
    Toolbar mToolbar;
    LayoutColor mLayoutColor;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout set up
        setContentView(R.layout.activity_settings);
        rvSettings = (RecyclerView)findViewById(R.id.rvSettings);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_settings);

        // Create DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(this);

        // LayoutColor set up
        mLayoutColor = new LayoutColor(this, 0);
        refreshActivity();

        // Allow back navigation through arrow in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // RecyclerView
        SettingsAdapter rvAdapter = new SettingsAdapter(this, getSupportFragmentManager());
        rvSettings.setLayoutManager(new LinearLayoutManager(this));
        rvSettings.setAdapter(rvAdapter);

        // Connection to MainActivity
        setResult(Activity.RESULT_OK);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshActivity() {

        // Layout color value
        mLayoutColor.setColorValue(mDatabaseHelper.getLayoutColorValue());

        // Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.settings));
        mToolbar.setBackgroundColor(mLayoutColor.getLayoutColor());

        // Status Bar
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mLayoutColor.getLayoutColor());
        }
    }
}
