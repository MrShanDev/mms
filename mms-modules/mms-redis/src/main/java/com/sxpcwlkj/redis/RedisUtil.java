package com.sxpcwlkj.redis;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName RedisUtil
 * @Description TODO
 * @Author mmsAdmin
 * @Date 2022/12/4 20:58
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisUtil {

    private static final RedissonClient CLIENT = SpringUtil.getBean(RedissonClient.class);

    /**
     * 全局 redis key (业务无关的key)
     */
    public static String  GLOBAL_REDIS_KEY = "global:";

    /**
     * 验证码 redis key
     */
    public static String CAPTCHA_CODE_KEY = GLOBAL_REDIS_KEY + "captcha_codes:";
    /**
     * 验证码 redis key
     */
    public static String PHONE_CODES_KEY = GLOBAL_REDIS_KEY + "phone_codes:";
    /**
     * 邮箱码 redis key
     */
    public static String EMAIL_CODES_KEY = GLOBAL_REDIS_KEY + "email_codes:";

    /**
     * 防重提交 redis key
     */
    public static String REPEAT_SUBMIT_KEY = GLOBAL_REDIS_KEY + "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static String RATE_LIMIT_KEY = GLOBAL_REDIS_KEY + "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static String PWD_ERR_CNT_KEY = GLOBAL_REDIS_KEY + "pwd_err_cnt:";

    public static NameMapper getNameMapper() {
        Config config = CLIENT.getConfig();
        if (config.isClusterConfig()) {
            return config.useClusterServers().getNameMapper();
        }
        return config.useSingleServer().getNameMapper();
    }

    /**
     * 限流
     *
     * @param key          限流key
     * @param rateType     限流类型
     * @param rate         速率
     * @param rateInterval 速率间隔
     * @return -1 表示失败
     */
    public static long rateLimiter(String key, RateType rateType, int rate, int rateInterval) {
        RRateLimiter rateLimiter = CLIENT.getRateLimiter(key);
        rateLimiter.trySetRate(rateType, rate, rateInterval, RateIntervalUnit.SECONDS);
        if (rateLimiter.tryAcquire()) {
            return rateLimiter.availablePermits();
        } else {
            return -1L;
        }
    }

    /**
     * 获取客户端实例
     */
    public static RedissonClient getClient() {
        return CLIENT;
    }

    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     * @param consumer   自定义处理
     */
    public static <T> void publish(String channelKey, T msg, Consumer<T> consumer) {
        RTopic topic = CLIENT.getTopic(channelKey);
        topic.publish(msg);
        consumer.accept(msg);
    }

    public static <T> void publish(String channelKey, T msg) {
        RTopic topic = CLIENT.getTopic(channelKey);
        topic.publish(msg);
    }

    /**
     * 订阅通道接收消息
     *
     * @param channelKey 通道key
     * @param clazz      消息类型
     * @param consumer   自定义处理
     */
    public static <T> void subscribe(String channelKey, Class<T> clazz, Consumer<T> consumer) {
        RTopic topic = CLIENT.getTopic(channelKey);
        topic.addListener(clazz, (channel, msg) -> consumer.accept(msg));
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public static <T> void setCacheObject(final String key, final T value) {
        setCacheObject(key, value, false);
    }

    /**
     * 缓存基本的对象，保留当前对象 TTL 有效期
     *
     * @param key       缓存的键值
     * @param value     缓存的值
     * @param isSaveTtl 是否保留TTL有效期(例如: set之前ttl剩余90 set之后还是为90)
     * @since Redis 6.X 以上使用 setAndKeepTTL 兼容 5.X 方案
     */
    public static <T> void setCacheObject(final String key, final T value, final boolean isSaveTtl) {
        RBucket<T> bucket = CLIENT.getBucket(key);
        if (isSaveTtl) {
            try {
                bucket.setAndKeepTTL(value);
            } catch (Exception e) {
                long timeToLive = bucket.remainTimeToLive();
                setCacheObject(key, value, Duration.ofMillis(timeToLive));
            }
        } else {
            bucket.set(value);
        }
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间
     */
    public static <T> void setCacheObject(final String key, final T value, final Duration duration) {
        RBatch batch = CLIENT.createBatch();
        RBucketAsync<T> bucket = batch.getBucket(key);
        bucket.setAsync(value);
        bucket.expireAsync(duration);
        batch.execute();
    }

    /**
     * 注册对象监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addObjectListener(final String key, final ObjectListener listener) {
        RBucket<T> result = CLIENT.getBucket(key);
        result.addListener(listener);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间 单位秒
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout) {
        return expire(key, Duration.ofSeconds(timeout));
    }

    /**
     * 设置有效时间
     *
     * @param key      Redis键
     * @param duration 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final Duration duration) {
        RBucket rBucket = CLIENT.getBucket(key);
        return rBucket.expire(duration);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout, final java.util.concurrent.TimeUnit unit) {
        RBucket rBucket = CLIENT.getBucket(key);
        return rBucket.expire(timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T getCacheObject(final String key) {
        RBucket<T> rBucket = CLIENT.getBucket(key);
        return rBucket.get();
    }

    /**
     * 将桶内 JSON 以纯文本读出再解析为 Map，不经过 Redisson 默认 {@code JsonJacksonCodec} 的类型反查。
     * <p>用于宿主进程读取「插件里写入的、带 {@code @class} 的」等业务对象历史数据：避免 ClassLoader 无该类时解码失败。</p>
     *
     * @param key Redis 键
     * @return Map；键不存在或解析失败返回 {@code null}
     */
    public static Map<String, Object> getCacheJsonObjectAsMap(final String key) {
        try {
            RBucket<String> bucket = CLIENT.getBucket(key, StringCodec.INSTANCE);
            String raw = bucket.get();
            if (raw == null || raw.isBlank()) {
                return null;
            }
            ObjectMapper om = new ObjectMapper();
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            LinkedHashMap<String, Object> map = om.readValue(raw, new TypeReference<LinkedHashMap<String, Object>>() {});
            map.remove("@class");
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 登录会话缓存：先按纯 JSON 文本解析（兼容历史带 @class）；失败则再走默认 Codec（新写入的 Map 无插件类型时常能直接反序列化为 Map）。
     */
    public static Map<String, Object> getLoginSessionMap(final String key) {
        Map<String, Object> fromText = getCacheJsonObjectAsMap(key);
        if (fromText != null) {
            return fromText;
        }
        try {
            Object raw = getCacheObject(key);
            if (raw instanceof Map<?, ?> m) {
                LinkedHashMap<String, Object> sm = new LinkedHashMap<>();
                for (Map.Entry<?, ?> e : m.entrySet()) {
                    if (e.getKey() != null) {
                        sm.put(e.getKey().toString(), e.getValue());
                    }
                }
                sm.remove("@class");
                return sm;
            }
        } catch (Exception ignored) {
            // 旧数据为 DocUser 等多态类型时，Codec 仍可能失败
        }
        return null;
    }

    /**
     * 获得key剩余存活时间
     *
     * @param key 缓存键值
     * @return 剩余存活时间
     */
    public static <T> long getTimeToLive(final String key) {
        RBucket<T> rBucket = CLIENT.getBucket(key);
        return rBucket.remainTimeToLive();
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存的键值
     */
    public static boolean deleteObject(final String key) {
        return CLIENT.getBucket(key).delete();
    }

    /**
     * 删除单个对象（别名方法）
     *
     * @param key 缓存的键值
     */
    public static boolean delCacheObject(final String key) {
        return CLIENT.getBucket(key).delete();
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     */
    public static void deleteObject(final Collection collection) {
        RBatch batch = CLIENT.createBatch();
        collection.forEach(t -> {
            batch.getBucket(t.toString()).deleteAsync();
        });
        batch.execute();
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public static <T> boolean setCacheList(final String key, final List<T> dataList) {
        RList<T> rList = CLIENT.getList(key);
        return rList.addAll(dataList);
    }

    /**
     * 注册List监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addListListener(final String key, final ObjectListener listener) {
        RList<T> rList = CLIENT.getList(key);
        rList.addListener(listener);
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public static <T> List<T> getCacheList(final String key) {
        RList<T> rList = CLIENT.getList(key);
        return rList.readAll();
    }

    /**
     * 模糊查询
     * @param key 缓存的键值 sys:user:*
     * @return 缓存键值对应的数据
     */
    public static <T> List<T> getkeys(final String key) {
        List<T> endData=new ArrayList<>();
        RKeys keys = CLIENT.getKeys();
        for (String keyObj : keys.getKeysByPattern(key)) {
            endData.add(getCacheObject(keyObj));
        }
        return endData;
    }
    /**
     * 模糊查询
     * @param key 缓存的键值 sys:user:*
     * @return 缓存键值对应的数据，和对应的key
     */
    public static <T> List<Map<String, Object>> getkeysAndData(final String key) {

        List<Map<String, Object>> endData=new ArrayList<>();

        RKeys keys = CLIENT.getKeys();
        for (String keyObj : keys.getKeysByPattern(key)) {
            // 处理找到的key
            Map<String, Object> map = new HashMap<>();
            map.put("key",keyObj);
            map.put("value",getCacheObject(keyObj));
            endData.add(map);
        }

        return endData;
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public static <T> boolean setCacheSet(final String key, final Set<T> dataSet) {
        RSet<T> rSet = CLIENT.getSet(key);
        return rSet.addAll(dataSet);
    }

    /**
     * 注册Set监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addSetListener(final String key, final ObjectListener listener) {
        RSet<T> rSet = CLIENT.getSet(key);
        rSet.addListener(listener);
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存的key
     * @return set对象
     */
    public static <T> Set<T> getCacheSet(final String key) {
        RSet<T> rSet = CLIENT.getSet(key);
        return rSet.readAll();
    }

    /**
     * 缓存Map
     *
     * @param key     缓存的键值
     * @param dataMap 缓存的数据
     */
    public static <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            RMap<String, T> rMap = CLIENT.getMap(key);
            rMap.putAll(dataMap);
        }
    }

    /**
     * 注册Map监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addMapListener(final String key, final ObjectListener listener) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        rMap.addListener(listener);
    }

    /**
     * 获得缓存的Map
     *
     * @param key 缓存的键值
     * @return map对象
     */
    public static <T> Map<String, T> getCacheMap(final String key) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        return rMap.getAll(rMap.keySet());
    }

    /**
     * 获得缓存Map的key列表
     *
     * @param key 缓存的键值
     * @return key列表
     */
    public static <T> Set<String> getCacheMapKeySet(final String key) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        return rMap.keySet();
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public static <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        rMap.put(hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static <T> T getCacheMapValue(final String key, final String hKey) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        return rMap.get(hKey);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static <T> T delCacheMapValue(final String key, final String hKey) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        return rMap.remove(hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public static <K, V> Map<K, V> getMultiCacheMapValue(final String key, final Set<K> hKeys) {
        RMap<K, V> rMap = CLIENT.getMap(key);
        return rMap.getAll(hKeys);
    }

    /**
     * Hash递增
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param delta 递增数(小于0递减)
     * @return 递增后的值
     */
    public static Long hIncrBy(final String key, final String hKey, final Long delta) {
        RMap<String, Long> rMap = CLIENT.getMap(key);
        return rMap.addAndGet(hKey, delta);
    }

    /**
     * Hash写入
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public static <T> void hPut(final String key, final String hKey, final T value) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        rMap.put(hKey, value);
    }

    /**
     * Hash获取
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return 值
     */
    public static <T> T hGet(final String key, final String hKey) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        return rMap.get(hKey);
    }

    /**
     * Hash删除
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return 值
     */
    public static <T> T hDel(final String key, final String hKey) {
        RMap<String, T> rMap = CLIENT.getMap(key);
        return rMap.remove(hKey);
    }

    /**
     * 检查Hash中是否存在指定的key
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return 是否存在
     */
    public static Boolean hHasKey(final String key, final String hKey) {
        RMap<String, String> rMap = CLIENT.getMap(key);
        return rMap.containsKey(hKey);
    }

    /**
     * Set添加
     *
     * @param key Redis键
     * @param values 值
     * @return 成功个数
     */
    public static long sSet(final String key, final Object... values) {
        RSet<Object> rSet = CLIENT.getSet(key);
        boolean result = rSet.addAll(Arrays.asList(values));
        return result ? values.length : 0;
    }

    /**
     * Set删除
     *
     * @param key Redis键
     * @param values 值
     * @return 成功个数
     */
    public static long sRemove(final String key, final Object... values) {
        RSet<Object> rSet = CLIENT.getSet(key);
        java.util.Set<Object> valueSet = new java.util.HashSet<>(java.util.Arrays.asList(values));
        boolean result = rSet.removeAll(valueSet);
        return result ? valueSet.size() : 0;
    }

    /**
     * Set是否存在
     *
     * @param key Redis键
     * @param value 值
     * @return 是否存在
     */
    public static Boolean sHasKey(final String key, final Object value) {
        RSet<Object> rSet = CLIENT.getSet(key);
        return rSet.contains(value);
    }

    /**
     * Set获取
     *
     * @param key Redis键
     * @return Set集合
     */
    public static <T> Set<T> sGet(final String key) {
        RSet<T> rSet = CLIENT.getSet(key);
        return rSet.readAll();
    }

    /**
     * Set大小
     *
     * @param key Redis键
     * @return 大小
     */
    public static long sSize(final String key) {
        RSet<Object> rSet = CLIENT.getSet(key);
        return rSet.size();
    }

    /**
     * List添加
     *
     * @param key Redis键
     * @param values 值
     */
    public static void lRightPush(final String key, final Object... values) {
        RList<Object> rList = CLIENT.getList(key);
        rList.addAll(java.util.Arrays.asList(values));
    }

    /**
     * List范围修剪
     *
     * @param key Redis键
     * @param start 开始位置
     * @param end 结束位置
     */
    public static void lTrim(final String key, final long start, final long end) {
        RList<Object> rList = CLIENT.getList(key);
        rList.trim((int) start, (int) end);
    }

    /**
     * List范围获取
     *
     * @param key Redis键
     * @param start 开始位置
     * @param end 结束位置
     * @return 列表
     */
    public static <T> List<T> lGet(final String key, final long start, final long end) {
        RList<T> rList = CLIENT.getList(key);
        if (rList == null) {
            return new ArrayList<>();
        }
        if (start >= 0 && end >= 0) {
            int listSize = rList.size();
            int adjustedEnd = Math.min((int) end + 1, listSize);
            if ((int) start >= listSize || (int) start >= adjustedEnd) {
                return new ArrayList<>();
            }
            return new ArrayList<>(rList.subList((int) start, adjustedEnd));
        } else {
            // 处理负数索引
            int size = rList.size();
            int actualStart = start < 0 ? Math.max(0, size + (int) start) : (int) start;
            int actualEnd = end < 0 ? Math.max(actualStart, size + (int) end + 1) : (int) Math.min((int) end + 1, size);
            if (actualStart >= size || actualStart >= actualEnd) {
                return new ArrayList<>();
            }
            return new ArrayList<>(rList.subList(actualStart, actualEnd));
        }
    }

    /**
     * List大小
     *
     * @param key Redis键
     * @return 大小
     */
    public static long lSize(final String key) {
        RList<Object> rList = CLIENT.getList(key);
        return rList.size();
    }

    /**
     * List从左侧弹出
     *
     * @param key Redis键
     * @return 弹出的值
     */
    public static <T> T lLeftPop(final String key) {
        RDeque<T> rDeque = CLIENT.getDeque(key);
        return rDeque.pollFirst();
    }

    /**
     * List从右侧弹出
     *
     * @param key Redis键
     * @return 弹出的值
     */
    public static <T> T lRightPop(final String key) {
        RDeque<T> rDeque = CLIENT.getDeque(key);
        return rDeque.pollLast();
    }

    /**
     * 设置原子值
     *
     * @param key   Redis键
     * @param value 值
     */
    public static void setAtomicValue(String key, long value) {
        RAtomicLong atomic = CLIENT.getAtomicLong(key);
        atomic.set(value);
    }

    /**
     * 获取原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public static long getAtomicValue(String key) {
        RAtomicLong atomic = CLIENT.getAtomicLong(key);
        return atomic.get();
    }

    /**
     * 递增原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public static long incrAtomicValue(String key) {
        RAtomicLong atomic = CLIENT.getAtomicLong(key);
        return atomic.incrementAndGet();
    }

    /**
     * 递减原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public static long decrAtomicValue(String key) {
        RAtomicLong atomic = CLIENT.getAtomicLong(key);
        return atomic.decrementAndGet();
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public static Collection<String> keys(final String pattern) {
        Stream<String> stream = CLIENT.getKeys().getKeysStreamByPattern(getNameMapper().map(pattern));
        return stream.map(key -> getNameMapper().unmap(key)).collect(Collectors.toList());
    }

    /**
     * 删除缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     */
    public static void deleteKeys(final String pattern) {
        CLIENT.getKeys().deleteByPattern(getNameMapper().map(pattern));
    }

    /**
     * 检查redis中是否存在key
     *
     * @param key 键
     */
    public static Boolean hasKey(String key) {
        RKeys rKeys = CLIENT.getKeys();
        return rKeys.countExists(getNameMapper().map(key)) > 0;
    }
    /**
     * 锁前缀
     */
    private static final String LOCK_PREFIX = "lock:";
    public static boolean tryLock(String lockKey, long expireTime, TimeUnit timeUnit) {
        RBucket<String> bucket = CLIENT.getBucket(LOCK_PREFIX + lockKey);
        // 使用 trySet 实现 SETNX 语义（如果 key 不存在则设置）
        return bucket.trySet("locked", expireTime, timeUnit);
    }

    /**
     * 释放锁
     * @param lockKey 锁的名称
     */
    public static void unlock(String lockKey) {
        RBucket<String> bucket = CLIENT.getBucket(LOCK_PREFIX + lockKey);
        bucket.delete();
    }
}
