package com.gmail.orinchen.utility;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 12-12-13
 * Time: 下午9:04
 */
public final class Preconditions {
  public static <T> T checkNotNull(T obj) {
    if (obj == null) {
      throw new NullPointerException();
    }
    return obj;
  }

  public static void checkArgument(boolean condition) {
    if (!condition) {
      throw new IllegalArgumentException();
    }
  }
}