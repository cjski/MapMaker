package JanowskiLuoYabut.HackTheNorth.MapMaker;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 3;
    private long startClickTime;
    private int currentMode = 0;
    Button endPathButton;
    public int xInt1;
    public int yInt1;
    public int xInt2;
    public int yInt2;
    ImageView iv;
    ImageView ivpath;
    Bitmap bm;
    Bitmap bmpath;
    ArrayList<Node> mNodes = new ArrayList<Node>();
    Astar mAstar = new Astar();

    public Node[][] grid;

    public ArrayList<Line> lines = new ArrayList<Line>();

    public class Line{
        int xa;
        int xb;
        int ya;
        int yb;
        public Line(int xa,int xb,int ya,int yb){
            this.xa=xa;
            this.xb=xb;
            this.ya=ya;
            this.yb=yb;
        }
        public void DrawLine(){
            int dx = xa - xb;
            int dy = ya - yb;

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

                for (int i = xb - 12; i < xb + 12; i++) {
                    for (int j = yb- 12; j < yb + 12; j++) {
                        bmpath.setPixel(i, j, Color.rgb(242, 242, 198));
                    }
                }
                grid[xb/25][yb/25] = new Node(xb/25,yb/25);
                grid[(xb+5)/25][yb/25] = new Node((xb+5)/25,yb/25);

                numerator += shortest;
                if (!(numerator<longest)) {
                    numerator -= longest;
                    xb += dx1;
                    yb += dy1;
                } else {
                    xb += dx2;
                    yb += dy2;
                }
            }
        }
    }
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

            for (int i = x1 - 12; i < x1 + 12; i++) {
                for (int j = y1 - 12; j < y1 + 12; j++) {
                    bm.setPixel(i, j, Color.rgb(242, 242, 198));
                }
            }
            grid[x1/25][y1/25] = new Node(x1/25,y1/25);
            grid[(x1+5)/25][y1/25] = new Node((x1+5)/25,y1/25);

            numerator += shortest;
            if (!(numerator<longest)) {
                numerator -= longest;
                x1 += dx1;
                y1 += dy1;
            } else {
                x1 += dx2;
                y1 += dy2;
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                bm = Bitmap.createScaledBitmap(bm, 2500, 2500, false);
                bmpath = Bitmap.createScaledBitmap(bm,2500,2500,false);
                iv.setImageBitmap(bm);

                //generate grid of nodes
                grid = new Node[bm.getWidth()/25][bm.getHeight()/25];
                /*
                for (int i = 0; i < bm.getWidth()/75; i++) {
                    for (int j = 0; j < bm.getHeight()/75; j++) {
                        grid[i][j] = new Node(i,j,false);
                    }
                }
                */

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void initViews() {
        endPathButton = (Button) findViewById(R.id.end_path_button);
        if (currentMode == 0) {
            endPathButton.setVisibility(View.GONE);
        }

        Button helpButton = (Button) findViewById(R.id.help_button);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });

        Button drawModeButton = (Button) findViewById(R.id.draw_mode_button);
        drawModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode = 1;
                endPathButton.setVisibility(View.VISIBLE);
                endPathButton.setText("End Path");
                endPathButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xInt1 = 0;
                        xInt2 = 0;
                        yInt1 = 0;
                        yInt2 = 0;
                    }
                });
            }
        });

        Button pathModeButton = (Button) findViewById(R.id.path_mode_button);
        pathModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMode = 2;
                endPathButton.setVisibility(View.VISIBLE);
                endPathButton.setText("Get Path");
                endPathButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mNodes.size() > 1) {
                            for (int i = 0; i < mNodes.size() - 1; i++) {
                                int dist = mAstar.getShortestPath(mNodes.get(i), mNodes.get(i + 1), grid, bm.getWidth()/25, bm.getHeight()/25,iv,bm);
                                
                                for(int n=0;n<bm.getWidth()/25;n++){
                                    for (int m = 0; m < bm.getHeight()/25; m++) {
                                        if(grid[n][m] != null){
                                            grid[n][m].parent = null;
                                            grid[n][m].sCost = 0;
                                            grid[n][m].eCost = 0;
                                            grid[n][m].tCost = 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });

        Button undoButton = (Button) findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(Line l:lines){
                    l.DrawLine();
                }
                bm = Bitmap.createScaledBitmap(bmpath,bmpath.getWidth(),bmpath.getHeight(),false);
                iv.setImageBitmap(bm);
                mNodes.clear();
            }
        });

        Button uploadImageButton = (Button) findViewById(R.id.upload_image_button);
        uploadImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
             public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
             }
        });

        iv = (ImageView) findViewById(R.id.map_image);
//        //ivpath = (ImageView) findViewById(R.id.map_image);
//        // Create a MUTABLE bitmap
//        final Bitmap originalBitMap = getMutableBitmap(getResources(), R.drawable.map_colour);
//        // A second bitmap is generated for sizing purposes.  This is the bitmap that will be used for everything
//        iv.setImageBitmap(bm);


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

                            if (xInt1 == 0 && yInt1 == 0) {
                                xInt1 = Math.round(pts[0]);
                                yInt1 = Math.round(pts[1]);
                            }

                            else if (xInt1 != 0 && yInt1 != 0 && xInt2 == 0 && yInt2 == 0){
                                xInt2 = Math.round(pts[0]);
                                yInt2 = Math.round(pts[1]);
                                lines.add(new Line(xInt2,xInt1,yInt2,yInt1));
                                DrawLine();
                                xInt1 = xInt2;
                                yInt1 = yInt2;
                            }

                            else {
                                xInt2 = Math.round(pts[0]);
                                yInt2 = Math.round(pts[1]);
                                lines.add(new Line(xInt2,xInt1,yInt2,yInt1));
                                DrawLine();
                                xInt1 = xInt2;
                                yInt1 = yInt2;
                            }

                        } else {
                            return false;
                        }
                    }
                } else if (currentMode == 2) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        startClickTime = System.currentTimeMillis();
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
                            int xPos = Math.round(pts[0]);
                            int yPos = Math.round(pts[1]);
                            if (grid[xPos/25][yPos/25] != null) {
                                mNodes.add(grid[ xPos / 25][(yPos / 25)]);
                                for (int i = xPos - 25; i < xPos + 25; i++) {
                                    for (int j = yPos - 25; j < yPos + 25; j++) {
                                        bm.setPixel(i, j, Color.rgb(121, 16, 16));
                                    }
                                }
                                iv.setImageBitmap(bm);
                            }
                        } else {
                            return false;
                        }
                    }
                }
                // The coordinates for the pixel
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

