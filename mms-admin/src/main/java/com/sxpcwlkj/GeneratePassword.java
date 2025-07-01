package com.sxpcwlkj;

import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;

/***
 * 密码加密
 * @author mmsAdmin
 */
public class GeneratePassword {


    //自定义publicKey
    public static void main(String[] args) throws Exception {
        //生成密钥对
        String[] arr = CryptoUtils.genKeyPair(512);
        //私钥
        System.out.println("privateKey:  " + arr[0]);
        //公钥
        System.out.println("publicKey:  " + arr[1]);

        //加密内容
        System.out.println("url:  " + CryptoUtils.encrypt(arr[0], "jdbc:mysql://localhost:3306/mms"));
        System.out.println("username:  " + CryptoUtils.encrypt(arr[0], "root"));
        System.out.println("password:  " + CryptoUtils.encrypt(arr[0], "123456"));

        //备注: 生成的密文 要和 publicKey 一起更新到配置文件中，否则会造成解密失败。
    }
}
