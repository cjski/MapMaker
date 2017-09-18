package JanowskiLuoYabut.HackTheNorth.MapMaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Saves bitmap image as a string to shared preferences as well as an array of points as a json array.
 */

public class SaveHandler {

    Context context;

    public SaveHandler(Context context){

    }

    public void saveBitmap (Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        // Turn bitmap into byte array and encode as a string
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.DEFAULT);

        // Save to shared preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("bitmap", encoded);
        editor.commit();
    }

    public void getBitmap () {
        // Do later
    }
}
