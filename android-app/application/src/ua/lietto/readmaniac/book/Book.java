package ua.lietto.readmaniac.book;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.mozilla.universalchardet.UniversalDetector;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import ua.lietto.devhelp.logs.DevToast;
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

    private final OnRender listener;
    private BookFormat format;
    private TextView textView;
    private String bookText;
    //private String text;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private boolean layoutRender = false;
    private List<int[]> pages;
    private int lines;
    private int currentPage = 0;
    private int currentSection = 0;
    private ArrayList<String> sections;

    public Book(OnRender listener) {
        this.listener = listener;
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

    public void setText(String text) {

    }

    public int getBookCapacity() {
        return pages.size();
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String getCurrentPageText() {
        return sections.get(currentSection).substring(pages.get(currentPage)[0], pages.get(currentPage)[1]);
    }

    public String getPageText(int page) {
        return sections.get(currentSection).substring(pages.get(page)[0], pages.get(page)[1]);
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

        public Builder(OnRender l) {
            book = new Book(l);
        }

        public Builder addTextView(TextView view) {
            book.textView = view;
            return this;
        }

        public Builder setBookSource(BookFormat f, InputStream asset) {
            book.format = f;
            switch (f) {
                case fb2:
                case txt:
                    book.bookText = book.wrapTextBook(asset);
                    break;
            }
            return this;
        }

        public Book getBook() {
            return book;
        }


        public Book createOnChoose() {

            create();

            book.listener.renderTextViewFinish();
            return book;
        }

        public Book createOnStart() {


            book.globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!book.layoutRender) {

                        book.layoutRender = true;

                        create();

                        book.listener.renderTextViewFinish();

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

            book.sections = new ArrayList<String>();

            if (book.format.equals(BookFormat.fb2)) {
//                book.text = book.text.substring(
//                        book.text.indexOf("<body>"),
//                        book.text.indexOf("</body>") + 6);
//
//                book.text = Html.fromHtml(book.text).toString();

//                book.text = book.text.replaceAll("(?m)^", "\t\t");

                String[] s = book.bookText.split("<section>");

                for (int i = 1; i < s.length; i++) {
                    book.sections.add(s[i].split("</section>")[0].replaceAll("<p>", "<strong>").replaceAll("</p>", "</strong>"));
                }

                s = null;


                // book.text = book.text.replaceAll("\n", "");

                // book.text = Html.fromHtml(book.text).toString();

                //   book.text = book.text.replaceAll("\n\n", "\n");

                //   book.text = book.text.replaceAll("(?m)^", "\t\t");

                //   book.text = book.text.replaceAll("\n\t\t\t", "");
                //  book.text = StringEscapeUtils.escapeJava(book.text);
            } else {
                book.sections.add(book.bookText);
            }

            book.bookText = null;

            book.textView.setText(Html.fromHtml(book.sections.get(0)));

            book.pages = new ArrayList<int[]>();

            int pageEnd = book.lines - 1;

            book.pages.add(new int[]{0, book.textView.getLayout().getLineEnd(pageEnd)});

            pageEnd = book.lines * (book.pages.size() + 1) - 1;

            for (int i = 0; i < book.textView.getLayout().getLineCount() - 1; i++) {

                if (i == pageEnd) {
                    book.pages.add(new int[]{
                            book.textView.getLayout().getLineStart(pageEnd - book.lines),
                            book.textView.getLayout().getLineEnd(pageEnd)
                    });

                    pageEnd = book.lines * (book.pages.size() + 1) - 1;
                }
            }

            book.pages.add(new int[]{
                    book.textView.getLayout().getLineStart(pageEnd - book.lines),
                    book.sections.get(0).length() - 1
            });

            //book.textView.setText(book.getCurrentPageText());

            SMTL.log(this).printValue("pages.size()", book.pages.size());

        }
    }
}