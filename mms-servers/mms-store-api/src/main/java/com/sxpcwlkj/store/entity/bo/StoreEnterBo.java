package com.sxpcwlkj.store.entity.bo;

import com.sxpcwlkj.datasource.entity.BaseEntity;
import com.sxpcwlkj.framework.config.ValidatedGroupConfig;
import com.sxpcwlkj.store.entity.StoreEnter;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
* 店铺入驻Bo
*
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
*/
@Data
@AutoMapper(target = StoreEnter.class, reverseConvertGenerate = false)
@EqualsAndHashCode(callSuper=false)
public class StoreEnterBo  extends BaseEntity {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@NotBlank(message = "ID不能为空" ,groups = {ValidatedGroupConfig.update.class})
	private String id;
    /**
     *  会员ID
     */
    private  String memberId;
	/**
	 * 公司名称
	 */
	@NotBlank(message = "公司名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String companyAme;
	/**
	 * 公司详细地址
	 */
	@NotBlank(message = "公司详细地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String companyAddress;
	/**
	 * 员工总数
	 */
	@NotNull(message = "员工总数不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer employeeNum;
	/**
	 * 公司电话
	 */
	@NotBlank(message = "公司电话不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String companyPhone;
	/**
	 * 注册资金
	 */
	@NotNull(message = "注册资金不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private Integer registeredCapital;
	/**
	 * 联系人姓名
	 */
	@NotBlank(message = "联系人姓名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String linkName;
	/**
	 * 联系人电话
	 */
	@NotBlank(message = "联系人电话不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String linkPhone;
	/**
	 * 电子邮箱
	 */
	@NotBlank(message = "电子邮箱不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String companyEmail;
	/**
	 * 营业执照号
	 */
	@NotBlank(message = "营业执照号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String licenseNum;
	/**
	 * 法定经营范围
	 */
	@NotBlank(message = "法定经营范围不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String scope;
	/**
	 * 营业执照电子版
	 */
	@NotBlank(message = "营业执照电子版不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String licencePhoto;
	/**
	 * 法人姓名
	 */
	@NotBlank(message = "法人姓名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String legalName;
	/**
	 * 法人证件号
	 */
	@NotBlank(message = "法人证件号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String legalId;
	/**
	 * 法人证件电子版
	 */
	@NotBlank(message = "法人证件电子版不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String legalPhoto;
	/**
	 * 银行开户名
	 */
	@NotBlank(message = "银行开户名不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String settlementBankAccountName;
	/**
	 * 银行账号
	 */
	@NotBlank(message = "银行账号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String settlementBankAccountNum;
	/**
	 * 开户银行支行名称
	 */
	@NotBlank(message = "开户银行支行名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String settlementBankBranchName;
	/**
	 * 支行联行号
	 */
	@NotBlank(message = "支行联行号不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String settlementBankjointName;
	/**
	 * 店铺名称
	 */
	@NotBlank(message = "店铺名称不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String storeName;
	/**
	 * 店铺logo
	 */
	@NotBlank(message = "店铺logo不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String storeLogo;
	/**
	 * 店铺简介
	 */
	@NotBlank(message = "店铺简介不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String storeDesc;
	/**
	 * 店铺经营类目
	 */
	@NotBlank(message = "店铺经营类目不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String goodsManagementCategory;
	/**
	 * 店铺所在地
	 */
	@NotBlank(message = "店铺所在地不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String storeAddressIdPath;
	/**
	 * 店铺详细地址
	 */
	@NotBlank(message = "店铺详细地址不能为空" ,groups = {ValidatedGroupConfig.insert.class,ValidatedGroupConfig.update.class})
	private String storeAddressDetail;

    /**
     * 店铺审核理由
     */
    private String storeAuditWhy;
}
