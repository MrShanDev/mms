package com.sxpcwlkj.base.utils;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 高可用订单号生成工具类
 * 支持多种生成策略：时间戳+序列号、雪花算法、Redis分布式ID等
 * 静态工具类，可直接调用
 */
public final class OrderNoUtil {

    // 私有构造函数，防止实例化
    private OrderNoUtil() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }

    // 生成策略枚举
    public enum GenerateStrategy {
        TIMESTAMP_SEQUENCE,  // 时间戳+序列号
        SNOWFLAKE,          // 雪花算法
        REDIS               // Redis分布式ID
    }

    // 默认配置
    private static final String DEFAULT_PREFIX = "MMS";
    private static final GenerateStrategy DEFAULT_STRATEGY = GenerateStrategy.TIMESTAMP_SEQUENCE;
    private static final int MAX_SEQUENCE = 9999;
    private static final int MAX_RETRY_COUNT = 3;
    private static final int MAX_CACHE_SIZE = 10000;

    // 序列号相关
    private static final AtomicLong sequence = new AtomicLong(0);
    private static String lastTimestamp = "";
    private static final ReentrantLock lock = new ReentrantLock();

    // 机器ID（用于分布式环境）
    private static final long MACHINE_ID = generateMachineId();

    // 缓存最近生成的订单号（用于防重复）
    private static final Map<String, Boolean> RECENT_ORDER_NOS = new ConcurrentHashMap<>();

    // Redis客户端（如果使用Redis策略）
    private static RedisService redisService;

    /**
     * 生成订单号（使用默认策略和前缀）
     */
    public static String generate() {
        return generate(DEFAULT_PREFIX, DEFAULT_STRATEGY);
    }

    /**
     * 生成订单号（指定前缀）
     */
    public static String generate(String prefix) {
        return generate(prefix, DEFAULT_STRATEGY);
    }

    /**
     * 生成订单号（指定策略）
     */
    public static String generate(GenerateStrategy strategy) {
        return generate(DEFAULT_PREFIX, strategy);
    }

    /**
     * 生成订单号（指定前缀和策略）
     */
    public static String generate(String prefix, GenerateStrategy strategy) {
        String orderNo = null;
        int retryCount = 0;

        while (retryCount < MAX_RETRY_COUNT) {
            try {
                switch (strategy) {
                    case TIMESTAMP_SEQUENCE:
                        orderNo = generateByTimestampSequence(prefix);
                        break;
                    case SNOWFLAKE:
                        orderNo = generateBySnowflake(prefix);
                        break;
                    case REDIS:
                        orderNo = generateByRedis(prefix);
                        break;
                    default:
                        orderNo = generateByTimestampSequence(prefix);
                }

                // 检查是否重复
                if (!isDuplicate(orderNo)) {
                    cacheOrderNo(orderNo);
                    return orderNo;
                }

                retryCount++;
                Thread.sleep(10); // 短暂等待后重试

            } catch (Exception e) {
                retryCount++;
                // 记录日志
                System.err.println("生成订单号失败，重试次数: " + retryCount + ", 错误: " + e.getMessage());

                // 最后一次重试仍然失败，使用备用方案
                if (retryCount >= MAX_RETRY_COUNT) {
                    orderNo = generateFallbackOrderNo(prefix);
                    cacheOrderNo(orderNo);
                    return orderNo;
                }
            }
        }

        return generateFallbackOrderNo(prefix);
    }

    /**
     * 时间戳+序列号生成策略
     */
    private static String generateByTimestampSequence(String prefix) {
        lock.lock();
        try {
            String currentTimestamp = getCurrentTimestamp();

            if (!currentTimestamp.equals(lastTimestamp)) {
                lastTimestamp = currentTimestamp;
                sequence.set(0);
            }

            long currentSequence = sequence.incrementAndGet();
            if (currentSequence > MAX_SEQUENCE) {
                // 序列号溢出，等待到下一毫秒
                Thread.sleep(1);
                return generateByTimestampSequence(prefix);
            }

            return String.format("%s%s%04d", prefix, currentTimestamp, currentSequence);

        } catch (Exception e) {
            throw new RuntimeException("时间戳序列号生成失败", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 雪花算法生成策略
     */
    private static String generateBySnowflake(String prefix) {
        long timestamp = System.currentTimeMillis();
        long sequenceValue = sequence.incrementAndGet() & 0xFFF; // 12位序列号

        // 雪花算法ID结构：时间戳(41位) + 机器ID(10位) + 序列号(12位)
        long snowflakeId = ((timestamp - 1609459200000L) << 22) | (MACHINE_ID << 12) | sequenceValue;

        return prefix + snowflakeId;
    }

    /**
     * Redis分布式ID生成策略
     */
    private static String generateByRedis(String prefix) {
        if (redisService == null) {
            throw new IllegalStateException("Redis服务未初始化，请先调用setRedisService方法");
        }

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = "order_no:" + date;
        Long sequence = redisService.increment(key, 1L);
        redisService.expire(key, 86400); // 24小时过期

        return String.format("%s%s%06d", prefix, date, sequence);
    }

    /**
     * 备用订单号生成方案
     */
    private static String generateFallbackOrderNo(String prefix) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf((int) (Math.random() * 1000));
        String threadId = String.valueOf(Thread.currentThread().getId());

        return prefix + timestamp + random + threadId;
    }

    /**
     * 获取当前时间戳（精确到毫秒）
     */
    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    /**
     * 生成机器ID（基于MAC地址和IP地址）
     */
    private static long generateMachineId() {
        try {
            // 尝试获取MAC地址
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = network.getHardwareAddress();
            long machineId = 0L;

            if (mac != null) {
                for (int i = 0; i < Math.min(mac.length, 6); i++) {
                    machineId |= (mac[i] & 0xFFL) << (8 * i);
                }
            } else {
                // 如果获取MAC地址失败，使用IP地址
                byte[] ip = InetAddress.getLocalHost().getAddress();
                for (int i = 0; i < Math.min(ip.length, 4); i++) {
                    machineId |= (ip[i] & 0xFFL) << (8 * i);
                }
            }

            return machineId & 0x3FF; // 10位机器ID

        } catch (Exception e) {
            // 如果获取机器信息失败，使用随机数
            return new Random().nextInt(1024);
        }
    }

    /**
     * 检查订单号是否重复
     */
    private static boolean isDuplicate(String orderNo) {
        return RECENT_ORDER_NOS.containsKey(orderNo);
    }

    /**
     * 缓存订单号
     */
    private static void cacheOrderNo(String orderNo) {
        // 限制缓存大小，防止内存溢出
        if (RECENT_ORDER_NOS.size() >= MAX_CACHE_SIZE) {
            // 移除最早的一半条目
            Iterator<String> iterator = RECENT_ORDER_NOS.keySet().iterator();
            int removeCount = MAX_CACHE_SIZE / 2;
            while (iterator.hasNext() && removeCount > 0) {
                iterator.next();
                iterator.remove();
                removeCount--;
            }
        }
        RECENT_ORDER_NOS.put(orderNo, true);
    }

    /**
     * 设置Redis服务（用于Redis策略）
     */
    public static void setRedisService(RedisService redisService) {
        OrderNoUtil.redisService = redisService;
    }

    /**
     * 验证订单号格式
     */
    public static boolean validateOrderNo(String orderNo) {
        if (orderNo == null || orderNo.length() < 10) {
            return false;
        }

        // 可以根据具体格式添加更详细的验证逻辑
        return orderNo.matches("^[A-Za-z0-9]+$");
    }

    /**
     * 批量生成订单号
     */
    public static List<String> batchGenerate(int count) {
        return batchGenerate(count, DEFAULT_PREFIX, DEFAULT_STRATEGY);
    }

    /**
     * 批量生成订单号（指定前缀）
     */
    public static List<String> batchGenerate(int count, String prefix) {
        return batchGenerate(count, prefix, DEFAULT_STRATEGY);
    }

    /**
     * 批量生成订单号（指定策略）
     */
    public static List<String> batchGenerate(int count, GenerateStrategy strategy) {
        return batchGenerate(count, DEFAULT_PREFIX, strategy);
    }

    /**
     * 批量生成订单号（指定前缀和策略）
     */
    public static List<String> batchGenerate(int count, String prefix, GenerateStrategy strategy) {
        List<String> orderNos = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            orderNos.add(generate(prefix, strategy));
        }
        return orderNos;
    }

    /**
     * 获取当前序列号（用于测试和监控）
     */
    public static long getCurrentSequence() {
        return sequence.get();
    }

    /**
     * 获取机器ID（用于测试和监控）
     */
    public static long getMachineId() {
        return MACHINE_ID;
    }

    /**
     * 清空缓存（用于测试）
     */
    public static void clearCache() {
        RECENT_ORDER_NOS.clear();
    }

    /**
     * Redis服务接口（需要实现）
     */
    public interface RedisService {
        Long increment(String key, long delta);
        void expire(String key, int seconds);
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {

        // 1. 基本使用 - 默认前缀和策略
        String orderNo1 = OrderNoUtil.generate();

        // 2. 指定前缀
        String orderNo2 = OrderNoUtil.generate("PAY");

        // 3. 指定策略
        String orderNo3 = OrderNoUtil.generate(GenerateStrategy.SNOWFLAKE);

        // 4. 指定前缀和策略
        String orderNo4 = OrderNoUtil.generate("REFUND", GenerateStrategy.TIMESTAMP_SEQUENCE);

        // 5. 批量生成
        List<String> orderNos = OrderNoUtil.batchGenerate(10, "BATCH");

        // 6. 验证订单号格式
        boolean isValid = OrderNoUtil.validateOrderNo("ORD2023120112304500010001");


        // 测试不同策略
        System.out.println("时间戳+序列号策略: " + OrderNoUtil.generate("TEST"));
        System.out.println("雪花算法策略: " + OrderNoUtil.generate("TEST", GenerateStrategy.SNOWFLAKE));

        // 测试批量生成
        List<String> batchOrderNos = OrderNoUtil.batchGenerate(5, "BATCH", GenerateStrategy.TIMESTAMP_SEQUENCE);
        System.out.println("批量生成:");
        batchOrderNos.forEach(System.out::println);

        // 测试性能
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            OrderNoUtil.generate();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("生成1000个订单号耗时: " + (endTime - startTime) + "ms");

        // 验证订单号格式
        String testOrderNo = OrderNoUtil.generate();
        System.out.println("订单号 " + testOrderNo + " 格式验证: " + OrderNoUtil.validateOrderNo(testOrderNo));

        // 测试机器ID
        System.out.println("机器ID: " + OrderNoUtil.getMachineId());
        System.out.println("当前序列号: " + OrderNoUtil.getCurrentSequence());
    }
}
