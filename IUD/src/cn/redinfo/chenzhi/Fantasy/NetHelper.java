package cn.redinfo.chenzhi.Fantasy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by NEO on 13-12-27.
 */
public class NetHelper {
    public static boolean IsHaveInternet(final Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        } catch (Exception e) {
            return false;
        }
    }

}
