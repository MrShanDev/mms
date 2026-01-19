package com.sxpcwlkj.store.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxpcwlkj.authority.LoginObject;
import com.sxpcwlkj.common.enums.SystemCommonEnum;
import com.sxpcwlkj.common.exception.MmsException;
import com.sxpcwlkj.common.utils.DataUtil;
import com.sxpcwlkj.common.utils.MapstructUtil;
import com.sxpcwlkj.common.utils.StringUtil;
import com.sxpcwlkj.datasource.entity.page.PageQuery;
import com.sxpcwlkj.datasource.entity.page.TableDataInfo;
import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import com.sxpcwlkj.framework.service.impl.BaseServiceImpl;
import com.sxpcwlkj.store.config.DroneProductTokenizer;
import com.sxpcwlkj.store.entity.*;
import com.sxpcwlkj.store.entity.bo.SkuBo;
import com.sxpcwlkj.store.entity.bo.SkuDataBo;
import com.sxpcwlkj.store.entity.bo.SkuValuesBo;
import com.sxpcwlkj.store.entity.bo.StoreProductSpuBo;
import com.sxpcwlkj.store.entity.export.StoreProductSpuExport;
import com.sxpcwlkj.store.entity.vo.*;
import com.sxpcwlkj.store.enums.FileType;
import com.sxpcwlkj.store.mapper.*;
import com.sxpcwlkj.store.service.StoreProductSpuService;
import com.sxpcwlkj.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * 店铺商品-接口实现
 *
* @author mmsAdmin
* @Doc <a href='https://www.mmsadmin.com'>MMS文档</a>
 */
@Slf4j
@Service("store_product_spu")
@RequiredArgsConstructor
// 使用类级别事务注解，确保所有方法都在事务中执行
@Transactional(rollbackFor = Exception.class)
public class StoreProductSpuServiceImpl extends BaseServiceImpl<StoreProductSpu, StoreProductSpuVo, StoreProductSpuBo> implements StoreProductSpuService {

   private final StoreProductSpuMapper baseMapper;
   private final StoreProductCateMapper storeProductCateMapper;
   private final StoreProductBrandMapper storeProductBrandMapper;
   private final StoreAttrKeySpuMapper storeAttrKeySpuMapper;
   private final StoreAttrValueSpuMapper storeAttrValueSpuMapper;
   private final StoreProductSkuMapper storeProductSkuMapper;
   private final StoreAttrKeyMapper storeAttrKeyMapper;
   private final StoreAttrValueMapper storeAttrValueMapper;
   private final StoreService storeService;
   private final DroneProductTokenizer droneTokenizer;

    @Override
    public BaseMapperPlus<StoreProductSpu, StoreProductSpuVo> getBaseMapper() {
        return baseMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insert(StoreProductSpuBo bo) {
        try {
            int row;
            bo.setId(null);
            StoreProductSpu obj = MapstructUtil.convert(bo, StoreProductSpu.class);
            assert obj != null;
            obj.setStoreId(LoginObject.getLoginTenant());
            this.getBaseMapper().insert(obj);
            bo.setId(obj.getId());
            return spuSkuSave(obj.getId(),bo.getMarketPrice(),bo.getSkuList());
        } catch (Exception e) {
            log.error("店铺商品,insert 操作失败", e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Serializable ids) {
        try {
            String[] array = DataUtil.getCatStr(ids.toString(), ",");
            List<String> idList = new ArrayList<>(List.of(array));

            // 先删除关联的SKU和属性数据
            for (String spuId : idList) {
                // 删除商品+规格项
                storeAttrKeySpuMapper.delete(new LambdaQueryWrapper<StoreAttrKeySpu>().eq(StoreAttrKeySpu::getSpuId, spuId));
                // 删除商品+规格项值
                storeAttrValueSpuMapper.delete(new LambdaQueryWrapper<StoreAttrValueSpu>().eq(StoreAttrValueSpu::getSpuId, spuId));
                // 删除商品SKU
                storeProductSkuMapper.delete(new LambdaQueryWrapper<StoreProductSku>().eq(StoreProductSku::getSpuId, spuId));
            }

            // 最后删除SPU
            return this.getBaseMapper().deleteByIds(idList) > 0;
        } catch (Exception e) {
            log.error("店铺商品,deleteById 操作失败", e);
            throw e;
        }
    }

    @Override
    public Boolean updateById(StoreProductSpuBo bo) {
        try {
            int row;
            StoreProductSpu obj = MapstructUtil.convert(bo, StoreProductSpu.class);
            assert obj != null;
//            obj.setStoreId(LoginObject.getLoginTenant());
            this.getBaseMapper().updateById(obj);
            return spuSkuSave(obj.getId(),bo.getMarketPrice(),bo.getSkuList());
        } catch (Exception e) {
            log.error("店铺商品,updateById 操作失败", e);
            throw e;
        }
    }

    @Override
    public StoreProductSpuVo selectVoById(Serializable id) {
        StoreProductSpuVo vo= this.getBaseMapper().selectVoById(id);
            List<String> end= new ArrayList<>();
            getIds(end,vo.getCateId());
            Collections.reverse(end);
            vo.setCateIds(end.toArray(new String[]{}));
            List<StoreAttrKey> attrKeys = storeAttrKeyMapper.selectListSpuId(id);
            if (attrKeys == null|| attrKeys.isEmpty()) {


                StoreAttrKeySpu  attrKeySpu = new StoreAttrKeySpu();
                attrKeySpu.setAttrKeyId("1");
                attrKeySpu.setSpuId(id+"");
                attrKeySpu.setStatus(1);
                attrKeySpu.setSort(1);
                storeAttrKeySpuMapper.insert(attrKeySpu);


                StoreAttrValueSpu  attrValueSpu = new StoreAttrValueSpu();
                attrValueSpu.setAttrKeyId("1");
                attrValueSpu.setAttrValueId("1");
                attrValueSpu.setSpuId(id+"");
                attrValueSpu.setStatus(1);
                attrValueSpu.setSort(1);
                storeAttrValueSpuMapper.insert(attrValueSpu);

                StoreProductSku  sku = new StoreProductSku();
                sku.setSkuCode(attrValueSpu.getId());
                sku.setSpuId(id+"");
                sku.setPrice(vo.getMarketPrice());
                sku.setCostPrice(vo.getMinPrice()==null?vo.getMarketPrice():vo.getMinPrice());
                sku.setSkuCode(attrValueSpu.getAttrValueId());
                sku.setSkuName("单品");
                sku.setStock(999999999);
                sku.setSort(1);
                sku.setStatus(1);
                storeProductSkuMapper.insert(sku);
                attrKeys = storeAttrKeyMapper.selectListSpuId(id);
            }
            List<StoreAttrKeyVo> attrKeyVos = MapstructUtil.convert(attrKeys, StoreAttrKeyVo.class);

            assert attrKeyVos != null;
            attrKeyVos.forEach(attrKey -> {
                // 添加null检查，防止空指针异常
                if (attrKey == null) {
                    return;
                }
                attrKey.setIsImage(0);
                List<StoreAttrValue> attrValues = storeAttrValueMapper.selectListSpuId(id,attrKey.getId());
                List<StoreAttrValueVo> attrValueVos = MapstructUtil.convert(attrValues, StoreAttrValueVo.class);
                if (attrValueVos != null) {
                    for (StoreAttrValueVo attrValueVo : attrValueVos) {
                        StoreAttrValueSpuVo attrValueSpu = storeAttrValueSpuMapper.selectVoOne(new LambdaQueryWrapper<StoreAttrValueSpu>()
                            .eq(StoreAttrValueSpu::getSpuId, id)
                            .eq(StoreAttrValueSpu::getAttrValueId, attrValueVo.getId())
                            .eq(StoreAttrValueSpu::getAttrKeyId, attrKey.getId())
                            .last("limit 1"));
                        if (attrValueSpu != null&& StringUtil.isNotBlank(attrValueSpu.getAttrImage())) {
                            attrValueVo.setColor(attrValueSpu.getAttrImage());
                            attrKey.setIsImage(1);
                        }
                    }
                    attrValueVos.add(new StoreAttrValueVo(attrKey.getId()));
                }else {
                    attrValueVos= new ArrayList<>();
                    attrValueVos.add(new StoreAttrValueVo(attrKey.getId()));
                }

                attrKey.setAttrValueList(attrValueVos);
            });

            //  封装SKU项和值
            vo.setAttrValueList(attrKeyVos);
            List<StoreProductSku>  skus = storeProductSkuMapper.selectListSpuId(id);
            List<StoreProductSkuVo> skuVos = MapstructUtil.convert(skus, StoreProductSkuVo.class);
            List<SkuBo> skuBos = new ArrayList<>();
            assert skuVos != null;
            for (StoreProductSkuVo skuVo : skuVos) {
                SkuBo skuBo = new SkuBo();
                skuBo.setId(skuVo.getSkuCode());
                String[] ids = skuVo.getSkuCode().split("_");
                List<SkuValuesBo> values = new ArrayList<>();
                for (String valueId : ids) {
                    StoreAttrValueSpuVo attrValueSpu = storeAttrValueSpuMapper.selectVoOne(new  LambdaQueryWrapper<StoreAttrValueSpu>()
                        .eq(StoreAttrValueSpu::getSpuId, id)
                        .eq(StoreAttrValueSpu::getAttrValueId,valueId)
                        .last("limit 1")
                    );
                    if (attrValueSpu != null) {
                        StoreAttrKeyVo  attrKeyVo = storeAttrKeyMapper.selectVoById(attrValueSpu.getAttrKeyId());
                        StoreAttrValueVo attrValueVo = storeAttrValueMapper.selectVoById(attrValueSpu.getAttrValueId());
                        SkuValuesBo skuValuesBo = new SkuValuesBo();
                        skuValuesBo.setKeyId(attrValueSpu.getAttrKeyId());
                        skuValuesBo.setValueId(attrValueSpu.getAttrValueId());
                        skuValuesBo.setKeyName(attrKeyVo==null?"":attrKeyVo.getName());
                        skuValuesBo.setValueName(attrValueVo==null?"":attrValueVo.getValue());
                        skuValuesBo.setColor(attrValueVo==null?"":attrValueVo.getColor());
                        values.add(skuValuesBo);
                    }

                }
                skuBo.setValues(values);

                SkuDataBo data = new SkuDataBo();
                data.setTitle(skuVo.getSkuName());
                data.setPrice(skuVo.getCostPrice());
                data.setWeight(skuVo.getWeight());
                data.setInventory(skuVo.getStock());
                data.setCode(skuVo.getCode());
                data.setOthers(skuVo.getMainImage());

                skuBo.setData(data);

                skuBos.add(skuBo);
            }
            vo.setSkuList(skuBos);

            vo.setMinPrice(selectMinPrice(vo.getId(),vo.getMarketPrice()));
            List<SpuFileVo> fileVos=new ArrayList<>();
            if(vo.getDescribeTwo()!=null&&!vo.getDescribeTwo().isEmpty()){
                SpuFileVo spuFileVo=new SpuFileVo();
                spuFileVo.setFileType(FileType.fromFilePath(vo.getDescribeTwo()));
                spuFileVo.setFileUrl(vo.getDescribeTwo());
                spuFileVo.setVideoImg(vo.getMainImage());
                fileVos.add(spuFileVo);
            }
            String[] listImages = DataUtil.getStrToStrArry(vo.getListImages());
            for (String listImage : listImages) {
                if("".equals(listImage)){
                    continue;
                }
                SpuFileVo fileVo=new SpuFileVo();
                fileVo.setFileType(FileType.fromFilePath(listImage));
                fileVo.setFileUrl(listImage);
                fileVos.add(fileVo);
            }
            if(fileVos.isEmpty()){
                SpuFileVo fileVo=new SpuFileVo();
                fileVo.setFileType(FileType.fromFilePath(vo.getMainImage()));
                fileVo.setFileUrl(vo.getMainImage());
                fileVos.add(fileVo);
            }
            vo.setFileList(fileVos);
            vo.setIfSelfSupport(vo.getStoreId().equals("1977670285589168129"));
            try {
                StoreVo storeVo= storeService.selectVoById(vo.getStoreId());
                Map<String, Object> map = new HashMap<>();
                map.put("title",storeVo.getStoreName());
                map.put("bgImg",storeVo.getStoreBgImg());
                map.put("phone",storeVo.getStorePhone());
                map.put("storeScore",storeVo.getStoreScore());
                map.put("storeRank",storeVo.getStoreRank());
                map.put("storeImgOne",storeVo.getStoreImgOne());
                map.put("logo",storeVo.getStoreLogo());
                vo.setStoreInfo(map);
            }catch (Exception e){
                log.error("店铺信息查询失败",e);
            }

        return vo;
    }
    private void getIds(List<String> end, String id) {
        StoreProductCateVo vo = storeProductCateMapper.selectVoById(id);
        if (vo != null) {
            end.add(vo.getId());
            getIds(end, vo.getParentId());
        }
    }
    @Override
    public TableDataInfo<StoreProductSpuVo> selectListVoPage(StoreProductSpuBo bo, PageQuery pageQuery) {
        Page<StoreProductSpuVo> page=null;
        if(StrUtil.isNotBlank(bo.getCateId())){
            if(StrUtil.isBlank(bo.getStoreId())){
                bo.setStoreId(null);
            }
            if(StrUtil.isBlank(bo.getBrandId())){
                bo.setBrandId(null);
            }
            if(StrUtil.isBlank(bo.getTitle())){
                bo.setTitle(null);
            }
            page = baseMapper.selectVoPageXmlAdmin(pageQuery.build(),bo.getStoreId(),bo.getCateId(), bo.getBrandId(), bo.getTitle(),bo.getStatus());
        }else {
            LambdaQueryWrapper<StoreProductSpu> lqw = buildQueryWrapper(bo);
            page = baseMapper.selectVoPage(pageQuery.build(),lqw);
        }
        return TableDataInfo.build(page(page));
    }

    @Override
    public TableDataInfo<StoreProductSpuVo> selectListVoPageXml(StoreProductSpuBo bo, PageQuery pageQuery) {
        Page<StoreProductSpuVo> page=baseMapper.selectVoPageXml(pageQuery.build(),bo.getCateId());
        return TableDataInfo.build(page(page));
    }

    private Page<StoreProductSpuVo> page(Page<StoreProductSpuVo> page){
        for (StoreProductSpuVo vo:page.getRecords()){
            StoreProductBrandVo brandVo= storeProductBrandMapper.selectVoById(vo.getBrandId());
            if(brandVo!=null){
                vo.setBrandName(brandVo.getName());
            }
            vo.setMinPrice(selectMinPrice(vo.getId(),vo.getMarketPrice()));

            List<SpuFileVo> fileVos=new ArrayList<>();
            SpuFileVo spuFileVo=new SpuFileVo();
            spuFileVo.setFileType(FileType.fromFilePath(vo.getMainImage()));
            spuFileVo.setFileUrl(vo.getMainImage());
            fileVos.add(spuFileVo);
            vo.setFileList(fileVos);
            vo.setIfSelfSupport(vo.getStoreId().equals("1977670285589168129"));
        }
        return page;
    }


    private LambdaQueryWrapper<StoreProductSpu> buildQueryWrapper(StoreProductSpuBo query){
        if(query==null){
            query=new StoreProductSpuBo();
        }
        LambdaQueryWrapper<StoreProductSpu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StringUtil.isNotBlank(query.getStoreId()),StoreProductSpu::getStoreId,query.getStoreId());
        wrapper.eq(StringUtil.isNotBlank(query.getCateId()),StoreProductSpu::getCateId,query.getCateId());
        wrapper.eq(StringUtil.isNotBlank(query.getBrandId()),StoreProductSpu::getBrandId,query.getBrandId());
        wrapper.eq(StringUtil.isNotEmpty(query.getStatus()),StoreProductSpu::getStatus,query.getStatus());
        // 扩展：支持分词搜索
        if (StringUtil.isNotBlank(query.getTitle())) {
            buildTitleSearchCondition(wrapper, query.getTitle());
        }
        if(StrUtil.isNotBlank(query.getSubTitle())){
            wrapper.like(StoreProductSpu::getTitle, query.getSubTitle());
        }
        wrapper.orderByDesc(StoreProductSpu::getSort);
        return wrapper;
    }

    /**
     * 使用 IK Analyzer 的标题搜索条件
     */
    private void buildTitleSearchCondition(LambdaQueryWrapper<StoreProductSpu> wrapper, String title) {
        List<String> tokens = droneTokenizer.smartTokenize(title);

        if (tokens.isEmpty()) {
            wrapper.like(StoreProductSpu::getTitle, title);
            return;
        }

        if (tokens.size() == 1) {
            wrapper.like(StoreProductSpu::getTitle, tokens.get(0));
            return;
        }

        // 创建 final 引用
        final List<String> finalTokens = tokens.size() > 8 ?
            new ArrayList<>(tokens.subList(0, 8)) : new ArrayList<>(tokens);

        wrapper.and(w -> {
            // 完全匹配原始词
            w.like(StoreProductSpu::getTitle, title);

            // 分词匹配
            for (String token : finalTokens) {
                w.or().like(StoreProductSpu::getTitle, token);
            }
        });
    }

    @Override
    public Boolean imports(Set<StoreProductSpuExport> list) {
        return true;
    }

    @Override
    public List<StoreProductSpuVo> selectVoByCateId(String spuCateId,Integer limit) {
        return baseMapper.selectVoByCateId(spuCateId,limit);
    }

    @Override
    public void checkOrder(String spuId, String skuId, Integer num) {
        //验证逻辑
        //商品SKU存在性检查：确保商品SKU存在且未下架。
        StoreProductSpuVo spu = baseMapper.selectVoById(spuId);
        if(spu==null){
            log.error("商品不存在");
            throw new MmsException("商品不存在");
        }
        if(spu.getStatus().equals(SystemCommonEnum.SYS_COMMON_STATE_CLOSE.getValue())){
            log.error("商品不存在");
            throw new MmsException("商品不存在");
        }
        //库存检查：验证购买数量不超过库存。
        StoreProductSkuVo sku = storeProductSkuMapper.selectVoById(skuId);
        if(sku==null){
            log.error("库存不足");
            throw new MmsException("商品规格不存在");
        }
        if(sku.getStock()<num){
            log.error("库存不足");
            throw new MmsException("所选规格已售罄");
        }

    }

    @Override
    public List<StoreProductSpuVo> homeSpuList(String spuCateId, Integer limit) {
        return baseMapper.homeSpuList(spuCateId,limit);
    }

    /**
     *  SKU保存
     * @param spuId 商品ID
     * @param marketPrice 市场价
     * @param skuList  SKU列表
     * @return  true:成功 false:失败
     */
    @Transactional(rollbackFor = Exception.class)
    protected Boolean spuSkuSave(String spuId, BigDecimal marketPrice, List<SkuBo> skuList){
        if(ArrayUtil.isEmpty(skuList)){
            return Boolean.TRUE;
        }
        try {
            //商品+规格项 删除
            storeAttrKeySpuMapper.delete(new LambdaQueryWrapper<StoreAttrKeySpu>().eq(StoreAttrKeySpu::getSpuId,spuId));
            //商品+规格项值 删除
            storeAttrValueSpuMapper.delete(new LambdaQueryWrapper<StoreAttrValueSpu>().eq(StoreAttrValueSpu::getSpuId,spuId));
            //商品SKU 删除
            storeProductSkuMapper.delete(new LambdaQueryWrapper<StoreProductSku>().eq(StoreProductSku::getSpuId,spuId));

        for (SkuBo skuBo:skuList){
                List<SkuValuesBo> values = skuBo.getValues();
                if(ArrayUtil.isEmpty(values)){
                    throw  new RuntimeException("SKU属性值不能为空");
                }
                StringBuilder codeId= new StringBuilder();
                StringBuilder title  = new StringBuilder();
                String imgUrl = "";
                SkuDataBo  data = skuBo.getData();
                for (SkuValuesBo skuValuesBo:values){
                    // sku 规格项名称，数据库中不存在则新增
                    String keyName  = skuValuesBo.getKeyName();
                    StoreAttrKey storeAttrKey = storeAttrKeyMapper.selectOne(new  LambdaQueryWrapper<StoreAttrKey>().eq(StoreAttrKey::getName,keyName).last(SystemCommonEnum.LIMIT_ONE.getCode()));
                    if(storeAttrKey==null){
                        storeAttrKey = new StoreAttrKey();
                        storeAttrKey.setName(keyName);
                        // 非系统设置规格项
                        storeAttrKey.setIsSale("0");
                        storeAttrKey.setTenantId(LoginObject.getLoginTenant());
                        storeAttrKey.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
                        storeAttrKeyMapper.insert(storeAttrKey);
                    }
                    //商品+规格项 重新绑定
                    StoreAttrKeySpu  storeAttrKeySpu = storeAttrKeySpuMapper.selectOne(new LambdaQueryWrapper<StoreAttrKeySpu>().eq(StoreAttrKeySpu::getAttrKeyId,storeAttrKey.getId()).eq(StoreAttrKeySpu::getSpuId,spuId).last(SystemCommonEnum.LIMIT_ONE.getCode()));
                    if(storeAttrKeySpu==null){
                        storeAttrKeySpu = new StoreAttrKeySpu();
                        storeAttrKeySpu.setAttrKeyId(storeAttrKey.getId());
                        storeAttrKeySpu.setSpuId(spuId);
                        storeAttrKeySpu.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
                        storeAttrKeySpuMapper.insert(storeAttrKeySpu);
                    }


                    // sku  规格项值，数据库中不存在则新增
                    String valueName = skuValuesBo.getValueName();
                    StoreAttrValue storeAttrValue = storeAttrValueMapper.selectOne(new LambdaQueryWrapper<StoreAttrValue>().eq(StoreAttrValue::getValue,valueName).eq(StoreAttrValue::getAttrKeyId,storeAttrKey.getId()).last(SystemCommonEnum.LIMIT_ONE.getCode()));
                    if(storeAttrValue==null){
                        storeAttrValue = new StoreAttrValue();
                        storeAttrValue.setAttrKeyId(storeAttrKey.getId());
                        storeAttrValue.setValue(valueName);
                        storeAttrValue.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
                        storeAttrValueMapper.insert(storeAttrValue);
                    }
                    //商品+规格项值 重新绑定

                    StoreAttrValueSpu storeAttrValueSpu =storeAttrValueSpuMapper.selectOne(new LambdaQueryWrapper<StoreAttrValueSpu>()
                        .eq(StoreAttrValueSpu::getAttrKeyId,storeAttrKey.getId())
                        .eq(StoreAttrValueSpu::getAttrValueId,storeAttrValue.getId())
                        .eq(StoreAttrValueSpu::getSpuId,spuId).last(SystemCommonEnum.LIMIT_ONE.getCode()));
                    if(storeAttrValueSpu==null){
                        storeAttrValueSpu = new StoreAttrValueSpu();
                        storeAttrValueSpu.setAttrValueId(storeAttrValue.getId());
                        storeAttrValueSpu.setSpuId(spuId);
                        storeAttrValueSpu.setAttrKeyId(storeAttrKey.getId());
                        storeAttrValueSpu.setAttrImage(skuValuesBo.getColor());
                        if(skuValuesBo.getColor()!=null&&skuValuesBo.getColor().length()>5){
                            imgUrl = skuValuesBo.getColor();
                        }
                        storeAttrValueSpu.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
                        storeAttrValueSpuMapper.insert(storeAttrValueSpu);
                    }
                    if(!codeId.isEmpty()){
                        codeId.append("_").append(storeAttrValue.getId());
                        title.append("-").append(storeAttrValue.getValue());
                    }else{
                        codeId = new StringBuilder(storeAttrValue.getId());
                        title = new StringBuilder(storeAttrValue.getValue());
                    }
                }
                // SKU组合
                StoreProductSku storeProductSku = new StoreProductSku();
                storeProductSku.setSpuId(spuId);
                storeProductSku.setSkuCode(codeId.toString());
                storeProductSku.setSkuName(data.getTitle());
                storeProductSku.setPrice(marketPrice);
                storeProductSku.setStock(data.getInventory());
                storeProductSku.setWeight(data.getWeight());
                storeProductSku.setCode(data.getCode());
                storeProductSku.setCostPrice(data.getPrice());
                storeProductSku.setMainImage(imgUrl);
                storeProductSku.setStatus(SystemCommonEnum.SYS_COMMON_STATE_OPEN.getValue());
                storeProductSkuMapper.insert(storeProductSku);
        }

            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("SKU保存失败", e);
            throw e;
        }
    }

    /**
     * 查询商品的最底价格
     * @param supId 商品ID
     * @param price 商品原价
     * @return 最低价
     */
    private BigDecimal selectMinPrice(String supId,BigDecimal price){
       StoreProductSkuVo sku= storeProductSkuMapper.selectVoOne(
            new LambdaQueryWrapper<StoreProductSku>()
                .eq(StoreProductSku::getSpuId,supId)
                .orderByAsc(StoreProductSku::getPrice)
                .last("LIMIT 1")
        );

        return sku==null? price:sku.getPrice();
    };
}
