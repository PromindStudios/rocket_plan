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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.MainActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.ContentHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.LayoutColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.AddEditCategoryDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.DeleteContentDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.DrawerFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

import static kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R.string.calendar;

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
    DrawerFragment mDrawerFragment;
    FragmentManager mFragmentManager;
    LayoutColor mLayoutColor;

    CategoryAdapterListener mCategoryAdapterListener;

    public CategoryAdapter(Context context, ArrayList<Category> categories, DatabaseHelper databaseHelper, DrawerFragment drawerFragment, FragmentManager fragmentManager) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mCategories = categories;
        mDatabaseHelper = databaseHelper;
        mLayoutColor = new LayoutColor(mContext, mDatabaseHelper.getLayoutColorValue());
        mDrawerFragment = drawerFragment;
        mFragmentManager = fragmentManager;
        mCategoryAdapterListener = (MainActivity) mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_TIME:
                view = mLayoutInflater.inflate(R.layout.item_time_calendar, parent, false);
                timeCalendarViewHolder timeHolder = new timeCalendarViewHolder(view, new timeCalendarViewHolder.timeViewHolderListener() {
                    @Override
                    public void onTimeCalendarClicked() {
                        mCategoryAdapterListener.onTimeClicked();
                    }
                });
                return timeHolder;
            case TYPE_CATEGORY:
                view = mLayoutInflater.inflate(R.layout.item_category, parent, false);
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
                                contentSize = contentHelper.getUndoneContentSize(mDatabaseHelper.getAllCategoryTasks(category.getId()), contentType);
                                break;
                            case MyConstants.CONTENT_EVENT:
                                contentSize = contentHelper.getUndoneContentSize(mDatabaseHelper.getAllCategoryEvents(category.getId()), contentType);
                                break;
                            case MyConstants.CONTENT_NOTE:
                                contentSize = mDatabaseHelper.getAllCategoryNotes(category.getId(), category.isNoteSortedByPriority()).size();
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
                    public void onCategoryExpandCollapse(RelativeLayout rlMain, ImageView ivExpandCollapse, RelativeLayout rlCategoryTitle, RelativeLayout llCategoryContent, int position) {
                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) rlMain.getLayoutParams();
                        Category category = getCorrectCategory(position);
                        CategoryColor categoryColor = new CategoryColor(mContext, category.getColor());
                        Drawable iconExpandCollapse;
                        if (category.isExpanded()) {
                            category.setExpanded(false);
                            params.height = MyMethods.dpToPx(mContext, CATEGORY_TITLE_HEIGHT);
                            rlMain.setLayoutParams(params);
                            rlCategoryTitle.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyMethods.dpToPx(mContext, CATEGORY_TITLE_HEIGHT)));
                            llCategoryContent.setVisibility(View.GONE);
                            iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_collapsed_24_white, null);
                            mDatabaseHelper.updateCategoryExpanded(category);
                        } else {
                            category.setExpanded(true);
                            params.height = MyMethods.dpToPx(mContext, CATEGORY_ALL_HEIGHT);
                            rlCategoryTitle.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyMethods.dpToPx(mContext, CATEGORY_TITLE_HEIGHT)));
                            rlMain.setLayoutParams(params);
                            llCategoryContent.setVisibility(View.VISIBLE);
                            iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_expanded_24_white, null);
                            mDatabaseHelper.updateCategoryExpanded(category);
                        }
                        iconExpandCollapse.mutate().setColorFilter(ResourcesCompat.getColor(mContext.getResources(), categoryColor.getCategoryColor(), null), PorterDuff.Mode.MULTIPLY);
                        Log.i("Coloor: ", Integer.toString(categoryColor.mColor));
                        ivExpandCollapse.setImageDrawable(iconExpandCollapse);
                    }

                    @Override
                    public void onCategoryLongClick(int position) {
                        final Category category = getCorrectCategory(position);
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
                                        dialog_edit.setTargetFragment(mDrawerFragment, 0);
                                        dialog_edit.show(mFragmentManager, "Add_Category");
                                        break;
                                    case 1:
                                        // Delete
                                        DeleteContentDialog d = new DeleteContentDialog();
                                        Bundle b = new Bundle();
                                        b.putInt(MyConstants.CONTENT_ID, category.getId());
                                        b.putInt(MyConstants.CATEGORY_ID, category.getId());
                                        b.putInt(MyConstants.CONTENT_TYPE, 0);
                                        d.setArguments(b);
                                        d.setTargetFragment(mDrawerFragment, 0);
                                        d.show(mFragmentManager, "..");
                                        break;
                                    case 2:
                                        // Move up
                                        int p = mCategories.indexOf(category);
                                        mCategories.remove(p);
                                        if ((p - 1) >= 1) {
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
                                        mCategories.remove(pDown);
                                        if ((pDown + 1) <= size) {
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
            case TYPE_CALENDAR:
                view = mLayoutInflater.inflate(R.layout.item_time_calendar, parent, false);
                timeCalendarViewHolder calendarHolder = new timeCalendarViewHolder(view, new timeCalendarViewHolder.timeViewHolderListener() {
                    @Override
                    public void onTimeCalendarClicked() {
                        mCategoryAdapterListener.onCalendarClicked();
                    }
                });
                return calendarHolder;
            case TYPE_ADD_CATEGORY:
                view = mLayoutInflater.inflate(R.layout.item_add_category, parent, false);
                categoryAddViewHolder categoryAddHolder = new categoryAddViewHolder(view, new categoryAddViewHolder.addCategoryHolderListener() {
                    @Override
                    public void onAddCategoryClicked() {
                        mDrawerFragment.addCategory();
                    }
                });
                return categoryAddHolder;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        if (position == 0) {
            timeCalendarViewHolder timeholder = (timeCalendarViewHolder) vholder;
            timeholder.tvTitle.setText(mContext.getString(R.string.overview));
            timeholder.ivTimeCalendar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_three_lines_vertical));
            timeholder.ivTimeCalendar.setColorFilter(mLayoutColor.getLayoutColor());
            timeholder.vDivider.setVisibility(View.GONE);
        } else {
            if (position == 1) {
                timeCalendarViewHolder calendarholder = (timeCalendarViewHolder) vholder;
                calendarholder.tvTitle.setText(mContext.getString(calendar));
                calendarholder .ivTimeCalendar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_two_lines_horizontal));
                calendarholder .ivTimeCalendar.setColorFilter(mLayoutColor.getLayoutColor());
            } else {
                if (position == mCategories.size()+2) {

                } else {
                    categoryHolder holder = (categoryHolder) vholder;
                    final Category category = getCorrectCategory(position);

                    // Set heigt of category item
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.rlMain.getLayoutParams();
                    if (category.isExpanded()) {
                        params.height = MyMethods.dpToPx(mContext, CATEGORY_ALL_HEIGHT);
                        holder.rlMain.setLayoutParams(params);
                        holder.rlCategoryTitle.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyMethods.dpToPx(mContext, CATEGORY_TITLE_HEIGHT)));
                        holder.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expanded_24dp));
                        holder.llCategoryContent.setVisibility(View.VISIBLE);
                    } else {
                        params.height = MyMethods.dpToPx(mContext, CATEGORY_TITLE_HEIGHT);
                        holder.rlMain.setLayoutParams(params);
                        holder.ivExpandCollapse.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_collapsed_24dp));
                        holder.llCategoryContent.setVisibility(View.GONE);
                    }

                    // Title
                    holder.tvCategoryTitle.setText(category.getTitle());

                    ContentHelper contentHelper = new ContentHelper(mContext, category.getId());

                    int taskSize = contentHelper.getUndoneContentSize(mDatabaseHelper.getAllCategoryTasks(category.getId()), MyConstants.CONTENT_TASK);
                    int eventSize = contentHelper.getUndoneContentSize(mDatabaseHelper.getAllCategoryEvents(category.getId()), MyConstants.CONTENT_EVENT);
                    int noteSize = mDatabaseHelper.getAllCategoryNotes(category.getId(), category.isNoteSortedByPriority()).size();

                    if (taskSize == 0) {
                        holder.tvTask.setText("-");
                    } else {
                        holder.tvTask.setText(Integer.toString(taskSize));
                    }
                    if (eventSize == 0) {
                        holder.tvEvent.setText("-");
                    } else {
                        holder.tvEvent.setText(Integer.toString(eventSize));
                    }
                    if (noteSize == 0) {
                        holder.tvNote.setText("-");
                    } else {
                        holder.tvNote.setText(Integer.toString(noteSize));
                    }

                    // Handle Category Color
                    CategoryColor categoryColor = new CategoryColor(mContext, category.getColor());


                    Drawable iconExpandCollapse;
                    if (category.isExpanded()) {
                        iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_expanded_24_white, null);
                    } else {
                        iconExpandCollapse = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_collapsed_24_white, null);
                    }
                    holder.ivExpandCollapse.setImageDrawable(categoryColor.colorIcon(iconExpandCollapse));


                    holder.ivTask.setImageDrawable(categoryColor.colorIcon(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_task, null)));
                    holder.ivEvent.setImageDrawable(categoryColor.colorIcon(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_event, null)));
                    holder.ivNote.setImageDrawable(categoryColor.colorIcon(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_note, null)));

                    if (position == mCategories.size()) {
                        //holder.vDivider.setVisibility(View.GONE);
                        holder.vDivider.setVisibility(View.GONE);
                    } else {
                        holder.vDivider.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mCategories.size() + 3);
    }

    @Override
    public int getItemViewType(int position) {
        int addCategoryPosition = mCategories.size()+2;
        if (position == 0) {
            return TYPE_TIME;
        }
        if (position == 1) {
            return TYPE_CALENDAR;
        }
        if (position == mCategories.size()+2) {
            return TYPE_ADD_CATEGORY;
        } else {
            return TYPE_CATEGORY;
        }
    }

    static class categoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvCategoryTitle;
        TextView tvTask;
        TextView tvEvent;
        TextView tvNote;

        ImageView ivExpandCollapse;
        ImageView ivTask;
        ImageView ivEvent;
        ImageView ivNote;

        View vDivider;

        RelativeLayout llCategoryContent;
        RelativeLayout rlMain;
        RelativeLayout rlCategoryTitle;

        myViewHolderClickListener mListener;

        public categoryHolder(View itemView, myViewHolderClickListener listener) {
            super(itemView);
            mListener = listener;
            tvCategoryTitle = (TextView) itemView.findViewById(R.id.tvCategoryTitle);
            tvTask = (TextView) itemView.findViewById(R.id.tvTask);
            tvEvent = (TextView) itemView.findViewById(R.id.tvEvent);
            tvNote = (TextView) itemView.findViewById(R.id.tvNote);
            ivExpandCollapse = (ImageView) itemView.findViewById(R.id.ivExpandCollapse);
            ivTask = (ImageView) itemView.findViewById(R.id.ivTask);
            ivEvent = (ImageView) itemView.findViewById(R.id.ivEvent);
            ivNote = (ImageView) itemView.findViewById(R.id.ivNote);
            llCategoryContent = (RelativeLayout) itemView.findViewById(R.id.llCategoryContent);
            rlMain = (RelativeLayout) itemView.findViewById(R.id.rlMain);
            rlCategoryTitle = (RelativeLayout) itemView.findViewById(R.id.rlCategory);
            vDivider = itemView.findViewById(R.id.vDivider);

            tvCategoryTitle.setOnClickListener(this);
            tvTask.setOnClickListener(this);
            tvEvent.setOnClickListener(this);
            tvNote.setOnClickListener(this);
            ivTask.setOnClickListener(this);
            ivEvent.setOnClickListener(this);
            ivNote.setOnClickListener(this);
            ivExpandCollapse.setOnClickListener(this);
            tvCategoryTitle.setOnLongClickListener(this);
            tvTask.setOnLongClickListener(this);
            ivTask.setOnLongClickListener(this);
            tvEvent.setOnLongClickListener(this);
            ivEvent.setOnLongClickListener(this);
            tvNote.setOnLongClickListener(this);
            ivNote.setOnLongClickListener(this);
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
            if (v.getId() == R.id.ivExpandCollapse) {
                mListener.onCategoryExpandCollapse(rlMain, ivExpandCollapse, rlCategoryTitle, llCategoryContent, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.tvCategoryTitle:
                    mListener.onCategoryLongClick(getAdapterPosition());
                    return true;
                case R.id.ivTask:
                    mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_TASK);
                    return true;
                case R.id.tvTask:
                    mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_TASK);
                    return true;
                case R.id.ivEvent:
                    mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_EVENT);
                    return true;
                case R.id.tvEvent:
                    mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_EVENT);
                    return true;
                case R.id.ivNote:
                    mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_NOTE);
                    return true;
                case R.id.tvNote:
                    mListener.onContentAdd(getAdapterPosition(), MyConstants.CONTENT_NOTE);
                    return true;
                default:
                    return false;
            }
        }


        public static interface myViewHolderClickListener {
            public void onCategoryClick(int position);

            public void onContentClick(int position, int contentType);

            public void onContentAdd(int position, int contentType);

            public void onCategoryExpandCollapse(RelativeLayout rlMain, ImageView ivExpandCollapse, RelativeLayout rlCategoryTitle, RelativeLayout llCategoryContent, int position);

            public void onCategoryLongClick(int position);
        }
    }

    static class timeCalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout rlTime;
        TextView tvTitle;
        ImageView ivTimeCalendar;
        timeViewHolderListener mListener;
        View vDivider;

        public timeCalendarViewHolder(View itemView, timeViewHolderListener listener) {
            super(itemView);

            mListener = listener;

            rlTime = (RelativeLayout) itemView.findViewById(R.id.rlTime);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivTimeCalendar = (ImageView)itemView.findViewById(R.id.ivTimeCalendar);
            vDivider = itemView.findViewById(R.id.vDivider);
            rlTime.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.rlTime) {
                mListener.onTimeCalendarClicked();
            }

        }

        public interface timeViewHolderListener {
            public void onTimeCalendarClicked();
        }
    }

    static class categoryAddViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout rlAddCategory;
        addCategoryHolderListener mListener;

        public categoryAddViewHolder(View itemView, addCategoryHolderListener listener) {
            super(itemView);

            mListener = listener;

            rlAddCategory = (RelativeLayout) itemView.findViewById(R.id.rlAddCategory);
            rlAddCategory.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.rlAddCategory) {
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

    public interface CategoryAdapterListener {
        public void ItemCategorySelected(int categoryId, String categoryName, int contentType);

        public void onCreateNewContent(int categoryId, String categoryName, int contentTyp);

        public void onTimeClicked();

        public void onCalendarClicked();
    }

    public Category getCorrectCategory(int position) {
        return mCategories.get(position - 2);
    }


}
