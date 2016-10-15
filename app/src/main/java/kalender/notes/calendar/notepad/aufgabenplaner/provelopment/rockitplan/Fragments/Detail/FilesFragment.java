package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.jar.Manifest;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 04.05.2016.
 */
public class FilesFragment extends Fragment {

    EditText etDescription;
    ImageView ivPicture;
    AppCompatImageView ivAddPicture;
    AppCompatImageView ivRemmovePicture;
    TextView tvAddPicture;

    int mContentType;
    Task mTask;
    Event mEvent;
    Note mNote;
    Content mContent;

    String mPicturePath;

    private int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 0;


    GeneralFragment.DetailActivityListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.tab_files, container, false);

        mContentType = getArguments().getInt(MyConstants.CONTENT_TYPE);

        switch (mContentType) {
            case 0:
                mContent = mListener.getTask();
                break;
            case 1:
                mContent = mListener.getEvent();
                break;
            case 2:
                mContent = mListener.getNote();
                break;
        }

        mPicturePath = mContent.getPicturePath();

        etDescription = (EditText) layout.findViewById(R.id.etDescription);
        ivPicture = (ImageView) layout.findViewById(R.id.ivPicture);
        ivAddPicture = (AppCompatImageView) layout.findViewById(R.id.ivAddPicture);
        ivRemmovePicture = (AppCompatImageView) layout.findViewById(R.id.ivRemoveDate);
        tvAddPicture = (TextView) layout.findViewById(R.id.tvAddPicture);

        // Description

        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mContent.setDescription(s.toString());
            }
        });
        etDescription.setText(mContent.getDescription());



        // Picture

        if (mPicturePath != null) {
            onSetPicture(mPicturePath);
        } else {
            ivAddPicture.setVisibility(View.VISIBLE);
            tvAddPicture.setVisibility(View.VISIBLE);
            ivPicture.setVisibility(View.GONE);
        }

        View.OnClickListener pictureOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for permission
                if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    mListener.takePicture(FilesFragment.this);
                }

            }
        };
        tvAddPicture.setOnClickListener(pictureOnClickListener);
        ivAddPicture.setOnClickListener(pictureOnClickListener);

        ivPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final File file = new File(mContent.getPicturePath());
                CharSequence[] items = {getActivity().getString(R.string.picture_edit), getActivity().getString(R.string.delete)};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                // Edit
                                file.delete();
                                mPicturePath = null;
                                mListener.takePicture(FilesFragment.this);
                                ivAddPicture.setVisibility(View.VISIBLE);
                                tvAddPicture.setVisibility(View.VISIBLE);
                                ivPicture.setVisibility(View.GONE);

                                break;
                            case 1:
                                // Delete
                                file.delete();
                                mPicturePath = null;
                                ivAddPicture.setVisibility(View.VISIBLE);
                                tvAddPicture.setVisibility(View.VISIBLE);
                                ivPicture.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

        return layout;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (GeneralFragment.DetailActivityListener) context;
    }

    public void onSetPicture(String filename) {

        File file = new File(filename);
        if (file.exists()) {
            mContent.setPicturePath(filename);
            //Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Bitmap bitmap = MyMethods.rotatePicture(filename);
            bitmap = MyMethods.pictureFillScreen(bitmap, MyMethods.getDisplayWidth(getActivity()));

            //bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
            Log.i("FilesFragment: ", Integer.toString(bitmap.getWidth()));
            Log.i("FilesFragment: ", Integer.toString(MyMethods.getDisplayWidth(getActivity())));

            ivPicture.setImageBitmap(bitmap);
            ivAddPicture.setVisibility(View.GONE);
            tvAddPicture.setVisibility(View.GONE);
            ivPicture.setVisibility(View.VISIBLE);
            Log.i("FilesFragment: ", "show Picture");
        } else {
            Log.i("FilesFragment: ", filename);
        }
    }

    public void handleFocus() {
        if (mContent != null) {
            Log.i("Meine Beschreibung", mContent.getDescription());
            if (mContent.getDescription().equals("")) {
                etDescription.requestFocus();
                Log.i("Descreption", "ist leer");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etDescription, InputMethodManager.SHOW_IMPLICIT);
            } else {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etDescription.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("Permission", "Grantedt");
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mListener.takePicture(FilesFragment.this);
            }
        }
        return;
    }

}