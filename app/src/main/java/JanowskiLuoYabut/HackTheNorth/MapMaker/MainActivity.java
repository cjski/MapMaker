package JanowskiLuoYabut.HackTheNorth.MapMaker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    Button endPathButton;
    public int xInt1;
    public int yInt1;
    public int xInt2;
    public int yInt2;
    boolean point = false;
    ImageView iv;
    Bitmap bm;

    public void DrawLine() {
        int x0 = xInt2;
        int x1 = xInt1;
        int y0 = yInt2;
        int y1 = yInt1;

        int dx = x0 - x1;
        int dy = y0 - y1;

        int dx1 = 0; int dy1 = 0; int dx2 = 0; int dy2 = 0;

        if (dx < 0) {
            dx1 = -1;
            dx2 = -1;
        }
        else if (dx > 0) {
            dx1 = 1;
            dx2 = 1;
        }

        if (dy < 0)
            dy1 = -1;
        else if (dy > 0)
            dy1 = 1;

        int longest = Math.abs(dx);
        int shortest = Math.abs(dy);

        if (!(longest>shortest)) {
            longest = Math.abs(dy);
            shortest = Math.abs(dx);
            if (dy < 0)
                dy2 = -1;
            else if (dy > 0)
                dy2 = 1;
            dx2 = 0 ;
        }

        int numerator = longest >> 1;
        for (int n = 0; n <= longest; n++) {

            for (int i = x1 - 5; i < x1 + 5; i++) {
                for (int j = y1 - 5; j < y1 + 5; j++) {
                    bm.setPixel(i, j, Color.rgb(255, 0, 0));
                }
            }

            numerator += shortest;
            if (!(numerator<longest)) {
                numerator -= longest;
                x1 += dx1;
                y1 += dy1;
            } else {
                x1 += dx2;
                y1 += dy2;
            }
            //g.drawImage(image,x1,y1,null);
        }
        iv.setImageBitmap(bm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentMode == 1) {
            endPathButton.setText("End Path");
        } else {
            endPathButton.setText("Get Shortest Path");
        }
    }

    public void initViews() {
        endPathButton = (Button) findViewById(R.id.end_path_button);
        Button drawModeButton = (Button) findViewById(R.id.draw_mode_button);
        drawModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode = 1;
                endPathButton.setText("End Path");
                endPathButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xInt1 = -1;
                        xInt2 = -1;
                        yInt1 = -1;
                        yInt2 = -1;
                    }
                });
            }
        });

        Button pathModeButton = (Button) findViewById(R.id.path_mode_button);
        pathModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode = 2;
                endPathButton.setText("Get Shortest Path");
            }
        });

        iv = (ImageView) findViewById(R.id.map_image);
        // Create a MUTABLE bitmap
        final Bitmap originalBitMap = getMutableBitmap(getResources(), R.drawable.map_colour);
        // A second bitmap is generated for sizing purposes.  This is the bitmap that will be used for everything
        bm = Bitmap.createScaledBitmap(originalBitMap, 4000, 4000, false);
        iv.setImageBitmap(bm);

        // Initial touch event in the ImageView
        View.OnTouchListener otl = new View.OnTouchListener() {
            Matrix inverse = new Matrix();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                iv.getImageMatrix().invert(inverse);
                float[] pts = {
                        event.getX(), event.getY()
                };
                inverse.mapPoints(pts);


                if (currentMode == 1) {
                    // The following conditionals allow differentiation between a tap and a drag gesture
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        startClickTime = System.currentTimeMillis();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
                            if (!point) {
                                xInt1 = Math.round(pts[0]);
                                yInt1 = Math.round(pts[1]);
                            } else {
                                xInt2 = Math.round(pts[0]);
                                yInt2 = Math.round(pts[1]);
                            }

                            if (point) {
                                DrawLine();
                            }

                            if (point) {
                                point = false;
                            } else {
                                point = true;
                            }
                        } else {
                            return false;
                        }
                    }
                } else if (currentMode == 2) {
                    Toast.makeText(MainActivity.this, "Will be implemented later", Toast.LENGTH_SHORT).show();
                }

                // The coordinates for the pixel
                if (!point) {
                    xInt1 = Math.round(pts[0]);
                    yInt1 = Math.round(pts[1]);
                    Log.v("Y value", String.valueOf(yInt1));
                } else {
                    xInt2 = Math.round(pts[0]);
                    yInt2 = Math.round(pts[1]);
                    Log.v("Y value", String.valueOf(bm.getHeight()));
                }

                if (point) {
                    DrawLine();
                }

                if (point) {
                    point = false;
                } else {
                    point = true;
                }

                /*
                // Reference for setting pixel color
                for(int i = 0; i < xInt1; i++) {
                    for(int j = 0; j < yInt1; j++) {
                        bm.setPixel(i, j, Color.rgb(255,0,0));
                        iv.setImageBitmap(bm);
                    }
                }
                */
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

