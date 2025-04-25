package com.sxpcwlkj.common.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 * 数据类型转换 工具类
 * shanpengnian
 * @author Administrator
 */
public class DataUtil<T> {

    public static void main(String[] args) {
        String str = "1.0.7";
        String[] arr = str.split("\\."); // 用,分割
        System.out.println(Arrays.toString(arr)); // [0, 1, 2, 3, 4, 5]
        System.out.println(underlineToCamel("id_name_age",false));
        //float[] ff = {2, 4, 6, 8, 10};
        // SysouUtil.sysou(Arraysum(ff).toString());


        /*   String num = "20.00";

        System.out.println(DataUtil.getBigDecimal("-0.06")); // 打印结果

        */
    	/*for(int i = 0;i<100;i++) {
    		System.out.println(getRandomNumS(10));
    	}*/

    	/*String z = "100.00";

    	String z1 = "50";

    	String z2 = "20";
    	  NumberFormat nf = NumberFormat.getNumberInstance();


          // 保留两位小数
          nf.setMaximumFractionDigits(2);


          // 如果不需要四舍五入，可以使用RoundingMode.DOWN
          nf.setRoundingMode(RoundingMode.UP);
    	Double z3 = getDouble((getBigDecimal(z).multiply(getBigDecimal(z1).divide(getBigDecimal("100")))).multiply((getBigDecimal(z2).divide(getBigDecimal("100")))));

    */
    	/*System.out.println(z3);
    	System.out.println(	String.format("%.2f", z3));*/


      /*  for(int i = 0;i<50;i++) {
        	Random random = new Random();
            int s = random.nextInt(1);
            System.out.println(s);
        }*/

       /* for(int i = 0;i<50;i++) {
        	int [] a = randomThreeNum(15);
        	for(int k = 0;k<a.length;k++) {
        		System.out.print(a[k]+"，");
        	}
        	System.out.println();
        }*/
       /* System.out.println("六位随机字符："+getRandomString(6));
        System.out.println("六位随机数字:" + getRandomSIX());
        System.out.println("1000范围的一个随机数:" + getRandomNum(1000));
        System.out.println("UUID随机数32位:" + getRandomUUID());
        System.out.println("16位随机数字:" + getOrderIdByUUId());
        System.out.println("手机号加****" + getPhoneNumber("13389186557"));
        System.out.println("判断是否为空或null" + getIsNull(""));
        System.out.println("解析HTML代码" + getStringForHtml("<h1>JAVA</h1>"));
        System.out.println("字符串转nicode编码" + getStringForUnicode("陕西品创网络"));
        System.out.println("nicode编码转字符串" + getUnicodeForString(getStringForUnicode("appkey=a3chc2dttt&key=%E9%9F%A9%E7%89%88")));

         String str="韩版";
         System.out.println("判断是否为汉字"+getIsChinese(str));
         System.out.println("判断是否为英文"+getIsEnglish(str));*/
    /*	for(int i = 0;i<100;i++) {
    		System.out.println(GenerateCode());
    	}*/
       /* for (int i = 0; i < 50; i++) {
            System.out.println(getRandomNumS(4));
        }*/

    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数字转万，亿
     * 如kBool 为ture 1000转为999+
     *
     */
    public static String formatNum(String num, Boolean kBool) {
        StringBuffer sb = new StringBuffer();

        if (kBool == null)
            kBool = false;

        BigDecimal b0 = new BigDecimal("1000");
        BigDecimal b1 = new BigDecimal("10000");
        BigDecimal b2 = new BigDecimal("100000000");
        BigDecimal b3 = new BigDecimal(num);

        String formatNumStr = "";
        String nuit = "";

        // 以千为单位处理
        if (kBool) {
            if (b3.compareTo(b0) == 0 || b3.compareTo(b0) == 1) {
                return "999+";
            }
            return num;
        }

        // 以万为单位处理
        if (b3.compareTo(b1) == -1) {
            sb.append(b3.toString());
        } else if ((b3.compareTo(b1) == 0 && b3.compareTo(b1) == 1)
                || b3.compareTo(b2) == -1) {
            formatNumStr = b3.divide(b1).toString();
            nuit = "万";
        } else if (b3.compareTo(b2) == 0 || b3.compareTo(b2) == 1) {
            formatNumStr = b3.divide(b2).toString();
            nuit = "亿";
        }
        if (!"".equals(formatNumStr)) {
            int i = formatNumStr.indexOf(".");
            if (i == -1) {
                sb.append(formatNumStr).append(nuit);
            } else {
                i = i + 1;
                String v = formatNumStr.substring(i, i + 1);
                if (!v.equals("0")) {
                    sb.append(formatNumStr.substring(0, i + 1)).append(nuit);
                } else {
                    sb.append(formatNumStr.substring(0, i - 1)).append(nuit);
                }
            }
        }
        if (sb.length() == 0)
            return "0";
        return sb.toString();
    }

    /*
     * 随机生成三位不一样的数字
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
     * 获取Long
     *
     * @param obj
     * @return
     * @author
     */
    public static Long getLong(Object obj) {
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取整形
     *
     * @param obj
     * @return
     */
    public static int getInt(Object obj) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception ex) {
            return 0;
        }
    }

    public static int getStrDoubleToInt(Object obj) {
        try {
            Double d = Double.parseDouble(obj.toString()); // 先转换成double类型
            Integer i = d.intValue(); // 再转换成int类型（会损失精度）
            return i;
        } catch (Exception ex) {
            return 0;
        }
    }


    /**
     * 获取float
     *
     * @param obj
     * @return
     */
    public static float getFloat(Object obj) {
        try {
            return Float.parseFloat(obj.toString());
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 获取Double
     *
     * @param obj
     * @return
     */
    public static Double getDouble(Object obj) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception ex) {
            return 0.0;
        }
    }

    /**
     * 获取字符串
     *
     * @param obj
     * @return
     */
    public static String getString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            try {
                return obj.toString().trim();
            } catch (Exception ex) {
                return "";
            }
        }
    }


    /**
     * 获取bigdecimal
     *
     * @param obj
     * @return
     */
    public static BigDecimal getBigDecimal(Object obj) {
        if (obj == null) {
            return new BigDecimal(0);
        } else {
            try {
                return new BigDecimal(obj.toString());
            } catch (Exception ex) {
                return new BigDecimal(0);
            }
        }
    }

    /**
     * 获取boolean值
     *
     * @param obj
     * @return
     */
    public static boolean getBoolean(Object obj) {
        if (obj == null) {
            return false;
        } else {
            try {
                return Boolean.parseBoolean(obj.toString());
            } catch (Exception ex) {
                return false;
            }
        }
    }


    /**
     * Unicode编码 转义 字符串
     *
     * @param unicode
     * @return
     */
    public static String getUnicodeForString(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            string.append((char) data);
        }
        return string.toString();
    }




    /**
     * 判断是否为空或者为NUll
     *
     * @param obj
     * @return 空或者NULL 返回false，否则返回true
     */
    public static boolean getIsNull(Object obj) {
        if (getString(obj).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 把字符串数组转换为int数组
     *
     * @param array
     * @return
     */
    public static int[] getStringArrayForIntArray(String[] array) {
        int[] newArray = new int[0];
        if (array == null || array.length == 0) {
            return newArray;
        } else {
            newArray = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                try {
                    newArray[i] = Integer.parseInt(array[i]);
                } catch (Exception ex) {
                    newArray = new int[0];
                    return newArray;
                }
            }
            return newArray;
        }
    }

    /**
     * 把Object数组转换为int数组
     *
     * @param array
     * @return
     */
    public static int[] getObjectForIntArray(Object[] array) {
        int[] newArray = new int[0];
        if (array == null || array.length == 0) {
            return newArray;
        } else {
            newArray = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                try {
                    newArray[i] = DataUtil.getInt(array[i]);
                } catch (Exception ex) {
                    newArray = new int[0];
                    return newArray;
                }
            }
            return newArray;
        }
    }

    /**
     * 电话号码转换***
     *
     * @param phoneNumber
     * @return
     */
    public static String getPhoneNumber(String phoneNumber) {
        phoneNumber = getString(phoneNumber);
        if (phoneNumber.length() > 4 && phoneNumber.length() <= 8) {
            phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 2) + "**";
        } else if (phoneNumber.length() > 8) {
            phoneNumber = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(phoneNumber.length() - 4, phoneNumber.length());
        }
        return phoneNumber;
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
     * <将List<Object>转换为List<Map<String, Object>>>
     *
     * @param list 需要转换的list
     * @return 转换的结果
     */
    public static List<Map<String, Object>> converterForMapList(List<Object> list) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Object tempObj : list) {
            result.add((HashMap<String, Object>) tempObj);
        }
        return result;
    }

    /**
     * 是否是英文
     *
     * @param
     * @return
     */
    public static boolean getIsEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }

    /**
     * 是否为全汉字
     *
     * @param str
     * @return
     */
    public static boolean getIsChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find())
            return true;
        else
            return false;
    }

    //产生随机码
    private static String[] inviteStr = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    public static String GenerateCode() {
        String str = "";
        for (int i = 0; i < 6; i++) {
            Random random = new Random();
            int s = random.nextInt(inviteStr.length);
            str += inviteStr[s];
        }
        return str;
    }

    //产生随机数
    public static String GenerateCodeNum(int num) {
        String str = "";
        for (int i = 0; i < num; i++) {
            Random random = new Random();
            int s = random.nextInt(inviteStr.length);
            str += inviteStr[s];
        }
        return str;
    }

    //字符串 去除表情
    public static String removeNonBmpUnicode(String str) {
        if (str == null) {
            return null;
        }
        str = str.replaceAll("[^\\u0000-\\uFFFF]", "");
        return str;
    }

    //URL编码加密  urlencode
    public static String getURLToUrlencode(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    //URL编码解密  urlencode
    public static String getURLDecodeToString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串最后一次出现的位置
     *
     * @param str
     * @return
     */
    public static String[] getCatStr(String str, String type) {
        String[] strs = str.split(type);
        return strs;
    }

    /**
     * 获取Ip
     */
    public static String getIp() {
        String ip = null;
        try {
            ip = DataUtil.getString(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        ip = ip.substring(ip.indexOf("/") + 1);
        return ip;
    }


    /**
     * 去除字符串首尾出现的某个字符.
     *
     * @param source  源字符串.
     * @param element 需要去除的字符.
     * @return String.
     */
    public static String trimFirstAndLastChar(String source, char element) {
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        try {
            do {
                int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
                int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element) : source.length();
                source = source.substring(beginIndex, endIndex);
                beginIndexFlag = (source.indexOf(element) == 0);
                endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
            } while (beginIndexFlag || endIndexFlag);
            return source;
        }catch (Exception e){
            return source;
        }
    }

    /**
     * 删除字符串中的某个字符
     *
     * @param str
     * @param delChar
     * @return
     */
    public static String deleteString(String str, char delChar) {
        String delStr = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != delChar) {
                delStr += str.charAt(i);
            }
        }
        return delStr;
    }

    /**
     * \\s* 可以匹配空格、制表符、换页符等空白字符的其中任意一个
     *
     * @param str
     * @return
     */
    public static String deleteString(String str) {
        str = str.replaceAll("\\s*", "");
        return str;
    }

    /**
     * 标准的字符串数组 转  数组  "1,2,3,4,5,6"
     *
     * @param str
     * @return
     */
    public static String[] getStrToStrArry(String str) {
        return str.split(",");
    }

    /**
     * 根据字符串下标 截取字符串   计算好下标长度
     *
     * @param str
     * @param star
     * @param end
     * @return
     */
    public static String getStrjiequ(String str, int star, int end) {
        if (str.length() < star) {
            return null;
        }
        if (str.length() < end) {
            return str;
        }

        return str.substring(star, end);
    }

    //去除空格
    public static String[] getStrTpSteArr(String str, String s) {
        /*s=\\.*/
        return str.split(s);
    }

    /**
     * 字符串是否仅含有数字和字母
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }

    /**
     * 四舍五入
     *
     * @param d
     * @return
     */
    public static double getDoubal(Double d) {
        return Math.round(d);
    }


    /**
     * 计算数组的平均数
     *
     * @param arr
     * @return
     */
    public static float[] Arraysum(float[] arr) {
        int len = arr.length;
        float[] newArray = new float[len];
        if (len == 1) {
            return newArray;
        } else {
            int k = 0;
            for (int i = 0; i < arr.length; i++) {
                k = i + 1;
                float sum = addSum(arr, i) / k;
                newArray[i] = sum;
            }
            return newArray;
        }
    }

    /**
     * 指定数组下标，计算数组的和
     *
     * @param arr
     * @param index
     * @return
     */
    private static float addSum(float[] arr, int index) {
        float s = 0;
        if (index > arr.length) {
            return 0;
        }
        for (int i = 0; i <= index; i++) {
            s = s + arr[i];
        }
        return s;
    }

    /**
     * 找最大值
     *
     * @param array
     * @return
     */
    public static int maxInt(int[] array) {
        int max = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static float maxFloat(float[] array) {
        float max = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }


    private static String adjective[] = {
            "一样的", "喜欢的", "美丽的", "一定的", "原来的", "美好的", "开心的", "可能的", "可爱的",
            "明白的", "所有的", "后来的", "重要的", "经常的", "自然的", "真正的", "害怕的", "空中的",
            "红色的", "痛苦的", "干净的", "辛苦的", "精彩的", "欢乐的", "进步的", "影响的", "黄色的",
            "亲爱的", "根本的", "完美的", "金黄的", "聪明的", "清新的", "迷人的", "光明的", "共同的",
            "直接的", "真实的", "听说的", "用心的", "飞快的", "雪白的", "着急的", "乐观的", "主要的",
            "鲜艳的", "冰冷的", "细心的", "奇妙的", "水平的", "动人的", "大量的", "无知的", "礼貌的",
            "暖和的", "深情的", "正常的", "平淡的", "光亮的", "落后的", "大方的", "老大的", "刻苦的",
            "晴朗的", "专业的", "永久的", "大气的", "知己的", "刚好的", "相对的", "平和的", "友好的",
            "广大的", "秀丽的", "日常的", "高级的", "相同的", "笔直的", "安定的", "知足的", "结实的",
            "许久的", "听话的", "知名的", "闷热的", "众多的", "拥挤的", "天生的", "迷你的", "老实的",
            "友爱的", "原始的", "可笑的", "合格的", "公共的", "大红的", "得力的", "洁净的", "暗淡的",
            "鲜红的", "桃红的", "吓人的", "多余的", "秀美的", "繁忙的", "冰凉的", "热心的", "空旷的",
            "冷清的", "公开的", "冷淡的", "齐全的", "草绿的", "能干的", "发火的", "可心的", "业余的",
            "空心的", "凉快的", "长远的", "土黄的", "和好的", "合法的", "明净的", "过时的", "低下的",
            "不快的", "低级的", "中用的", "不定的", "公办的", "用功的", "少许的", "忙乱的", "日用的",
            "要紧的", "少见的", "非分的", "怕人的", "大忙的", "幸福的", "特别的", "未来的", "伟大的",
            "困难的", "伤心的", "实在的", "现实的", "丰富的", "同样的", "巨大的", "耐心的", "优秀的",
            "亲切的", "讨厌的", "严厉的", "积极的", "整齐的", "环保的"};

    private static String[] noun = {
            "生活", "一起", "不是", "人们", "今天", "孩子", "心里", "奶奶", "眼睛",
            "学校", "原来", "爷爷", "地方", "过去", "事情", "以后", "可能", "晚上",
            "里面", "才能", "知识", "故事", "多少", "比赛", "冬天", "所有", "样子",
            "成绩", "后来", "以前", "童年", "问题", "日子", "活动", "公园", "作文",
            "旁边", "下午", "自然", "房间", "空气", "笑容", "明天", "风景", "音乐",
            "岁月", "文化", "生气", "机会", "身影", "天气", "空中", "红色", "书包",
            "今年", "汽车", "早晨", "道路", "认识", "办法", "精彩",
            "中午", "礼物", "星星", "习惯", "树木", "女儿", "友谊", "夜晚", "意义",
            "家长", "耳朵", "家人", "门口", "班级", "人间", "厨房", "风雨", "影响",
            "过年", "电话", "黄色", "种子", "广场", "清晨", "根本", "故乡", "笑脸",
            "水面", "思想", "伙伴", "美景", "照片", "水果", "彩虹", "刚才", "月光",
            "先生", "鲜花", "灯光", "感情", "亲人", "语言", "爱心", "光明", "左右",
            "新年", "角落", "青蛙", "电影", "行为", "阳台", "用心", "题目", "天地",
            "动力", "花园", "诗人", "树林", "雨伞", "去年", "少女", "乡村", "对手",
            "上午", "分别", "活力", "作用", "古代", "公主", "力气", "从前", "作品",
            "空间", "黑夜", "说明", "青年", "面包", "往事", "大小", "司机",
            "中心", "对面", "心头", "嘴角", "家门", "书本", "雪人", "笑话",
            "云朵", "早饭", "右手", "水平", "行人", "乐园", "花草", "人才", "左手",
            "目的", "课文", "优点", "灰尘", "年代", "沙子", "小说", "儿女", "明星",
            "难题", "本子", "水珠", "彩色", "路灯", "把握", "房屋", "心愿", "左边",
            "新闻", "早点", "市场", "雨点", "细雨", "书房", "毛巾", "画家", "元旦",
            "绿豆", "本领", "起点", "青菜", "土豆", "总结", "礼貌", "右边", "窗帘",
            "萝卜", "深情", "楼房", "对话", "面条", "北方", "木头", "商店", "疑问",
            "后果", "现场", "诗词", "光亮", "白菜", "男子", "风格", "大道", "梦乡",
            "姐妹", "毛衣", "园丁", "两边", "大风", "乡下", "广播", "规定", "围巾",
            "意见", "大方", "头脑", "老大", "成语", "专业", "背景", "大衣", "黄豆",
            "高手", "叶片", "过往", "选手", "奶油", "时空", "大气", "借口", "抹布",
            "画笔", "山羊", "晚会", "拖鞋", "手心", "手工", "明年", "手术", "火苗",
            "知己", "价格", "树苗", "主意", "摇篮", "对比", "胖子", "专家", "年级",
            "落日", "东风", "屋子", "创意", "报道", "下巴", "面子", "迷宫", "雪山",
            "友好", "烟雾", "西方", "姨妈", "问号", "年轮", "居民", "选手", "奶油",
            "时空", "大气", "借口", "抹布", "画笔", "山羊", "晚会", "拖鞋", "手心",
            "手工", "明年", "手术", "火苗", "知己", "价格", "树苗", "主意", "摇篮",
            "对比", "胖子", "专家", "年级", "落日", "东风", "屋子", "创意", "报道",
            "下巴", "面子", "迷宫", "雪山", "友好", "烟雾", "西方", "姨妈", "问号",
            "年轮", "居民", "选手", "奶油", "时空", "大气", "借口", "抹布", "画笔",
            "山羊", "晚会", "拖鞋", "手心", "手工", "明年", "手术", "火苗", "知己",
            "价格", "树苗", "主意", "摇篮", "对比", "胖子", "专家", "年级", "落日",
            "东风", "屋子", "创意", "报道", "下巴", "面子", "迷宫", "雪山", "友好",
            "烟雾", "西方", "姨妈", "问号", "年轮", "居民"};

    /**
     * 生成随机昵称
     *
     * @return
     */
    public static String getGenerateName() {
        int adjLen = adjective.length;
        int nLen = noun.length;
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        sb.append(adjective[random.nextInt(adjLen)]);
        sb.append(noun[random.nextInt(nLen)]);
        return sb.toString();
    }

    /**
     * 精度转化
     * @param f1
     * @param num
     * @return
     */
    public static Float getDoubleF(float f1, int num) {
        if (num == 1) {
            DecimalFormat df = new DecimalFormat("#.#");
            return DataUtil.getFloat(df.format(f1));
        } else if (num == 2) {
            DecimalFormat df = new DecimalFormat("#.##");
            return DataUtil.getFloat(df.format(f1));
        } else if (num == 3) {
            DecimalFormat df = new DecimalFormat("#.###");
            return DataUtil.getFloat(df.format(f1));
        } else if (num == 4) {
            DecimalFormat df = new DecimalFormat("#.####");
            return DataUtil.getFloat(df.format(f1));
        } else if (num == 5) {
            DecimalFormat df = new DecimalFormat("#.#####");
            return DataUtil.getFloat(df.format(f1));
        }
        return f1;
    }

    /**
     * 判断图片是否有效
     * @param posturl
     * @return
     * @throws IOException
     */
    public static boolean isImagesTrue(String posturl) throws IOException {
        URL url = new URL(posturl);
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
        urlcon.setRequestMethod("POST");
        urlcon.setRequestProperty("Content-type",
                "application/x-www-form-urlencoded");
        if (urlcon.getResponseCode() == HttpURLConnection.HTTP_OK) {
            System.out.println(HttpURLConnection.HTTP_OK + posturl
                    + ":posted ok!");
            return true;
        } else {
            System.out.println(urlcon.getResponseCode() + posturl
                    + ":Bad post...");
            return false;
        }
    }

    /**
     * 判断url是否是http开头的
     * @param url
     * @return
     */
    public static boolean isUrlHttp(String url){

        if(url==null||url.length()<4) return true;
        String str=url.substring(0,4);
        if("http".equals(str)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 数组转字符串
     * 去除空字符串,空格，重复
     * @param list
     * @return
     */
    public static String getListToString(List<String> list){
        String result="";
        if(list!=null&& !list.isEmpty()){
            //去除空字符串
            list = list.stream().filter(str -> !str.equals("")).collect(Collectors.toList());
            //去除空格
            list = list.stream().map(s -> s.trim()).collect(Collectors.toList());
            //去除重复
            list = list.stream().distinct().collect(Collectors.toList());
            //转为，拼接的字符串
            result=  list.stream().collect(Collectors.joining(","));
        }
        return result;
    }

    /**
     * 字符串转数组
     * 去除空字符串,空格，重复
     * @param str
     * @return
     */
    public static List<String> getStringToList(String str){
        List<String> list= Arrays.asList(str.split(","));
        //去除空字符串
        list = list.stream().filter(s -> !str.equals("")).collect(Collectors.toList());
        //去除空格
        list = list.stream().map(s -> s.trim()).collect(Collectors.toList());
        //去除重复
        list = list.stream().distinct().collect(Collectors.toList());
        return list;
    }


    public static boolean isObjectEmpty(Object obj) {
        if(obj==null){
            return true;
        }
        if(obj instanceof String){
            return ((String) obj).isEmpty();
        }
        if(obj instanceof  List<?>){
            return ((List<?>) obj).isEmpty();
        }
        if(obj instanceof  Optional){
            return ((Optional<?>) obj).isEmpty();
        }

        return false;
    }

    public static String underlineToCamel(String underlineName, boolean firstIsUpperCase) {
        StringBuilder result = new StringBuilder();
        String[] parts = underlineName.split("_");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i == 0) {
                result.append(part);
            } else {
                result.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
            }
        }
        if (firstIsUpperCase) {
            result= new StringBuilder(Character.toUpperCase(result.charAt(0)) + result.substring(1));
        }

        return result.toString();
    }
}
