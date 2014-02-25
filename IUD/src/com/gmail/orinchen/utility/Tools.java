package com.gmail.orinchen.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

  public static String padRight(String oriStr, int len, char alexin) {
    int strlen = oriStr.length();
    String str = "";
    if (strlen < len) {
      for (int i = 0; i < len - strlen; i++) {
        str = str + alexin;
      }
    }
    str = oriStr + str;
    return str;
  }

  public static int c(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);

    cal.setTime(date);

    int year2 = cal.get(Calendar.YEAR);
    int month2 = cal.get(Calendar.MONTH);
    int day2 = cal.get(Calendar.DAY_OF_MONTH);
    int y_c = year - year2;
    int m_c = month = month2;
    int d_c = day - day2;

    if (d_c < 0) {
      m_c -= 1;
    }
    if (m_c < 0) {
      y_c -= 1;
    }

    return y_c;
  }
}
