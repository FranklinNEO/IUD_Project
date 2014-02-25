package com.soft.interfaces;

public class BarcodeEngine {

  public native int OpenPort(String device, int rx, String port);
  public native int ClosePort();
  public native int OpenPort2(String device, int rx, String port);
  public native int ClosePort2();
  public native int ScanBegin();
  public native int ScanEnd();
  public native int ScanBegin2(byte[] b);
  public native int ScanEnd2();
  public native void ReceiveData(byte[] b);
  public native int SetScanMode();
  public native int SetScanMode2();
  public native int SetScanMode3();

	static {
		System.loadLibrary("barcode");
	}
}