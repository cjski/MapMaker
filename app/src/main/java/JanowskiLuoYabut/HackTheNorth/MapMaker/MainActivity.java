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
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private long startClickTime;
    private int currentMode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    public void initViews() {
        final TextView modeTextView = (TextView) findViewById(R.id.text_view_mode);
        modeTextView.setText("Draw Mode");
        Button drawModeButton = (Button) findViewById(R.id.draw_mode_button);
        drawModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode = 1;
                modeTextView.setText("Draw Mode");
            }
        });
        Button nodeModeButton = (Button) findViewById(R.id.node_mode_button);
        nodeModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode = 2;
                modeTextView.setText("Node Mode");
            }
        });
        Button pathModeButton = (Button) findViewById(R.id.path_mode_button);
        pathModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode = 3;
                modeTextView.setText("Path Mode");
            }
        });

        final ImageView iv = (ImageView) findViewById(R.id.map_image);
        // Create a MUTABLE bitmap
        final Bitmap originalBitMap = getMutableBitmap(getResources(), R.drawable.map_colour);
        // A second bitmap is generated for sizing purposes.  This is the bitmap that will be used for everything
        final Bitmap resizedBitMap = Bitmap.createScaledBitmap(originalBitMap, 4000, 4000, false);
        iv.setImageBitmap(resizedBitMap);

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

                if (currentMode == 1) {
                    // The following conditionals allow differentiation between a tap and a drag gesture
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        startClickTime = System.currentTimeMillis();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
                            for (int i = 0; i < xInt; i++) {
                                for (int j = 0; j < yInt; j++) {
                                    resizedBitMap.setPixel(i, j, Color.rgb(255, 0, 0));
                                }
                            }
                            iv.setImageBitmap(resizedBitMap);
                        } else {
                            return false;
                        }
                    }
                } else if (currentMode == 2) {
                    Toast.makeText(MainActivity.this, "Will be implemented later", Toast.LENGTH_SHORT).show();
                } else if (currentMode == 3) {
                    Toast.makeText(MainActivity.this, "Shall be implemented too", Toast.LENGTH_SHORT).show();
                }

                return true;
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

