package com.sxpcwlkj.common.utils;


import com.sxpcwlkj.common.exception.MmsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @ClassName ValidatorUtil
 * @Description 验证工具
 * @Author mmsAdmin
 * @Date 2023/1/23 20:48
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtil {
    private static final Validator VALIDATOR;

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static void validateEntity(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> validate = VALIDATOR.validate(object, groups);
        List<String> list = new ArrayList<>();
        for(ConstraintViolation<Object> aa :validate) {
            throw new MmsException(aa.getMessage());
        }
    }
}
