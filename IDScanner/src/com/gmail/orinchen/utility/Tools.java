package com.gmail.orinchen.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 12-12-17
 * Time: 下午1:00
 */
public final class Tools {
  private Tools() {
  }

  public static String DateToString(Date date, String formatStr) {
    SimpleDateFormat format = new SimpleDateFormat(formatStr);
    return format.format(date);
  }

  public static Date DateFromString(String dateStr, String formatStr) {
    SimpleDateFormat format = new SimpleDateFormat(formatStr);
    try {
      return format.parse(dateStr);
    } catch (ParseException e) {
      return null;
    }
  }
}
