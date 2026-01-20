package com.sxpcwlkj.member.enums;

import lombok.Getter;

/**
 * 附件类型
 */
@Getter
public enum FileType {
    // 图片类型
    IMAGE("jpg", "jpeg", "png", "gif", "bmp", "webp", "svg", "ico", "tiff"),

    // 视频类型
    VIDEO("mp4", "avi", "mov", "wmv", "flv", "mkv", "webm", "mpeg", "mpg", "3gp"),

    // 音频类型
    AUDIO("mp3", "wav", "flac", "aac", "ogg", "wma", "m4a", "amr"),

    // 文档类型
    PDF("pdf"),
    DOC("doc", "docx"),
    TXT("txt"),
    EXCEL("xls", "xlsx", "csv"),
    POWERPOINT("ppt", "pptx"),

    // 压缩文件类型
    ZIP("zip", "rar", "7z", "tar", "gz", "bz2"),

    // 代码文件类型
    CODE("java", "py", "cpp", "c", "html", "css", "js", "php", "xml", "json"),

    // 可执行文件类型
    EXECUTABLE("exe", "msi", "bat", "sh", "apk", "dmg"),

    // 其他类型
    UNKNOWN();

    private final String[] extensions;

    // 构造函数
    FileType(String... extensions) {
        this.extensions = extensions;
    }

    // 根据文件路径获取文件类型
    public static FileType fromFilePath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return UNKNOWN;
        }

        // 获取文件扩展名
        String extension = extractExtension(filePath);
        if (extension.isEmpty()) {
            return UNKNOWN;
        }

        // 查找匹配的文件类型
        for (FileType type : values()) {
            if (type != UNKNOWN && type.hasExtension(extension)) {
                return type;
            }
        }

        return UNKNOWN;
    }

    // 从文件路径中提取扩展名
    private static String extractExtension(String filePath) {
        if (filePath == null) {
            return "";
        }

        int lastDotIndex = filePath.lastIndexOf('.');
        int lastSeparatorIndex = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));

        // 确保点号在最后一个路径分隔符之后
        if (lastDotIndex > lastSeparatorIndex && lastDotIndex < filePath.length() - 1) {
            return filePath.substring(lastDotIndex + 1).toLowerCase();
        }

        return "";
    }

    // 获取该类型的所有扩展名
    public String[] getExtensions() {
        return extensions;
    }

    // 检查扩展名是否属于此类型
    public boolean hasExtension(String extension) {
        if (extension != null) {
            String ext = extension.toLowerCase().replace(".", "");
            for (String typeExt : extensions) {
                if (typeExt.equals(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 获取类型的描述信息
    public String getDescription() {
        return switch (this) {
            case IMAGE -> "图片文件";
            case VIDEO -> "视频文件";
            case AUDIO -> "音频文件";
            case PDF -> "PDF文档";
            case DOC -> "Word文档";
            case TXT -> "文本文件";
            case EXCEL -> "Excel表格";
            case POWERPOINT -> "PowerPoint演示文稿";
            case ZIP -> "压缩文件";
            case CODE -> "代码文件";
            case EXECUTABLE -> "可执行文件";
            default -> "未知文件类型";
        };
    }
}
