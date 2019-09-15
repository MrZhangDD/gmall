package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SkuSaleAttrValue;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ItemController {

    @Reference
    private ManageService manageService;


    @RequestMapping("{skuId}.html")
    public String index(@PathVariable String skuId, HttpServletRequest request){
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);

        List<SpuSaleAttr> spuSaleAttrs = manageService.selectSpuSaleAttrListChechBySku(skuInfo);

        //保存销售属性的值集合
        request.setAttribute("skuInfo",skuInfo);
        request.setAttribute("saleAttrList",spuSaleAttrs);

        //实现销售属性值的切换skuid
        List<SkuSaleAttrValue> skuSaleAttrValueList =  manageService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        //拼接字符串 145|110
        String valueIdsKey = "";
        Map<String,String> map = new HashMap<>();
        if(CollectionUtils.isNotEmpty(skuSaleAttrValueList)){
            for (int i = 0; i < skuSaleAttrValueList.size(); i++) {
                SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueList.get(i);
                if(StringUtils.isNotEmpty(valueIdsKey)){
                    valueIdsKey += "|";
                }
                valueIdsKey += skuSaleAttrValue.getSaleAttrValueId();
                if((i+1) == skuSaleAttrValueList.size() ||
                        !skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueList.get(i+1).getSkuId())){
                    map.put(valueIdsKey,skuSaleAttrValue.getId() );
                }
            }
        }
        //将map转换成json串
        String valuesSkuJson = JSON.toJSONString(map);
        //存储到页面，由js获取页面中的选中属性值，使选中的值和valuesSkuJson做匹配
        request.setAttribute("valuesSkuJson", valuesSkuJson);

        return "item";
    }



}
