package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.SearchResult;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.ParticipantDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.SearchResultInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 26.02.2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<SearchResult> mSearchResults;
    LayoutInflater mLayoutInflater;
    ParticipantDialog mParticipantDialog;
    SearchResultInterface mSearchResultInterface;
    int mDrawableId;

    public SearchAdapter(Context context, ArrayList<SearchResult> searchResults, Fragment fragment, int drawableId) {
        mContext = context;
        mDrawableId = drawableId;
        mSearchResultInterface = (SearchResultInterface)fragment;
        mLayoutInflater = LayoutInflater.from(mContext);
        mSearchResults = searchResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_search, parent, false);
        searchHolder holder = new searchHolder(view, new searchHolder.searchHolderClickListener() {
            @Override
            public void onClick(int position) {
                mSearchResultInterface.onSearchResultSelected(mSearchResults.get(position));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        searchHolder holder = (searchHolder)h;
        holder.tvSearch.setText(mSearchResults.get(position).getName());
        Drawable icon = ResourcesCompat.getDrawable(mContext.getResources(), mDrawableId, null);
        icon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorDivider, null), PorterDuff.Mode.MULTIPLY);
        holder.ivSearch .setImageDrawable(icon);
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }

    static class searchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout rlSearch;
        TextView tvSearch;
        ImageView ivSearch;

        searchHolderClickListener mListener;

        public searchHolder(View itemView, searchHolderClickListener listener) {
            super(itemView);
            rlSearch = (RelativeLayout) itemView.findViewById(R.id.rlSearch);
            tvSearch = (TextView)itemView.findViewById(R.id.tvSearch);
            ivSearch = (ImageView)itemView.findViewById(R.id.ivSearch);
            mListener = listener;
            rlSearch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.rlSearch) {
                mListener.onClick(getAdapterPosition());
            }
        }

        public interface searchHolderClickListener {
            public void onClick(int position);
        }
    }

    public void updateSearchResults(ArrayList<SearchResult> contacts) {
        mSearchResults = contacts;
        notifyDataSetChanged();
    }
}
