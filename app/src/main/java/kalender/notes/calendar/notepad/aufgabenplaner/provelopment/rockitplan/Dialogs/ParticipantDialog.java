package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Category;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.SearchResult;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Participant;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.CategoryColor;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.DetailsFragmentInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.SearchResultInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter.SearchAdapter;

import static android.view.View.GONE;

/**
 * Created by Eric on 25.02.2017.
 */

public class ParticipantDialog extends DialogFragment implements SearchResultInterface{

    int participantId;
    Participant mParticipant;
    DetailsFragmentInterface detailsFragmentInterface;
    DatabaseHelper mDatabaseHelper;

    ImageView ivName;
    ImageView ivInformation;
    RecyclerView rvContacts;

    RelativeLayout rlTitle;
    RelativeLayout rlMetaData;

    EditText etName;

    ArrayList<SearchResult> mContacts;

    boolean mPermissionReadContacts;

    @Override
    public void onResume() {
        super.onResume();
        // Handle Focus
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mParticipant.getName().equals("")) {
            ParticipantDialog.this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            Log.i("HEY; ", "Eric!");
            getActivity().getWindow().getDecorView().clearFocus();
            etName.requestFocus();

            imm.showSoftInput(etName, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInputFromWindow(
                    etName.getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);
        } else {
            ParticipantDialog.this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mDatabaseHelper = new DatabaseHelper(getActivity());

        // Get Participant
        mParticipant = mDatabaseHelper.getParticipant(getArguments().getInt(MyConstants.PARTICIPANT_ID));

        // Set up Listener
        detailsFragmentInterface = (DetailsFragmentInterface) getTargetFragment();

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            mPermissionReadContacts = false;
        } else {
            mPermissionReadContacts = true;
        }

        // Set up Layout
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_participant, null);

        ivName = (ImageView) dialogView.findViewById(R.id.ivName);
        ivInformation = (ImageView) dialogView.findViewById(R.id.ivInformation);

        etName = (EditText) dialogView.findViewById(R.id.etName);
        EditText etInformation = (EditText) dialogView.findViewById(R.id.etInformation);

        rlTitle = (RelativeLayout)dialogView.findViewById(R.id.rlTitle);
        rlMetaData = (RelativeLayout)dialogView.findViewById(R.id.rlMetaData);

        ImageButton ibSave = (ImageButton) dialogView.findViewById(R.id.ibSave);
        ImageButton ibDelete = (ImageButton) dialogView.findViewById(R.id.ibDelete);

        // Set up RecyclerView
        rvContacts = (RecyclerView)dialogView.findViewById(R.id.rvParticipantDialog);
        rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContacts = new ArrayList<SearchResult>();
        final SearchAdapter contactsAdapter = new SearchAdapter(getActivity(), mContacts, ParticipantDialog.this, R.drawable.ic_phone_book_18dp);
        rvContacts.setAdapter(contactsAdapter);



        // Set up Color
        Category category = mDatabaseHelper.getCategory(getArguments().getInt(MyConstants.CATEGORY_ID));
        CategoryColor categoryColor = new CategoryColor(getActivity(), category.getColor());
        rlTitle.setBackgroundColor(ResourcesCompat.getColor(getResources(), categoryColor.getCategoryColor(), null));

        etName.setText(mParticipant.getName());
        etInformation.setText(mParticipant.getInformation());
        handleName();
        handleInformation();

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mParticipant.setName(editable.toString());
                handleName();
                if (editable.toString().length() > 0 && mPermissionReadContacts) {
                    mContacts.clear();
                    rlMetaData.setVisibility(GONE);
                    // start search
                    String selection = ContactsContract.Contacts.DISPLAY_NAME + " LIKE '%" + editable.toString() + "%'";
                    ContentResolver cr = getActivity().getContentResolver();
                    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, selection, null, null);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            SearchResult contact = new SearchResult();
                            contact.setContactId(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                            contact.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                            mContacts.add(contact);
                        }
                        cursor.close();
                        rvContacts.setVisibility(View.VISIBLE);
                        contactsAdapter.updateSearchResults(mContacts);

                    }
                }
                if(editable.toString().equals("")) {
                    rvContacts.setVisibility(View.GONE);
                    rlMetaData.setVisibility(View.VISIBLE);
                }
            }
        });

        etInformation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mParticipant.setInformation(editable.toString());
                handleInformation();
            }
        });

        ibSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mParticipant.getName().equals("")) {
                    mDatabaseHelper.updateParticipant(mParticipant);
                } else {
                    mDatabaseHelper.deleteParticipant(mParticipant.getId());
                }
                detailsFragmentInterface.updateParticipants();
                ParticipantDialog.this.getDialog().cancel();

            }
        });

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteParticipant(mParticipant.getId());
                detailsFragmentInterface.updateParticipants();
                ParticipantDialog.this.getDialog().cancel();
            }
        });


        builder.setView(dialogView);
        setCancelable(false);
        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;

    }

    private boolean isEmpty(String string) {
        if (string.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private void setImageView(ImageView iv, int drawableId, int colorId) {
        Drawable icon = ResourcesCompat.getDrawable(getActivity().getResources(), drawableId, null);
        icon.mutate().setColorFilter(ResourcesCompat.getColor(getActivity().getResources(), colorId, null), PorterDuff.Mode.MULTIPLY);
        iv.setImageDrawable(icon);
    }

    private void handleName() {
        if (isEmpty(mParticipant.getName())) {
            ivName.setVisibility(View.VISIBLE);
        } else {
            ivName.setVisibility(GONE);
        }
    }
    private void handleInformation() {
        if (isEmpty(mParticipant.getInformation())) {
            setImageView(ivInformation, R.drawable.ic_add_18dp, R.color.colorDivider);
        } else {
            setImageView(ivInformation, R.drawable.ic_info_18dp, R.color.colorSecondaryText);
        }
    }

    @Override
    public void onSearchResultSelected(SearchResult searchResult) {
        mParticipant.setName(searchResult.getName());
        etName.setText(searchResult.getName());
        mParticipant.setContactId(searchResult.getContactId());
        rvContacts.setVisibility(GONE);
        rlMetaData.setVisibility(View.VISIBLE);
    }
}
