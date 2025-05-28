package com.sxpcwlkj.docApi.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.sxpcwlkj.docApi.entity.DocUser;
import com.sxpcwlkj.docApi.entity.vo.DocUserVo;
import com.sxpcwlkj.docApi.service.DocUserService;
import com.sxpcwlkj.docApi.utils.DocBaseTool;
import com.sxpcwlkj.docApi.utils.DocR;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/vpapi/meb")
public class DocUserController extends DocBaseTool {

    private final DocUserService docUserService;

    @SaIgnore
    @PostMapping("/userinfo")
    public DocR<DocUserVo> userinfo(HttpServletRequest request){
        DocUserVo docUserVo = docUserService.selectVoById("1");
        docUserVo.setVip_date("2025-12-12");
        return DocR.ok(docUserVo);
    }

}
