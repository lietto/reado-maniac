package ua.lietto.readmaniac;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import ebook.EBook;
import ebook.parser.InstantParser;
import ebook.parser.Parser;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        InstantParser parser = new InstantParser();

        EBook ebook = parser.parse("/storage/sdcard0/Books/06. Властелин Хаоса.fb2", true);

        if (ebook.isOk) {
           TextView.class.cast(findViewById(R.id.text)).setText(ebook.annotation);
//            Bitmap bmp = BitmapFactory.decodeByteArray(ebook.cover, 0, ebook.cover.length);
//            ImageView.class.cast(findViewById(R.id.imageView)).setImageBitmap(bmp);
        }
    }
}
