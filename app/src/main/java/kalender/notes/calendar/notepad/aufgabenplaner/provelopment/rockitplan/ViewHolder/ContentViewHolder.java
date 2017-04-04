package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 16.07.2016.
 */
public class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public vhTasksClickListener mListener;

    public TextView tvTitle;
    public TextView tvDate;
    public TextView tvSubtitle;
    public TextView tvSubtask;

    public ImageView ivSubtaskDetails;
    public ImageView ivFiles;
    public ImageView ivContent;
    public ImageView ivRepeat;
    public ImageView ivCategory;

    public LinearLayout llSubtitleDate;
    public LinearLayout llContent;
    public LinearLayout llFiles;
    public LinearLayout llSubtaskDetails;
    public LinearLayout llContentItem;

    public View vSubtitleDateDivider;
    public View vDivider;

    public RadioButton rbActionMode;

    public ContentViewHolder(View itemView, vhTasksClickListener listener, int color) {
        super(itemView);

        mListener = listener;

        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        tvSubtitle = (TextView) itemView.findViewById(R.id.tvSubtitle);
        tvSubtask = (TextView) itemView.findViewById(R.id.tvSubtask);

        ivSubtaskDetails = (ImageView) itemView.findViewById(R.id.ivSubtaskDetails);
        ivFiles = (ImageView) itemView.findViewById(R.id.ivFiles);
        ivContent = (ImageView) itemView.findViewById(R.id.ivContent);
        ivRepeat = (ImageView) itemView.findViewById(R.id.ivRepeat);
        ivCategory = (ImageView) itemView.findViewById(R.id.ivCategory);

        llSubtitleDate = (LinearLayout) itemView.findViewById(R.id.llSubtitleDate);
        llContent = (LinearLayout) itemView.findViewById(R.id.llContent);
        llFiles = (LinearLayout) itemView.findViewById(R.id.llFiles);
        llSubtaskDetails = (LinearLayout) itemView.findViewById(R.id.llSubtaskDetails);
        llContentItem = (LinearLayout) itemView.findViewById(R.id.llContentItem);
        vSubtitleDateDivider = itemView.findViewById(R.id.vSubtitleDateDivider);
        vDivider = itemView.findViewById(R.id.divider);

        rbActionMode = (RadioButton)itemView.findViewById(R.id.rbContextualMenu);

        if (Build.VERSION.SDK_INT >= 21) {
            rbActionMode.setButtonTintList(ColorStateList.valueOf(color));
        } else {
            int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
            rbActionMode.setButtonDrawable(id);
        }

        //llContent.setOnClickListener(this);
        rbActionMode.setOnClickListener(this);
        llFiles.setOnClickListener(this);
        llSubtaskDetails.setOnClickListener(this);
        llContentItem.setOnClickListener(this);
        llContentItem.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.rbContextualMenu) {
            mListener.onItemClicked(getAdapterPosition(), MyConstants.DETAIL_GENERAL);
        }
        if (v.getId() == R.id.llFiles) {
            mListener.openContent(getAdapterPosition(), MyConstants.DETAIL_FILES);
        }
        if (v.getId() == R.id.llSubtaskDetails) {
            mListener.openContent(getAdapterPosition(), MyConstants.DETAIL_SUBTASK);
        }
        if (v.getId() == R.id.llContentItem) {
            mListener.onItemClicked(getAdapterPosition(), MyConstants.DETAIL_GENERAL);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.llContentItem) {
            mListener.onItemLongClicked(getAdapterPosition());
            return true;
        }
        return false;
    }

    public static interface vhTasksClickListener {
        public void openContent(int position, int type);
        public void onItemClicked(int position, int type);
        public void onItemLongClicked(int position);
    }
}
