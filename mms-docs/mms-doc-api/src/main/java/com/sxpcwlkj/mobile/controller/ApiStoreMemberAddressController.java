package com.sxpcwlkj.mobile.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.code.controller.BaseController;
import com.sxpcwlkj.common.utils.R;

import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.mobile.entity.StoreMember;
import com.sxpcwlkj.mobile.entity.StoreMemberAddress;

import com.sxpcwlkj.mobile.entity.bo.StoreMemberAddressBo;
import com.sxpcwlkj.mobile.entity.vo.StoreMemberAddressVo;
import com.sxpcwlkj.mobile.service.StoreMemberAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
* 会员收件地址
*
* @author 西决 942879858@qq.com
* @since 1.0.0 2024-05-11
*/
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/member/address")
public class ApiStoreMemberAddressController extends BaseController{
    private final StoreMemberAddressService baseService;

    /**
     * 地址列表-分页查询
     *
     * @param pageQuery
     * @return
     */
    @SaCheckLogin
    @PostMapping("/list")
    public R<TableDataInfo<StoreMemberAddressVo>> listPage(PageQuery pageQuery){
        StoreMemberAddressBo bo=new StoreMemberAddressBo();
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        bo.setMemberId(storeMember.getId());
         return R.success(baseService.selectListVoPage(bo, pageQuery));
    }

    /**
     * 地址详情
     *
     * @param id 主键ID
     * @return
     */
    @SaCheckLogin
    @GetMapping("/{id}")
    public R<StoreMemberAddressVo> queryById(@PathVariable String id) {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        return success(baseService.selectVoByIdAndMid(id, storeMember.getId()));
    }

    /**
     * 地址编辑
     *
     * @param bo
     * @return
     */
    @SaCheckLogin
    @PostMapping("/edit")
    public R<Boolean> edit(@Validated @RequestBody(required = false) StoreMemberAddressBo bo) {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        bo.setMemberId(storeMember.getId());
         // 如果是默认地址，则将之前的默认地址设为非默认
        if(bo.getTolerant().equals("1")){
            baseService.update(new LambdaUpdateWrapper<StoreMemberAddress>()
                    .eq(StoreMemberAddress::getMemberId,bo.getMemberId())
                    .set(StoreMemberAddress::getTolerant,0));
        }
        return success(baseService.updateById(bo));
    }

    /**
     * 地址设为默认
     *
     * @param id 地址ID
     * @return
     */
    @SaCheckLogin
    @GetMapping("/updateDef")
    public R<Boolean> updateDef( String id) {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        StoreMemberAddressVo storeMemberAddressVo = baseService.selectVoByIdAndMid(id, storeMember.getId());
        if(storeMemberAddressVo!=null){

            baseService.update(new LambdaUpdateWrapper<StoreMemberAddress>()
                    .eq(StoreMemberAddress::getMemberId, storeMember.getId())
                    .set(StoreMemberAddress::getTolerant,0));

            baseService.update(new LambdaUpdateWrapper<StoreMemberAddress>()
                    .eq(StoreMemberAddress::getId,id)
                    .set(StoreMemberAddress::getTolerant,1));
            return success(true);
        }
        return fail("修改失败");
    }

    /**
     * 地址新增
     *
     * @param bo
     * @return
     */
    @SaCheckLogin
    @PostMapping("/insert")
    public R<Boolean> insert(@Validated @RequestBody(required = false) StoreMemberAddressBo bo) {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        bo.setMemberId(storeMember.getId());
        bo.setStatus(0);
        bo.setId(null);
        bo.setMemberId(storeMember.getId());
        // 如果是默认地址，则将之前的默认地址设为非默认
        if(bo.getTolerant().equals("1")){
            baseService.update(new LambdaUpdateWrapper<StoreMemberAddress>()
                    .eq(StoreMemberAddress::getMemberId,bo.getMemberId())
                    .set(StoreMemberAddress::getTolerant,0));
        }
        return success(baseService.insert(bo));
    }

    /**
     * 地址删除
     *
     * @param id 主键ID
     * @return
     */
    @SaCheckLogin
    @GetMapping("/delete/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        StoreMember storeMember = LoginObject.getLoginObject(StoreMember.class);
        return success(baseService.deleteByIdAndMid(id, storeMember.getId()));
    }

}
