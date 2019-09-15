package com.gmall.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * @Description
 * @ClassName SequenceHandler
 * @Author
 */
public class SequenceHandler {

    //获取逻辑主键
    public long getLogicSequence(String tableName){
        long timeStanp = System.currentTimeMillis();
        String datePattern = "yyyyMMddHHmmssSSS";
        String id = DateFormatUtils.format(timeStanp, datePattern);
        Long seqId = Long.parseLong(id);
        return seqId;
    }
}
