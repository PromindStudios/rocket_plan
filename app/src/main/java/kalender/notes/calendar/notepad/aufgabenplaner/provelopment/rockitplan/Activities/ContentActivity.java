package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.AnalyticsApplication;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.AppWidget.AppWidgetProvider;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.PremiumDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments.NotesFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.AnalyticsInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.PremiumInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 19.02.2017.
 */

public class ContentActivity extends AppCompatActivity implements PremiumInterface, AnalyticsInterface {

    // Layout views
    FloatingActionButton fabDone;
    RelativeLayout rlActionBar;
    ImageView ivContent;
    TextView tvContent;
    //EditText etTitle;
    //CheckBox cbTitle;
    //View vDummy;

    // Content
    int mContentId;
    Content mContent;
    Category mCategory;
    String mPicturePath;
    int mContentType;

    // Others
    DatabaseHelper mDatabaseHelper;
    NotesFragment mNotesFragment;

    // InApp Purchase
    IInAppBillingService mPurchaseService;

    // Premium
    boolean mPremium = false;
    String mDeveloperPayload;

    // Google Analytics
    Tracker mTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up tracker
        AnalyticsApplication application = (AnalyticsApplication)getApplication();
        mTracker = application.getDefaultTracker();

        // Track onCreate mehtod
        mTracker.setScreenName("Activity-Content");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // Initialize DatabaseHelper
        mDatabaseHelper = new DatabaseHelper(this);

        // Check if user has premium version
        if (mDatabaseHelper.hasPremiumSilver() || mDatabaseHelper.getContentCounterValue() <= getResources().getInteger(R.integer.free_premium_silver_content_number_final) || MyConstants.DEVELOPER_PREMIUM_MODE) {
            mPremium = true;
        }

        // Set up InApp Purchase
        ServiceConnection mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mPurchaseService = null;
            }
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mPurchaseService = IInAppBillingService.Stub.asInterface(service);
            }};
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        // Set up Layout
        //etTitle = (EditText)findViewById(R.id.etTitle);
        rlActionBar = (RelativeLayout)findViewById(R.id.rlActionBar);
        ivContent = (ImageView)findViewById(R.id.ivContent);
        tvContent = (TextView)findViewById(R.id.tvContent);
        //cbTitle = (CheckBox)findViewById(R.id.cbTitle);
        //vDummy = findViewById(R.id.vDummy);
        fabDone = (FloatingActionButton)findViewById(R.id.fabDone);

        // Get Extras
        Bundle extras = getIntent().getExtras();
        mContentId = extras.getInt(MyConstants.CONTENT_ID);
        mContentType = extras.getInt(MyConstants.CONTENT_TYPE);

        // Content
        mDatabaseHelper = new DatabaseHelper(this);
        mContent = mDatabaseHelper.getContent(mContentId, mContentType);
        mCategory = mDatabaseHelper.getCategory(mContent.getCategoryId());

        // Title
        /*
        etTitle.setText(mContent.getTitle());
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                mContent.setTitle(s.toString());
            }
        });

        if (mContent.getTitle().equals("") || mContent.getTitle().matches("")) {
            etTitle.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etTitle, InputMethodManager.SHOW_IMPLICIT);
        } else {
            closeKeyboard();
            vDummy.requestFocus();
        }

        etTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    closeKeyboard();
                    vDummy.requestFocus();
                }
                return false;
            }
        });
        */

        // Set up Fab Button
        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endActivity(false);
            }
        });

        // Handle Color
        Category category = mDatabaseHelper.getCategory(mContent.getCategoryId());
        CategoryColor categoryColor = new CategoryColor(this, category.getColor());
        LayoutColor layoutColor = new LayoutColor(this, mDatabaseHelper.getLayoutColorValue());
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // Status Bar
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(layoutColor.getLayoutColor());
        }
        rlActionBar.setBackgroundColor(layoutColor.getLayoutColor());
        fabDone.setBackgroundTintList(ColorStateList.valueOf(layoutColor.getLayoutColor()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyConstants.REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            mNotesFragment.onSetPicture(mPicturePath);
        }
        if (requestCode == MyConstants.REQUEST_VIDEO_RECORD && resultCode == Activity.RESULT_OK) {
            mContent.setVideoPath(getFilesFragment().getCurrentVideoPath());
            getFilesFragment().updateVideoViews();
        }
        if (requestCode == MyConstants.REQUEST_PURCHASE && resultCode == Activity.RESULT_OK) {
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            try {
                JSONObject json = new JSONObject(purchaseData);
                String developerPayload = json.getString("developerPayload");
                if (mDeveloperPayload.equals(developerPayload)) {
                    Toast.makeText(this, getString(R.string.premium_purchase_successful), Toast.LENGTH_LONG).show();
                    mDatabaseHelper.setPremiumSilver(true);
                    mPremium = true;
                    setResult(MyConstants.RESULT_PREMIUM_UPDATE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabaseHelper.updateContent(mContent);
        Intent intentAppWidget = new Intent(this, AppWidgetProvider.class);
        intentAppWidget.setAction(MyConstants.UPDATE);
        sendBroadcast(intentAppWidget);
    }

    @Override
    public void onBackPressed() {
        endActivity(false);
    }

    public Category getCategory() {
        return mCategory;
    }

    public Content getContent() {
        return mContent;
    }

    public void initiateContent() {

    }

    public void takePicture(NotesFragment notesFragment) {
        mNotesFragment = notesFragment;
        String path = "RockitPlan_"+ Long.toString(System.nanoTime());
        File directory = new File(Environment.getExternalStorageDirectory(), "RockitPlan");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File pictureFile = new File(directory, path + ".jpg");
        mPicturePath = pictureFile.getAbsolutePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
        startActivityForResult(intent, MyConstants.REQUEST_CODE_IMAGE);
    }

    public boolean hasPremium() {
        return mPremium;
    }

    public void openDialogPremiumFunction(String title, String subtitle, String text) {
        DialogFragment dialog = new PremiumDialog();
        Bundle arg = new Bundle();
        arg.putString(MyConstants.TITLE, title);
        arg.putString(MyConstants.SUBTITLE, subtitle);
        arg.putString(MyConstants.TEXT, text);
        dialog.setArguments(arg);
        dialog.show(getSupportFragmentManager(), "premium_dialog");
    }

    public void startPurchase() {
        String uuid = UUID.randomUUID().toString();
        mDeveloperPayload = uuid.replaceAll("-","");
        try {
            Bundle buyIntentBundle = mPurchaseService.getBuyIntent(3, getPackageName(), MyConstants.PREMIUM_SILVER, "subs", mDeveloperPayload);
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            startIntentSenderForResult(pendingIntent.getIntentSender(), MyConstants.REQUEST_PURCHASE, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(etTitle.getWindowToken(), 0);
    }

    public void endActivity(boolean delete) {
    }

    public NotesFragment getFilesFragment() {
        return null;
    }

    public void setFabVisible(boolean visible) {
        if(visible) {
            fabDone.setVisibility(View.VISIBLE);
        } else {
            fabDone.setVisibility(View.GONE);
        }
    }

    @Override
    public void track(String category, String action) {
        mTracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).build());
    }
}
