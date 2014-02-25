package cn.redinfo.chenzhi.Fantasy.GuoTeng;
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
	
	static public native int FindCardCmd();//Ѱ������
	static public native int SelCardCmd();//ѡ������
	static public native void Authenticate();//��Ȩ����ڲ�����FindCardCmd��SelCardCmd	
	static public native void CloseComm();//�ر��豸
	static public native byte[] ReadSamidCmd();//����ȫģ��� 
	
	static public native int ReadCard1(byte[] txt, byte[] wlt, byte[] bmp);
	static public native byte[] Wlt2Bmp1(byte[] szWlt);   
	
	static public native int ReadCard2(byte[] txt, byte[] wlt, byte[] bmp);
	static public native byte[] Wlt2Bmp2(byte[] szWlt); 
	
	static public native int ReadCard3(byte[] txt, byte[] wlt);
	
	static public native int ReadCard4(byte[] txt, byte[] wlt, byte[] bmp);//��ָ�ƿ�
	static public native int GetFingerData(byte[] finger);//ȡָ�����
	
    static {
        try {
        	System.loadLibrary("GtTermb");
        } catch (UnsatisfiedLinkError e) {
            Log.d("GtTermb", "guoteng termb library not found!");
        }
    }
}