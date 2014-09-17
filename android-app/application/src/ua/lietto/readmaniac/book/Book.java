package ua.lietto.readmaniac.book;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import org.mozilla.universalchardet.UniversalDetector;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import ua.lietto.devhelp.utils.ApplicationUtil;
import ua.lietto.readmaniac.constants.PrefKey;
import ua.lietto.devhelp.logs.SMTL;
import ua.lietto.readmaniac.utils.AppUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lietto on 12.09.2014.
 */
public class Book {

    private BookFormat format;
    private TextView textView;
    private String text;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private boolean layoutRender = false;
    private List<int[]> pages;
    private int lines;
    private int currentPage = 0;

    public Book() {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void finish() {
        if (ApplicationUtil.isJellyBean())
            textView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
    }


    private String wrapTextBook(InputStream input) {

        try {

            BufferedInputStream is = new BufferedInputStream(input);

            is.mark(is.available());

            String encoding = AppUtil.getFileEncoding(is);

            is.reset();

            String text = "";
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(is, encoding));

            String str;
            StringBuilder buf = new StringBuilder();

            while ((str = br.readLine()) != null) {
                buf.append(str);
                buf.append("\n");
            }

            is.close();

            text = buf.toString();

            text = text.replaceAll("(?m)^", "\t");

            return text;

        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    private String wrapFb2Book(InputStream input) throws XmlPullParserException {

        try {

            BufferedInputStream is = new BufferedInputStream(input);
            is.mark(is.available());

            String encoding = AppUtil.getFileEncoding(is);

            is.reset();


            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(is, encoding);

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name=xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.END_TAG:
                        if(name.equals("genre")){

                            SMTL.log().printSingleTextLog("Text " + xpp.getAttributeValue(null,"value"));
                        }
                        break;
                }
                eventType = xpp.next();
            }

            text = "";

            return text;

        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    public void goPageForward() {

        if (currentPage + 1 < getBookCapacity()) {
            currentPage++;
            textView.setText(getCurrentPageText());
        }


    }

    public void goPageBackward() {

        if (currentPage - 1 > -1) {
            currentPage--;
            textView.setText(getCurrentPageText());
        }
    }

    public int getBookCapacity() {
        return pages.size();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getCurrentPageText() {
        return text.substring(pages.get(currentPage)[0], pages.get(currentPage)[1]);
    }

    public List<int[]> getPages() {
        return pages;
    }

    public boolean saveCurrentPageNumber(Context ctx) {
        return ctx.getSharedPreferences(PrefKey.LOCAL_BOOK_STATES, Activity.MODE_PRIVATE)
                .edit()
                .putInt(PrefKey.LOCAL_BOOK_PAGE_NUMBER, getCurrentPage()).commit();
    }

    public int getLocalSavedPageNumber(Context ctx) {
        return ctx.getSharedPreferences(PrefKey.LOCAL_BOOK_STATES, Activity.MODE_PRIVATE)
                .getInt(PrefKey.LOCAL_BOOK_PAGE_NUMBER, 0);
    }

    public void newBook() {
        layoutRender = false;
        textView.getViewTreeObserver().dispatchOnGlobalLayout();
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
                case fb2:
                    try {
                        book.text = book.wrapFb2Book(asset);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                        book.text = "";
                    }
                    break;
            }
            return this;
        }

        public Book getBook() {
            return book;
        }

        ;

        public Book createOnChoose(final OnRender listener) {

            create();

            listener.renderTextViewFinish();
            return book;
        }

        public Book createOnStart(final OnRender listener) {


            book.globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!book.layoutRender) {

                        book.layoutRender = true;

                        create();

                        listener.renderTextViewFinish();

                    }

                }
            };

            book.textView.getViewTreeObserver().addOnGlobalLayoutListener(book.globalLayoutListener);

            return book;
        }

        private void create() {
            book.currentPage = 0;

            book.lines = Math.round(book.textView.getHeight() / book.textView.getLineHeight());

            SMTL.log(this).printValue("lines", book.lines);

            book.textView.setMaxLines(book.lines);

            book.textView.setText(book.text);

            book.pages = new ArrayList<int[]>();

            int pageEnd = book.lines - 1;

            book.pages.add(new int[]{0, book.textView.getLayout().getLineEnd(pageEnd)});

            if (!book.text.equals(""))
            while (true) {
                try {

//                    if (book.pages.size() == 500)
//                        break;

                    book.pages.add(new int[]{
                            book.textView.getLayout().getLineEnd(pageEnd),
                            book.textView.getLayout().getLineEnd(book.lines * (book.pages.size() + 1) - 1)
                    });
                    pageEnd = book.lines * (book.pages.size()) - 1;
                } catch (IndexOutOfBoundsException e) {

                    book.pages.add(new int[]{
                            book.textView.getLayout().getLineEnd(pageEnd), book.text.length() - 1});

                    break;
                }
            }

            book.textView.setText(book.getCurrentPageText());

            SMTL.log(this).printValue("pages.size()", book.pages.size());


        }

    }


}
