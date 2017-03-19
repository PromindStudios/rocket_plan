package kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Activities.Element_Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.Constants.MyConstants;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.MyMethods;
import kalender.notes.calendar.notepad.aufgabenplaner.provelopment.rockitplan.R;

/**
 * Created by Eric on 17.02.2017.
 */

public class ImageActivity extends AppCompatActivity {

    ImageView ivImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up Layout and initialize components
        setContentView(R.layout.activity_image);
        ivImage = (ImageView)findViewById(R.id.ivImage);

        // Set up image
        String imagePath = getIntent().getStringExtra(MyConstants.IMAGE_PATH);
        File file = new File(imagePath);
        if (file.exists()) {
            Bitmap bitmap = MyMethods.rotatePicture(imagePath);
            bitmap = MyMethods.pictureFillScreen(bitmap, MyMethods.getDisplayWidth(this));
            ivImage.setImageBitmap(bitmap);
        }

    }
}
