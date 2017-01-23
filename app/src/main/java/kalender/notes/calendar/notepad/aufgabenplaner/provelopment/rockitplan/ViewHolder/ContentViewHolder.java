package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 16.07.2016.
 */
public class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public vhTasksClickListener mListener;

    public TextView tvTitle;
    public TextView tvDate;
    public TextView tvSubtitle;
    public TextView tvSubtask;

    public ImageView ivSubtask;
    public ImageView ivFiles;
    public ImageView ivContent;
    public ImageView ivRepeat;
    public ImageView ivCategory;

    public LinearLayout llSubtitleDate;
    public LinearLayout llContent;
    public LinearLayout llFiles;
    public LinearLayout llSubtask;

    public View vSubtitleDateDivider;
    public View vDivider;

    public ContentViewHolder(View itemView, vhTasksClickListener listener) {
        super(itemView);

        mListener = listener;

        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        tvSubtitle = (TextView) itemView.findViewById(R.id.tvSubtitle);
        tvSubtask = (TextView) itemView.findViewById(R.id.tvSubtask);

        ivSubtask = (ImageView) itemView.findViewById(R.id.ivSubtask);
        ivFiles = (ImageView) itemView.findViewById(R.id.ivFiles);
        ivContent = (ImageView) itemView.findViewById(R.id.ivContent);
        ivRepeat = (ImageView) itemView.findViewById(R.id.ivRepeat);
        ivCategory = (ImageView) itemView.findViewById(R.id.ivCategory);

        llSubtitleDate = (LinearLayout) itemView.findViewById(R.id.llSubtitleDate);
        llContent = (LinearLayout) itemView.findViewById(R.id.llContent);
        llFiles = (LinearLayout) itemView.findViewById(R.id.llFiles);
        llSubtask = (LinearLayout) itemView.findViewById(R.id.llSubtask);
        vSubtitleDateDivider = itemView.findViewById(R.id.vSubtitleDateDivider);
        vDivider = itemView.findViewById(R.id.divider);

        llContent.setOnClickListener(this);
        llFiles.setOnClickListener(this);
        llSubtask.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llContent) {
            mListener.openContent(getAdapterPosition(), MyConstants.DETAIL_GENERAL);
        }
        if (v.getId() == R.id.llFiles) {
            mListener.openContent(getAdapterPosition(), MyConstants.DETAIL_FILES);
        }
        if (v.getId() == R.id.llSubtask) {
            mListener.openContent(getAdapterPosition(), MyConstants.DETAIL_SUBTASK);
        }
    }

    public static interface vhTasksClickListener {
        public void openContent(int position, int type);
    }
}
