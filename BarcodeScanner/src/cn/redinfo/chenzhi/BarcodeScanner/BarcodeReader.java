package cn.redinfo.chenzhi.BarcodeScanner;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import com.soft.interfaces.BarcodeEngine;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: orinchen
 * Date: 12-12-18
 * Time: 下午12:57
 */
public final class BarcodeReader {
  private BarcodeEngine be = new BarcodeEngine();
  private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  private BarcodeReaderListener listener = null;

  private byte[] a, b;
  private ReadThread rt = null;
  private int mobileType = 1;

  private void onBarcodeReaded(String barcode) {
    if (listener != null)
      listener.onBarcodeReaded(barcode);
  }

  public void open() {
    if (bluetoothAdapter.getState() != BluetoothAdapter.STATE_OFF) {//此判断代表蓝牙当前为开，反之为关
      bluetoothAdapter.disable();//关闭蓝牙
    }

    if (be.OpenPort("/dev/uart_switch", 2, "/dev/ttyHS0") < 0) {
      if (be.OpenPort("/dev/isl4260e", 0, "/dev/ttyHSL0") < 0) {
        if (be.OpenPort2("/dev/ttyHSL0", '1', "/sys/devices/platform/msm_serial_hsl.0/uart_switch") < 0) {
          //finish();
        } else {
          mobileType = 3;
        }
      } else {
        mobileType = 2;
      }
    }

    //be.SetScanMode();
    //this.rt = new ReadThread();
    //this.rt.start();
  }

  public void scan(){
    byte[] b = new byte[256];

    be.SetScanMode2();
    be.ScanBegin2(b);
    try {

      String result = new String(b, "UTF-8");
      if(this.listener!=null)
        this.listener.onBarcodeReaded(result);
    } catch (UnsupportedEncodingException e) {

    }
  }

  public void setListener(BarcodeReaderListener listener) {
    this.listener = listener;
  }

  public void close() {
    if (mobileType == 3) be.ClosePort2();
    else be.ClosePort();
  }

  private Handler hd = new Handler() {
    public void handleMessage(android.os.Message msg) {
      try {
        String barcode = new String(b, "UTF-8");
        onBarcodeReaded(barcode);
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    ;
  };

  class ReadThread extends Thread {
    @Override
    public void run() {
      // TODO Auto-generated method stub
      // super.run();
      while (true) {
        a = new byte[256];
        be.ReceiveData(a);
        if (a[0] > 0) {
          b = a;
          Message m = new Message();
          hd.sendMessage(m);
        }
      }
    }
  }
}
