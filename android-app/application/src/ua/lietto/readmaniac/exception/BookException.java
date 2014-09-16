package ua.lietto.readmaniac.exception;

/**
 * Created by lietto on 16.09.2014.
 */
public class BookException extends Exception{

    public BookException(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
