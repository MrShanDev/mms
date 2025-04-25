package com.sxpcwlkj.common.utils;

/**
 * 字节转换
 * @author Xijue
 */
public class ByteStringUtil {

    /**
     * 字节数组转成16进制字符串
     *
     * @param b 一个字节的数
     * @return 16进制字符串
     */
    public static String byte2str(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        String tmp;
        for (byte value : b) {
            // 整数转成十六进制表示
            tmp = (Integer.toHexString(value & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        // 转成大写
        return sb.toString().toUpperCase();
    }

    /**
     * 将hex字符串转换成字节数组
     *
     * @param inputString 16进制字符串
     * @return 字节数组
     */
    public static byte[] hex2byte(String inputString) {
        if (inputString == null || inputString.length() < 2) {
            return new byte[0];
        }
        inputString = inputString.toLowerCase();
        int l = inputString.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = inputString.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }

}
