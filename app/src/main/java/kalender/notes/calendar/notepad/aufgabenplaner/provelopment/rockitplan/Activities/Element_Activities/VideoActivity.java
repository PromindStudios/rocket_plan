package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.Element_Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 21.02.2017.
 */

public class VideoActivity extends AppCompatActivity {

    VideoView vvVideo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        vvVideo = (VideoView)findViewById(R.id.vvVideo);

        vvVideo.setVideoPath(getIntent().getStringExtra(MyConstants.VIDEO_PATH));
        vvVideo.start();
        vvVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i("VideoView", "clicked");
                vvVideo.start();
                return true;
            }
        });

    }
}
