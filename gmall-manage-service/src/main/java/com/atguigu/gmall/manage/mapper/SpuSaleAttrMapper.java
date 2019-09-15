package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.SpuSaleAttr;
import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {

    //根据spuId查询销售属性列表
    List<SpuSaleAttr> selectSpuSaleAttrList(String spuId);

    //商品详情，根据spuid，skuid查询所有的销售属性，销售属性值，被选中的销售属性值
    List<SpuSaleAttr> selectSpuSaleAttrListChechBySku(@Param("skuId") long skuId,@Param("spuId") long spuId);



}
