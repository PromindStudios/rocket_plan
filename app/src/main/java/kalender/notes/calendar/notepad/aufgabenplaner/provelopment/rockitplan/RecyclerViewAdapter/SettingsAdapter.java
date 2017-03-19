package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.InformationDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.LayoutColorDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.PremiumInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.Functions;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 12.01.2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Field Variables
    Context mContext;
    FragmentManager mFragmentManager;
    String[] mItemNames;
    int[] mIconIds;

    // Interface
    PremiumInterface mPremiumInterface;

    public SettingsAdapter(Context context, FragmentManager fm) {
        mContext = context;
        mPremiumInterface = (PremiumInterface)context;
        mFragmentManager = fm;
        mItemNames = new String[]{mContext.getString(R.string.layout_color), mContext.getString(R.string.about_rocket_plan), mContext.getString(R.string.contact_mail), mContext.getString(R.string.rate_app), mContext.getString(R.string.rocket_plan_premium)};
        mIconIds = new int[]{R.drawable.ic_layout_color, R.drawable.ic_info_24dp, R.drawable.ic_mail, R.drawable.ic_thumb_up, R.drawable.ic_action_star_rate};

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_settings, parent, false);
        settingsHolder holder = new settingsHolder(view, new settingsHolder.interfaceSettings() {
            @Override
            public void onSettingClicked(int position) {
                switch (position) {
                    case 0:
                        if (!Functions.PREMIUM_FUNCTION_LAYOUT_COLOR || mPremiumInterface.hasPremium()) {
                            DialogFragment dialogLayoutColor = new LayoutColorDialog();
                            dialogLayoutColor.show(mFragmentManager, "dialog_layout_color");
                        } else {
                            mPremiumInterface.openDialogPremiumFunction(mContext.getString(R.string.premium_function), mContext.getString(R.string.premium_silver_colors), mContext.getString(R.string.premium_expired));
                        }
                        break;
                    case 1:
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
                    case 4:
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
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        settingsHolder myHolder = (settingsHolder)holder;
        myHolder.ivSettings.setImageDrawable(ContextCompat.getDrawable(mContext, mIconIds[position]));
        myHolder.tvSettings.setText(mItemNames[position]);


    }

    @Override
    public int getItemCount() {
        return mItemNames.length;
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



}
