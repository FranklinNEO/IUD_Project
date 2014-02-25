package com.softsz.gongdian;

public class GongdianEngine {

	public native int OpenPort(String device);
	public native int ClosePort(int fd);
	public native int Switch(int fd, int rx);
	public native int SetAttr(int fd);

	static {
		System.loadLibrary("gongdian");
	}
}
