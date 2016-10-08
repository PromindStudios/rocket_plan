package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.ContentHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditCategoryDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.DrawerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 03.05.2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // ViewHolder Types
    private final int TYPE_TIME = 0;
    private final int TYPE_CATEGORY = 1;

    Context mContext;
    ArrayList<Category> mCategories;
    LayoutInflater mLayoutInflater;
    DatabaseHelper mDatabaseHelper;
    DrawerFragment mDrawerFragment;
    FragmentManager mFragmentManager;

    boolean isExpanded;

    CategoryAdapterListener mCategoryAdapterListener;

    public CategoryAdapter(Context context, ArrayList<Category> categories, DatabaseHelper databaseHelper, DrawerFragment drawerFragment, FragmentManager fragmentManager) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mCategories = categories;
        mDatabaseHelper = databaseHelper;
        mDrawerFragment = drawerFragment;
        mFragmentManager = fragmentManager;
        mCategoryAdapterListener = (MainActivity)mContext;
        isExpanded = false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TIME) {
            View view = mLayoutInflater.inflate(R.layout.item_time, parent, false);
            timeViewHolder holder = new timeViewHolder(view, new timeViewHolder.timeViewHolderListener() {
                @Override
                public void onTimeClicked() {
                    mCategoryAdapterListener.onTimeClicked();
                }
            });
            return holder;
        } else {
            View view = mLayoutInflater.inflate(R.layout.item_category, parent, false);
            categoryHolder holder = new categoryHolder(view, new categoryHolder.myViewHolderClickListener() {
                @Override
                public void onCategoryClick(int position) {
                    Category category = getCorrectCategory(position);
                    mCategoryAdapterListener.ItemCategorySelected(category.getId(), category.getTitle(), MyConstants.CONTENT_TASK);
                }

                @Override
                public void onContentClick(int position, int contentType) {
                    Category category = getCorrectCategory(position);
                    ContentHelper contentHelper = new ContentHelper(mContext, category.getId());

                    int contentSize = 0;
                    switch (contentType) {
                        case MyConstants.CONTENT_TASK:
                            contentSize = contentHelper.getUndoneContentSize(mDatabaseHelper.getAllCategoryTasks(category.getId()),contentType);
                            break;
                        case MyConstants.CONTENT_EVENT:
                            contentSize = contentHelper.getUndoneContentSize(mDatabaseHelper.getAllCategoryEvents(category.getId()),contentType);
                            break;
                        case MyConstants.CONTENT_NOTE:
                            contentSize = mDatabaseHelper.getAllCategoryNotes(category.getId()).size();
                            break;
                    }

                    if (contentSize == 0) {
                        mCategoryAdapterListener.onCreateNewContent(category.getId(), category.getTitle(), contentType);
                    } else {
                        mCategoryAdapterListener.ItemCategorySelected(category.getId(), category.getTitle(), contentType);
                    }
                }

                @Override
                public void onContentAdd(int position, int contentType) {
                    Category category = getCorrectCategory(position);
                    mCategoryAdapterListener.onCreateNewContent(category.getId(), category.getTitle(), contentType);
                }

                @Override
                public void onCategoryExpandCollapse(RelativeLayout rlMain, ImageView ivExpandCollapse, RelativeLayout rlCategoryContent, int position) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) rlMain.getLayoutParams();
                    Category category = getCorrectCategory(position);
                    CategoryColor categoryColor = new CategoryColor(mContext, category.getColor());
                    Drawable iconExpandCollapse;
                    if (isExpanded) {
                        isExpanded = false;
                        params.height = MyMethods.dpToPx(mContext, 60);
                        rlMain.setLayoutParams(params);
                        rlCategoryContent.setVisibility(View.GONE);
                        iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_collapsed_24_white, null);
                    } else {
                        isExpanded = true;
                        params.height = MyMethods.dpToPx(mContext, 210);
                        rlMain.setLayoutParams(params);
                        rlCategoryContent.setVisibility(View.VISIBLE);
                        iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_expanded_24_white, null);
                    }
                    iconExpandCollapse.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), categoryColor.getCategoryColor(), null), PorterDuff.Mode.MULTIPLY);
                    Log.i("Coloor: ", Integer.toString(categoryColor.mColor));
                    ivExpandCollapse.setImageDrawable(iconExpandCollapse);
                }

                @Override
                public void onCategoryLongClick(int position) {
                    final Category category = getCorrectCategory(position);
                    CharSequence[] items = {mContext.getString(R.string.edit), mContext.getString(R.string.delete)};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                    // Edit
                                    DialogFragment dialog_edit = new AddEditCategoryDialog();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(MyConstants.DIALOGE_TYPE, MyConstants.DIALOGE_CATEGORY_EDIT);
                                    bundle.putInt(MyConstants.CATEGORY_ID, category.getId());
                                    dialog_edit.setArguments(bundle);
                                    dialog_edit.setTargetFragment(mDrawerFragment, 0);
                                    dialog_edit.show(mFragmentManager, "Add_Category");
                                    break;
                                case 1:
                                    // Delete
                                    mDatabaseHelper.deleteCategory(category);
                                    mDrawerFragment.updateDrawer();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    builder.create().show();
                }
            });
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        if (position == 0) {

        } else {
            categoryHolder holder = (categoryHolder)vholder;
            final Category category = getCorrectCategory(position);

            // Set heigt of category item
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.rlMain.getLayoutParams();
            if (isExpanded) {
                params.height = MyMethods.dpToPx(mContext, 210);
                holder.rlMain.setLayoutParams(params);
                holder.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expanded_24dp));
                holder.rlCategoryContent.setVisibility(View.VISIBLE);
            } else {
                params.height = MyMethods.dpToPx(mContext, 60);
                holder.rlMain.setLayoutParams(params);
                holder.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_collapsed_24dp));
                holder.rlCategoryContent.setVisibility(View.GONE);
            }

            // Title
            holder.tvCategoryTitle.setText(category.getTitle());

            ContentHelper contentHelper = new ContentHelper(mContext, category.getId());

            int taskSize = contentHelper.getUndoneContentSize(mDatabaseHelper.getAllCategoryTasks(category.getId()),MyConstants.CONTENT_TASK);
            int eventSize = contentHelper.getUndoneContentSize(mDatabaseHelper.getAllCategoryEvents(category.getId()),MyConstants.CONTENT_EVENT);
            int noteSize = mDatabaseHelper.getAllCategoryNotes(category.getId()).size();

            if (taskSize == 1) {
                holder.tvTask.setText(Integer.toString(taskSize) + " " + mContext.getString(R.string.task));
            } else {
                holder.tvTask.setText(Integer.toString(taskSize) + " " + mContext.getString(R.string.tasks));
            }
            if (eventSize == 1) {
                holder.tvEvent.setText(Integer.toString(eventSize) + " " + mContext.getString(R.string.event));
            } else {
                holder.tvEvent.setText(Integer.toString(eventSize) + " " + mContext.getString(R.string.events));
            }
            if (noteSize == 1) {
                holder.tvNote.setText(Integer.toString(noteSize) + " " + mContext.getString(R.string.note));
            } else {
                holder.tvNote.setText(Integer.toString(noteSize) + " " + mContext.getString(R.string.notes));
            }

            // Handle Category Color
            CategoryColor categoryColor = new CategoryColor(mContext, category.getColor());

            Drawable iconExpandCollapse;
            if (isExpanded) {
                iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_expanded_24_white, null);
            } else {
                iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_collapsed_24_white, null);
            }
            holder.ivExpandCollapse.setImageDrawable(categoryColor.colorIcon(iconExpandCollapse));

            holder.ivTask.setImageDrawable(categoryColor.colorIcon(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_task, null)));
            holder.ivEvent.setImageDrawable(categoryColor.colorIcon(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_event, null)));
            holder.ivNote.setImageDrawable(categoryColor.colorIcon(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_note, null)));

            if (position == mCategories.size()) {
                holder.vDivider.setVisibility(View.GONE);
            } else {
                holder.vDivider.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mCategories.size()+1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TIME;
        } else {
            return TYPE_CATEGORY;
        }
    }

    static class categoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView tvCategoryTitle;
        TextView tvTask;
        TextView tvEvent;
        TextView tvNote;

        ImageView ivExpandCollapse;
        ImageView ivTask;
        FrameLayout flTaskAdd;
        ImageView ivEvent;
        FrameLayout flEventAdd;
        ImageView ivNote;
        FrameLayout flNoteAdd;

        View vDivider;

        RelativeLayout rlCategoryContent;
        RelativeLayout rlMain;

        myViewHolderClickListener mListener;

        public categoryHolder(View itemView, myViewHolderClickListener listener) {
            super(itemView);
            mListener = listener;
            tvCategoryTitle = (TextView)itemView.findViewById(R.id.tvCategoryTitle);
            tvTask = (TextView)itemView.findViewById(R.id.tvTask);
            tvEvent = (TextView)itemView.findViewById(R.id.tvEvent);
            tvNote = (TextView)itemView.findViewById(R.id.tvNote);
            ivExpandCollapse = (ImageView)itemView.findViewById(R.id.ivExpandCollapse);
            ivTask = (ImageView)itemView.findViewById(R.id.ivTask);
            flTaskAdd = (FrameLayout)itemView.findViewById(R.id.flTaskAdd);
            ivEvent = (ImageView)itemView.findViewById(R.id.ivEvent);
            flEventAdd = (FrameLayout)itemView.findViewById(R.id.flEventAdd);
            ivNote = (ImageView)itemView.findViewById(R.id.ivNote);
            flNoteAdd = (FrameLayout)itemView.findViewById(R.id.flNoteAdd);
            rlCategoryContent = (RelativeLayout)itemView.findViewById(R.id.rlCategoryContent);
            rlMain = (RelativeLayout)itemView.findViewById(R.id.rlMain);
            vDivider = itemView.findViewById(R.id.vDivider);

            tvCategoryTitle.setOnClickListener(this);
            tvTask.setOnClickListener(this);
            tvEvent.setOnClickListener(this);
            tvNote.setOnClickListener(this);
            ivTask.setOnClickListener(this);
            ivEvent.setOnClickListener(this);
            ivNote.setOnClickListener(this);
            flTaskAdd.setOnClickListener(this);
            flEventAdd.setOnClickListener(this);
            flNoteAdd.setOnClickListener(this);
            ivExpandCollapse.setOnClickListener(this);
            tvCategoryTitle.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvCategoryTitle) {
                mListener.onCategoryClick(getAdapterPosition());
            }
            if (v.getId() == R.id.ivTask || v.getId() == R.id.tvTask) {
                mListener.onContentClick(getAdapterPosition(), MyConstants.CONTENT_TASK);
            }
            if (v.getId() == R.id.ivEvent || v.getId() == R.id.tvEvent) {
                mListener.onContentClick(getAdapterPosition(), MyConstants.CONTENT_EVENT);
            }
            if (v.getId() == R.id.ivNote || v.getId() == R.id.tvNote) {
                mListener.onContentClick(getAdapterPosition(), MyConstants.CONTENT_NOTE);
            }
            if (v.getId() == R.id.flTaskAdd) {
                mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_TASK);
            }
            if (v.getId() == R.id.flEventAdd) {
                mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_EVENT);
            }
            if (v.getId() == R.id.flNoteAdd) {
                mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_NOTE);
            }
            if (v.getId() == R.id.ivExpandCollapse) {
                mListener.onCategoryExpandCollapse(rlMain, ivExpandCollapse, rlCategoryContent, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.tvCategoryTitle) {
                mListener.onCategoryLongClick(getAdapterPosition());
                return true;
            } else {
                return false;
            }

        }


        public static interface myViewHolderClickListener {
            public void onCategoryClick(int position);
            public void onContentClick(int position, int contentType);
            public void onContentAdd (int position, int contentType);
            public void onCategoryExpandCollapse(RelativeLayout rlMain, ImageView ivExpandCollapse, RelativeLayout rlCategoryContent, int position);
            public void onCategoryLongClick(int position);
        }
    }

    static class timeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout rlTime;
        timeViewHolderListener mListener;

        public timeViewHolder(View itemView, timeViewHolderListener listener) {
            super(itemView);

            mListener = listener;

            rlTime = (RelativeLayout)itemView.findViewById(R.id.rlTime);
            rlTime.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.rlTime) {
                mListener.onTimeClicked();
            }

        }

        public interface timeViewHolderListener {
            public void onTimeClicked();
        }
    }

    public void updateData() {
        ArrayList<Category> categories = mDatabaseHelper.getAllCategories();
        mCategories.clear();
        mCategories.addAll(categories);
        notifyDataSetChanged();
        Log.i("CategoryAdapter: ", "Title clicked");
    }

    public interface CategoryAdapterListener {
        public void ItemCategorySelected(int categoryId, String categoryName, int contentType);
        public void onCreateNewContent(int categoryId, String categoryName, int contentTyp);
        public void onTimeClicked();
    }

    public Category getCorrectCategory(int position) {
        return mCategories.get(position-1);
    }


}
