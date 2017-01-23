package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.InformationDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.LayoutColorDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
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



    public SettingsAdapter(Context context, FragmentManager fm) {
        mContext = context;
        mFragmentManager = fm;
        mItemNames = new String[]{mContext.getString(R.string.layout_color), mContext.getString(R.string.about_rocket_plan), mContext.getString(R.string.contact_mail), mContext.getString(R.string.rate_app)};
        mIconIds = new int[]{R.drawable.ic_layout_color, R.drawable.ic_info_24dp, R.drawable.ic_mail, R.drawable.ic_action_star_rate};

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_settings, parent, false);
        settingsHolder holder = new settingsHolder(view, new settingsHolder.interfaceSettings() {
            @Override
            public void onSettingClicked(int position) {
                switch (position) {
                    case 0:
                        DialogFragment dialogLayoutColor = new LayoutColorDialog();
                        dialogLayoutColor.show(mFragmentManager, "dialog_layout_color");
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
                        // open Play Store
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

        RelativeLayout rlSettings;
        ImageView ivSettings;
        TextView tvSettings;

        interfaceSettings iSettings;

        public settingsHolder(View itemView, interfaceSettings i) {
            super(itemView);

            rlSettings = (RelativeLayout) itemView.findViewById(R.id.rlSettings);
            ivSettings = (ImageView)itemView.findViewById(R.id.ivIcon);
            tvSettings = (TextView)itemView.findViewById(R.id.tvSettings);

            rlSettings.setOnClickListener(this);
            iSettings = i;
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.rlSettings) {
                iSettings.onSettingClicked(getAdapterPosition());
            }
        }

        public interface interfaceSettings {
            public void onSettingClicked(int position);
        }
    }



}
