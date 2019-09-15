package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description
 * @ClassName OrderController
 * @Author
 */
@Controller
public class OrderController {

    //@Autowired
    @Reference //dubbo消费者使用此注解
    private UserInfoService userInfoService;
     @RequestMapping("trade")
    @ResponseBody
    public List<UserAddress> trade(String userId){
         System.out.println(userId);
         return userInfoService.getUserAddressList(userId);
     }

}
