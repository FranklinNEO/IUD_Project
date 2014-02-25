package com.fri.idcread;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 12-12-17
 * Time: 上午9:49
 */
public final class Idcread {
  public native int InitComm(String device, int rx, String port);

  public native int CloseComm();

  public native int Authenticate();

  public native int ReadContent();

  public native int TestConn();

  static {
    System.loadLibrary("idcread");
  }
}
