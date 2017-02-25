package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.PremiumDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.LayoutColorInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.PremiumInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.SettingsAdapter;

/**
 * Created by Eric on 12.01.2017.
 */

public class SettingsActivity extends AppCompatActivity implements LayoutColorInterface, PremiumInterface {

    RecyclerView rvSettings;
    Toolbar mToolbar;
    LayoutColor mLayoutColor;
    DatabaseHelper mDatabaseHelper;

    // InApp Purchase
    IInAppBillingService mPurchaseService;

    // Premium
    boolean mPremium = false;
    String mDeveloperPayload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout set up
        setContentView(R.layout.activity_settings);
        rvSettings = (RecyclerView)findViewById(R.id.rvSettings);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_settings);

        // Create DatabaseHelper
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

        // LayoutColor set up
        mLayoutColor = new LayoutColor(this, 0);
        refreshLayoutColor();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyConstants.REQUEST_PURCHASE && resultCode == Activity.RESULT_OK) {
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            try {
                JSONObject json = new JSONObject(purchaseData);
                String developerPayload = json.getString("developerPayload");
                if (mDeveloperPayload.equals(developerPayload)) {
                    Toast.makeText(this, getString(R.string.premium_purchase_successful), Toast.LENGTH_LONG).show();
                    mDatabaseHelper.setPremiumSilver(true);
                    mPremium = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshLayoutColor() {

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

    @Override
    public boolean isPremium() {
        return mPremium;
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
        mDeveloperPayload = uuid.replaceAll("-","");
        try {
            Bundle buyIntentBundle = mPurchaseService.getBuyIntent(3, getPackageName(), MyConstants.PREMIUM_SILVER, "subs", mDeveloperPayload);
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            startIntentSenderForResult(pendingIntent.getIntentSender(), MyConstants.REQUEST_PURCHASE, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
