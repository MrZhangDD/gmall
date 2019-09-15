package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.BaseAttrInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Description
 * @ClassName BaseAttrInfoMapper
 * @Author
 */
public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo> {

    /**
     * 重新写方法，两张表关联查询
     */
    List<BaseAttrInfo> getBaseAttrInfoListByCatalog3Id(Long catalog3Id);
}
