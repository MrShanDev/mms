package com.sxpcwlkj.member.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.*;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.member.entity.StoreMember;
import com.sxpcwlkj.member.entity.StoreMemberAuthentication;
import com.sxpcwlkj.member.entity.bo.StoreMemberBo;
import com.sxpcwlkj.member.entity.bo.StoreMemberUpdateBo;
import com.sxpcwlkj.member.entity.export.StoreMemberExport;
import com.sxpcwlkj.member.entity.vo.StoreMemberVo;
import com.sxpcwlkj.member.mapper.StoreMemberAuthenticationMapper;
import com.sxpcwlkj.member.mapper.StoreMemberDistributionMapper;
import com.sxpcwlkj.member.mapper.StoreMemberMapper;
import com.sxpcwlkj.member.service.StoreMemberService;
import com.sxpcwlkj.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;


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
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreMemberBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreMember obj = MapstructUtil.convert(bo, StoreMember.class);
            assert obj != null;
            row = this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return row > 0;
        } catch (Exception e) {
            log.error("会员列表,insert 操作失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        try {
            String[] array = DataUtil.getCatStr(ids.toString(), ",");
            return this.getBaseMapper().deleteByIds(new ArrayList<>(List.of(array)))>0;
        } catch (Exception e) {
            log.error("会员列表,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreMemberBo bo) {
        try {
            int row;
            StoreMember obj = MapstructUtil.convert(bo, StoreMember.class);
            row = this.getBaseMapper().updateById(obj);
            return row > 0;
        } catch (Exception e) {
            log.error("会员列表,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreMemberVo selectVoById(Serializable id) {
        return this.getBaseMapper().selectVoById(id);

    }
    @Override
    public TableDataInfo<StoreMemberVo> selectListVoPage(StoreMemberBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreMember> lqw = buildQueryWrapper(bo);
        Page<StoreMemberVo> page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        return TableDataInfo.build(page);
    }

    private LambdaQueryWrapper<StoreMember> buildQueryWrapper(StoreMemberBo query){
        if(query==null){
            query=new StoreMemberBo();
        }
        LambdaQueryWrapper<StoreMember> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotEmpty(query.getAccount()), StoreMember::getAccount, query.getAccount());
        wrapper.eq(StringUtil.isNotEmpty(query.getPhone()), StoreMember::getPhone, query.getPhone());
        return wrapper;
    }

    @Override
    public Boolean imports(Set<StoreMemberExport> list) {
        return true;
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
//        StoreMemberVo memberVo = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreMember>().and(
//                wrapper -> {
//                    wrapper.eq(StoreMember::getAccount, account).or().eq(StoreMember::getPhone, account);
//                }
//        ).last("LIMIT 1"));
       StoreMemberVo memberVo = baseMapper.selectVoOne(new LambdaQueryWrapper<StoreMember>()
           .eq(StoreMember::getAccount, account).last("LIMIT 1"));
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
        if(bo.getType()==8){
            row = baseMapper.update(null,new LambdaUpdateWrapper<StoreMember>().eq(StoreMember::getId,bo.getMemberId()).set(StoreMember::getMemberBgImg,bo.getMemberBgImg()));
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
            bo.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
            bo.setCreatedTime(new Date());
            bo.setUpdatedTime(new Date());
            bo.setCreatedBy(0L);
            bo.setUpdatedBy(0L);
            storeMemberAuthenticationMapper.insert(bo);
        }
        return icrdInfo;

    }

    @Override
    public R<Object> updateEmail(String loginId, String email) {
        StoreMember storeMember = baseMapper.selectOne(new LambdaQueryWrapper<StoreMember>().eq(StoreMember::getId, loginId));
        if (storeMember == null) {
            return R.fail("用户不存在");
        }
        storeMember.setAccount(email);
        if(baseMapper.updateById(storeMember)>0){
            return R.success("修改成功");
        }
        return R.fail("修改失败");
    }

    @Override
    public R<Object> setPassword(String loginId, String password) {
        StoreMember storeMember = baseMapper.selectOne(new LambdaQueryWrapper<StoreMember>().eq(StoreMember::getId, loginId));
        if (storeMember == null) {
            return R.fail("用户不存在");
        }
        storeMember.setPassword(SecureUtil.md5(password));
        if(baseMapper.updateById(storeMember)>0){
            return R.success("设置成功");
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLocation(String loginId, Double latitude, Double longitude) {
        // 获取最新版本号
        StoreMember member = baseMapper.selectById(loginId);
        if (member != null) {
            member.setLatitude(latitude);
            member.setLongitude(longitude);
            // updateById 会自动处理 @Version 字段 (revision)
            // 如果版本冲突，它会返回 0 (更新失败)
            int rows = baseMapper.updateById(member);
            if (rows == 0) {
                log.warn("更新用户经纬度失败，可能存在并发修改 (ID: {}, Revision: {})", loginId, member.getRevision());
            }
        }
    }
}
