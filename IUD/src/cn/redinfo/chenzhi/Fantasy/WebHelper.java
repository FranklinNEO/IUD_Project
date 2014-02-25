package cn.redinfo.chenzhi.Fantasy;

import cn.redinfo.chenzhi.Fantasy.DataModle.BsOperator;
import cn.redinfo.chenzhi.Fantasy.DataModle.Station;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 13-1-7
 * Time: 上午10:57
 */
public final class WebHelper {
  private static String WEB_HOST = "http://219.232.243.21:7001/";
  private static String INIT_STATION = WEB_HOST + "redts/service/redts/trecord.pass";
  private static String SEARCH_RESULT = WEB_HOST + "redts/service/redts/searchrecord.pass";

  public static void search(final WebHelperListener listener) {
    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = new RequestParams();
    params.put("st", "monitorManageRecord");
    params.put("rows", "5");
    params.put("page", "1");
    params.put("qr", "{\"terUuid\":\"000000024\"}");
    client.post(
      SEARCH_RESULT,
      params,
      new AsyncHttpResponseHandler() {
        private String resultStr = null;
        private Throwable err = null;

        @Override
        public void onSuccess(String s) {
          resultStr = s;
        }

        @Override
        public void onFinish() {
          if (listener != null)
            listener.onFinished(resultStr, err);
        }

        @Override
        public void onFailure(Throwable throwable, String s) {
          err = throwable;
          resultStr = s;
        }
      });
  }

  public static void postOpInfo(BsOperator op, final WebHelperListener listener) {
    if (op == null)
      return;

    Gson gson = new Gson();
    String jsonStr = gson.toJson(op);

    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = new RequestParams();
    params.put("st", "PASS_BSOPT");

    params.put("datas", jsonStr);
    client.post(
      INIT_STATION,
      params,
      new AsyncHttpResponseHandler() {
        private String resultStr = null;
        private Throwable err = null;

        @Override
        public void onSuccess(String s) {
          resultStr = s;
        }

        @Override
        public void onFinish() {
          if (listener != null)
            listener.onFinished(resultStr, err);
        }

        @Override
        public void onFailure(Throwable throwable, String s) {
          err = throwable;
          resultStr = s;
        }
      });
  }

  public static void postSeitSettings(Station is, final WebHelperListener listener) {
    if (is == null)
      return;

    Gson gson = new Gson();
    String jsonStr = gson.toJson(is);

    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = new RequestParams();
    params.put("st", "INIT_STATION");

    params.put("datas", jsonStr);
    client.post(
      INIT_STATION,
      params,
      new AsyncHttpResponseHandler() {
        private String resultStr = null;
        private Throwable err = null;

        @Override
        public void onSuccess(String s) {
          resultStr = s;
        }

        @Override
        public void onFinish() {
          if (listener != null)
            listener.onFinished(resultStr, err);
        }

        @Override
        public void onFailure(Throwable throwable, String s) {
          err = throwable;
          resultStr = s;
        }
      });
  }
}
