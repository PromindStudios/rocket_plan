package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Admin on 16.07.2016.
 */
public class DividerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public vhShowHideClickListener mListener;
    public TextView tvText;
    public ImageView ivExpandCollapse;
    public RelativeLayout llExpandCollapse;

    public DividerViewHolder(View itemView, vhShowHideClickListener listener) {
        super(itemView);
        mListener = listener;
        tvText = (TextView) itemView.findViewById(R.id.tvText);
        ivExpandCollapse = (ImageView)itemView.findViewById(R.id.ivExpandCollapse);
        llExpandCollapse = (RelativeLayout) itemView.findViewById(R.id.rlExpandCollapse);
        llExpandCollapse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlExpandCollapse) {
            mListener.onExpandCollapse(getAdapterPosition());
        }
    }

    public interface vhShowHideClickListener {
        public void onExpandCollapse(int position);
    }
}
