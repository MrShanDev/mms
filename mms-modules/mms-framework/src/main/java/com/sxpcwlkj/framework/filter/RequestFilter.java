package com.sxpcwlkj.framework.filter;

import com.sxpcwlkj.framework.handler.HttpServletRequestWrapperHandler;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * 请求流转换为多次读取的请求流 过滤器
 *
 * @Author Xi jue
 */
@Slf4j
@WebFilter(filterName = "requestFilter", urlPatterns = {"/**"})
@Component
@Order(HIGHEST_PRECEDENCE + 1)  // 优先级最高
@RequiredArgsConstructor
public class RequestFilter implements Filter {

    @Value("${spring.profiles.active}")
    private String active;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        // 转换为可以多次获取流的request
        log.info("======HttpServletRequestWrapper======");
        //实现参数可以重复读取的功能
        HttpServletRequestWrapperHandler myRequest = new HttpServletRequestWrapperHandler((HttpServletRequest) request);
        // 全局请求接口  添加一个参数
        // myRequest.setParameter("parameter", "自定义请求参数");
        //放行
        chain.doFilter(myRequest, response);

    }

}
