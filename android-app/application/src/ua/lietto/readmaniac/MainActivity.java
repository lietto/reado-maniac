package ua.lietto.readmaniac;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.lang3.text.WordUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.devhelp.activity.ParentActivity;
import ua.devhelp.logs.DevToast;

import java.io.*;
import java.util.ArrayList;

public class MainActivity extends ParentActivity {
    private TextView bookText;
    private String finalText;
    private int last = 0;
    private boolean layoutRender;
    private int lines;
    private int line;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bookText = (TextView) findViewById(R.id.text);
       // bookText.setMovementMethod(ScrollingMovementMethod.getInstance());

        StringBuilder buf = new StringBuilder();
        InputStream json = null;

        String text = "";


        try {
            json = getAssets().open("books/Longlife.txt");

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
                buf.append("\n");
            }

            in.close();

            text = buf.toString();
        } catch (IOException e) {
            Toast.makeText(this, "Exception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        finalText = text;
        bookText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookText.setText(finalText.substring(bookText.getLayout().getLineEnd(lines - 1)));
            }
        });

       layoutRender = false;

        ViewTreeObserver vto = bookText.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!layoutRender) {
                    layoutRender = true;
                    int textViewWidth = bookText.getWidth();
                    int numChars;

                    Paint paint = bookText.getPaint();
                    for (numChars = 1; numChars <= finalText.length(); ++numChars) {
                        if (paint.measureText(finalText, 0, numChars) > textViewWidth) {
                            break;
                        }
                    }

                    lines = Math.round(bookText.getHeight() / bookText.getLineHeight());
                    line = (numChars - 1);

                    Log.e("Text", "Number of characters that fit = " + line);
                    Log.e("Text", "Number of lines that fit = " + lines);
                    bookText.setMaxLines(lines);

                    finalText = finalText.replaceAll("(?m)^", "\t");

                    bookText.setText(finalText);

                    showSuccessrToastToUser(bookText.getText().toString()
                            .substring(
                                    bookText.getLayout().getLineStart(lines - 1),
                                    bookText.getLayout().getLineEnd(lines - 1)));

//                    bookText.setText(WordUtils.wrap(finalText, line));

                   /* String[] linesArray = finalText.split("\n");

                    Log.e("Text", "linesArray = " + linesArray.length);

                    String fullText = "";

                    StringBuilder newStr = new StringBuilder();

                    for (String str : linesArray) {
                        String reverse = new StringBuilder(str).reverse().toString();
                        if (str.length() > line) {
                            str =  new StringBuilder(reverse.replaceFirst("\\s", "\n")).reverse().toString();
                        }

                       newStr.append(str).append("\n");

                    }

                    bookText.setText(newStr.toString());*/



                }


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
