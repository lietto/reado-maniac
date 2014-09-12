package ua.lietto.readmaniac.book;

import android.content.Context;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lietto on 12.09.2014.
 */
public class Book {

    private BookFormat format;
    private TextView textView;

    public static Book create(Context ctx, String path) throws IOException {
        String[] arr = path.split(".");
        switch (BookFormat.valueOf(arr[arr.length - 1])) {
            case txt:
                return new Book(BookFormat.txt, ctx.getAssets().open(path));
            default:
                return new Book(BookFormat.txt, ctx.getAssets().open(path));
        }

    }

    public static Book create() {
        return new Book();
    }

    public static Book create(BookFormat format, InputStream asset) {
        return new Book(format, asset);
    }

    public Book() {}

    public Book(BookFormat format, InputStream asset) {

        this.format = format;

        switch (format) {
            case txt:
                wrapTextBook(asset);
                break;
        }

    }

    public Book setBookSource(InputStream asset) {
        switch (format) {
            case txt:
                wrapTextBook(asset);
                break;
        }
        return this;
    }

    public Book addTextView(TextView textView) {
        this.textView = textView;
        return this;
    }

    public Book setFormat(BookFormat format) {
        this.format = format;
        return this;
    }


    private void wrapTextBook(InputStream asset) {
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



        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
