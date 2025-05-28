package com.sxpcwlkj.mobile.service.impl;

import cn.hutool.core.convert.Convert;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.*;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;

import com.sxpcwlkj.framework.sercice.impl.BaseServiceImpl;


import com.sxpcwlkj.mobile.entity.StoreMember;
import com.sxpcwlkj.mobile.entity.StoreMemberAuthentication;
import com.sxpcwlkj.mobile.entity.StoreMemberDistribution;
import com.sxpcwlkj.mobile.entity.bo.StoreMemberBo;
import com.sxpcwlkj.mobile.entity.bo.StoreMemberUpdateBo;
import com.sxpcwlkj.mobile.entity.vo.StoreMemberVo;
import com.sxpcwlkj.mobile.mapper.StoreMemberDistributionMapper;
import com.sxpcwlkj.mobile.mapper.StoreMemberMapper;
import com.sxpcwlkj.mobile.mapper.StoreMemberAuthenticationMapper;
import com.sxpcwlkj.mobile.service.StoreMemberService;
import com.sxpcwlkj.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;
import java.util.Map;


/**
 * 店铺会员;
 *
 * @author 西决 942879858@qq.com
 * @since 1.0.0 2024-01-30
 */
@Slf4j
@Service("store_member")
@RequiredArgsConstructor
public class StoreMemberServiceImpl extends BaseServiceImpl<StoreMember, StoreMemberVo, StoreMemberBo> implements StoreMemberService {

    private final StoreMemberMapper baseMapper;
    private final StoreMemberDistributionMapper storeMemberDistributionMapper;
    private final StoreMemberAuthenticationMapper storeMemberAuthenticationMapper;
    private final Environment environment;

    @Override
    public BaseMapperPlus<StoreMember, StoreMemberVo> getBaseMapper() {
        return baseMapper;
    }


    @Override
    public Boolean insert(StoreMemberBo bo) {
        int row = 0;

        String invitationCode= bo.getInvitationCode();

        // 生成一个可用的邀请码
        String code = getInvitationCode();
        bo.setInvitationCode(code);
        StoreMember obj = MapstructUtil.convert(bo, StoreMember.class);
        row = this.getBaseMapper().insert(obj);

        //关系记录

        if(StringUtil.isNotEmpty(invitationCode)){
            StoreMemberVo isInvitationCode = this.selectVoByInvitationCode(invitationCode);
            if(isInvitationCode!=null){
                StoreMemberDistribution memberDistribution=new StoreMemberDistribution();
                memberDistribution.setMemberId(obj.getId());
                memberDistribution.setReferrerId(isInvitationCode.getId());
                memberDistribution.setFatherIdOne(isInvitationCode.getId());
                storeMemberDistributionMapper.insert(memberDistribution);
            }
        }

        return row > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable id) {
        return this.getBaseMapper().deleteById(id) > 0;
    }

    @Override
    public Boolean updateById(StoreMemberBo bo) {
        int row = 0;
        StoreMember obj = MapstructUtil.convert(bo, StoreMember.class);
        row = this.getBaseMapper().updateById(obj);
        return row > 0;
    }

    @Override
    public StoreMemberVo selectVoById(Serializable id) {
        StoreMemberVo memberVo = this.getBaseMapper().selectVoById(id);
        StoreMemberAuthentication authentication = storeMemberAuthenticationMapper.selectOne(new LambdaQueryWrapper<StoreMemberAuthentication>().eq(StoreMemberAuthentication::getMemberId, memberVo.getId()).last("LIMIT 1"));
        if (authentication != null) {
            memberVo.setAuthentication(authentication);
        }
        return memberVo;
    }

    @Override
    public TableDataInfo<StoreMemberVo> selectListVoPage(StoreMemberBo bo, PageQuery pageQuery) {
        Page<StoreMemberVo> page = baseMapper.listPageXml(pageQuery.build(), this.buildQueryWrapper(bo).getCustomSqlSegment());
        return TableDataInfo.build(page);
    }


    private Wrapper<StoreMember> buildQueryWrapper(StoreMemberBo query) {

        LambdaQueryWrapper<StoreMember> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getId()), StoreMember::getId, query.getId());
        wrapper.eq(StringUtil.isNotEmpty(query.getAccount()), StoreMember::getAccount, query.getAccount());
        wrapper.eq(StringUtil.isNotEmpty(query.getSex()), StoreMember::getSex, query.getSex());
        wrapper.eq(StringUtil.isNotEmpty(query.getPhone()), StoreMember::getPhone, query.getPhone());
        wrapper.eq(StringUtil.isNotEmpty(query.getWxOpenid()), StoreMember::getWxOpenid, query.getWxOpenid());
        wrapper.eq(StringUtil.isNotEmpty(query.getLevel()), StoreMember::getLevel, query.getLevel());
        wrapper.eq(StringUtil.isNotEmpty(query.getInvitationCode()), StoreMember::getInvitationCode, query.getInvitationCode());
        wrapper.eq(StringUtil.isNotEmpty(query.getStatus()), StoreMember::getStatus, query.getStatus());
        return wrapper;
    }

    @Override
    public StoreMemberVo selectVoByPhone(String phoneNumber) {
        StoreMemberVo memberVo = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreMember>().eq(StoreMember::getPhone, phoneNumber).last("LIMIT 1"));
        if(memberVo==null){
            return memberVo;
        }
        StoreMemberAuthentication authentication = storeMemberAuthenticationMapper.selectOne(new LambdaQueryWrapper<StoreMemberAuthentication>().eq(StoreMemberAuthentication::getMemberId, memberVo.getId()).last("LIMIT 1"));
        if (authentication != null) {
            memberVo.setAuthentication(authentication);
        }
        return memberVo;
    }

    @Override
    public StoreMemberVo selectVoByOpenId(String openId) {
        StoreMemberVo memberVo = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreMember>().eq(StoreMember::getWxOpenid, openId).last("LIMIT 1"));
        if(memberVo==null){
            return memberVo;
        }
        StoreMemberAuthentication authentication = storeMemberAuthenticationMapper.selectOne(new LambdaQueryWrapper<StoreMemberAuthentication>().eq(StoreMemberAuthentication::getMemberId, memberVo.getId()).last("LIMIT 1"));
        if (authentication != null) {
            memberVo.setAuthentication(authentication);
        }
        return memberVo;
    }

    @Override
    public StoreMemberVo selectVoByAccount(String account) {
        StoreMemberVo memberVo = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreMember>().and(
                wrapper -> {
                    wrapper.eq(StoreMember::getAccount, account).or().eq(StoreMember::getPhone, account);
                }
        ).last("LIMIT 1"));
        if(memberVo==null){
            throw new MmsException("账号不存在!");
        }
        StoreMemberAuthentication authentication = storeMemberAuthenticationMapper.selectOne(new LambdaQueryWrapper<StoreMemberAuthentication>().eq(StoreMemberAuthentication::getMemberId, memberVo.getId()).last("LIMIT 1"));
        if (authentication != null) {
            memberVo.setAuthentication(authentication);
        }
        return memberVo;
    }

    @Override
    public StoreMemberVo selectVoByInvitationCode(String code) {
        StoreMemberVo memberVo = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreMember>().eq(StoreMember::getInvitationCode, code).last("LIMIT 1"));
        if (memberVo==null){
            return memberVo;
        }
        StoreMemberAuthentication authentication = storeMemberAuthenticationMapper.selectOne(new LambdaQueryWrapper<StoreMemberAuthentication>().eq(StoreMemberAuthentication::getMemberId, memberVo.getId()).last("LIMIT 1"));
        if (authentication != null) {
            memberVo.setAuthentication(authentication);
        }
        return memberVo;
    }

    @Override
    public String getInvitationCode() {
        String code = RandomUtil.getInvitationCode();
        StoreMemberVo vo = this.selectVoByInvitationCode(code);
        if(vo!=null){
            getInvitationCode();
        }
        return code;
    }

    @Override
    public R<String> updateMember(StoreMemberUpdateBo bo) {
        //1:手机号 2:密码 3:昵称 4:头像 5:性别 6:账号 7:生日
        int row = 0;
        if(bo.getType()==1){
            row = baseMapper.update(null,new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getId,bo.getMemberId()).set(StoreMember::getPhone,bo.getPhone()));
        }
        if(bo.getType()==2){
            row = baseMapper.update(null,new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getId,bo.getMemberId()).set(StoreMember::getPassword,bo.getPassword()));
        }
        if(bo.getType()==3){
            row = baseMapper.update(null,new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getId,bo.getMemberId()).set(StoreMember::getNickname,bo.getNickname()));
        }
        if(bo.getType()==4){
            row = baseMapper.update(null,new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getId,bo.getMemberId()).set(StoreMember::getHeadPortrait,bo.getHeadPortrait()));
        }
        if(bo.getType()==5){
            row = baseMapper.update(null,new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getId,bo.getMemberId()).set(StoreMember::getSex,bo.getSex()));
        }
        if(bo.getType()==6){
            row = baseMapper.update(null,new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getId,bo.getMemberId()).set(StoreMember::getAccount,bo.getAccount()));
        }
        if(bo.getType()==7){
            row = baseMapper.update(null,new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getId,bo.getMemberId()).set(StoreMember::getBirthday,bo.getBirthday()));
        }

        if(row>0){
            return R.success("修改成功");
        }

        return null;
    }

    @Override
    public R<Map<String, String>> authentication(String a, String b) {
        // 获取身份证信息
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);

        StoreMemberAuthentication selectOne = storeMemberAuthenticationMapper.selectOne(new LambdaQueryWrapper<StoreMemberAuthentication>()
                .eq(StoreMemberAuthentication::getMemberId, storeMember.getId()));

        if (selectOne != null) {
            return R.fail("您已经认证过了");
        }

        if (environment.getProperty("spring.profiles.active").equals("prod")) {
            try {
                log.info("authentication:" + storeMember.getId());
                Object s = RedisUtil.getCacheObject("authentication:" + storeMember.getId());
                int num = Convert.toInt(s == null ? 0 : Convert.toInt(s), 0);
                if (num >= 5) {
                    log.info("实名认证>=5,拦截:" + storeMember.getId());
                    return R.fail("操作频繁,请24小时后再试哦！");
                }
                num++;
                RedisUtil.setCacheObject("authentication:" + storeMember.getId(), num);
                RedisUtil.setCacheObject("authentication:" + storeMember.getId(), num, Duration.ofDays(1));
            } catch (Exception e) {
                throw new MmsException("实名认证失败,请联系管理员");
            }
        }

        R<Map<String, String>> icrdInfo = OcrIdcardUtil.getIcrdInfo(a, storeMember.getPhone());
        if (icrdInfo.getCode() == 200) {
            Map<String, String> data = icrdInfo.getData();
            String name = data.get("name");
            String number = data.get("num");
            String address = data.get("address");
            String nationality = data.get("nationality");
            String sex = data.get("sex");
            StoreMemberAuthentication bo=new StoreMemberAuthentication();
            bo.setMemberId(storeMember.getId());
            bo.setName(name);
            bo.setNumber(number);
            bo.setAddress(address);
            bo.setSex(sex);
            bo.setNationality(nationality);
            bo.setPhone(storeMember.getPhone());
            bo.setImageFront(a);
            bo.setImageBack(b);
            bo.setStatus(1);
            bo.setCreatedTime(new Date());
            bo.setUpdatedTime(new Date());
            bo.setCreatedBy(0L);
            bo.setUpdatedBy(0L);
            storeMemberAuthenticationMapper.insert(bo);
        }
        return icrdInfo;

    }
}
