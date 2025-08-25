package com.sxpcwlkj.ai.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * @author shanpengnian
 */
public class FileParserUtil {
    // 支持的文档类型
    private static final Set<String> SUPPORTED_DOC_TYPES = Set.of(
        "text/plain",
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    // 支持的图片类型（需OCR）
    private static final Set<String> SUPPORTED_IMAGE_TYPES = Set.of(
        "image/png",
        "image/jpeg",
        "image/gif"
    );

    public static String parseFile(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        String fileType = tika.detect(file.getInputStream());

        try {
            if (SUPPORTED_DOC_TYPES.contains(fileType)) {
                return parseDocument(file);
            } else if (SUPPORTED_IMAGE_TYPES.contains(fileType)) {
                return parseImageWithOCR(file); // 需要OCR实现
            } else {
                throw new IOException("Unsupported file type: " + fileType);
            }
        } catch (TikaException | IOException e) {
            throw new IOException("File parsing failed: " + e.getMessage());
        }
    }

    private static String parseDocument(MultipartFile file) throws IOException, TikaException {
        Tika tika = new Tika();
        try (InputStream stream = file.getInputStream()) {
            return tika.parseToString(stream);
        }
    }

    // 需要实现OCR集成（这里留作扩展点）
    private static String parseImageWithOCR(MultipartFile file) throws IOException {
        try {
            // 初始化Tesseract实例
            ITesseract tesseract = new Tesseract();

            //System.setProperty("jna.library.path", "/usr/local/lib");

            // 设置Tesseract数据路径（根据实际路径调整）
            // 示例：tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
            tesseract.setDatapath("/opt/homebrew/Cellar/tesseract/5.5.0_1");
            // 多语言支持
            tesseract.setLanguage("eng+chi_sim");

            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new IOException("Unsupported image format");
            }

            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            throw new IOException("OCR processing failed: " + e.getMessage(), e);
        }
    }
}
