package ua.lietto.readmaniac.book;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by lietto on 12.09.2014.
 */
public class Book {

    private BookFormat format;
    private TextView textView;
    private String text;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private boolean layoutRender = false;

    public Book() {}

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void finish() {
        textView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
    }


    private String wrapTextBook(InputStream asset) {
        try {
            String text;

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(asset, "UTF-8"));
            String str;
            StringBuilder buf = new StringBuilder();

            while ((str = in.readLine()) != null) {
                buf.append(str);
                buf.append("\n");
            }

            in.close();

            text = buf.toString();

            text = text.replaceAll("(?m)^", "\t");

            return text;

        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    public interface OnRender {
        public void renderTextViewFinish();
    }

    public static class Builder {

        private Book book;

        public Builder() {
                book = new Book();
        }

        public Builder addTextView(TextView view) {
            book.textView = view;
            return this;
        }

        public Builder setBookSource(BookFormat f, InputStream asset) {
            book.format = f;
            switch (f) {
                case txt:
                    book.text = book.wrapTextBook(asset);
                    break;
            }
            return this;
        }

        public Book create(final OnRender listener) {

            book.globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!book.layoutRender) {
                        book.layoutRender = true;

                        int lines = Math.round(book.textView.getHeight() / book.textView.getLineHeight());

                        book.textView.setMaxLines(lines);

                        book.textView.setText(book.text);

                        ArrayList<String>

                        while () {

                        }

                        listener.renderTextViewFinish();

                    }

                }
            };

            book.textView.getViewTreeObserver().addOnGlobalLayoutListener(book.globalLayoutListener);

            return book;
        }



    }


}
