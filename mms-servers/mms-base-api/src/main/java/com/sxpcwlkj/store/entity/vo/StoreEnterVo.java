package com.sxpcwlkj.store.entity.vo;


import com.sxpcwlkj.framework.entity.BaseEntityVo;
import com.sxpcwlkj.store.entity.StoreEnter;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 店铺入驻Vo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/

@Data
@AutoMapper(target = StoreEnter.class)
@EqualsAndHashCode(callSuper=false)
public class StoreEnterVo  extends BaseEntityVo{
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
    /**
     *  会员ID
     */
    private  String memberId;
	/**
	 * 公司名称
	 */
	private String companyAme;
	/**
	 * 公司详细地址
	 */
	private String companyAddress;
	/**
	 * 员工总数
	 */
	private Integer employeeNum;
	/**
	 * 公司电话
	 */
	private String companyPhone;
	/**
	 * 注册资金
	 */
	private Integer registeredCapital;
	/**
	 * 联系人姓名
	 */
	private String linkName;
	/**
	 * 联系人电话
	 */
	private String linkPhone;
	/**
	 * 电子邮箱
	 */
	private String companyEmail;
	/**
	 * 营业执照号
	 */
	private String licenseNum;
	/**
	 * 法定经营范围
	 */
	private String scope;
	/**
	 * 营业执照电子版
	 */
	private String licencePhoto;
	/**
	 * 法人姓名
	 */
	private String legalName;
	/**
	 * 法人证件号
	 */
	private String legalId;
	/**
	 * 法人证件电子版
	 */
	private String legalPhoto;
	/**
	 * 银行开户名
	 */
	private String settlementBankAccountName;
	/**
	 * 银行账号
	 */
	private String settlementBankAccountNum;
	/**
	 * 开户银行支行名称
	 */
	private String settlementBankBranchName;
	/**
	 * 支行联行号
	 */
	private String settlementBankjointName;
	/**
	 * 店铺名称
	 */
	private String storeName;
	/**
	 * 店铺logo
	 */
	private String storeLogo;
	/**
	 * 店铺简介
	 */
	private String storeDesc;
	/**
	 * 店铺经营类目
	 */
	private String goodsManagementCategory;
	/**
	 * 店铺所在地
	 */
	private String storeAddressIdPath;
	/**
	 * 店铺详细地址
	 */
	private String storeAddressDetail;

}
