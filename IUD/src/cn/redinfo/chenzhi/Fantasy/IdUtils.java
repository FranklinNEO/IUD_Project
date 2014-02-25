package cn.redinfo.chenzhi.Fantasy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NEO on 13-12-27.
 */
public class IdUtils {
    public static boolean CheckIDCard18(String Id) {
        long n = 0;

        try {
            n = Long.parseLong(Id.substring(0, 17));
            if (n < Math.pow(10, 16))
                return false;
        } catch (NumberFormatException ex) {
            return false;
        }

        try {
            n = Long.parseLong(Id.replace('x', '0').replace('X', '0'));
        } catch (NumberFormatException ex) {
            return false;
        }

        String address = "11x22x35x44x53x12x23x36x45x54x13x31x37x46x61x14x32x41x50x62x15x33x42x51x63x21x34x43x52x64x65x71x81x82x91";
        if (address.indexOf(Id.substring(0, 2)) == -1) {
            return false;//省份验证
        }
        String birth = Id.substring(6, 10) + "-" + Id.substring(10, 12) + "-" + Id.substring(12, 14);

        Date time = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            time = sdf.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        String[] arrVarifyCode = ("1,0,x,9,8,7,6,5,4,3,2").split(",");
        String[] Wi = ("7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2").split(",");
        char[] Ai = Id.substring(0, 17).toCharArray();
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += Integer.parseInt(Wi[i]) * Integer.parseInt(String.valueOf(Ai[i]));
        }
        int y = -1;
        //Math.DivRem(sum, 11, out y);
        y = sum % 11;
        String judge = Id.substring(17).toLowerCase();
        if (!(arrVarifyCode[y]).equals(judge)) {
            return false;//校验码验证
        }
        return true;//符合GB11643-1999标准
    }

    public  static boolean CheckIDCard15(String Id) {
        long n = 0;

        try {
            n = Long.parseLong(Id);

            if (n < Math.pow(10, 14))
                return false;
        } catch (NumberFormatException ex) {
            return false;
        }

        String address = "11x22x35x44x53x12x23x36x45x54x13x31x37x46x61x14x32x41x50x62x15x33x42x51x63x21x34x43x52x64x65x71x81x82x91";
        if (address.indexOf(Id.substring(0, 2)) == -1) {
            return false;//省份验证
        }

        String birth = "19" + Id.substring(6, 8) + "-" + Id.substring(8, 10) + "-" + Id.substring(10, 12);

        Date time = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            time = sdf.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;//符合15位身份证标准
    }

}
