package com.sxpcwlkj.mobile.service;

import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.framework.sercice.BaseService;
import com.sxpcwlkj.mobile.entity.StoreMember;
import com.sxpcwlkj.mobile.entity.bo.StoreMemberBo;
import com.sxpcwlkj.mobile.entity.bo.StoreMemberUpdateBo;
import com.sxpcwlkj.mobile.entity.vo.StoreMemberVo;

import java.util.Map;

/**
 * 店铺会员;
 *
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-01-30
 */
public interface StoreMemberService extends BaseService<StoreMember, StoreMemberVo, StoreMemberBo> {

    /**
     * 根据手机号查会员
     * @param phoneNumber
     * @return
     */
    StoreMemberVo selectVoByPhone(String phoneNumber);
    /**
     * 根据openid号查会员
     * @param openId
     * @return
     */
    StoreMemberVo selectVoByOpenId(String openId);
    /**
     * 根据账号
     * @param account
     * @return
     */
    StoreMemberVo selectVoByAccount(String account);

    /**
     * 根据邀请码
     * @param code
     * @return
     */
    StoreMemberVo selectVoByInvitationCode(String code);

    /**
     * 生成一个可用的邀请码
     * @return
     */
    String getInvitationCode();

    R<String> updateMember(StoreMemberUpdateBo bo);

    /**
     * 实名认证
     * @param a 正面
     * @param b 背面
     * @return
     */
    R<Map<String, String>> authentication(String a, String b);
}
