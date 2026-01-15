package com.sxpcwlkj.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author: mmsAdmin
 */
public class FileUtil extends cn.hutool.core.io.FileUtil  {
    /**
     * 文件流转base64
     */
    public static String byteToBase64(byte[] data) {
        String base64String = java.util.Base64.getEncoder().encodeToString(data);
        return "data:image/jpg;base64,"+base64String;
    }
    public static String imgToBase64(String path) {
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
    public static String fileUrlToBase64(String fileUrl){
        try {
            URL url = URI.create(fileUrl).toURL();
            InputStream inputStream = url.openStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = outputStream.toByteArray();
            String base64String = java.util.Base64.getEncoder().encodeToString(imageBytes);
            inputStream.close();
            outputStream.close();
            return base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8);
        return encode.replaceAll("\\+", "%20");
    }

    public static File convertMultipartFileToFile(MultipartFile file) throws IOException {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 创建文件对象
        assert fileName != null;
        File convFile = new File(fileName);
        // 将MultipartFile转换为File
        Path path = Paths.get(convFile.getName());
        Files.copy(file.getInputStream(), path);
        return convFile;
    }


    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {
        String percentEncodedFileName = URLEncoder.encode(realFileName, StandardCharsets.UTF_8);

        String contentDispositionValue = "attachment; filename=" +
            percentEncodedFileName +
            ";" +
            "filename*=" +
            "utf-8''" +
            percentEncodedFileName;

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue);
        response.setHeader("download-filename", percentEncodedFileName);
    }

    public static String bufferedImageToBase64(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 1. 将图像写入字节流
            ImageIO.write(image, "PNG", baos);
            baos.flush();

            // 2. 转换为字节数组并Base64编码
            return "data:image/png;base64," +java.util.Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("转换失败: " + e.getMessage());
        }
    }

}
