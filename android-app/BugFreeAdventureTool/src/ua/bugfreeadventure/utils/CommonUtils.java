package ua.bugfreeadventure.utils;

import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class CommonUtils {

	// static functions

	public static String md5(final String s) {
        try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuilder hexString = new StringBuilder();
			for (byte aMessageDigest : messageDigest) {
				String h = Integer.toHexString(0xFF & aMessageDigest);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static boolean isAccountNumber(String str) {
		return ACCOUNT_NUMBER_PATTERN.matcher(str).matches();
	}

	public static boolean isEmail(String str) {
		return EMAIL_PATTERN.matcher(str).matches();
	}

	public static boolean isPhoneNumber(String str) {
		str = str.replaceAll("\\D", "");
		return PHONE_NUMBER_PATTERN.matcher(str).matches();
	}

	public static String formatCurrency(double value) {
		return NUMBER_CURRENCY_FORMAT.format(value);
	}

	public static String formatPayType(String value) {
		if (value.equalsIgnoreCase("1")) {
			return "Зачислен";
		} else {
			return "Обрабатывается";
		}
	}

	/**
	 * Форматирование даты в строку с форматом dd.MM.yyyy
	 * 
	 * @param date
	 *            - дата
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, FMT_DATE);
	}

	/**
	 * Форматирование даты в строку
	 * 
	 * @param date
	 *            - дата
	 * @param dateFormat
	 *            - формат строки
	 * @return
	 */
	public static String formatDate(Date date, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date);
	}

	public static String dateYearFromString(String str) {
		if (TextUtils.isEmpty(str))
			return null;

		String[] parts = str.split("\\.");
		return parts.length == 3 ? parts[2] : null;
	}

	// -- constants

	public static final long READINGS_INTERVAL = 190 * 24 * 60 * 60; // 1/2
																		// years
																		// + 1
																		// week
																		// ago

	public static final String FMT_DATE = "dd.MM.yyyy";
	public static String BA_DATE_STORE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern
			.compile("[0-9]{5}([0-9]{3}|(-[0-9]{3}-))[0-9]{2}");

	private static final Pattern EMAIL_PATTERN = Pattern
//			.compile("[a-zA-Z0-9_-]+([._-]?[a-zA-Z0-9-_]+)*@([a-zA-Z0-9_-]+\\.)*[a-zA-Z0-9-_]+\\.[a-zA-Z]{2,6}$");
			.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

	private static final Pattern PHONE_NUMBER_PATTERN = Pattern
			.compile("^(7|8)[0-9]{10}$");

	private static final NumberFormat NUMBER_CURRENCY_FORMAT = NumberFormat
			.getCurrencyInstance(new Locale("ru", "RU"));

	public static Date parseDateTime(String givenDate, String formatType) {
		Date retDate = null;

		if (givenDate != null) {
			try {
				retDate = new SimpleDateFormat(formatType == null ? FMT_DATE
						: formatType).parse(givenDate);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
		return retDate;

	}

	static public String[] concat(String[] A, String[] B) {
		int aLen = A.length;
		int bLen = B.length;
		String[] C = new String[aLen + bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}

}