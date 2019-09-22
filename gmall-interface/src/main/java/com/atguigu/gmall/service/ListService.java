package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SkuLsInfo;
import com.atguigu.gmall.bean.SkuLsParams;
import com.atguigu.gmall.bean.SkuLsResult;

import java.io.IOException;

public interface ListService {


    //保存数据到es
    void saveSkuInfo(SkuLsInfo skuLsInfo) throws IOException;

    //根据用户输入的信息查询es
    SkuLsResult search(SkuLsParams skuLsParams) throws IOException;
}
