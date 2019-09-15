package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.UserInfo;

import java.util.List;

public interface UserInfoService {

    /*查询所有用户信息*/
    List<UserInfo> findAll();
    // 根据用户Id 查询用户地址
    List<UserAddress> getUserAddressList(String userId);
    /*// 用户登录方法
    UserInfo login(UserInfo userInfo);
    // 认证方法
    UserInfo verify(String userId);*/
}
