package com.sxpcwlkj.store.config;

import com.alibaba.excel.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分词器
 * 结合 IK Analyzer 和领域知识
 */
@Component
@RequiredArgsConstructor
public class DroneProductTokenizer {

    private final IKAnalyzerService ikAnalyzerService;

    // 无人机领域关键词
    private static final Set<String> DRONE_KEYWORDS = Set.of(
        "大疆", "DJI", "无人机", "UAV", "工业无人机", "消费级无人机",
        "植保无人机", "测绘无人机", "巡检无人机", "物流无人机",
        "无人机培训", "无人机驾照", "AOPA", "空域咨询",
        "无人机调运", "无人机巡检", "无人机应急救援",
        "低空增程器", "无人机电池", "智能电池",
        "手持式无人机侦测", "侦测打击设备", "反制设备",
        "无人机保险", "无人机采购", "无人机解决方案",
        "Mavic", "Phantom", "Inspire", "Matrice", "Agras",
        "航拍", "RTK", "4K", "8K", "避障", "图传", "FPV"
    );

    /**
     * 智能分词 - 结合 IK Analyzer 和领域知识
     */
    public List<String> smartTokenize(String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }

        // 1. 使用 IK Analyzer 进行基础分词
        List<String> tokens = ikAnalyzerService.tokenize(text);

        // 2. 领域关键词增强
        List<String> enhancedTokens = enhanceWithDomainKnowledge(tokens, text);

        // 3. 过滤和去重
        return enhancedTokens.stream()
            .filter(this::isValidDroneToken)
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * 领域知识增强
     */
    private List<String> enhanceWithDomainKnowledge(List<String> tokens, String originalText) {
        List<String> enhanced = new ArrayList<>(tokens);

        // 检测并添加领域关键词
        for (String keyword : DRONE_KEYWORDS) {
            if (originalText.contains(keyword) && !enhanced.contains(keyword)) {
                enhanced.add(keyword);
            }
        }

        // 处理品牌+型号组合（如"大疆2代"）
        enhanced.addAll(extractBrandModels(originalText));

        return enhanced;
    }

    /**
     * 提取品牌和型号组合
     */
    private List<String> extractBrandModels(String text) {
        List<String> models = new ArrayList<>();

        // 匹配模式：品牌 + 数字 + 代
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(大疆|DJI)\\s*(\\d+)\\s*代?");
        java.util.regex.Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String brand = matcher.group(1);
            String generation = matcher.group(2);
            models.add(brand + generation + "代");
        }

        return models;
    }

    /**
     * 验证无人机领域 token
     */
    private boolean isValidDroneToken(String token) {
        if (StringUtils.isBlank(token)) return false;
        if (token.length() == 1 && !isMeaningfulSingleChar(token)) return false;

        // 领域关键词优先保留
        if (DRONE_KEYWORDS.contains(token)) {
            return true;
        }

        // 过滤常见停用词
        return !isStopWord(token);
    }

    private boolean isMeaningfulSingleChar(String word) {
        if (word.length() != 1) return false;
        char c = word.charAt(0);
        return Character.isDigit(c) ||
            (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
            c == '代' || c == '版' || c == '型';
    }

    private boolean isStopWord(String word) {
        Set<String> stopWords = Set.of(
            "的", "了", "在", "是", "我", "有", "和", "就",
            "不", "人", "都", "一", "一个", "上", "也", "很"
        );
        return stopWords.contains(word);
    }

    /**
     * 获取分词结果（用于调试）
     */
    public void debugTokenize(String text) {
        System.out.println("原始文本: " + text);
        List<String> tokens = smartTokenize(text);
        System.out.println("分词结果: " + tokens);
        System.out.println("---");
    }
}
