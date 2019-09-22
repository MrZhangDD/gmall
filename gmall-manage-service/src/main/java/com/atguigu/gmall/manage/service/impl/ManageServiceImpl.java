package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.config.RedisUtil;
import com.atguigu.gmall.manage.constant.ManageConstant;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.ManageService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @Description
 * @ClassName ManageServiceImpl
 * @Author
 */
@Service
public class ManageServiceImpl implements ManageService {

    //调用mapper
    @Autowired
    private BaseCatalog1Mapper baseCatalog1Mapper;
    @Autowired
    private BaseCatalog2Mapper baseCatalog2Mapper;
    @Autowired
    private BaseCatalog3Mapper baseCatalog3Mapper;
    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SpuSaleAttrValeMapper spuSaleAttrValeMapper;
    @Autowired
    private SpuInmageMapper spuInmageMapper;

    @Autowired
    private SkuImgMapper skuImgMapper;
    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List<BaseCatalog1> getCatalog1() {
        return baseCatalog1Mapper.selectAll();
    }


    @Override
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {
        BaseCatalog2 baseCatalog2 = new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);
        return baseCatalog2Mapper.select(baseCatalog2);
    }

    @Override
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {
        BaseCatalog3 baseCatalog3 = new BaseCatalog3();
        baseCatalog3.setCatalog2Id(catalog2Id);
        return baseCatalog3Mapper.select(baseCatalog3);
    }

    @Override
    public List<BaseAttrInfo> getAttrList(String catalog3Id) {
        //单张表
       /* BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        return baseAttrInfoMapper.select(baseAttrInfo);
        */
        //改造
       return baseAttrInfoMapper.getBaseAttrInfoListByCatalog3Id(Long.parseLong(catalog3Id));
    }


    @Override
    public BaseAttrInfo getAttrInfo(String attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(baseAttrInfo.getId());
        List<BaseAttrValue> baseAttrValueList = baseAttrValueMapper.select(baseAttrValue);
        baseAttrInfo.setAttrValueList(baseAttrValueList);
        return baseAttrInfo;
    }

    /**
     * 删除属性值
     * @param attrId
     */
    @Override
    public void deleteAttrInfo(String attrId) {
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        baseAttrValueMapper.delete(baseAttrValue);
        baseAttrInfoMapper.deleteByPrimaryKey(attrId);

    }

    /**
     * 保存和编辑写在一起
     * @param baseAttrInfo
     */
    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //先生成自动随机数，后续更改为主键自增
        //获取时间主键
        String logicSequence = String.valueOf(getLogicSequence("0"));
       /* Random ran = new Random();
        int i = ran.nextInt();
        String j = "" + i;*/
        if(baseAttrInfo.getId() != null && baseAttrInfo.getId().length() > 0){
            //编辑
            baseAttrInfoMapper.updateByPrimaryKeySelective(baseAttrInfo);
        } else {
           //保存
            if(baseAttrInfo.getId().length() == 0){
                /*int i1 = (int) (Math.random() * 1000);
                String j1 = "" + i1;
                baseAttrInfo.setId(j1);*/
                baseAttrInfo.setId(logicSequence);
            }

            baseAttrInfoMapper.insertSelective(baseAttrInfo);
        }

        //添加baseAttrValue的时候先去删除，再添加
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValue);

        //数据插入
        String logicSequence2 = String.valueOf(getLogicSequence("0"));
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if(!CollectionUtils.isEmpty(attrValueList)){
            for (BaseAttrValue attrValue : attrValueList) {
                if(attrValue.getId().length() == 0){
                    /*int i1 = (int) (Math.random() * 1000);
                    String j1 = "" + i1;*/
                    attrValue.setId(logicSequence2);
                }
                //attrValue.setAttrId(baseAttrInfo.getId());
                attrValue.setAttrId(baseAttrInfo.getCatalog3Id());
                baseAttrValueMapper.insertSelective(attrValue);
            }
        }


    }
    //生成主键
    private long getLogicSequence(String tableName){
        long timeStanp = System.currentTimeMillis();
        String datePattern = "yyyyMMddHHmmssSSS";
        String id = DateFormatUtils.format(timeStanp, datePattern);
        Long seqId = Long.parseLong(id);
        return seqId;
    }

    //查询spu信息
    @Override
    public List<SpuInfo> getSpuInfoList(SpuInfo spuInfo) {
        return spuInfoMapper.select(spuInfo);
    }

    @Override
    public List<BaseAttrInfo> getSpuInfoLists(BaseAttrInfo baseAttrInfo) {
        return baseAttrInfoMapper.select(baseAttrInfo);
    }

    /**
     * 查询所有销售属性
     * @return
     */
    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectAll();
    }

    /*
    保存spu数据
     */
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //如果spu的id为空则插入的为null逐渐自增
        if(spuInfo.getCatalog3Id() == null || spuInfo.getCatalog3Id().length() == 0){
            return;
        }
        if(spuInfo.getId() == null || spuInfo.getId().length() == 0){
            spuInfo.setId(null);
            spuInfoMapper.insertSelective(spuInfo);
        } else {
            //更新
            spuInfoMapper.updateByPrimaryKeySelective(spuInfo);
        }
        //spuimage先删除再插入
        SpuImage spuImage = new SpuImage();
        spuImage.setId(spuInfo.getId());
        spuInmageMapper.delete(spuImage);

        List<SpuImage> spuImages = spuInfo.getSpuImageList();

        if( com.alibaba.dubbo.common.utils.CollectionUtils.isNotEmpty(spuImages)){
            for (SpuImage image : spuImages) {
                image.setId(null);
                image.setSpuId(spuInfo.getId());
                spuInmageMapper.insertSelective(image);
            }
        }

        //先删除原有销售属性再插入
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuInfo.getId());
        spuSaleAttrMapper.delete(spuSaleAttr);

        SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
        spuSaleAttrValue.setSpuId(spuInfo.getId());
        spuSaleAttrValeMapper.delete(spuSaleAttrValue);

        List<SpuSaleAttr> spuSaleAttrs = spuInfo.getSpuSaleAttrList();
        if( com.alibaba.dubbo.common.utils.CollectionUtils.isNotEmpty(spuSaleAttrs)){
            for (SpuSaleAttr saleAttr : spuSaleAttrs) {
                saleAttr.setId(null);
                saleAttr.setSpuId(spuInfo.getId());
                //插入的销售属性
                spuSaleAttrMapper.insertSelective(saleAttr);
                //添加销售属性值
                List<SpuSaleAttrValue> spuSaleAttrValueList = saleAttr.getSpuSaleAttrValueList();
                if(com.alibaba.dubbo.common.utils.CollectionUtils.isNotEmpty(spuSaleAttrValueList)){
                    for (SpuSaleAttrValue saleAttrValue : spuSaleAttrValueList) {
                        saleAttrValue.setId(null);
                        saleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValeMapper.insertSelective(saleAttrValue);
                    }
                }
            }
        }
    }

    @Override
    public List<SpuImage> getSpuImageBySpuId(String spuId) {
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        return spuInmageMapper.select(spuImage);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttr(String spuId) {
        //调用maper
        return spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    @Override
    public void saveSku(SkuInfo skuInfo) {
        if(skuInfo.getId() == null || skuInfo.getId().length() == 0){
            skuInfo.setId(null);
            skuInfoMapper.insertSelective(skuInfo);
        }else{
            skuInfoMapper.updateByPrimaryKeySelective(skuInfo);
        }

        //skuimage
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuInfo.getId());
        skuImgMapper.delete(skuImage);

        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if(com.alibaba.dubbo.common.utils.CollectionUtils.isNotEmpty(skuImageList)){
            for (SkuImage image : skuImageList) {
                image.setId(null);
                image.setSkuId(skuInfo.getId());
                skuImgMapper.insertSelective(image);
            }
        }

        //平台属性
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuInfo.getId());
        skuAttrValueMapper.delete(skuAttrValue);

        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if(com.alibaba.dubbo.common.utils.CollectionUtils.isNotEmpty(skuAttrValueList)){
            for (SkuAttrValue attrValue : skuAttrValueList) {
                attrValue.setId(null);
                attrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insertSelective(attrValue);
            }
        }

        //销售属性
        SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
        skuSaleAttrValue.setId(skuInfo.getId());
        skuSaleAttrValueMapper.delete(skuSaleAttrValue);

        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(com.alibaba.dubbo.common.utils.CollectionUtils.isNotEmpty(skuSaleAttrValueList)){
            for (SkuSaleAttrValue saleAttrValue : skuSaleAttrValueList) {
                saleAttrValue.setId(null);
                saleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insertSelective(saleAttrValue);
            }
        }
    }

    @Override
    public SkuInfo getSkuInfo(String skuId) {
/*
        Jedis jedis = redisUtil.getJedis();
        //使用redis首先定义key
        String skuInfoKey = ManageConstant.SKUKEY_PREFIX + skuId + ManageConstant.SKUKEY_PSUEFIX;
        SkuInfo skuInfo = null;
        try {
            if(jedis.exists(skuInfoKey)){
                //取值
                String skuInfoJson = jedis.get(skuInfoKey);
                if(StringUtils.isNotEmpty(skuInfoJson)){
                    skuInfo = JSON.parseObject(skuInfoJson, SkuInfo.class);
                }
            } else {
                skuInfo = getSkuInfoDB(skuId);
                //将数据放入redis
                String jsonString = JSON.toJSONString(skuInfo);
                jedis.setex(skuInfoKey, ManageConstant.SKUKEY_TIMEOUT,jsonString);

            }
            return skuInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //jedis.set("test01", "你会啥！！！");
        return getSkuInfoDB(skuId);*/

        SkuInfo skuInfo = null;
        try {
            Jedis jedis = redisUtil.getJedis();
            String skuInfoKey = ManageConstant.SKUKEY_PREFIX + skuId + ManageConstant.SKUKEY_PSUEFIX;
            String skuInfoJson = jedis.get(skuInfoKey);
            if(StringUtils.isNotEmpty(skuInfoJson)){
                skuInfo = JSON.parseObject(skuInfoJson, SkuInfo.class);
                jedis.close();
            } else {
                String skuLockKey = ManageConstant.SKUKEY_PREFIX + skuId + ManageConstant.SKULOCK_SUFFIX;
                String lockKey = jedis.set(skuLockKey, "OK","NX","PX",ManageConstant.SKULOCK_EXPIRE_PX);
                if(StringUtils.isEquals("OK", lockKey)){
                    skuInfo = getSkuInfoDB(skuId);
                    //将数据放入redis
                    String jsonString = JSON.toJSONString(skuInfo);
                    jedis.setex(skuInfoKey, ManageConstant.SKUKEY_TIMEOUT,jsonString);
                } else {
                    Thread.sleep(1000);
                    return getSkuInfo(skuId);
                }
            }
            return skuInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getSkuInfoDB(skuId);
    }

    private SkuInfo getSkuInfoDB(String skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImageList = skuImgMapper.select(skuImage);
        //将平台属性值封装到skuinfo中
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.select(skuAttrValue);
        skuInfo.setSkuAttrValueList(skuAttrValues);

        skuInfo.setSkuImageList(skuImageList);
        return skuInfo;
    }

    @Override
    public List<SpuSaleAttr> selectSpuSaleAttrListChechBySku(SkuInfo skuInfo) {
        return spuSaleAttrMapper.selectSpuSaleAttrListChechBySku(Long.parseLong(skuInfo.getId()),Long.parseLong(skuInfo.getSpuId()));
    }

    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) {
        return skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(spuId);
    }
}
