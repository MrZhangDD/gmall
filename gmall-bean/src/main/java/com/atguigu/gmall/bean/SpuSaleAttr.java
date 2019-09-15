package com.atguigu.gmall.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SpuSaleAttr implements Serializable {

    @Id
    @Column
    String id ;
    /*spuInfo.id*/
    @Column
    String spuId;
    /*销售属性的Id BaseSaleAttr.id*/
    @Column
    String saleAttrId;
    /*销售属性的name BaseSaleAttr.name*/
    @Column
    String saleAttrName;

    /*销售属性值的集合*/
    @Transient
    List<SpuSaleAttrValue> spuSaleAttrValueList;
    /* spuSaleAttr 是一个对象  spuSaleAttrValueJson spuSaleAttr对象下的一个属性*/
    /* spuSaleAttrValueJson={saleAttrId:1,saleAttrName:颜色,spuSaleAttrValueJson:"red,green"}*/
    @Transient
    Map spuSaleAttrValueJson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getSaleAttrId() {
        return saleAttrId;
    }

    public void setSaleAttrId(String saleAttrId) {
        this.saleAttrId = saleAttrId;
    }

    public String getSaleAttrName() {
        return saleAttrName;
    }

    public void setSaleAttrName(String saleAttrName) {
        this.saleAttrName = saleAttrName;
    }

    public List<SpuSaleAttrValue> getSpuSaleAttrValueList() {
        return spuSaleAttrValueList;
    }

    public void setSpuSaleAttrValueList(List<SpuSaleAttrValue> spuSaleAttrValueList) {
        this.spuSaleAttrValueList = spuSaleAttrValueList;
    }

    public Map getSpuSaleAttrValueJson() {
        return spuSaleAttrValueJson;
    }

    public void setSpuSaleAttrValueJson(Map spuSaleAttrValueJson) {
        this.spuSaleAttrValueJson = spuSaleAttrValueJson;
    }
}
