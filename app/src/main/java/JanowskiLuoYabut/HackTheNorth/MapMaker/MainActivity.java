package JanowskiLuoYabut.HackTheNorth.MapMaker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ImageView iv = (ImageView) findViewById(R.id.map_image);
        // Create a MUTABLE bitmap
        final Bitmap bm = getMutableBitmap(getResources(), R.drawable.map_colour);
        iv.setImageBitmap(bm);

        // Listens for touch events in the ImageView
        View.OnTouchListener otl = new View.OnTouchListener() {
            Matrix inverse = new Matrix();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                iv.getImageMatrix().invert(inverse);
                float[] pts = {
                        event.getX(), event.getY()
                };
                inverse.mapPoints(pts);
                // The coordinates for the pixel
                int xInt = Math.round(pts[0]);
                int yInt = Math.round(pts[1]);

                // Reference for setting pixel color
                // bm.setPixel(xInt, yInt, Color.rgb(255,0,0));
                // iv.setImageBitmap(bm);
                return false;
            }
        };
        iv.setOnTouchListener(otl);

    }

    // Creates mutable bitmap
    public static Bitmap getMutableBitmap(Resources resources, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeResource(resources, resId, options);
    }
}

