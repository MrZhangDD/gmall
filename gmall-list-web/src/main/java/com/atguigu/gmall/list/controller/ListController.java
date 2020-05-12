package com.atguigu.gmall.list.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.SkuLsInfo;
import com.atguigu.gmall.bean.SkuLsParams;
import com.atguigu.gmall.bean.SkuLsResult;
import com.atguigu.gmall.service.ListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class ListController {
    @Reference
    private ListService listService;

    @RequestMapping("list.html")
    public String list(SkuLsParams skuLsParams, HttpServletRequest request) throws IOException {
        //SkuLsResult search = listService.search(skuLsParams);
        //return JSON.toJSONString(search);
        SkuLsResult skuLsResult = listService.search(skuLsParams);
        List<SkuLsInfo> skuLsInfoList = skuLsResult.getSkuLsInfoList();
        request.setAttribute("skuLsInfoList",skuLsInfoList);
        return "list";
    }
}
