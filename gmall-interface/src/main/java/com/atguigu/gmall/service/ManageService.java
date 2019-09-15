package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.*;

import java.util.List;

public interface ManageService {
    //查询所有一级分类
    List<BaseCatalog1> getCatalog1();
    //根据一级分类查二级分类
    List<BaseCatalog2> getCatalog2(String catalog1Id);
    //二级查三级
    List<BaseCatalog3> getCatalog3(String catalog2Id);

    //根据三级分类id查询平台属性集合(集合中包含平台属性值的集合)
    //三级id查所有平台属性
    List<BaseAttrInfo> getAttrList(String catalog3Id);

    //添加平台属性值
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    //根据平台属性id获取平台属性对象
    BaseAttrInfo getAttrInfo(String attrId);

    //删除属性值
    void deleteAttrInfo(String attrId);

    //获取spuinfo数据,实际取的值为catalogId3的值
    List<SpuInfo> getSpuInfoList(SpuInfo spuInfo);

    //获取spuinfo数据,实际取的值为catalogId3的值  2019/9/14
    List<BaseAttrInfo> getSpuInfoLists(BaseAttrInfo baseAttrInfo);

    //查询所有销售属性
    List<BaseSaleAttr> getBaseSaleAttrList();

    //spu保存方法
    void saveSpuInfo(SpuInfo spuInfo);

    //根据spuId查询所有图片列表
    List<SpuImage> getSpuImageBySpuId(String spuId);

    //根据spuId查询销售属性列表
    List<SpuSaleAttr> getSpuSaleAttr(String spuId);
    //保存skuinfo信息
    void saveSku(SkuInfo skuInfo);

    //根据商品id查询商品信息数据
    SkuInfo getSkuInfo(String skuId);

    //根据spuid，skuid查询所有的销售属性，销售属性值，被选中的销售属性值
    List<SpuSaleAttr>selectSpuSaleAttrListChechBySku(SkuInfo skuInfo);

    //根据spuid查询该spu下的sku
    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);
}
