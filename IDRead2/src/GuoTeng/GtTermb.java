package GuoTeng;
import android.util.Log;

public class GtTermb
{
	static public native int PowerOpen();  
	static public native void PowerClose();
	static public native int PowerIoCtl(int led_num,int on_off);//1 on, 2 off 
	
	/* Serial Port */
	static public native void InitDebug( String sFile);
	static public native byte[] GetUsbPath();
	static public native int InitComm( int iPort);
	static public native int InitComm1( String sPort);//InitComm1("/dev/ttyS0");
	static public native int InitComm2( String sPort, byte[] szKey);//InitComm1("/dev/ttyS0");
	static public native int InitComm3( String sPort);//InitComm1("/dev/ttyS0");
	
	static public native int FindCardCmd();//寻卡命令
	static public native int SelCardCmd();//选卡命令
	static public native void Authenticate();//鉴权命令，内部调用FindCardCmd和SelCardCmd	
	static public native void CloseComm();//关闭设备
	static public native byte[] ReadSamidCmd();//读安全模块号 
	
	static public native int ReadCard1(byte[] txt, byte[] wlt, byte[] bmp);
	static public native byte[] Wlt2Bmp1(byte[] szWlt);   
	
	static public native int ReadCard2(byte[] txt, byte[] wlt, byte[] bmp);
	static public native byte[] Wlt2Bmp2(byte[] szWlt); 
	
	static public native int ReadCard3(byte[] txt, byte[] wlt);
	
	static public native int ReadCard4(byte[] txt, byte[] wlt, byte[] bmp);//读指纹卡
	static public native int GetFingerData(byte[] finger);//取指纹数据
	
    static {
        try {
        	System.loadLibrary("GtTermb");
        } catch (UnsatisfiedLinkError e) {
            Log.d("GtTermb", "guoteng termb library not found!");
        }
    }
}