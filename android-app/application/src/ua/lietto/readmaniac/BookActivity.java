package ua.lietto.readmaniac;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import ua.bugfreeadventure.activity.ParentActivity;
import ua.lietto.readmaniac.book.Book;
import ua.lietto.readmaniac.book.BookFormat;

import java.io.*;

public class BookActivity extends ParentActivity implements Book.OnRender {
    private TextView bookText;
    private String finalText;
    private int last = 0;
    private boolean layoutRender;
    private int lines;
    private int line;
    private Book book;
    private int i = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bookText = (TextView) findViewById(R.id.text);
        // bookText.setMovementMethod(ScrollingMovementMethod.getInstance());



        getActionBar().setHomeButtonEnabled(true);

        try {
            showProgressDialog();
            book = new Book.Builder(this)
                    .addTextView(bookText)
//                    .setBookSource(BookFormat.txt, getAssets().open("books/Longlife.txt")).createOnStart(this);
                    .setBookSource(BookFormat.fb2, getAssets().open("books/Fire.fb2")).createOnStart();
        } catch (IOException e) {
            e.printStackTrace();
        }

        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.goPageBackward();
            }
        });
        findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book.goPageForward();
            }
        });
    }

    @Override
    protected void onPause() {
        if (book != null) {
            book.finish();

            book.saveCurrentPageNumber(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.e(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        showProgressDialog();
                        book = new Book.Builder(this)
                                .addTextView(bookText)
                                .setBookSource(BookFormat.txt,
                                        new FileInputStream(new File(uri.getPath()))).createOnChoose();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void renderTextViewFinish() {
       hideProgressDialog();
//        book.setCurrentPage(80);
//       bookText.setText(book.getCurrentPageText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_book_activity, menu);

        MenuItem itemTxt = menu.findItem(R.id.action_txt);

        itemTxt.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/txt");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a TXT"),
                            0);
                } catch (android.content.ActivityNotFoundException ex) {

                }
                return true;
            }
        });

        MenuItem itemFb2 = menu.findItem(R.id.action_fb2);

        itemFb2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a FB2"),
                            1);
                } catch (android.content.ActivityNotFoundException ex) {

                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
