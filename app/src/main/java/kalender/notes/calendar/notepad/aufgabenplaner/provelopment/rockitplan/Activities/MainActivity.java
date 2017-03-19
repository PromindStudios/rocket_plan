package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.AnalyticsApplication;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.AppWidget.AppWidgetProvider;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditCategoryDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DeleteCategoryDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.PremiumDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Main_Fragments.CalendarFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Main_Fragments.ContentListPagerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Navigation_Drawer.DrawerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Main_Fragments.OverviewPagerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.AnalyticsInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.BodyManagerInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.ContentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.LayoutColorInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.PremiumInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.StarterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.Functions;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

public class MainActivity extends AppCompatActivity implements StarterInterface, LayoutColorInterface, ContentInterface, PremiumInterface, BodyManagerInterface, AnalyticsInterface {

    // Layout
    public Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    // Variables
    DatabaseHelper mDatabaseHelper;
    private ActionBarDrawerToggle mDrawerToggle;

    // Fragment
    FragmentManager mFragmentManager;
    private DrawerFragment mDrawerFragment;

    // Color Issues
    private CategoryColor mCategoryColor;
    private LayoutColor mLayoutColor;

    // InApp Purchase
    IInAppBillingService mPurchaseService;

    // Premium
    boolean mPremiumCharged = false;
    boolean mPremiumFree = false;
    String mDeveloperPayload;

    // Google Analytics
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up tracker
        AnalyticsApplication application = (AnalyticsApplication)getApplication();
        mTracker = application.getDefaultTracker();

        // Track onCreate mehtod
        mTracker.setScreenName("Activity-Main");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // Set up Database Helper
        mDatabaseHelper = new DatabaseHelper(this);

        // Set up InApp Purchase
        ServiceConnection mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mPurchaseService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mPurchaseService = IInAppBillingService.Stub.asInterface(service);
                Log.i("onServiceConnected", "called");
                // Check if user has charged premium version
                try {
                    Bundle ownedItems = mPurchaseService.getPurchases(3, getPackageName(), "subs", null);
                    int response = ownedItems.getInt("RESPONSE_CODE");
                    if (response == 0) {
                        ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                        ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                        for (int i = 0; i < purchaseDataList.size(); ++i) {
                            Log.i("SKU owned by user", ownedSkus.get(i));
                            if (ownedSkus.get(i).equals(MyConstants.PREMIUM_SILVER)) {
                                mDatabaseHelper.setPremiumSilver(true);
                                mPremiumCharged = true;
                            } else {
                                mDatabaseHelper.setPremiumSilver(false);
                            }
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        // Set up layout
        setContentView(R.layout.activity_main);

        // Check if user has free premium version
        updatePremiumFree();

        // create user and personal information
        mDatabaseHelper.createUser();

        // Iniatiate layout components
        mToolbar = (Toolbar) findViewById(R.id.toolbarMain);
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawerFragment);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Set up toolbar
        setSupportActionBar(mToolbar);

        // Initiate color objects
        mCategoryColor = new CategoryColor(this);
        colorLayout();

        // Set up navigation drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerFragment.updateDrawer();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // Add Fragment
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        OverviewPagerFragment overviewPagerFragment = new OverviewPagerFragment();
        fragmentTransaction.replace(R.id.flContainer, overviewPagerFragment);
        fragmentTransaction.commit();


        // open Drawer in the beginning after a little delay to show the animation
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        }, 500);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onPostResume() {
        try {
            super.onPostResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDrawer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Update App Widget
        Intent intentAppWidget = new Intent(this, AppWidgetProvider.class);
        intentAppWidget.setAction(MyConstants.UPDATE);
        sendBroadcast(intentAppWidget);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MyConstants.REQUEST_ACTIVITY_SETTINGS:
                if (resultCode == Activity.RESULT_OK) {
                    colorLayout();
                    mDrawerFragment.colorLayout();
                    mPremiumCharged = mDatabaseHelper.hasPremiumSilver();
                }
                break;
            case MyConstants.REQUEST_PURCHASE:
                String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        JSONObject json = new JSONObject(purchaseData);
                        String developerPayload = json.getString("developerPayload");
                        if (mDeveloperPayload.equals(developerPayload)) {
                            Toast.makeText(this, getString(R.string.premium_purchase_successful), Toast.LENGTH_LONG).show();
                            mDatabaseHelper.setPremiumSilver(true);
                            mPremiumCharged = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case MyConstants.REQUEST_ACTIVITY_DETAIL:
                if (resultCode == MyConstants.RESULT_PREMIUM_UPDATE)
                    mPremiumCharged = mDatabaseHelper.hasPremiumSilver();
        }
    }


    // Implemented methods from Interfaces

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragment);
        fragmentTransaction.commit();
        closeDrawer();
    }

    public void updateCalendarFragmentList(Calendar calendar, int calendarPage) {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.flContainer);
        if (fragment instanceof CalendarFragment) {
            ((CalendarFragment) fragment).updateContentList(calendar, calendarPage);
        }
    }

    private void closeDrawer() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawers();
            }
        }, 100);
    }

    public void updateDrawer() {
        mDrawerFragment.updateDrawer();
    }

    private void colorLayout() {
        mLayoutColor = new LayoutColor(this, mDatabaseHelper.getLayoutColorValue());
        mToolbar.setBackgroundColor(mLayoutColor.getLayoutColor());
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mLayoutColor.getLayoutColorDark());
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void refreshLayoutColor() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void selectCategory(final int contentType) {
        final ArrayList<Category> categories = mDatabaseHelper.getAllCategories();
        if (categories.size() > 0) {
            List<String> listItems = new ArrayList<>();
            for (Category category : categories) {
                listItems.add(category.getTitle());
            }
            CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    Category category = categories.get(item);
                    addContent(category, contentType, MyConstants.DETAIL_GENERAL);
                }
            });
            builder.create().show();
        } else {
            Toast.makeText(this, getString(R.string.toast_add_category), Toast.LENGTH_LONG).show();
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    public void updatePremiumFree() {
        mPremiumFree = mDatabaseHelper.getContentCounterValue() <= getResources().getInteger(R.integer.free_premium_silver_content_number_final);
    }


    @Override
    public boolean hasPremium() {
        if (mPremiumFree || mPremiumCharged || MyConstants.DEVELOPER_PREMIUM_MODE) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void openDialogPremiumFunction(String title, String subtitle, String text) {
        DialogFragment dialog = new PremiumDialog();
        Bundle arg = new Bundle();
        arg.putString(MyConstants.TITLE, title);
        arg.putString(MyConstants.SUBTITLE, subtitle);
        arg.putString(MyConstants.TEXT, text);
        dialog.setArguments(arg);
        dialog.show(getSupportFragmentManager(), "premium_dialog");
    }

    @Override
    public void startPurchase() {
        String uuid = UUID.randomUUID().toString();
        mDeveloperPayload = uuid.replaceAll("-", "");
        try {
            Bundle buyIntentBundle = mPurchaseService.getBuyIntent(3, getPackageName(), MyConstants.PREMIUM_SILVER, "subs", mDeveloperPayload);
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            startIntentSenderForResult(pendingIntent.getIntentSender(), MyConstants.REQUEST_PURCHASE, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startOverviewPagerFragment() {
        track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_OVERVIEW);
        if (!Functions.PREMIUM_FUNCTION_OVERVIEW || hasPremium()) {
            OverviewPagerFragment overviewPagerFragment = new OverviewPagerFragment();
            changeFragment(overviewPagerFragment);
        } else {
            openDialogPremiumFunction(getString(R.string.premium_function), getString(R.string.premium_silver_overview), getString(R.string.premium_expired));
        }
    }

    @Override
    public void startCalendarFragment() {
        track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_CALENDAR);
        if (!Functions.PREMIUM_FUNCTION_CALENDAR || hasPremium()) {
            CalendarFragment calendarFragment = new CalendarFragment();
            changeFragment(calendarFragment);
        } else {
            openDialogPremiumFunction(getString(R.string.premium_function), getString(R.string.premium_silver_calendar), getString(R.string.premium_expired));
        }
    }

    @Override
    public void startContentPagerFragment(Category category, int contentType) {
        ContentListPagerFragment contentListPagerFragment = new ContentListPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CATEGORY_ID, category.getId());
        bundle.putInt(MyConstants.CONTENT_TYPE, contentType);
        contentListPagerFragment.setArguments(bundle);
        changeFragment(contentListPagerFragment);
    }

    @Override
    public void startDetailActivity(Category category, int contentType, int contentId, int detailType) {
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.CONTENT_ID, contentId);
        bundle.putInt(MyConstants.CATEGORY_ID, category.getId());
        bundle.putInt(MyConstants.CONTENT_TYPE, contentType);
        if (contentType == MyConstants.CONTENT_NOTE) {
            startActivityForResult(new Intent(this, NoteActivity.class).putExtras(bundle), MyConstants.REQUEST_ACTIVITY_DETAIL);
        } else {
            bundle.putInt(MyConstants.DETAIL_TYPE, detailType);
            startActivityForResult(new Intent(this, TaskEventActivity.class).putExtras(bundle), MyConstants.REQUEST_ACTIVITY_DETAIL);
        }
    }

    @Override
    public void addCategory(Fragment targetFragment) {
        // Let editor know that this is first content
        DialogFragment dialog = new AddEditCategoryDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(MyConstants.DIALOGE_TYPE, MyConstants.DIALOGE_CATEGORY_ADD);
        dialog.setArguments(bundle);
        dialog.setTargetFragment(targetFragment, 0);
        dialog.show(getSupportFragmentManager(), "Add_Category");
    }

    @Override
    public void deleteCategory(Category category, Fragment targetFragment) {
        DeleteCategoryDialog d = new DeleteCategoryDialog();
        Bundle b = new Bundle();
        b.putInt(MyConstants.CATEGORY_ID, category.getId());
        d.setArguments(b);
        d.setTargetFragment(targetFragment, 0);
        d.show(mFragmentManager, "..");
    }

    @Override
    public void addContent(Category category, int contentType, int detailType) {
        mDatabaseHelper.incrementContentCounter();
        updatePremiumFree();

        int contentId = 0;
        Content newContent = null;
        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                newContent = new Task(category.getId(), category.getTitle());
                track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_ADD_TASK);
                break;
            case MyConstants.CONTENT_EVENT:
                newContent = new Event(category.getId(), category.getTitle());
                track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_ADD_EVENT);
                break;
            case MyConstants.CONTENT_NOTE:
                newContent = new Note(category.getId(), category.getTitle());
                track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_ADD_NOTE);
        }
        contentId = mDatabaseHelper.createContent(newContent);
        startDetailActivity(category, contentType, contentId, detailType);

    }

    @Override
    public void addContent(Category category, int contentType, int detailType, String title, Calendar date, Calendar time) {
        mDatabaseHelper.incrementContentCounter();
        updatePremiumFree();

        switch (contentType) {
            case MyConstants.CONTENT_TASK:
                track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_QUICK_ADD_TASK);
                Task task = new Task(category.getId(), category.getTitle());
                task.setTitle(title);
                task.setDate(date);
                task.setTime(time);
                mDatabaseHelper.createContent(task);
                break;
            case MyConstants.CONTENT_EVENT:
                track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_QUICK_ADD_EVENT);
                Event event = new Event(category.getId(), category.getTitle());
                event.setTitle(title);
                event.setDate(date);
                event.setTime(time);
                mDatabaseHelper.createContent(event);
                break;
            case MyConstants.CONTENT_NOTE:
                track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_QUICK_ADD_NOTE);
                Note note = new Note(category.getId(), category.getTitle());
                note.setTitle(title);
                mDatabaseHelper.createContent(note);
        }
    }

    @Override
    public void track(String category, String action) {
        mTracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).build());
    }
}
