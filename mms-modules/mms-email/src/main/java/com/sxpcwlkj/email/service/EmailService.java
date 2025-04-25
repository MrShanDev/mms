package com.sxpcwlkj.email.service;


import com.sxpcwlkj.common.utils.R;

/**
 * @author shanpengnian
 */
public interface EmailService {

    R<Object> sendEmailCode(String email, String code);
}
