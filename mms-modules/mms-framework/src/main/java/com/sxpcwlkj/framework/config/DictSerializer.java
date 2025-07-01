package com.sxpcwlkj.framework.config;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.constant.CacheConstants;
import com.sxpcwlkj.redis.RedisUtil;
import lombok.SneakyThrows;

/**
 * 字典自定序列化类
 *
 * @author mmsAdmin
 */
public class DictSerializer extends StdSerializer<Object> implements ContextualSerializer {

    private static final long serialVersionUID = -6157558261755426448L;

    private String dictCode;

    public DictSerializer() {
        super(Object.class);
    }

    public DictSerializer(String dictCode) {
        super(Object.class);
        this.dictCode = dictCode;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        Dict annotation = property.getAnnotation(Dict.class);
        return new DictSerializer(annotation.value());
    }

    @SneakyThrows
    @Override
    public void serialize(Object code, JsonGenerator gen, SerializerProvider provider) {
        /*
        获取键值对，这里我处理成map的方式返回给前端
        这部分要自己处理，我这里是通过一个业务类从redis中获取字典sys_dict:SYS_STATE:1
         */
//        Console.error(CacheConstants.SYS_DICT_KEY+dictCode+":"+code);
        Object object = RedisUtil.getCacheObject(CacheConstants.SYS_DICT_KEY + dictCode + ":" + code);
        if (object != null) {
            gen.writeObject(object.toString());
        } else {
            gen.writeObject(code);
        }
    }
}
