package cn.redinfo.chenzhi.Fantasy;

import android.app.Application;

/**
 * Created with IntelliJ IDEA. User: orinchen Date: 12-12-13 Time: 下午3:31
 */
public class FantasyApplication extends Application {
	private static FantasyApplication ourInstance = null;

	public static FantasyApplication getInstance() {
		return ourInstance;
	}

	private String deviceId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ourInstance = this;
	}

	@Override
	public void onTerminate() {
		super.onTerminate(); // To change body of overridden methods use File |
								// Settings | File Templates.
	}
}
