package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Fragments.Element_Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.Element_Activities.ImageActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.Element_Activities.VideoActivity;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Content;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Event;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Note;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.BasicClasses.Task;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.AnalyticsInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Interfaces.PremiumInterface;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.Functions;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by eric on 04.05.2016.
 */
public class NotesFragment extends Fragment {

    // Layout variables
    View vDummy;
    ImageView ivDescriptionIcon;
    ImageView ivDescriptionIconAdd;
    EditText etDescription;

    ImageView ivImageIconAdd;
    ImageView ivImageIcon;
    ImageView ivImage;
    TextView tvImage;
    ImageView ivRemoveAudio;
    View vImageMeasure;

    ImageView ivAudioIcon;
    TextView tvAudio;

    ImageView ivVideoIconAdd;
    TextView tvVideo;
    ImageView ivRemoveVideo;

    int mContentType;
    Task mTask;
    Event mEvent;
    Note mNote;
    Content mContent;


    String mPicturePath;
    String mAudioPath;
    String mVideoPath;

    // Audio
    int mAudioStatus;
    boolean mAudioRecording = false;
    boolean mAudioPlaying = false;
    private final int AUDIO_INACTIVE = 0;
    private final int AUDIO_RECORDING = 1;
    private final int AUDIO_PLAYING = 2;
    private final int AUDIO_NONE = 4;

    MediaRecorder mRecorder;
    MediaPlayer mPlayer;

    PremiumInterface mPremiumInterface;
    AnalyticsInterface mAnalyticsInterface;


    private final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    private final int PERMISSION_REQUEST_RECORD_AUDIO = 200;

    GeneralFragment.DetailActivityListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.tab_files, container, false);

        mContent = mListener.getContent();

        mPicturePath = mContent.getPicturePath();

        // Initialize Layout components
        etDescription = (EditText) layout.findViewById(R.id.etDescription);
        ivDescriptionIconAdd = (ImageView) layout.findViewById(R.id.ivDescriptionIconAdd);
        ivDescriptionIcon = (ImageView) layout.findViewById(R.id.ivDescriptionIcon);
        vDummy = layout.findViewById(R.id.vDummy);

        ivImageIconAdd = (ImageView) layout.findViewById(R.id.ivImageIconAdd);
        ivImageIcon = (ImageView) layout.findViewById(R.id.ivImageIcon);
        ivImage = (ImageView) layout.findViewById(R.id.ivImage);
        tvImage = (TextView) layout.findViewById(R.id.tvImage);
        vImageMeasure = layout.findViewById(R.id.vImageMeasure);

        ivAudioIcon = (ImageView) layout.findViewById(R.id.ivAudioIconAdd);
        tvAudio = (TextView) layout.findViewById(R.id.tvAudio);
        ivRemoveAudio = (ImageView) layout.findViewById(R.id.ivRemoveAudio);

        ivVideoIconAdd = (ImageView) layout.findViewById(R.id.ivVideoIconAdd);
        ivRemoveVideo = (ImageView) layout.findViewById(R.id.ivRemoveVideo);
        tvVideo = (TextView) layout.findViewById(R.id.tvVideo);

        // Description
        setImageView(ivDescriptionIconAdd, R.drawable.ic_add_18dp, R.color.colorDivider);
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
                handleDescriptionVisibility();
            }
        });
        etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) mAnalyticsInterface.track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_DESCRIPTION);
            }
        });

        etDescription.setText(mContent.getDescription());
        handleDescriptionVisibility();

        // Picture
        setImageView(ivImageIconAdd, R.drawable.ic_add_18dp, R.color.colorDivider);
        handleImageVisibility();

        View.OnClickListener pictureOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnalyticsInterface.track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_IMAGE);
                if (!Functions.PREMIUM_FUNCTION_NOTES_IMAGE || mPremiumInterface.hasPremium()) {
                    // Check for permission
                    if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                    } else {
                        mListener.takePicture(NotesFragment.this);
                    }
                } else {
                    mPremiumInterface.openDialogPremiumFunction(getString(R.string.premium_function), getString(R.string.premium_silver_image), getString(R.string.premium_expired));
                }


            }
        };
        ivImageIconAdd.setOnClickListener(pictureOnClickListener);
        tvImage.setOnClickListener(pictureOnClickListener);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImageActivity.class);
                intent.putExtra(MyConstants.IMAGE_PATH, mContent.getPicturePath());
                startActivity(intent);
            }
        });
        ivImage.setOnLongClickListener(new View.OnLongClickListener() {
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
                                mContent.setPicturePath(null);
                                mListener.takePicture(NotesFragment.this);
                                break;
                            case 1:
                                // Delete
                                file.delete();
                                mContent.setPicturePath(null);
                                handleImageVisibility();
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

        // Video
        if (mContent.hasVideo()) {
            setImageView(ivVideoIconAdd, R.drawable.ic_video_18dp, R.color.colorSecondaryText);
            setTextView(tvVideo, R.string.detail_video_play, R.color.colorSecondaryText);
            ivRemoveVideo.setVisibility(View.VISIBLE);
        } else {
            setImageView(ivVideoIconAdd, R.drawable.ic_add_18dp, R.color.colorDivider);
            setTextView(tvVideo, R.string.detail_video, R.color.colorDivider);
            ivRemoveVideo.setVisibility(View.GONE);
        }
        View.OnClickListener videoOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Functions.PREMIUM_FUNCTION_NOTES_VIDEO || mPremiumInterface.hasPremium()) {
                    if (mContent.hasVideo()) {
                        // play video in extra activity
                        getActivity().startActivity(new Intent(getActivity(), VideoActivity.class).putExtra(MyConstants.VIDEO_PATH, mContent.getVideoPath()));
                    } else {
                        // record video
                        mAnalyticsInterface.track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_VIDEO);
                        getActivity().startActivityForResult(new Intent(MediaStore.ACTION_VIDEO_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, createVideoPath()), MyConstants.REQUEST_VIDEO_RECORD);
                    }
                } else {
                    mPremiumInterface.openDialogPremiumFunction(getString(R.string.premium_function), getString(R.string.premium_silver_video), getString(R.string.premium_expired));
                }

            }
        };
        tvVideo.setOnClickListener(videoOnClickListener);
        ivVideoIconAdd.setOnClickListener(videoOnClickListener);
        ivRemoveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new File(mContent.getVideoPath()).delete();
                mContent.setVideoPath(null);
                setImageView(ivVideoIconAdd, R.drawable.ic_add_18dp, R.color.colorDivider);
                setTextView(tvVideo, R.string.detail_video, R.color.colorDivider);
                ivRemoveVideo.setVisibility(View.GONE);
            }
        });

        // Audio
        handleAudioViews();


        View.OnClickListener audioClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Functions.PREMIUM_FUNCTION_NOTES_AUDIO || mPremiumInterface.hasPremium()) {
                    if (mAudioRecording || !mContent.hasAudio()) {
                        onRecordAudio();
                    } else {
                        onPlayAudio();
                    }
                } else {
                    mPremiumInterface.openDialogPremiumFunction(getString(R.string.premium_function), getString(R.string.premium_silver_audio), getString(R.string.premium_expired));
                }

            }
        };
        ivAudioIcon.setOnClickListener(audioClickListener);
        tvAudio.setOnClickListener(audioClickListener);
        ivRemoveAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new File(mContent.getAudioPath()).delete();
                mContent.setAudioPath(null);
                mAudioRecording = false;
                handleAudioViews();
            }
        });

        // Handle Focus
        //handleFocus();

        return layout;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.selectTabTwo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener.getCurrentTab() == 2) {
            //mListener.selectTabTwo();
            handleFocus();
        }
        if (mContent.getPicturePath() != null) onSetPicture(mContent.getPicturePath());

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (GeneralFragment.DetailActivityListener) context;
        mPremiumInterface = (PremiumInterface)context;
        mAnalyticsInterface = (AnalyticsInterface)context;
    }

    public void onSetPicture(String filename) {

        File file = new File(filename);
        if (file.exists()) {
            mContent.setPicturePath(filename);
            Bitmap bitmap = MyMethods.rotatePicture(filename);

            //int prefWidth = vImageMeasure.getWidth();
            int prefWidth = MyMethods.getDisplayWidth(getActivity()) - MyMethods.dpToPx(getActivity(), 66);
            bitmap = MyMethods.pictureFillScreen(bitmap, prefWidth);

            int y = (bitmap.getHeight() - MyMethods.dpToPx(getActivity(), 90)) / 2;
            Bitmap bitmapMiddle = Bitmap.createBitmap(bitmap, 0, y, prefWidth, MyMethods.dpToPx(getActivity(), 90));

            // bitmap = MyMethods.pictureFillScreen(bitmap, MyMethods.getDisplayWidth(getActivity()));
            Log.i("NotesFragment: ", Integer.toString(bitmap.getWidth()));
            Log.i("NotesFragment: ", Integer.toString(MyMethods.getDisplayWidth(getActivity())));
            ivImage.setImageBitmap(bitmapMiddle);

            handleImageVisibility();

        }
    }

    public void handleFocus() {
        /*
        if (mContent != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            Log.i("Meine Beschreibung", mContent.getDescription());
            if (hasDescription()) {
                vDummy.requestFocus();
                Log.i("Description", "is not empty");
                imm.hideSoftInputFromWindow(etDescription.getWindowToken(), 0);
            } else {
                getActivity().getWindow().getDecorView().clearFocus();
                etDescription.requestFocus();
                Log.i("Descreption", "ist leer");
                imm.showSoftInput(etDescription, InputMethodManager.SHOW_IMPLICIT);
            }
        }
        */
    }

    private boolean hasDescription() {
        if (mContent.getDescription().equals("") || etDescription.getText().toString().matches("")) {
            return false;
        } else {
            return true;
        }
    }

    private void handleDescriptionVisibility() {
        if (hasDescription()) {
            ivDescriptionIconAdd.setVisibility(View.INVISIBLE);
            ivDescriptionIcon.setVisibility(View.VISIBLE);
        } else {
            ivDescriptionIconAdd.setVisibility(View.VISIBLE);
            ivDescriptionIcon.setVisibility(View.GONE);
        }
    }

    private void handleImageVisibility() {
        if (mContent.getPicturePath() == null) {
            ivImageIconAdd.setVisibility(View.VISIBLE);
            ivImageIcon.setVisibility(View.GONE);
            tvImage.setVisibility(View.VISIBLE);
            ivImage.setVisibility(View.GONE);
        } else {
            ivImageIconAdd.setVisibility(View.INVISIBLE);
            ivImageIcon.setVisibility(View.VISIBLE);
            tvImage.setVisibility(View.GONE);
            ivImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("Permission", "Grantedt");
        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mListener.takePicture(NotesFragment.this);
                }
                break;
            case PERMISSION_REQUEST_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecording();
                }
                break;
        }
        return;
    }

    private void setImageView(ImageView iv, int drawableId, int colorId) {
        Drawable icon = ResourcesCompat.getDrawable(getActivity().getResources(), drawableId, null);
        icon.mutate().setColorFilter(ResourcesCompat.getColor(getActivity().getResources(), colorId, null), PorterDuff.Mode.MULTIPLY);
        iv.setImageDrawable(icon);
    }

    private void setTextView(TextView tv, int textId, int colorId) {
        tv.setText(getString(textId));
        tv.setTextColor(ResourcesCompat.getColor(getActivity().getResources(), colorId, null));
    }

    // Audio methods
    private void startRecording() {
        mAnalyticsInterface.track(Functions.CATEGORY_FUNCTION, Functions.FUNCTION_AUDIO);
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        String audioPath = createAudioPath();
        mRecorder.setOutputFile(audioPath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mContent.setAudioPath(audioPath);
        mRecorder.start();
        mAudioRecording = true;
        handleAudioViews();
    }

    private String createAudioPath() {
        String path = "rocket_plan_audio"+ Long.toString(System.nanoTime());
        File directory = new File(Environment.getExternalStorageDirectory(), "RockitPlan");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File audioFile = new File(directory, path + ".jpg");
        return audioFile.getAbsolutePath();
    }

    private Uri createVideoPath() {
        String path = "rocket_plan_video"+ Long.toString(System.nanoTime());
        File directory = new File(Environment.getExternalStorageDirectory(), "RockitPlan");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File videoFile = new File(directory, path + ".jpg");
        mVideoPath = videoFile.getAbsolutePath();
        return Uri.fromFile(videoFile);
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        mAudioRecording = false;
        handleAudioViews();
    }

    private void startPlayingAudio() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mContent.getAudioPath());
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mPlayer = null;
                    mAudioPlaying = false;
                    handleAudioViews();
                }
            });
            mPlayer.prepare();
            mAudioPlaying = true;
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlayingAudio() {
        mPlayer.release();
        mAudioPlaying = false;
        mPlayer = null;
    }

    private void onPlayAudio() {
        if (!mAudioPlaying) {
            startPlayingAudio();
        } else {
            stopPlayingAudio();
        }
        handleAudioViews();
    }

    private void onRecordAudio() {
        if (!mAudioRecording) {
            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_RECORD_AUDIO);
            } else {
                startRecording();
            }
        } else {
            stopRecording();
        }
    }

    private void handleAudioViews() {
        if (!mContent.hasAudio() && !mAudioRecording) {
            setImageView(ivAudioIcon, R.drawable.ic_add_18dp, R.color.colorDivider);
            setTextView(tvAudio, R.string.detail_audio, R.color.colorDivider);
            ivRemoveAudio.setVisibility(View.GONE);
        }
        if (mContent.hasAudio() && mAudioRecording) {
            setImageView(ivAudioIcon, R.drawable.ic_pause_18dp, R.color.colorSecondaryText);
            setTextView(tvAudio, R.string.detail_audio_running, R.color.colorSecondaryText);
            return;
        }
        if (mContent.hasAudio() && !mAudioPlaying) {
            setImageView(ivAudioIcon, R.drawable.ic_audio_18dp, R.color.colorSecondaryText);
            setTextView(tvAudio, R.string.detail_audio_play, R.color.colorSecondaryText);
            ivRemoveAudio.setVisibility(View.VISIBLE);
        }
        if (mContent.hasAudio() && mAudioPlaying) {
            setImageView(ivAudioIcon, R.drawable.ic_pause_18dp, R.color.colorSecondaryText);
            setTextView(tvAudio, R.string.detail_audio_playing, R.color.colorSecondaryText);
            ivRemoveAudio.setVisibility(View.GONE);
        }
    }

    public void updateVideoViews() {
        if (mContent.hasVideo()) {
            setImageView(ivVideoIconAdd, R.drawable.ic_video_18dp, R.color.colorSecondaryText);
            setTextView(tvVideo, R.string.detail_video_play, R.color.colorSecondaryText);
            ivRemoveVideo.setVisibility(View.VISIBLE);
        } else {
            setImageView(ivVideoIconAdd, R.drawable.ic_add_18dp, R.color.colorDivider);
            setTextView(tvVideo, R.string.detail_video, R.color.colorDivider);
            ivRemoveVideo.setVisibility(View.GONE);
        }
    }

    public String getCurrentVideoPath() {
        return mVideoPath;
    }

}