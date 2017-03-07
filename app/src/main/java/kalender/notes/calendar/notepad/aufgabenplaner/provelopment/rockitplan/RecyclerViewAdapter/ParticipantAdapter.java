package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.RecyclerViewAdapter;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Participant;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.DatabaseHelper;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Dialogs.ParticipantDialog;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail.DetailsFragment;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 24.02.2017.
 */

public class ParticipantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Participant> mParticipants;
    DatabaseHelper mDatabaseHelper;
    Context mContext;
    int mCategoryId;
    LayoutInflater mLayoutInflater;
    DetailsFragment mFragment;
    FragmentManager mFragmentManager;

    public ParticipantAdapter(Context context, int contentId, int categoryId, DetailsFragment fragment, FragmentManager fragmentManager) {
        mContext = context;
        mFragment = fragment;
        mFragmentManager = fragmentManager;
        mCategoryId = categoryId;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatabaseHelper = new DatabaseHelper(context);
        mParticipants = mDatabaseHelper.getAllContentParticipants(contentId);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_participant, parent, false);
        ParticipantAdapter.participantHolder holder = new participantHolder(view, new participantHolder.participantHolderClickListener() {
            @Override
            public void onEdit(int position) {
                DialogFragment dialog = new ParticipantDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(MyConstants.PARTICIPANT_ID, mParticipants.get(position).getId());
                bundle.putInt(MyConstants.CATEGORY_ID, mCategoryId);
                dialog.setArguments(bundle);
                dialog.setTargetFragment(mFragment, 0);
                dialog.show(mFragmentManager, "participant_fragment");
            }

            @Override
            public void onCall(int position) {
                Participant participant = mParticipants.get(position);
                final ArrayList<String> phoneNumbers = new ArrayList<>();
                ContentResolver cr = mContext.getContentResolver();
                Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{participant.getContactId()}, null);
                while (pCur.moveToNext()) {
                    String number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    number = number.replace(" ", "");
                    phoneNumbers.add(number);
                }
                pCur.close();
                Set<String> hs = new HashSet<>();
                hs.addAll(phoneNumbers);
                phoneNumbers.clear();
                phoneNumbers.addAll(hs);
                if (phoneNumbers.size() > 1) {
                    final CharSequence[] items = new CharSequence[phoneNumbers.size()];
                    for (int i = 0; i < phoneNumbers.size(); i++) {
                        items[i] = phoneNumbers.get(i);
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int items) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + phoneNumbers.get(items)));
                            mContext.startActivity(callIntent);

                        }
                    });
                    builder.create().show();
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumbers.get(0)));
                    mContext.startActivity(callIntent);
                }

            }

            @Override
            public void onMail(int position) {
                final Participant participant = mParticipants.get(position);
                final ArrayList<String> phoneNumbers = new ArrayList<>();

                final ContentResolver cr = mContext.getContentResolver();

                if (hasMailAddress(participant) && hasPhoneNumber(participant)) {
                    final CharSequence[] items = {mContext.getString(R.string.e_mail), mContext.getString(R.string.sms)};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int items) {
                            switch (items) {
                                case 0:
                                    final ArrayList<String> eMailAdresses = new ArrayList<String>();
                                    Cursor emailCur = cr.query(
                                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{participant.getContactId()}, null);
                                    while (emailCur.moveToNext()) {
                                        // This would allow you get several email addresses
                                        // if the email addresses were stored in an array
                                        eMailAdresses.add(emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
                                        String emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                                    }
                                    emailCur.close();
                                    if (eMailAdresses.size() > 1) {
                                        final CharSequence[] items2 = new CharSequence[]{};
                                        for (int i = 0; i < eMailAdresses.size(); i++) {
                                            items2[i] = eMailAdresses.get(i);
                                        }
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setItems(items2, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int items) {
                                                writeEmail(eMailAdresses.get(items));

                                            }
                                        });
                                        builder.create().show();
                                    } else {
                                        writeEmail(eMailAdresses.get(0));
                                    }
                                    break;
                                case 1:
                                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{participant.getContactId()}, null);
                                    while (pCur.moveToNext()) {
                                        String number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        number = number.replace(" ", "");
                                        phoneNumbers.add(number);
                                    }
                                    pCur.close();
                                    Set<String> hs = new HashSet<>();
                                    hs.addAll(phoneNumbers);
                                    phoneNumbers.clear();
                                    phoneNumbers.addAll(hs);
                                    if (phoneNumbers.size() > 1) {
                                        final CharSequence[] items2 = new CharSequence[phoneNumbers.size()];
                                        for (int i = 0; i < phoneNumbers.size(); i++) {
                                            items2[i] = phoneNumbers.get(i);
                                        }
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                        builder.setItems(items2, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int items) {
                                                writeSms(phoneNumbers.get(items));

                                            }
                                        });
                                        builder.create().show();
                                    } else {
                                        writeSms(phoneNumbers.get(0));
                                    }
                                    break;
                            }
                        }
                    });
                    builder.create().show();
                } else {
                    if (hasMailAddress(participant)) {
                        final ArrayList<String> eMailAdresses = new ArrayList<String>();
                        Cursor emailCur = cr.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{participant.getContactId()}, null);
                        while (emailCur.moveToNext()) {
                            // This would allow you get several email addresses
                            // if the email addresses were stored in an array
                            eMailAdresses.add(emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
                            String emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        }
                        emailCur.close();
                        if (eMailAdresses.size() > 1) {
                            final CharSequence[] items2 = new CharSequence[eMailAdresses.size()];
                            for (int i = 0; i < eMailAdresses.size(); i++) {
                                items2[i] = eMailAdresses.get(i);
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setItems(items2, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int items) {
                                    writeEmail(eMailAdresses.get(items));

                                }
                            });
                            builder.create().show();
                        } else {
                            writeEmail(eMailAdresses.get(0));
                        }
                    } else {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{participant.getContactId()}, null);
                        while (pCur.moveToNext()) {
                            String number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            number = number.replace(" ", "");
                            phoneNumbers.add(number);
                        }
                        pCur.close();
                        Set<String> hs = new HashSet<>();
                        hs.addAll(phoneNumbers);
                        phoneNumbers.clear();
                        phoneNumbers.addAll(hs);
                        if (phoneNumbers.size() > 1) {
                            final CharSequence[] items2 = new CharSequence[phoneNumbers.size()];
                            for (int i = 0; i < phoneNumbers.size(); i++) {
                                items2[i] = phoneNumbers.get(i);
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setItems(items2, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int items) {
                                    writeSms(phoneNumbers.get(items));

                                }
                            });
                            builder.create().show();
                        } else {
                            writeSms(phoneNumbers.get(0));
                        }
                    }
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        Participant participant = mParticipants.get(position);
        participantHolder holder = (participantHolder) h;
        holder.tvParticipant.setText(participant.getName());

        if (!hasPhoneNumber(participant)) {
            holder.ivCallParticipant.setVisibility(View.GONE);
            if (hasMailAddress(participant)) {
                holder.ivMailParticipant.setVisibility(View.VISIBLE);
            } else {
                holder.ivMailParticipant.setVisibility(View.GONE);
            }
        } else {
            holder.ivCallParticipant.setVisibility(View.VISIBLE);
            holder.ivMailParticipant.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    static class participantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvParticipant;
        ImageView ivCallParticipant;
        ImageView ivMailParticipant;
        ImageView ivParticipant;

        ParticipantAdapter.participantHolder.participantHolderClickListener mListener;

        public participantHolder(View itemView, ParticipantAdapter.participantHolder.participantHolderClickListener listener) {
            super(itemView);
            tvParticipant = (TextView) itemView.findViewById(R.id.tvParticipant);
            ivCallParticipant = (ImageView) itemView.findViewById(R.id.ivParticipantPhone);
            ivMailParticipant = (ImageView) itemView.findViewById(R.id.ivParticipantMail);
            ivParticipant = (ImageView) itemView.findViewById(R.id.ivParticipantIcon);
            mListener = listener;
            tvParticipant.setOnClickListener(this);
            ivCallParticipant.setOnClickListener(this);
            ivMailParticipant.setOnClickListener(this);
            ivParticipant.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvParticipant || v.getId() == R.id.ivParticipantIcon) {
                mListener.onEdit(getAdapterPosition());
            }
            if (v.getId() == R.id.ivParticipantPhone) {
                mListener.onCall(getAdapterPosition());
            }
            if (v.getId() == R.id.ivParticipantMail) {
                mListener.onMail(getAdapterPosition());
            }
        }

        public interface participantHolderClickListener {
            public void onEdit(int position);

            public void onCall(int position);

            public void onMail(int position);
        }

    }

    private void writeEmail(String email) {
        String uriText = "mailto:" + email;

        Uri uri = Uri.parse(uriText);

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        mContext.startActivity(Intent.createChooser(sendIntent, "Send email"));
    }

    private void writeSms(String phoneNumber) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        mContext.startActivity(it);
    }

    private boolean hasPhoneNumber(Participant participant) {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts.HAS_PHONE_NUMBER}, ContactsContract.Contacts._ID + " = ?", new String[]{participant.getContactId()}, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private boolean hasMailAddress(Participant participant) {
        ContentResolver cr = mContext.getContentResolver();
        Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{participant.getContactId()}, null);
        while (emailCur.moveToNext()) {
            return true;
        }
        emailCur.close();
        return false;
    }

}
