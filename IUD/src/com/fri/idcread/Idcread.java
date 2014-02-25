package com.fri.idcread;

public class Idcread {
	
	public native int InitComm(String device, int rx, String port);
	public native int InitComm2(String device, int rx, String port);
	public native int CloseComm();
	public native int CloseComm2();
	public native int Authenticate();
	public native int ReadContent();
	public native int TestConn();
	
	static {
		System.loadLibrary("idcread");
	}
}
