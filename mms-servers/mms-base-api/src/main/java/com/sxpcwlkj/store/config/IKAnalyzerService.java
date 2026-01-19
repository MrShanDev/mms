package com.sxpcwlkj.store.config;

import com.alibaba.excel.util.StringUtils;
import org.springframework.stereotype.Component;
//import org.wltea.analyzer.core.IKSegmenter;
//import org.wltea.analyzer.core.Lexeme;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * ik-analyzer 分词服务
 */
@Component
public class IKAnalyzerService {

    /**
     * 使用 IK Analyzer 进行中文分词
     */
    public List<String> tokenize(String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        return fallbackTokenize(text);

//        List<String> tokens = new ArrayList<>();

//        try (StringReader reader = new StringReader(text)) {
            // 创建 IK 分词器，true 表示智能分词模式
//            IKSegmenter segmenter = new IKSegmenter(reader, true);
//            Lexeme lexeme;
//
//            while ((lexeme = segmenter.next()) != null) {
//                String word = lexeme.getLexemeText();
//                if (isValidToken(word)) {
//                    tokens.add(word);
//                }
//            }
//        } catch (Exception e) {
//            // 如果 IK 分词失败，降级到简单分词
//            return fallbackTokenize(text);
//        }
//
//        return tokens;
    }

    /**
     * 验证 token 是否有效
     */
    private boolean isValidToken(String word) {
        if (StringUtils.isBlank(word)) {
            return false;
        }

        // 过滤单字无意义词
        if (word.length() == 1) {
            return isMeaningfulSingleChar(word);
        }

        // 过滤停用词
        return !isStopWord(word);
    }

    /**
     * 判断有意义的单字符
     */
    private boolean isMeaningfulSingleChar(String word) {
        if (word.length() != 1) return false;
        char c = word.charAt(0);
        return Character.isDigit(c) ||
            (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z');
    }

    /**
     * 停用词过滤
     */
    private boolean isStopWord(String word) {
        Set<String> stopWords = Set.of(
            "的", "了", "在", "是", "我", "有", "和", "就",
            "不", "人", "都", "一", "一个", "上", "也", "很",
            "到", "说", "要", "去", "你", "会", "着", "没有"
        );
        return stopWords.contains(word);
    }

    /**
     * 降级分词方案
     */
    private List<String> fallbackTokenize(String text) {
        List<String> tokens = new ArrayList<>();
        String cleaned = text.replaceAll("[^\\u4e00-\\u9fa5a-zA-Z0-9]", " ");
        String[] words = cleaned.split("\\s+");

        for (String word : words) {
            if (StringUtils.isNotBlank(word)) {
                tokens.add(word);
            }
        }
        return tokens;
    }
}
