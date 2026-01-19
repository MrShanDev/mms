package com.sxpcwlkj.base.utils;

import com.sxpcwlkj.redis.RedisUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class KdniaoAPIUtils {

    private static final String API_ID="kdniao:api_id";
    private static final String API_KEY="kdniao:api_key";

    private final String eBusinessId;
    private final String apiKey;
    // 请求地址
    private static final String REQ_URL = "https://api.kdniao.com/api/dist";

    /**
     * 构造方法
     */
    public KdniaoAPIUtils() {
        this.eBusinessId = RedisUtil.getCacheObject("kdniao:id");
        this.apiKey = RedisUtil.getCacheObject("kdniao:key");
    }

    public static void main(String[] args) {
        try {
            // 动态配置电商ID和API Key
            String eBusinessId = "1899263";
            String apiKey = "b1de8663-eeff-44c4-8554-f2c83ab0280d";

            // 创建工具类实例
            KdniaoAPIUtils kdniaoAPI = new KdniaoAPIUtils();

            // 快递公司编码
            String shipperCode = "YT";
            // 物流单号
            String logisticCode = "YT3759120394538";
            // 手机号\座机号后四位
            String orderCode = "9200";

            // 调用查询方法
            String result = kdniaoAPI.getOrderTraces(shipperCode, logisticCode, orderCode);
            System.out.println("查询结果:\n" + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询物流轨迹
     *
     * @param ShipperCode  快递公司编码
     * @param LogisticCode 物流单号
     * @param CustomerName 手机号\座机号后四位
     * @return API返回的JSON字符串
     */
    public String getOrderTraces(String ShipperCode, String LogisticCode, String CustomerName)  {
        try {
            // 1. 准备请求数据 (JSON格式)
            String requestData = String.format("{'LogisticCode':'%s','CustomerName':'%s','CustomerName':'%s'}",
                LogisticCode, CustomerName, ShipperCode);

            // 2. 生成数据签名 (DataSign)
            String dataSign = generateSign(requestData, this.apiKey);

            // 3. 组装系统级参数
            Map<String, String> params = new HashMap<>();
            params.put("RequestData", urlEncode(requestData)); // 请求内容需进行URL(utf-8)编码
            params.put("EBusinessID", this.eBusinessId);
            params.put("RequestType", "8002"); // 请求指令类型：1002-查询物流轨迹
            params.put("DataSign", urlEncode(dataSign));
            params.put("DataType", "2"); // 请求、返回数据类型：2-json

            // 4. 发送POST请求
            return sendPost(REQ_URL, params);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成数据签名 (DataSign)
     * 步骤：原始请求数据(未URL编码) + APIKey -> MD5 -> Base64 -> URL编码(UTF-8)
     *
     * @param requestData 原始请求数据JSON字符串
     * @param apiKey      你的API Key
     * @return 签名
     */
    private String generateSign(String requestData, String apiKey) throws Exception {
        // 1. 使用原始、未编码的requestData与APIKey拼接
        String content = requestData + apiKey;

        // 2. MD5加密 (32位小写)
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md.digest(content.getBytes(StandardCharsets.UTF_8));
        StringBuilder md5Str = new StringBuilder();
        for (byte b : md5Bytes) {
            md5Str.append(String.format("%02x", b));
        }

        // 3. Base64编码
        String base64Sign = Base64.getEncoder().encodeToString(md5Str.toString().getBytes(StandardCharsets.UTF_8));

        return base64Sign;
    }

    /**
     * 对字符串进行URL编码 (UTF-8)
     */
    private String urlEncode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
    }

    /**
     * 发送POST请求
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return 响应结果
     */
    private String sendPost(String url, Map<String, String> param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置请求属性
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            // 发送请求参数
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
            out.print(buildParamsString(param));
            out.flush();

            // 读取响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 将参数Map构造成"key=value&"格式的字符串
     */
    private String buildParamsString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sb.substring(0, sb.length() - 1); // 去除最后一个"&"
    }
}
