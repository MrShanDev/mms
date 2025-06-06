package com.sxpcwlkj.docApi.utils;

import java.util.Random;

/**
 * @author shanpengnian
 */
public class NicknameGenerator {
    // 形容词列表（可自行扩展）
    private static final String[] ADJECTIVES = {
        "快乐的", "神秘的", "勇敢的", "聪明的", "优雅的", "顽皮的", "阳光的", "冷静的",
        "热情的", "幽默的", "勤奋的", "温柔的", "机灵的", "威武的", "浪漫的", "潇洒的"
    };

    // 名词列表（可自行扩展）
    private static final String[] NOUNS = {
        "熊猫", "狮子", "猎豹", "海豚", "雄鹰", "狐狸", "鲨鱼", "老虎",
        "飞鸟", "骏马", "天鹅", "鲸鱼", "蝴蝶", "狼", "孔雀", "凤凰"
    };

    public static String generateRandomNickname() {
        Random random = new Random();

        // 随机选择形容词和名词
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];

        // 随机生成2-4位数字（可选部分）// 0-2的随机数决定是否添加数字
        int numberSuffix = random.nextInt(3);
        StringBuilder nickname = new StringBuilder(adjective + noun);
        // 50%概率添加数字
        if (numberSuffix > 0) {
            // 2-4位数字
            int digitCount = random.nextInt(3) + 2;
            for (int i = 0; i < digitCount; i++) {
                // 添加0-9的随机数字
                nickname.append(random.nextInt(10));
            }
        }

        return nickname.toString();
    }

    // 测试方法
    public static void main(String[] args) {
        // 生成10个示例昵称
        for (int i = 0; i < 10; i++) {
            System.out.println(generateRandomNickname());
        }
    }
}
