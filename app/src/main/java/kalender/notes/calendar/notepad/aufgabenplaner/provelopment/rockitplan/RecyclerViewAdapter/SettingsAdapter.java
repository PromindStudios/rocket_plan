package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.Functions;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.InformationDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.Introduction.CategoryIntroductionDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.LayoutColorDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.PremiumInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.StatusBar_Interface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 12.01.2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private final int TYPE_NORMAL = 0;
    private final int TYPE_CHECKABLE = 1;

    // Field Variables
    Context mContext;
    FragmentManager mFragmentManager;
    String[] mItemNames;
    int[] mIconIds;

    // Interface
    PremiumInterface mPremiumInterface;
    StatusBar_Interface mStatusBarInterface;

    // Shared Preferences

    public SettingsAdapter(Context context, FragmentManager fm) {
        mContext = context;
        mPremiumInterface = (PremiumInterface)context;
        mStatusBarInterface = (StatusBar_Interface)context;
        mFragmentManager = fm;
        mItemNames = new String[]{mContext.getString(R.string.introduction_help), mContext.getString(R.string.layout_color), mContext.getString(R.string.about_rocket_plan), mContext.getString(R.string.rate_app), mContext.getString(R.string.status_bar)};
        mIconIds = new int[]{R.drawable.ic_lightbulb_24dp, R.drawable.ic_layout_color, R.drawable.ic_info_24dp, R.drawable.ic_thumb_up};
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_settings, parent, false);
            settingsHolder holder = new settingsHolder(view, new settingsHolder.interfaceSettings() {
                @Override
                public void onSettingClicked(int position) {
                    switch (position) {
                        case 0:
                            CategoryIntroductionDialog dialogC = new CategoryIntroductionDialog();
                            Bundle bundleC = new Bundle();
                            bundleC.putBoolean(MyConstants.DIALOG_INTRODUCTION_IS_START, false);
                            dialogC.setArguments(bundleC);
                            dialogC.show(mFragmentManager, "dialog");
                            break;
                        case 1:
                            if (!Functions.PREMIUM_FUNCTION_LAYOUT_COLOR || mPremiumInterface.hasPremium()) {
                                DialogFragment dialogLayoutColor = new LayoutColorDialog();
                                dialogLayoutColor.show(mFragmentManager, "dialog_layout_color");
                            } else {
                                mPremiumInterface.openDialogPremiumFunction(mContext.getString(R.string.premium_function), mContext.getString(R.string.premium_silver_colors), mContext.getString(R.string.premium_expired));
                            }
                            break;
                        case 2:
                            // open Dialog with information about Rocket-Plan
                            DialogFragment dialog = new InformationDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString(MyConstants.DIALOGE_TITLE, mContext.getString(R.string.title_about));
                            bundle.putString(MyConstants.DIALOGE_CONTENT, mContext.getString(R.string.dialog_about_text));
                            dialog.setArguments(bundle);
                            dialog.show(mFragmentManager, "dialog_about");
                            break;
                        case 3:
                            if (mPremiumInterface.hasPremium()) {
                                Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    mContext.startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
                                }
                            }
                            break;
                        case 5:
                            if (mPremiumInterface.hasPremium()) {
                                Toast.makeText(mContext, mContext.getString(R.string.status_premium), Toast.LENGTH_LONG).show();
                            } else {
                                mPremiumInterface.openDialogPremiumFunction(mContext.getString(R.string.rocket_plan_premium), mContext.getString(R.string.premium_running_out_subtitle), mContext.getString(R.string.premium_expired));
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
            return holder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_settings_checkable, parent, false);
            settingsCheckableHolder holder = new settingsCheckableHolder(view, new settingsCheckableHolder.interfaceSettingsCheckable() {
                @Override
                public void onSettingClicked(int position) {

                }

                @Override
                public void onChecked(int position, boolean b) {
                    switch (position) {
                        case 4:
                            if (b) {
                                mStatusBarInterface.activateStatusBar();

                            } else {
                                mStatusBarInterface.deactivateStatusBar();
                            }
                            break;
                    }
                }
            });
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < 4) {
            settingsHolder myHolder = (settingsHolder)holder;
            myHolder.ivSettings.setImageDrawable(ContextCompat.getDrawable(mContext, mIconIds[position]));
            myHolder.tvSettings.setText(mItemNames[position]);
        } else {
            settingsCheckableHolder myHolder = (settingsCheckableHolder)holder;
            myHolder.tvSettings.setText(mItemNames[position]);
            myHolder.cbSettings.setChecked(mContext.getSharedPreferences(MyConstants.SHARED_PREFERENCES, 0).getBoolean(MyConstants.STATUS_BAR_ACTIVATED, true));
            if (android.os.Build.VERSION.SDK_INT < 21) {
                int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                myHolder.cbSettings.setButtonDrawable(id);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItemNames.length;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 4) {
            return TYPE_NORMAL;
        } else {
            return TYPE_CHECKABLE;
        }
    }

    static class settingsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout llSettings;
        ImageView ivSettings;
        TextView tvSettings;

        interfaceSettings iSettings;

        public settingsHolder(View itemView, interfaceSettings i) {
            super(itemView);

            llSettings = (LinearLayout) itemView.findViewById(R.id.llSettings);
            ivSettings = (ImageView)itemView.findViewById(R.id.ivIcon);
            tvSettings = (TextView)itemView.findViewById(R.id.tvSettings);

            llSettings.setOnClickListener(this);
            iSettings = i;
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.llSettings) {
                iSettings.onSettingClicked(getAdapterPosition());
            }
        }

        public interface interfaceSettings {
            public void onSettingClicked(int position);
        }
    }

    static class settingsCheckableHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        LinearLayout llSettings;
        TextView tvSettings;
        CheckBox cbSettings;

        interfaceSettingsCheckable iSettings;

        public settingsCheckableHolder(View itemView, interfaceSettingsCheckable i) {
            super(itemView);

            llSettings = (LinearLayout) itemView.findViewById(R.id.llSettings);
            tvSettings = (TextView)itemView.findViewById(R.id.tvSettings);
            cbSettings = (CheckBox)itemView.findViewById(R.id.cbSettings);

            llSettings.setOnClickListener(this);
            cbSettings.setOnCheckedChangeListener(this);
            iSettings = i;
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.llSettings) {
                iSettings.onSettingClicked(getAdapterPosition());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (compoundButton.getId() == R.id.cbSettings) {
                iSettings.onChecked(getAdapterPosition(), b);
            }
        }

        public interface interfaceSettingsCheckable {
            public void onSettingClicked(int position);
            public void onChecked(int position, boolean b);
        }
    }



}
