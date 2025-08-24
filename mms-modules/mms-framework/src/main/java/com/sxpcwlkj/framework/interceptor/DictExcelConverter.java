package com.sxpcwlkj.framework.interceptor;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.sxpcwlkj.common.annotation.Dict;
import com.sxpcwlkj.common.constant.CacheConstants;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.redis.RedisUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author: mmsAdmin
 * Excel 字典转换器
 */
public class DictExcelConverter implements Converter<Object> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 导入：这里读的时候会调用
     */
    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        Dict anno = getAnnotation(contentProperty.getField());
        String type = anno.value();
        String label = cellData.getStringValue();
        List<Map<String, Object>> list = RedisUtil.getkeysAndData(CacheConstants.SYS_DICT_KEY + type + ":*");

        for (Map<String, Object> map : list) {
            String key = Convert.toStr(map.get("key"));
            String value = Convert.toStr(map.get("value"));
            if (value.equals(label)) {
                String[] keys = key.split(":");
                return Convert.convert(contentProperty.getField().getType(), keys[keys.length - 1]);
            }
        }
        if (contentProperty.getField().getType().isAssignableFrom(String.class)) {
            return label;
        }
        if (contentProperty.getField().getType().isAssignableFrom(Integer.class)) {
            return 0;
        }
        return Convert.convert(contentProperty.getField().getType(), label);
    }

    /**
     * 导出：这里是写的时候会调用 不用管
     */
    @Override
    public WriteCellData<String> convertToExcelData(Object object, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if (ObjectUtil.isNull(object)) {
            return new WriteCellData<>("");
        }
        Dict anno = getAnnotation(contentProperty.getField());
        String dictCode = anno.value();
        String code = Convert.toStr(object);
        if (StringUtil.isBlank(dictCode) || StringUtil.isBlank(code)) {
            return new WriteCellData<>(code);
        }
        String label = RedisUtil.getCacheObject(CacheConstants.SYS_DICT_KEY + dictCode + ":" + code);
        return new WriteCellData<>(label == null ? code : label);
    }

    private Dict getAnnotation(Field field) {
        return AnnotationUtil.getAnnotation(field, Dict.class);
    }
}
