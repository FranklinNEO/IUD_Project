package cn.redinfo.chenzhi.IDScanner;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 12-12-17
 * Time: 上午10:33
 */
public interface CardReaderListener {
  public void onReady();

  public void onError();

  public void onCardReaded(IDCard card);
}
