package com.sxpcwlkj.common.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AliyunRealNameAuthUtil {

    /**
     * 调用阿里云实名认证API验证姓名、身份证号和手机号的一致性
     *
     * @param name 真实姓名
     * @param idNumber 身份证号码
     * @param phoneNumber 绑定的手机号
     * @param appCode 阿里云市场购买服务后获得的AppCode
     * @return 认证结果
     */
    public static R<Map<String, String>> verifyRealName(String name, String idNumber, String phoneNumber, String appCode) {
        String host = "https://dfphone3.market.alicloudapi.com";
        String path = "/verify_id_name_phone";
        String method = "POST";

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appCode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        Map<String, String> bodys = new HashMap<>();
        bodys.put("name", name);
        bodys.put("id_number", idNumber);
        bodys.put("phone_number", phoneNumber);

        try {
            String result = doPost(host, path, method, headers, bodys);

            if (result == null) {
                return R.fail("实名认证API调用失败，请稍后重试！");
            }

            // 解析返回结果
            JSONObject jsonObject = JSONUtil.parseObj(result);
            String status = jsonObject.getOrDefault("status", "").toString();
            String msg = jsonObject.getOrDefault("msg", "认证失败").toString();
            String state = jsonObject.getOrDefault("state", "").toString();
            Map<String, String> data = new HashMap<>();
            data.put("name", name);
            data.put("id_number", idNumber);
            data.put("phone_number", phoneNumber);
            data.put("status", status);
            data.put("msg", msg);

            // 根据返回的状态判断认证结果
            if ("OK".equals(status) && "1".equals(state)) {
                // 认证成功
                return R.success("实名认证成功！", data);
            } else {
                return R.fail(msg);
            }

        } catch (Exception e) {
            log.error("实名认证API调用异常: ", e);
            return R.fail("实名认证过程中发生错误，请稍后重试！");
        }
    }

    /**
     * 执行HTTP POST请求
     */
    private static String doPost(String host, String path, String method, Map<String, String> headers, Map<String, String> bodys) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        FormBody.Builder formBuilder = new FormBody.Builder();

        // 添加请求体参数
        if (bodys != null) {
            for (Map.Entry<String, String> entry : bodys.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        FormBody body = formBuilder.build();
        Request.Builder requestBuilder = new Request.Builder()
                .url(host + path)
                .post(body);

        // 添加请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();

        if (response.code() == HttpStatus.SC_OK) {
            return response.body().string();
        } else {
            log.error("HTTP请求失败，状态码: {}, 错误信息: {}", response.code(), response.message());
            return null;
        }
    }
}
