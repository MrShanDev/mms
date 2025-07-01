package com.sxpcwlkj.common.captcha;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.sxpcwlkj.common.annotation.SensitivityEncrypt;
import com.sxpcwlkj.common.enums.SensitivityTypeEnum;
import com.sxpcwlkj.common.utils.SensitivityUtil;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

/**
 * @author mmsAdmin
 */
@NoArgsConstructor
@AllArgsConstructor
public class SensitivitySerializer extends JsonSerializer<String> implements ContextualSerializer {

    // 脱敏类型
    private SensitivityTypeEnum sensitivityTypeEnum;
    // 前几位不脱敏
    private Integer prefixNoMaskLen;
    // 最后几位不脱敏
    private Integer suffixNoMaskLen;
    // 用什么打码
    private String symbol;

    /**
     * 序列化 数据处理
     */
    @Override
    public void serialize(final String origin, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        //判断是否为空脱敏类型
        if (StringUtil.isNotBlank(origin) && null != sensitivityTypeEnum) {
            //判断脱敏类型，进入对应类型的数据处理
            switch (sensitivityTypeEnum) {
                case CUSTOMER:
                    jsonGenerator.writeString(SensitivityUtil.desValue(origin, prefixNoMaskLen, suffixNoMaskLen, symbol));
                    break;
                case NAME:
                    jsonGenerator.writeString(SensitivityUtil.hideChineseName(origin));
                    break;
                case ID_CARD:
                    jsonGenerator.writeString(SensitivityUtil.hideIDCard(origin));
                    break;
                case PHONE:
                    jsonGenerator.writeString(SensitivityUtil.hidePhone(origin));
                    break;
                case EMAIL:
                    jsonGenerator.writeString(SensitivityUtil.hideEmail(origin));
                    break;
                default:
                    throw new IllegalArgumentException("unknown privacy type enum " + sensitivityTypeEnum);
            }
        } else {
            //如果脱敏类型为空则赋值空，要不然会导致序列化错误
            jsonGenerator.writeString("");
        }
    }

    /**
     * 读取自定义注解SensitivityEncrypt 创建上下文所需
     */
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
                                              final BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                SensitivityEncrypt sensitivityEncrypt = beanProperty.getAnnotation(SensitivityEncrypt.class);
                if (sensitivityEncrypt == null) {
                    sensitivityEncrypt = beanProperty.getContextAnnotation(SensitivityEncrypt.class);
                }
                if (sensitivityEncrypt != null) {
                    return new SensitivitySerializer(sensitivityEncrypt.type(), sensitivityEncrypt.prefixNoMaskLen(),
                            sensitivityEncrypt.suffixNoMaskLen(), sensitivityEncrypt.symbol());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
