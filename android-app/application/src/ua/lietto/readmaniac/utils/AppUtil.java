package ua.lietto.readmaniac.utils;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by lietto on 17.09.2014.
 */
public class AppUtil {

    public static String getFileEncoding (BufferedInputStream input) throws IOException {
        byte[] buff = new byte[4096];

        // (1)
        UniversalDetector detector = new UniversalDetector(null);

        // (2)
        int nread;
        while ((nread = input.read(buff)) > 0 && !detector.isDone()) {

            detector.handleData(buff, 0, nread);
        }

        // (3)
        detector.dataEnd();

        // (4)
        String encoding = detector.getDetectedCharset();

        // (5)
        detector.reset();

        return encoding;
    }

}
