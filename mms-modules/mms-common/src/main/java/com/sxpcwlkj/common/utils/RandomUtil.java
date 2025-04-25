package com.sxpcwlkj.common.utils;

import java.util.Random;
import java.util.UUID;

/**
 * 随机对象工具类
 *
 * @name: RandomUtils
 * @author: 西决
 * @date: 2022/12/01
 **/

public class RandomUtil {

    /**
     * 随机生成三位不一样的数字
     * @param num 范围
     * @return
     */
    public static int[] randomThreeNum(int num) {
        int[] a = new int[]{-1, -1, -1};//初始化数组
        int[] b = new int[3];
        Random random = new Random();
        int count = 0;//记录有效的随机数个数
        while (count < a.length) {
            boolean flag = true;//用来标志的变量
            int r = random.nextInt(num);
            for (int i = 0; i < a.length; i++) {
                if (r == a[i]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                a[count] = r;
                count++;
            }
        }
        return a;
    }

    /**
     * 随机生成两位不一样的数字
     * @param num 范围
     * @return
     */
    public static int[] randomTwoNum(int num) {
        int[] a = new int[]{-1, -1};//初始化数组
        int[] b = new int[3];
        Random random = new Random();
        int count = 0;//记录有效的随机数个数
        while (count < a.length) {
            boolean flag = true;//用来标志的变量
            int r = random.nextInt(num);
            for (int i = 0; i < a.length; i++) {
                if (r == a[i]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                a[count] = r;
                count++;
            }
        }
        return a;
    }

    /**
     * 获取一个随机数
     * 范围： [1   int)
     *
     * @param num
     * @return
     */
    public static int getRandomNum(int num) {
        if (num < 1) {
            return 1;
        }
        int newNum = (int) (1 + Math.random() * num);
        return newNum;
    }

    public static int getRandomNumS(int num) {
        if (num < 1) {
            return 1;
        }
        int newNum = (int) (0 + Math.random() * num);
        return newNum;
    }

    /**
     * 获取6位随机数字
     * 范围：[0  9]
     *
     * @return
     */
    public static String getRandomSIX() {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, 6);
        String fixLenthString = String.valueOf(pross);
        return fixLenthString.substring(1, 7);
    }

    /**
     * 成一个随机字符串，长度可自定义
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 成一个随机数字，长度可自定义
     *
     * @param length
     * @return
     */
    public static String getRandomNumber(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            Random r = new Random();
            int n2 = r.nextInt(10);
            sb.append(n2);
        }
        return sb.toString();
    }

    /**
     * 生成一个32位的UUID随机数
     *
     * @return
     */
    public static String getRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");  //因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        return uuid;
    }

    /**
     * UUID生成16位数字
     *
     * @return
     */
    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    /**
     * 指定数组数据，生产一个随机数
     * @param num
     * @return
     */
    public static String GenerateCodeNum(int num) {
        String[] inviteStr = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String str = "";
        for (int i = 0; i < num; i++) {
            Random random = new Random();
            int s = random.nextInt(inviteStr.length);
            str += inviteStr[s];
        }
        return str;
    }

    /**
     * 成一个随机字符串，长度可自定义
     *
     * @param 6位
     * @return
     */
    public static String getInvitationCode() {
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
