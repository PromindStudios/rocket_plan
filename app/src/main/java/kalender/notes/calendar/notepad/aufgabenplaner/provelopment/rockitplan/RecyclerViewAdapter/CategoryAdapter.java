package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.ContentHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditCategoryDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Navigation_Drawer.HomeFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.BodyManagerInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.StarterInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 03.05.2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // ViewHolder Types
    private final int TYPE_TIME = 0;
    private final int TYPE_CATEGORY = 1;
    private final int TYPE_CALENDAR = 2;
    private final int TYPE_ADD_CATEGORY = 3;
    private final int CATEGORY_TITLE_HEIGHT = 80;
    private final int CATEGORY_ALL_HEIGHT = 130;

    Context mContext;
    ArrayList<Category> mCategories;
    LayoutInflater mLayoutInflater;
    DatabaseHelper mDatabaseHelper;
    HomeFragment mHomeFragment;
    FragmentManager mFragmentManager;
    LayoutColor mLayoutColor;

    // Interfaces
    BodyManagerInterface mBodyManagerInterface;
    StarterInterface mStarterInterface;

    public CategoryAdapter(Context context, ArrayList<Category> categories, DatabaseHelper databaseHelper, HomeFragment homeFragment, FragmentManager fragmentManager) {
        mContext = context;
        mBodyManagerInterface = (BodyManagerInterface) context;
        mStarterInterface = (StarterInterface) context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mCategories = categories;
        mDatabaseHelper = databaseHelper;
        mLayoutColor = new LayoutColor(mContext, mDatabaseHelper.getLayoutColorValue());
        mHomeFragment = homeFragment;
        mFragmentManager = fragmentManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_CATEGORY:
                view = mLayoutInflater.inflate(R.layout.item_category, parent, false);
                categoryHolder holder = new categoryHolder(view, new categoryHolder.myViewHolderClickListener() {
                    @Override
                    public void onCategoryClick(int position) {
                        Category category = mCategories.get(position);
                        mStarterInterface.startContentPagerFragment(category, MyConstants.CONTENT_TASK);
                    }

                    @Override
                    public void onContentClick(int position, int contentType) {
                        Category category = mCategories.get(position);
                        mStarterInterface.startContentPagerFragment(category, contentType);

                    }

                    @Override
                    public void onContentAdd(int position, int contentType) {
                        Category category = mCategories.get(position);
                        mBodyManagerInterface.addContent(category, contentType, MyConstants.DETAIL_GENERAL);
                    }

                    @Override
                    public void onCategoryExpandCollapse(LinearLayout llContent, ImageView ivExpandCollapse, int position) {
                        Category category = mCategories.get(position);
                        CategoryColor categoryColor = new CategoryColor(mContext, category.getColor());
                        Drawable iconExpandCollapse;
                        if (category.isExpanded()) {
                            category.setExpanded(false);
                            llContent.setVisibility(View.GONE);
                            iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_collapsed_13dp, null);
                            mDatabaseHelper.updateCategoryExpanded(category);
                        } else {
                            category.setExpanded(true);
                            llContent.setVisibility(View.VISIBLE);
                            iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_expanded_13dp, null);
                            mDatabaseHelper.updateCategoryExpanded(category);
                        }
                        iconExpandCollapse.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorSecondaryText, null), PorterDuff.Mode.MULTIPLY);
                        Log.i("Coloor: ", Integer.toString(categoryColor.mColor));
                        ivExpandCollapse.setImageDrawable(iconExpandCollapse);
                    }

                    @Override
                    public void onCategoryLongClick(int position) {
                        final Category category = mCategories.get(position);
                        CharSequence[] items = {mContext.getString(R.string.edit), mContext.getString(R.string.delete), mContext.getString(R.string.category_up), mContext.getString(R.string.category_down)};
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
                                        dialog_edit.setTargetFragment(mHomeFragment, 0);
                                        dialog_edit.show(mFragmentManager, "Add_Category");
                                        break;
                                    case 1:
                                        // Delete
                                        mBodyManagerInterface.deleteCategory(category, mHomeFragment);
                                        break;
                                    case 2:
                                        // Move up
                                        int p = mCategories.indexOf(category);
                                        if (p > 0) {
                                            mCategories.remove(p);
                                            mCategories.add((p - 1), category);
                                            for (Category c : mCategories) {
                                                c.setPosition(mCategories.indexOf(c));
                                                mDatabaseHelper.updateCategory(c);
                                            }
                                            mCategories = mDatabaseHelper.getAllCategories();
                                            notifyDataSetChanged();
                                        }
                                        break;
                                    case 3:
                                        // Move down
                                        int pDown = mCategories.indexOf(category);
                                        int size = mCategories.size();
                                        if ((pDown + 1) < size) {
                                            mCategories.remove(pDown);
                                            mCategories.add((pDown + 1), category);
                                            for (Category c : mCategories) {
                                                c.setPosition(mCategories.indexOf(c));
                                                mDatabaseHelper.updateCategory(c);
                                            }
                                            mCategories = mDatabaseHelper.getAllCategories();
                                            notifyDataSetChanged();
                                        }
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
            case TYPE_ADD_CATEGORY:
                view = mLayoutInflater.inflate(R.layout.item_category_add, parent, false);
                categoryAddViewHolder categoryAddHolder = new categoryAddViewHolder(view, new categoryAddViewHolder.addCategoryHolderListener() {
                    @Override
                    public void onAddCategoryClicked() {
                        mBodyManagerInterface.addCategory(mHomeFragment);
                    }
                });
                return categoryAddHolder;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        if (position != mCategories.size()) {
            categoryHolder holder = (categoryHolder) vholder;
            final Category category = mCategories.get(position);
            Drawable iconExpandCollapse;

            if (category.isExpanded()) {
                holder.llContent.setVisibility(View.VISIBLE);
                iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_expanded_13dp, null);
            } else {
                holder.llContent.setVisibility(View.GONE);
                iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_collapsed_13dp, null);
            }

            // Title
            holder.tvCategory.setText(category.getTitle());

            ContentHelper contentHelper = new ContentHelper(mContext, category.getId());

            int taskSize = contentHelper.getUndoneContentSize(mDatabaseHelper.getAllCategoryTasks(category.getId()), MyConstants.CONTENT_TASK);
            int eventSize = contentHelper.getUndoneContentSize(mDatabaseHelper.getAllCategoryEvents(category.getId()), MyConstants.CONTENT_EVENT);
            int noteSize = mDatabaseHelper.getAllCategoryNotes(category.getId(), category.isNoteSortedByPriority()).size();

            if (taskSize == 1) {
                holder.tvTask.setText("" + taskSize + " " + mContext.getString(R.string.task));
            } else {
                holder.tvTask.setText("" + taskSize + " " + mContext.getString(R.string.tasks));
            }
            if (eventSize == 1) {
                holder.tvEvent.setText("" + eventSize + " " + mContext.getString(R.string.event));
            } else {
                holder.tvEvent.setText("" + eventSize + " " + mContext.getString(R.string.events));
            }
            if (noteSize == 1) {
                holder.tvNote.setText("" + noteSize + " " + mContext.getString(R.string.note));
            } else {
                holder.tvNote.setText("" + noteSize + " " + mContext.getString(R.string.notes));
            }

            // Handle Category Color
            CategoryColor categoryColor = new CategoryColor(mContext, category.getColor());
            setImageView(holder.ivCategoryIcon, R.drawable.ic_category_18dp, categoryColor.getCategoryColor());
            iconExpandCollapse.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorSecondaryText, null), PorterDuff.Mode.MULTIPLY);
            holder.ivExpandCollapse.setImageDrawable(iconExpandCollapse);

            if (position == mCategories.size()) {
                holder.vDivider.setVisibility(View.VISIBLE);
            } else {
                holder.vDivider.setVisibility(View.VISIBLE);
            }
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mCategories.size()) {
            return TYPE_ADD_CATEGORY;
        } else {
            return TYPE_CATEGORY;
        }
    }

    static class categoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvCategory;
        TextView tvTask;
        TextView tvEvent;
        TextView tvNote;

        ImageView ivExpandCollapse;
        ImageView ivTask;
        ImageView ivEvent;
        ImageView ivNote;
        ImageView ivCategoryIcon;
        ImageView ivTaskAdd;
        ImageView ivEventAdd;
        ImageView ivNoteAdd;

        View vDivider;

        LinearLayout llContent;
        RelativeLayout rlCategory;
        RelativeLayout rlTask;
        RelativeLayout rlEvent;
        RelativeLayout rlNote;

        myViewHolderClickListener mListener;

        public categoryHolder(View itemView, myViewHolderClickListener listener) {
            super(itemView);
            mListener = listener;
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvTask = (TextView) itemView.findViewById(R.id.tvTask);
            tvEvent = (TextView) itemView.findViewById(R.id.tvEvent);
            tvNote = (TextView) itemView.findViewById(R.id.tvNote);
            ivExpandCollapse = (ImageView) itemView.findViewById(R.id.ivExpandCollapse);
            ivTask = (ImageView) itemView.findViewById(R.id.ivTaskIcon);
            ivEvent = (ImageView) itemView.findViewById(R.id.ivEventIcon);
            ivNote = (ImageView) itemView.findViewById(R.id.ivNoteIcon);
            ivTaskAdd = (ImageView) itemView.findViewById(R.id.ivTaskAdd);
            ivEventAdd = (ImageView) itemView.findViewById(R.id.ivEventAdd);
            ivNoteAdd = (ImageView) itemView.findViewById(R.id.ivNoteAdd);
            ivCategoryIcon = (ImageView) itemView.findViewById(R.id.ivCategoryIcon);
            llContent = (LinearLayout) itemView.findViewById(R.id.llContent);
            rlCategory = (RelativeLayout) itemView.findViewById(R.id.rlCategory);
            rlTask = (RelativeLayout) itemView.findViewById(R.id.rlTask);
            rlEvent = (RelativeLayout) itemView.findViewById(R.id.rlEvent);
            rlNote = (RelativeLayout) itemView.findViewById(R.id.rlNote);
            vDivider = itemView.findViewById(R.id.vDivider);

            tvCategory.setOnClickListener(this);
            tvTask.setOnClickListener(this);
            tvEvent.setOnClickListener(this);
            tvNote.setOnClickListener(this);
            ivCategoryIcon.setOnClickListener(this);
            ivTask.setOnClickListener(this);
            ivEvent.setOnClickListener(this);
            ivNote.setOnClickListener(this);
            ivExpandCollapse.setOnClickListener(this);
            ivTaskAdd.setOnClickListener(this);
            ivEventAdd.setOnClickListener(this);
            ivNoteAdd.setOnClickListener(this);


            tvCategory.setOnLongClickListener(this);
            tvTask.setOnLongClickListener(this);
            ivTask.setOnLongClickListener(this);
            tvEvent.setOnLongClickListener(this);
            ivCategoryIcon.setOnLongClickListener(this);
            ivEvent.setOnLongClickListener(this);
            tvNote.setOnLongClickListener(this);
            ivNote.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvCategory || v.getId() == R.id.ivCategoryIcon) {
                mListener.onCategoryClick(getAdapterPosition());
            }
            if (v.getId() == R.id.ivTaskIcon || v.getId() == R.id.tvTask) {
                mListener.onContentClick(getAdapterPosition(), MyConstants.CONTENT_TASK);
            }
            if (v.getId() == R.id.ivEventIcon || v.getId() == R.id.tvEvent) {
                mListener.onContentClick(getAdapterPosition(), MyConstants.CONTENT_EVENT);
            }
            if (v.getId() == R.id.ivNoteIcon || v.getId() == R.id.tvNote) {
                mListener.onContentClick(getAdapterPosition(), MyConstants.CONTENT_NOTE);
            }
            if (v.getId() == R.id.ivExpandCollapse) {
                mListener.onCategoryExpandCollapse(llContent, ivExpandCollapse, getAdapterPosition());
            }
            if (v.getId() == R.id.ivTaskAdd) {
                mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_TASK);
            }
            if (v.getId() == R.id.ivEventAdd) {
                mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_EVENT);
            }
            if (v.getId() == R.id.ivNoteAdd) {
                mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_NOTE);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.tvCategory || v.getId() == R.id.ivCategoryIcon) {
                mListener.onCategoryLongClick(getAdapterPosition());
                return true;
            }
            if (v.getId() == R.id.tvTask || v.getId() == R.id.ivTaskIcon) {
                mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_TASK);
                return true;
            }
            if (v.getId() == R.id.tvEvent || v.getId() == R.id.ivEventIcon) {
                mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_EVENT);
                return true;
            }
            if (v.getId() == R.id.tvNote || v.getId() == R.id.ivNoteIcon) {
                mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_NOTE);
                return true;
            }
            return false;
        }


        public static interface myViewHolderClickListener {
            public void onCategoryClick(int position);

            public void onContentClick(int position, int contentType);

            public void onContentAdd(int position, int contentType);

            public void onCategoryExpandCollapse(LinearLayout llContent, ImageView ivExpandCollapse, int position);

            public void onCategoryLongClick(int position);
        }
    }

    static class categoryAddViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout rlAddCategory;
        addCategoryHolderListener mListener;

        public categoryAddViewHolder(View itemView, addCategoryHolderListener listener) {
            super(itemView);

            mListener = listener;

            rlAddCategory = (RelativeLayout) itemView.findViewById(R.id.rlCategoryAdd);
            rlAddCategory.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.rlCategoryAdd) {
                mListener.onAddCategoryClicked();
            }

        }

        public interface addCategoryHolderListener {
            public void onAddCategoryClicked();
        }
    }

    public void updateData() {
        ArrayList<Category> categories = mDatabaseHelper.getAllCategories();
        mCategories.clear();
        mCategories.addAll(categories);
        notifyDataSetChanged();
        Log.i("CategoryAdapter: ", "Title clicked");
    }

    private void setImageView(ImageView iv, int drawableId, int colorId) {
        Drawable icon = ResourcesCompat.getDrawable(mContext.getResources(), drawableId, null);
        icon.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), colorId, null), PorterDuff.Mode.MULTIPLY);
        iv.setImageDrawable(icon);
    }
}
