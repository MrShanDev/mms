package com.sxpcwlkj.common.utils;

import cn.hutool.core.lang.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal的加法运算封装
 *
 * @name: BigDecimalUtil
 * @author: 西决
 * @date: 2022/12/01
 **/
public class BigDecimalUtil {

    // 除法运算默认精度
    private static final int DEF_DIV_SCALE = 10;


    /**
     * 将单位为元的金额转换为单位为分
     *
     * @param yuan 单位为元的字符型值
     * @return
     */
    public static int yuanToFen(String yuan) {
        int value = 0;

        try {
            BigDecimal var1 = new BigDecimal(yuan);
            BigDecimal var2 = new BigDecimal(100);
            BigDecimal var3 = var1.multiply(var2);
            value = Integer.parseInt(var3.stripTrailingZeros().toPlainString());
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("非法金额[%s]", yuan));
        }

        Assert.isTrue(value >= 0, String.format("非法金额[%s]", yuan));
        return value;
    }

    /**
     * 精确加法  +
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确加法 +
     */
    public static double add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确减法 -
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确减法 -
     */
    public static double sub(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确乘法  *
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 精确乘法  *
     */
    public static double mul(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 精确乘法  * 四舍五入
     */
    public static BigDecimal multiply(BigDecimal value1, BigDecimal value2){
        //BigDecimal.ROUND_DOWN:直接省略多余的小数，比如1.28如果保留1位小数，得到的就是1.2
        //
        //BigDecimal.ROUND_UP:直接进位，比如1.21如果保留1位小数，得到的就是1.3
        //
        //BigDecimal.ROUND_HALF_UP:四舍五入，2.35保留1位，变成2.4
        //
        //BigDecimal.ROUND_HALF_DOWN:四舍五入，2.35保留1位，变成2.3
        return value1.multiply(value2).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 精确除法 使用默认精度  /
     */
    public static double div(double value1, double value2) throws IllegalAccessException {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 精确除法 使用默认精度  /
     */
    public static double div(String value1, String value2) throws IllegalAccessException {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 精确除法
     *
     * @param scale   /
     *            精度
     */
    public static double div(double value1, double value2, int scale) throws IllegalAccessException {
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 精确除法   /
     *
     * @param scale
     *            精度
     */
    public static double div(String value1, String value2, int scale) throws IllegalAccessException {
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入
     *
     * @param scale
     *            小数点后保留几位
     */
    public static double round(double v, int scale) throws IllegalAccessException {
        return div(v, 1, scale);
    }

    /**
     * 四舍五入
     *
     * @param scale
     *            小数点后保留几位
     */
    public static double round(String v, int scale) throws IllegalAccessException {
        return div(v, "1", scale);
    }

    /**
     * 比较大小 小于0：v1 < v2 大于0：v1 > v2 等于0：v1 = v2
     *
     * @param
     * @param
     * @return
     */
    public static int getCompare(BigDecimal n1,BigDecimal n2) {
        return n1.compareTo(n2);
    }
    /**
     * 比较大小 小于0：v1 < v2 大于0：v1 > v2 等于0：v1 = v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static int getCompare(double v1, double v2) {
        BigDecimal n1 = new BigDecimal(Double.toString(v1));
        BigDecimal n2 = new BigDecimal(Double.toString(v2));
        return n1.compareTo(n2);
    }


    public static double getDoubleDian(double d,int n) {
        BigDecimal bg = new BigDecimal(d);
        /**
         * 参数：
         newScale - 要返回的 BigDecimal 值的标度。
         roundingMode - 要应用的舍入模式。
         返回：
         一个 BigDecimal，其标度为指定值，其非标度值可以通过此 BigDecimal 的非标度值乘以或除以十的适当次幂来确定。
         */
        double f1 = bg.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 转为负数
     * @param bigDecimal
     * @return
     */
    public  static BigDecimal getNegate(BigDecimal bigDecimal){
        return bigDecimal.negate();
    }
    /**
     * 转为正数
     * @param bigDecimal
     * @return
     */
    public  static BigDecimal getAbs(BigDecimal bigDecimal){
        return bigDecimal.abs();
    }

    /**
     * 小数点向右移动n位
     * @param yuan
     * @return
     */
    public static final Integer ONE=1;
    public static final Integer TWO=2;
    public static final Integer THREE=3;
    public static final Integer FOUR=4;
    public static final Integer FIVE=5;
    public static Integer getBigDecimalDian(BigDecimal yuan,int num) {
        //（重点）Double直接转BigDecimal丢失精度，此处需要将Double转换为String
        return yuan.movePointRight(num).intValue();
    }

    /**
     * 价格精度处理，四合五入，保留2位小数
     * @param pice
     * @return
     */
    public BigDecimal getPiceJindu(BigDecimal pice){
        return  pice.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 价格精度处理，四合五入，保留n位小数
     * @param pice
     * @param num
     * @return
     */
    public BigDecimal getPiceJindu(BigDecimal pice,int num){
        return  pice.setScale(num, RoundingMode.HALF_UP);
    }



}
