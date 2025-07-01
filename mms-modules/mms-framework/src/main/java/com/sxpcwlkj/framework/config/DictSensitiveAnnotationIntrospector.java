package com.sxpcwlkj.framework.config;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.sxpcwlkj.common.annotation.Dict;
import org.springframework.stereotype.Component;

/**
 * 字典注解序列化拦截器
 *
 * @author mmsAdmin
 */
@Component
public class DictSensitiveAnnotationIntrospector extends NopAnnotationIntrospector {

    private static final long serialVersionUID = 1L;

    @Override
    public Object findSerializer(Annotated am) {
        Dict dict = am.getAnnotation(Dict.class);
        if (dict != null) {
            return DictSerializer.class;
        }
        return null;
    }
}
