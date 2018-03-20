package org.hopto.atkseegow.utility;

public class EncodeUtility {
    public static String GetHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                stringBuilder.append('0');
            stringBuilder.append(Integer.toHexString(b));
        }
        return stringBuilder.toString();
    }

    public static byte[] GetHexByte(String value)
    {
        int m=0,n=0;
        int l=value.length()/2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++)
        {
            m=i*2+1;
            n=m+1;
            ret[i] = Byte.decode("0x" + value.substring(i*2, m) + value.substring(m,n));
        }
        return ret;
    }
}
