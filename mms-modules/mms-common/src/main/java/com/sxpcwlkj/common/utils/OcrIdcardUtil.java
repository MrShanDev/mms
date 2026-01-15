package com.sxpcwlkj.common.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class OcrIdcardUtil {


    public static String img_base64(String path) {
        /**
         *  对path进行判断，如果是本地文件就二进制读取并base64编码，如果是url,则返回
         */
        String imgBase64 = "";
        if (path.startsWith("http")) {
            imgBase64 = path;
        } else {
            try {
                File file = new File(path);
                byte[] content = new byte[(int) file.length()];
                FileInputStream finputstream = new FileInputStream(file);
                finputstream.read(content);
                finputstream.close();
                imgBase64 = new String(
                        Base64.encodeBase64(content));
            } catch (IOException e) {
                e.printStackTrace();
                return imgBase64;
            }
        }

        return imgBase64;
    }


    public static R<Map<String, String>> getIcrdInfo(String imgFile, String phone) {
        // 阿里云 身份证识别 接口
        //https://market.aliyun.com/products/57124001/cmapi010401.html?spm=5176.2020520132.0.0.386a7218SrYkJP#sku=yuncode4401000018
        Map<String, String> end = new HashMap<String, String>();
        String host = "https://cardnumber.market.alicloudapi.com";
        String path = "/rest/160601/ocr/ocr_idcard.json";
        String appcode = "换成你自己的appcode";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");

        Map<String, String> querys = new HashMap<String, String>();
        // 对图像进行base64编码
        String imgBase64 = img_base64(imgFile);

        //configure配置
        JSONObject configObj = new JSONObject();
        configObj.set("side", "face");

        String config_str = configObj.toString();

        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        requestObj.set("image", imgBase64);
        if (configObj.size() > 0) {
            requestObj.set("configure", config_str);
        }
        String bodys = requestObj.toString();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */

            HttpResponse response =null;
                    //HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            方式二：
//            String result2 = HttpRequest.post(host+path)
//                    .header("Authorization", "APPCODE " + appcode)
//                    .header("Content-Type", "application/json; charset=UTF-8")
//                    .body(bodys)
//                    .execute().body();
//            Console.log(result2);
            int stat = response.getStatusLine().getStatusCode();
            end.put("code", String.valueOf(stat));
            if (stat != 200) {
                System.out.println("Http code: " + stat);
                System.out.println("http header error msg: " + response.getFirstHeader("X-Ca-Error-Message"));
                System.out.println("Http body error msg:" + EntityUtils.toString(response.getEntity()));
                end.put("msg", "身份证不清晰无法识别!");
                return R.fail("身份证不清晰无法识别!");
            }

            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSONUtil.parseObj(res);
            if (!res_obj.get("success").toString().equals("true")) {
                end.put("msg", "身份证不清晰无法识别!");
                return R.fail("身份证不清晰无法识别!");
            }
            System.out.println(JSONUtil.toJsonStr(res_obj));
            end.put("msg", "身份证识别成功!");
            end.put("address", res_obj.get("address").toString());

            end.put("name", res_obj.get("name").toString());
            end.put("num", res_obj.get("num").toString());
            end.put("sex", res_obj.get("sex").toString());
            end.put("nationality", res_obj.get("nationality").toString());
            System.out.println(JSONUtil.toJsonStr(end));


            // 调用身份证实名认证接口
            // https://market.aliyun.com/products/57002003/cmapi026100.html?spm=5176.2020520132.0.0.386a7218SrYkJP#sku=yuncode20100000022
            String AppKey = "换成你自己的AppKey";
            String AppSecret = "换成你自己的AppSecret";
            String AppCode = "换成你自己的AppCode";

            String url = "https://mobile3elements.shumaidata.com/mobile/verify_real_name";

            Map<String, String> params = new HashMap<>();
            params.put("idcard", res_obj.get("num").toString());
            params.put("mobile", phone);
            params.put("name", res_obj.get("name").toString());
            String result = postForm(AppCode, url, params);
            if (result == null || result.equals("")) return R.fail("认证失败,请稍后再试！");
            cn.hutool.json.JSONObject object = JSONUtil.parseObj(result);
            if (object.get("code").equals("0")) {
                Object o = object.get("result");
                cn.hutool.json.JSONObject obj = JSONUtil.parseObj(o);
                if (obj.get("description").equals("不一致")) return R.fail("认证失败,信息有误！");
                if (!obj.get("res").equals("1")) return R.fail("认证失败,信息有误！");
                return R.success("认证成功！",end);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.fail("认证失败!");
    }

    /**
     * 用到的HTTP工具包：okhttp 3.13.1
     * <dependency>
     * <groupId>com.squareup.okhttp3</groupId>
     * <artifactId>okhttp</artifactId>
     * <version>3.13.1</version>
     * </dependency>
     */
    public static String postForm(String appCode, String url, Map<String, String> params) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        FormBody.Builder formbuilder = new FormBody.Builder();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            formbuilder.add(key, params.get(key));
        }
        FormBody body = formbuilder.build();
        Request request = new Request.Builder().url(url).addHeader("Authorization", "APPCODE " + appCode).post(body).build();
        Response response = client.newCall(request).execute();
        log.info("返回状态码" + response.code() + ",message:" + response.message());
        String result = response.body().string();
        return result;
    }
}
